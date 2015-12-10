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
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;

public class MainActivity extends AppCompatActivity
        implements StartFragment.OnClickListener, MasterSetupFragment.OnClickListener,
        Player.ShowQuestionable, Player.Interruptable, Player.ShowGameOverable,
        QuestionFragment.QuestionFragmentListener, Observer {
    private static final String TAG = "MainActivity";
    private static final String START_FRAGMENT = "START_FRAGMENT";
    private static final String MASTER_SETUP_FRAGMENT = "MASTER_SETUP_FRAGMENT";
    private static final String WIFI_SETUP_FRAGMENT = "WIFI_SETUP_FRAGMENT";
    private static final String QUESTION_FRAGMENT = "QUESTION_FRAGMENT";
    private WiFiConnectionsModel wifi;
    private Player player;
    private Host host;


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

        //player = wifi.getPlayer();

//        if (player != null) {
//            player.setActivity(this);
//        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: invoked");
        wifi.unregisterReceiver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
    public void masterButtonClicked() {
        Log.i(TAG, "Connecting as an MC");
        final AppCompatActivity me = this;

        if (host != null) {
            player = Player.getInstance(wifi.getWifiP2pInfo().groupOwnerAddress, host);
        } else {
            player = Player.getInstance(wifi.getWifiP2pInfo().groupOwnerAddress);
        }

            player.setConnectedListener(new HostConnection.ConnectedListener() {
                @Override
                public void onConnected() {
                    onConnectionSetup();
                }

                @Override
                public void onDisconnected() {
                    Toast.makeText(me, "Disconnected from host", Toast.LENGTH_SHORT).show();
                }
            });

        player.setActivity(this);
        //wifi.setPlayer(player);

    }


    private void onConnectionSetup() {
        Toast.makeText(this, "Connected to host", Toast.LENGTH_SHORT).show();
        Fragment fragment = MasterSetupFragment.newInstance(wifi);
        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(MASTER_SETUP_FRAGMENT)
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
    public void showQuestion(int key) {
        Fragment fragment = new QuestionFragment();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("KEY", key);
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
                host = Host.getInstance(new PlayerConnection.SetupListener() {
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
        interruptDialog.setTitle(("Question Success"));
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
        interruptDialog.setTitle(("Question Failure"));
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
    public void onAnswerButtonClick(int buttonNbr) {
        Log.i(TAG, String.format("onAnswerButtonClick: invoked with buttonNbr [%d]", buttonNbr));
        player.answer(buttonNbr == 1);
    }
}
