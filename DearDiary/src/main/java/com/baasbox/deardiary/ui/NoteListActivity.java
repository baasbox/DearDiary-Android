package com.baasbox.deardiary.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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

    private final static int EDIT_CODE = 1;

    private boolean mUseTwoPane;
    private NotesListFragment mListFragment;
    private RequestToken mRefresh;
    private RequestToken mSaving;

    private ProgressDialog mDialog;
    private boolean mDoRefresh;

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
            mRefresh = savedInstanceState.getParcelable(REFRESH_TOKEN_KEY);
            mSaving = savedInstanceState.getParcelable(SAVING_TOKEN_KEY);
        }
        mDoRefresh = savedInstanceState==null;

        setContentView(R.layout.activity_diary_list);

        mListFragment = (NotesListFragment)getSupportFragmentManager().findFragmentById(R.id.ItemList);

        if (findViewById(R.id.fragment_host) != null) {
            mUseTwoPane = true;
            mListFragment.setActivateOnItemClick(true);
        }

        // handle intents
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSaving!=null||mRefresh!=null){
            mDialog.show();
        }

        if (mSaving!=null){
            mSaving.resume(onSave);
        }

        if (mRefresh!=null){
            mRefresh.resume(onRefresh);
        } else if(mDoRefresh){
            refreshDocuments();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog.isShowing()){
            mDialog.dismiss();
        }
        if (mRefresh!=null){
            mRefresh.suspend();
        }
        if (mSaving!=null){
            mSaving.suspend();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRefresh!=null){
            outState.putParcelable(REFRESH_TOKEN_KEY,mRefresh);
        }
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
        mDialog.show();
        mRefresh =BaasDocument.fetchAll("memos",onRefresh);
    }

    private final BaasHandler<List<BaasDocument>>
        onRefresh = new BaasHandler<List<BaasDocument>>() {
        @Override
        public void handle(BaasResult<List<BaasDocument>> result) {
            if (result.isSuccess()){
                mDialog.dismiss();
                mRefresh=null;
                mListFragment.refresh(result.value());
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
