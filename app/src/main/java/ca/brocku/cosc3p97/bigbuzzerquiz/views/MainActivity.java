package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.HostConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.PlayerConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.Question;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.QuizDatabase;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.ShowQuestionRequest;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;

public class MainActivity extends AppCompatActivity
        implements StartFragment.OnClickListener, MasterSetupFragment.OnClickListener,
        Player.Playable, QuestionFragment.QuestionFragmentListener, Observer {
    private static final String TAG = "MainActivity";
    private static final String START_FRAGMENT = "START_FRAGMENT";
    private static final String MASTER_SETUP_FRAGMENT = "MASTER_SETUP_FRAGMENT";
    private static final String START_PLAYER_FRAGMENT = "START_PLAYER_FRAGMENT";
    private static final String WIFI_SETUP_FRAGMENT = "WIFI_SETUP_FRAGMENT";
    private static final String QUESTION_FRAGMENT = "QUESTION_FRAGMENT";
    private WiFiConnectionsModel wifi;
    private Player player;
    private Host host;
    private MasterSetupFragment masterSetupFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: invoked");

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        wifi = new WiFiConnectionsModel(this);
        wifi.addObserver(this);

        if (findViewById(R.id.fragment_container) != null) {
            Fragment fragment = StartFragment.newInstance(wifi);
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(START_FRAGMENT).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: invoked");
        wifi.registerReceiver(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "onPause: invoked");
        wifi.unregisterReceiver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void wifiSetupButtonClicked() {
        Fragment fragment = WifiSetupFragment.newInstance(wifi);
        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(WIFI_SETUP_FRAGMENT)
                .commit();
    }


    @Override
    public void masterButtonClicked(String name) {
        Log.i(TAG, "Connecting as an MC");
        setupPlayer(name, true);

    }


    public void setupPlayer(final String name, final boolean isMaster) {
        final AppCompatActivity me = this;

        if (host != null) {
            player = Player.getInstance(wifi.getWifiP2pInfo().groupOwnerAddress, host);
        } else {
            player = Player.getInstance(wifi.getWifiP2pInfo().groupOwnerAddress);
        }

        player.setConnectedListener(new HostConnection.ConnectedListener() {
            @Override
            public void onConnected() {
                player.sendName(name);

                if (isMaster) {
                    onMasterConnectionSetup();
                } else {
                    onConnectionSetup();
                }
            }

            @Override
            public void onDisconnected() {
                Toast.makeText(me, "Disconnected from host", Toast.LENGTH_SHORT).show();
            }
        });

        player.setActivity(this);
    }



    @Override
    public void playerButtonClicked(String name) {
        setupPlayer(name ,false);

    }


    private void onMasterConnectionSetup() {
        Toast.makeText(this, "Connected to host", Toast.LENGTH_SHORT).show();
        masterSetupFragment = MasterSetupFragment.newInstance(wifi);
        masterSetupFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, masterSetupFragment)
                .addToBackStack(MASTER_SETUP_FRAGMENT)
                .commit();
    }


    private void onConnectionSetup() {
        Toast.makeText(this, "Connected to host", Toast.LENGTH_SHORT).show();
        Fragment fragment = StartPlayerFragment.newInstance();
        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(START_PLAYER_FRAGMENT)
                .commit();
    }


    @Override
    public void startGameButtonClicked(int numberOfQuestions, List<Integer> categories) {
        Player player = null;
        try {
            player = Player.getInstance();
            player.play(numberOfQuestions, categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showQuestion(Question question) {
        Fragment fragment = new QuestionFragment();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ShowQuestionRequest.CORRECT_ANSWER, question.indexOfCorrectAnswer);
        bundle.putString(ShowQuestionRequest.TEXT, question.text);
        bundle.putStringArray(ShowQuestionRequest.ANSWERS, question.answers);

        fragment.setArguments(bundle);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(QUESTION_FRAGMENT)
                .commit();
    }


    @Override
    public void update(Observable observable, Object o) {
        Log.i(TAG, "update:invoked");
        WiFiConnectionsModel wifi = (WiFiConnectionsModel) observable;

        if(wifi.getWifiP2pInfo() != null) {
            if (wifi.getWifiP2pInfo().isGroupOwner) {
                Log.i(TAG, "Connected as group owner");

                host = Host.getInstance(new QuizDatabase(this), new PlayerConnection.SetupListener() {
                    @Override
                    public void onSetup(Host host) {
                        Log.i(TAG, "onSetup: game SERVER is ready, creating player");
                    }
                });



                Log.i(TAG, "update: player is null? " + (player == null));

            }

            TextView statusTextView = (TextView) findViewById(R.id.mainStatusTextView);
            statusTextView.setText(String.format("WIFI STATUS: %s", wifi.getNetworkInfo().getDetailedState().toString()));
        } else {
            TextView statusTextView = (TextView) findViewById(R.id.mainStatusTextView);
            statusTextView.setText("WIFI STATUS: UNAVAILABLE");
        }
    }


    @Override
    public void showTimeout() {
        AlertDialog interruptDialog = new AlertDialog.Builder(MainActivity.this).create();
        interruptDialog.setTitle(("Timeout"));
        interruptDialog.setMessage("Time is up!");
        interruptDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ready for next question?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        player.ready();
                    }
                });
        interruptDialog.show();
    }


    @Override
    public void showSomebodySucceeded(String playerName) {
        AlertDialog interruptDialog = new AlertDialog.Builder(MainActivity.this).create();
        interruptDialog.setTitle(("Turn Success"));
        interruptDialog.setMessage(String.format("%s answered this question successfully", playerName));
        interruptDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ready for next question?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        player.ready();
                    }
                });
        interruptDialog.show();
    }


    @Override
    public void showEveryoneFailed() {
        AlertDialog interruptDialog = new AlertDialog.Builder(MainActivity.this).create();
        interruptDialog.setTitle(("Turn Failure"));
        interruptDialog.setMessage("Well, nobody got that one right");
        interruptDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ready for next question?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        player.ready();
                    }
                });
        interruptDialog.show();
    }


    @Override
    public void showGameOver(List<Participant> players) {
        AlertDialog timeoutDialog = new AlertDialog.Builder(MainActivity.this).create();
        timeoutDialog.setTitle(("Game Over"));

        StringBuilder message = new StringBuilder();
        message.append("The game is over here are the results...\n\n");
        for (Participant player : players) {
            message.append(String.format("Name: %s\tScore: %d\n", player.name, player.score));
        }

        timeoutDialog.setMessage(message.toString());
        timeoutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        // TODO: 2015-12-08 have the activity return to the main page
                    }
                });
        timeoutDialog.show();
    }

    @Override
    public void updatePlayersNames(List<String> names) {
        Log.i(TAG, "updatePlayerNames: invoked");

        if (masterSetupFragment != null) {
            masterSetupFragment.updatePlayerNames(names);
        } else {
            Log.i(TAG, "updatePlayerNames: could not find the fragment");
        }

    }


    @Override
    public void onAnswerButtonClick(boolean isCorrect) {
        player.answer(isCorrect);
    }
}
