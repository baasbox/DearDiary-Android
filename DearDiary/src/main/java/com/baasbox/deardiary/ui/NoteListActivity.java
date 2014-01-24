package com.baasbox.deardiary.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.baasbox.deardiary.R;
import com.baasbox.deardiary.model.Contract;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class NoteListActivity extends ActionBarActivity
    implements NotesListFragment.Callbacks,AddNoteFragment.OnAddNote {

    private final static int EDIT_CODE = 1;

    private boolean mUseTwoPane;
    private NotesListFragment mListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_diary_list);
        mListFragment = (NotesListFragment)getSupportFragmentManager().findFragmentById(R.id.ItemList);

        if (findViewById(R.id.fragment_host) != null) {
            mUseTwoPane = true;
            mListFragment.setActivateOnItemClick(true);
        }

        // handle intents
    }

    private void startLoginScreen(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_action){
            if (mUseTwoPane){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_host,new AddNoteFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Intent intent = new Intent(this,EditActivity.class);
                startActivityForResult(intent,EDIT_CODE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(long id) {
        Uri uri = ContentUris.withAppendedId(Contract.Notes.CONTENT_URI,id);
        if (mUseTwoPane) {
            Bundle args = new Bundle();

            args.putParcelable(NoteDetailsFragment.CURRENTLY_SHOWN_ITEM_KEY, uri);
            NoteDetailsFragment fragment = new NoteDetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host,fragment)
                    .commit();
        } else {
            Intent details = new Intent(this,NotesDetailsActivity.class);
            details.setData(uri);
//            details.putExtra(NoteDetailsFragment.CURRENTLY_SHOWN_ITEM_KEY,id);
            startActivity(details);
        }
    }

    @Override
    public void onAddNote(ContentValues v) {
        getContentResolver().insert(Contract.Notes.CONTENT_URI,v);
    }
}
