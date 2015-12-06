package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * This Activity provides setting for the master-player to setup the game
 */
public class MasterSetupActivity extends AppCompatActivity {
    Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_setup);

        try {
            player = Player.getInstance();

            player.getPlayers(new Player.CallbackListener() {
                @Override
                public void onCallback(Object players) {
                    loadListView((List<String>) players);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadListView(List<String> players) {
        final ListView listView = (ListView) findViewById(R.id.playerListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, players);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master_setup, menu);
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

    /**
     * Method that is called when you click the More-Button
     *
     * @param view
     */
    public void clickMore(View view) {
        TextView amountView = (TextView) findViewById(R.id.questionAmount);
        int amount = Integer.decode(amountView.getText().toString());

        if (amount == amountPossibleQuestions()) {

            CharSequence text = "You have reached the maximum amount of questions for the chosen categories";

            Toast toast = Toast.makeText(getApplicationContext(), text,  Toast.LENGTH_LONG);
            toast.show();

            return;
        }

        increaseAmountView(1);
    }

    /**
     * Method that is called when you click the Less-Button
     *
     * @param view
     */
    public void clickLess(View view) {
        TextView amountView = (TextView) findViewById(R.id.questionAmount);
        int amount = Integer.decode(amountView.getText().toString());

        if (amount == 0) {
            return;
        }

        increaseAmountView(-1);
    }

    /**
     * Validates the input and starts the game
     * called by the StartGame-Button
     *
     * @param view
     */
    public void startGame(View view) {

    }

    /**
     * Calculates the amount of possible Questions based on the checkboxes that are checked
     * For every Categorie there are 10 Questions
     *
     * @return
     */
    private int amountPossibleQuestions() {
        int amount = 0;

        boolean checkboxArt = ((CheckBox) findViewById(R.id.CheckboxArt)).isChecked();
        boolean checkboxScience = ((CheckBox) findViewById(R.id.CheckboxScience)).isChecked();
        boolean checkboxGeography = ((CheckBox) findViewById(R.id.CheckboxGeography)).isChecked();

        if (checkboxArt) {
            amount += 10;
        }

        if (checkboxScience) {
            amount += 10;
        }

        if (checkboxGeography) {
            amount += 10;
        }

        return amount;
    }

    /**
     * if a checkbox is checked, the default value of the amountQuestions view should increase by 10,
     * if a checkbox is unchecked, it should decrease by 10
     */
    public void clickCheckbox(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {
            increaseAmountView(10);
        } else {
            increaseAmountView(-10);
        }
    }

    /**
     * Increases the number in the AmountView by the transferred number.
     * Of Course - if the number is negative, the number will be decreased
     *
     * @param byNumber amount by which the number should be increased
     */
    private void increaseAmountView(int byNumber) {
        TextView amountView = (TextView) findViewById(R.id.questionAmount);
        int amount = Integer.decode(amountView.getText().toString());

        amount += byNumber;

        amountView.setText(Integer.toString(amount));
    }
}

