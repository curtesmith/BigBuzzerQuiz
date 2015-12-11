package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;

public class StartActivity extends AppCompatActivity implements Observer {
    private WiFiConnectionsModel wifi;
    private static final String TAG = "StartActivity";
    public static final String EXTRA_WIFI_MODEL = "EXTRA_WIFI_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.wiFiSetupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                // TODO: 2015-12-07 pass the wifi object to the masteractivity
                startActivity(intent);
            }
        });

//        wifi = new WiFiConnectionsModel(this);
//        wifi.addObserver(this);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void choseMasterMode(View view) {
//        Intent intent = new Intent(this, MasterSetupActivity.class);
//        startActivity(intent);
    }

    public void chosePlayerMode(View view) {
//        Intent intent = new Intent(this, StartPlayerActivity.class);
//        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object o) {
        WiFiConnectionsModel wifi = (WiFiConnectionsModel) observable;

        findViewById(R.id.masterButton).setEnabled(wifi.isConnected());
        findViewById(R.id.playerButton).setEnabled(wifi.isConnected());
    }
}
