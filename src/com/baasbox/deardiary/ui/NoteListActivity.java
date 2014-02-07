package com.baasbox.deardiary.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.RequestToken;
import com.baasbox.deardiary.R;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class NoteListActivity extends ActionBarActivity
    implements NotesListFragment.Callbacks,AddNoteFragment.OnAddNote {
    private final static String REFRESH_TOKEN_KEY = "refresh";
    private final static String SAVING_TOKEN_KEY = "saving";
    private final static String LOGOUT_TOKEN_KEY = "logout";

    private final static int EDIT_CODE = 1;

    private boolean mUseTwoPane;
    private NotesListFragment mListFragment;

    private ProgressDialog mDialog;
    private boolean mDoRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 2.1 check user login
        // fixme

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Refreshing...");

        if (savedInstanceState!=null){
            //todo 6 refrsh
            //todo 5.4 save reume
            //todo 3.4 logout resume
        }

        //todo 3.5 show the dialog

        mDoRefresh = savedInstanceState==null;

        setContentView(R.layout.activity_diary_list);

        mListFragment = (NotesListFragment)getSupportFragmentManager().findFragmentById(R.id.ItemList);

        if (findViewById(R.id.fragment_host) != null) {
            mUseTwoPane = true;
            mListFragment.setActivateOnItemClick(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog.isShowing()){
            mDialog.dismiss();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
         //todo 5.8
        //todo 3,3 suspend token
    }

    private void startLoginScreen(){
        mDoRefresh = false;
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
        } else if (item.getItemId()==R.id.logout_action){
            //todo 3.1 logout
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogout(){
        startLoginScreen();
    }
//todo 3.2 logout handler

    @Override
    public void onItemSelected(BaasDocument document) {
        if (mUseTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(NoteDetailsFragment.CURRENTLY_SHOWN_ITEM_KEY, document);
            NoteDetailsFragment fragment = new NoteDetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host,fragment)
                    .commit();
        } else {
            Intent details = new Intent(this,NotesDetailsActivity.class);
            details.putExtra(NoteDetailsFragment.CURRENTLY_SHOWN_ITEM_KEY,document);
            startActivity(details);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==EDIT_CODE){
            if (resultCode==RESULT_OK){
                refreshDocuments();
            } else if(resultCode==EditActivity.RESULT_SESSION_EXPIRED){
                startLoginScreen();
            } else if (resultCode==EditActivity.RESULT_FAILED){
                Toast.makeText(this,"Failed to add note",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshDocuments(){
        //todo 6.1
    }

    //todo 6,1

    @Override
    public void onAddNote(BaasDocument document) {
// todo 5.1 save
    }

    //todo 5.2 saveHandler
}
