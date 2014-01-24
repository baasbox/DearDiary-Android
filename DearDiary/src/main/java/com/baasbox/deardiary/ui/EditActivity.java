package com.baasbox.deardiary.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.baasbox.deardiary.R;
import com.baasbox.deardiary.model.Contract;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class EditActivity extends ActionBarActivity {

    private AddNoteFragment mAddNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        mAddNotes = (AddNoteFragment)getSupportFragmentManager().findFragmentById(R.id.Edit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_note_action){
            ContentValues values = mAddNotes.getData();
            getContentResolver().insert(Contract.Notes.CONTENT_URI,values);
            setResult(RESULT_OK);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
