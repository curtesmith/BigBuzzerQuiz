package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class StartPlayerActivity extends AppCompatActivity {
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_player);

        try {
            player = Player.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.setActivity(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        player.setActivity(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_player, menu);
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
}
