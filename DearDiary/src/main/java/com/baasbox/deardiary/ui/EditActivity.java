package com.baasbox.deardiary.ui;

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
    private static final String PENDING_SAVE = "PENDING_SAVE";

    private AddNoteFragment mAddNotes;
    private RequestToken mAddToken;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Uploading...");
        mAddNotes = (AddNoteFragment)getSupportFragmentManager().findFragmentById(R.id.Edit);
        if (savedInstanceState!=null){
            mAddToken = savedInstanceState.getParcelable(PENDING_SAVE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAddToken!=null){
            mDialog.show();
            mAddToken.resume(uploadHandler);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog.isShowing()){
            mDialog.dismiss();
        }
        if (mAddToken!=null) mAddToken.suspend();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAddToken!=null){
            outState.putParcelable(PENDING_SAVE,mAddToken);
        }
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
         mDialog.show();
         mAddToken=document.save(SaveMode.IGNORE_VERSION,uploadHandler);
    }

    private final BaasHandler<BaasDocument> uploadHandler = new BaasHandler<BaasDocument>() {
        @Override
        public void handle(BaasResult<BaasDocument> doc) {
            mDialog.dismiss();
            mAddToken=null;
            if(doc.isSuccess()){
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(EditActivity.this,"ERROR SAVING DOCUMENT",Toast.LENGTH_LONG).show();
                Log.d("ERROR","Failed with error",doc.error());
            }
        }
    };
}
