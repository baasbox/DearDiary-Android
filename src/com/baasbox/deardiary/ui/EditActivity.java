package com.baasbox.deardiary.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.baasbox.android.*;
import com.baasbox.deardiary.R;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class EditActivity extends ActionBarActivity {
    //todo
    private static final String PENDING_SAVE = "PENDING_SAVE";
    public static final int RESULT_SESSION_EXPIRED = Activity.RESULT_FIRST_USER+1;
    public static final int RESULT_FAILED = RESULT_SESSION_EXPIRED+1;


    private AddNoteFragment mAddNotes;

    //todo

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Uploading...");
        mAddNotes = (AddNoteFragment)getSupportFragmentManager().findFragmentById(R.id.Edit);
        if (savedInstanceState!=null){
            //todo 5.6 resume add
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    //todo resume add
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_note_action){
            saveOnBaasBox(mAddNotes.getData());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveOnBaasBox(BaasDocument document){
        //todo 5.6 save on baasbox
    }
    // todo 5.7 save handler
}
