package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.database.DataSetObserver;
import android.util.Log;

public class ListDataSetObserver extends DataSetObserver{
    private static final String TAG = "...DataSetObserver";
    private MasterActivity activity;


    public ListDataSetObserver(MasterActivity activity) {
        this.activity = activity;
    }


    public void onChanged() {
        activity.updateSelectedPlayers();
    }


    public void onInvalidated() {
        Log.i(TAG, "onInvalidated called");
    }
}
