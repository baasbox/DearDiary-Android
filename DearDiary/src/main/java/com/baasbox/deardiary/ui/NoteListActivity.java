package com.baasbox.deardiary.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.baasbox.android.*;
import com.baasbox.deardiary.R;

import java.util.List;

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
    private RequestToken mRefresh;
    private RequestToken mSaving;

    private ProgressDialog mDialog;
    private boolean mDoRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 2
        if (BaasUser.current() == null){
            startLoginScreen();
            return;
        }

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Refreshing...");

        if (savedInstanceState!=null){
            mRefresh = RequestToken.loadAndResume(savedInstanceState,REFRESH_TOKEN_KEY,onRefresh);
            mSaving = RequestToken.loadAndResume(savedInstanceState,SAVING_TOKEN_KEY,onSave);
            logoutToken = RequestToken.loadAndResume(savedInstanceState,LOGOUT_TOKEN_KEY,logoutHandler);
        }

        if (mSaving!=null||mRefresh!=null||logoutToken!=null){
            mDialog.show();
        }
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
        if(mRefresh==null&&mDoRefresh){
            refreshDocuments();
        }
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
        if (mRefresh!=null){
            mRefresh.suspendAndSave(outState,REFRESH_TOKEN_KEY);
        }
        if (mSaving!=null){
            mSaving.suspendAndSave(outState,SAVING_TOKEN_KEY);
        }
        if (logoutToken!=null){
            mSaving.suspendAndSave(outState,LOGOUT_TOKEN_KEY);
        }
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
            BaasUser.current().logout(logoutHandler);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogout(){
        startLoginScreen();
    }

    private RequestToken logoutToken;
    private final BaasHandler<Void> logoutHandler =
            new BaasHandler<Void>() {
                @Override
                public void handle(BaasResult<Void> voidBaasResult) {
                    logoutToken=null;
                    onLogout();
                }
            };
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
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshDocuments(){
        if(!mDialog.isShowing())mDialog.show();
        mRefresh =BaasDocument.fetchAll("memos",onRefresh);
    }

    private final BaasHandler<List<BaasDocument>>
        onRefresh = new BaasHandler<List<BaasDocument>>() {
        @Override
        public void handle(BaasResult<List<BaasDocument>> result) {
            mDialog.dismiss();
            mRefresh=null;
            if (result.isSuccess()){
                mListFragment.refresh(result.value());
            } else {
                Log.e("LOGERR","Error "+result.error().getMessage(),result.error());
                Toast.makeText(NoteListActivity.this,"Error while talking with the box",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onAddNote(BaasDocument document) {
        mDialog.show();
        mSaving =document.save(SaveMode.IGNORE_VERSION,onSave);
    }

    private final BaasHandler<BaasDocument> onSave =
            new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> baasDocumentBaasResult) {
                    mSaving=null;
                    mDialog.dismiss();
                    refreshDocuments();
                }
            };
}
