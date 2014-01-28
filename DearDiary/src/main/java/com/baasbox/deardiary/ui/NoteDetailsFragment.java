package com.baasbox.deardiary.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baasbox.deardiary.R;
import com.baasbox.deardiary.model.Contract;
import com.baasbox.deardiary.model.Note;


/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class NoteDetailsFragment extends Fragment {

    static final String CURRENTLY_SHOWN_ITEM_KEY = "currently_shown_item";

    private Note mNote;

    private TextView mTitleView;
    private TextView mContentView;
    private TextView mDateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(CURRENTLY_SHOWN_ITEM_KEY)){
            Uri uri = getArguments().getParcelable(CURRENTLY_SHOWN_ITEM_KEY);
            mNote = Contract.Notes.loadNote(getActivity().getContentResolver(),uri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diary_details,container,false);
        mTitleView = (TextView) root.findViewById(R.id.tv_diary_note_title);
        mContentView = (TextView) root.findViewById(R.id.tv_diary_note_content);
        mDateView = (TextView) root.findViewById(R.id.tv_diary_note_date);
        refreshViews();
        return root;
    }

    private void refreshViews(){
        if (mNote!=null) {
            mTitleView.setText(mNote.getTitle());
            mContentView.setText(mNote.getNote());
        }
    }

}
