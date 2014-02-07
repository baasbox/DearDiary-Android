package com.baasbox.deardiary.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.*;
import android.widget.EditText;
import com.baasbox.android.BaasDocument;
import com.baasbox.deardiary.R;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class AddNoteFragment extends DialogFragment {

    private EditText mTitle;
    private EditText mContent;
    private boolean mShowAsDialog;

    public interface OnAddNote {
        public void onAddNote(BaasDocument values);
    }

    private OnAddNote mListener;

    private final static OnAddNote NOOP = new OnAddNote() {
        @Override
        public void onAddNote(BaasDocument values) {
        }
    };

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs,R.styleable.AddNoteFragment);
        mShowAsDialog =a.getBoolean(R.styleable.AddNoteFragment_showAsDialog,true);
        a.recycle();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnAddNote){
            mListener = (OnAddNote)activity;
        } else {
            mListener = NOOP;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowsDialog(mShowAsDialog);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = NOOP;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = loadView(inflater, null);
        return v;
    }

    private View loadView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.add_diary_note,container,false);
        mTitle = (EditText)v.findViewById(R.id.in_title);
        mContent = (EditText)v.findViewById(R.id.in_content);
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v=loadView(LayoutInflater.from(getActivity()), null);
        builder.setView(v)
               .setTitle("Add note")
               .setPositiveButton(getString(android.R.string.ok), fOk)
               .setNegativeButton(getString(android.R.string.cancel),null);
        return builder.create();
    }


    public BaasDocument getData() {
        String title = mTitle.getText().toString();
        String content  = mContent.getText().toString();
        BaasDocument doc = new BaasDocument("memos");
        doc.putString("title",title);
        doc.putString("content",content);
        return doc;
    }

    private void dispatchAddNote(){
        mListener.onAddNote(getData());
    }

    private final DialogInterface.OnClickListener fOk = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dispatchAddNote();
        }
    };
}
