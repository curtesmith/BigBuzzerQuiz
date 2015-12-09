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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: invoked");

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        wifi = new WiFiConnectionsModel(this);

        if(findViewById(R.id.fragment_container) != null) {
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
        wifi.addObserver(this);

        player = wifi.getPlayer();

        if(player != null) {
            player.setActivity(this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
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
        Fragment fragment = MasterSetupFragment.newInstance(wifi);
        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(MASTER_SETUP_FRAGMENT)
                .commit();
    }


    @Override
    public void startGameButtonClicked() {
        Player player = null;
        try {
            player = Player.getInstance();
            player.play();
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
        this.player = wifi.getPlayer();
        if(this.player != null) {
            this.player.setActivity(this);
        }
        Log.i(TAG, "update: player is null? " + (this.player == null));

        ((TextView) findViewById(R.id.statusTextView)).setText("Your are not connected");
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
                        wifi.getPlayer().ready();
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
                        wifi.getPlayer().ready();
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
                        wifi.getPlayer().ready();
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
        for(Participant player : players) {
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
        wifi.getPlayer().answer(buttonNbr == 1);
    }
}
