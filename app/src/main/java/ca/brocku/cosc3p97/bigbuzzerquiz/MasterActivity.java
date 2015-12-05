package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;


public class MasterActivity extends AppCompatActivity
        implements  View.OnClickListener, Observer {

    private static final String TAG = "MasterActivity";
    private WiFiConnectionsModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        model = new WiFiConnectionsModel(this);
        model.addObserver(this);

        ListView list = (ListView) findViewById(R.id.devicesList);
        list.setAdapter(model.getPeersListAdapter());

        Button scanWifi = (Button) findViewById(R.id.scanWifiButton);
        scanWifi.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        model.registerReceiver(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        model.unregisterReceiver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.scanWifiButton:
                model.discoverPeers();
                break;
        }
    }


    @Override
    public void update(Observable observable, Object o) {
        WiFiConnectionsModel model = (WiFiConnectionsModel) observable;

        setScanningWidgetVisibility(model.isScanning());

        ((TextView) findViewById(R.id.groupOwnerFyiTextView))
                .setText("Group Owner: {" + model.getGroupOwner() + "}");

        if(o != null) {
            Toast.makeText(this, (String) o, Toast.LENGTH_SHORT).show();
        }
    }


    private void setScanningWidgetVisibility(boolean isVisible) {
        if(isVisible) {
            findViewById(R.id.scanWifiButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.scanWifiTextView).setVisibility(View.VISIBLE);
            findViewById(R.id.scanWifiProgressBar).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.scanWifiButton).setVisibility(View.VISIBLE);
            findViewById(R.id.scanWifiTextView).setVisibility(View.GONE);
            findViewById(R.id.scanWifiProgressBar).setVisibility(View.GONE);
        }

    }
}
