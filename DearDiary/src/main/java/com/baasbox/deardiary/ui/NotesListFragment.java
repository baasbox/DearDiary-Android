package com.baasbox.deardiary.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import com.baasbox.android.BaasDocument;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Andrea Tortorella on 24/01/14.
 */
public class NotesListFragment extends ListFragment {

    private final static String CURRENTLY_SELECTED_ITEM_KEY = "currently_selected_item";
    private final static String SAVED_DOCS = "saved_docs";

    public void refresh(List<BaasDocument> docs) {
        mDocuments.clear();
        mDocuments.addAll(docs);
        getListAdapter().notifyDataSetChanged();
    }

    public interface Callbacks {
        public void onItemSelected(BaasDocument document);
    }

    private final static Callbacks sNoopCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(BaasDocument document) {
        }
    };

    private int mSelectedPostion = ListView.INVALID_POSITION;

    private Callbacks mCallbacks;
    private NotesAdapter mAdapter;
    private ArrayList<BaasDocument> mDocuments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            mDocuments = new ArrayList<BaasDocument>();
        } else {
            mDocuments = savedInstanceState.getParcelableArrayList(SAVED_DOCS);
        }
        mAdapter = new NotesAdapter(getActivity(),mDocuments);
        setListAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
            && savedInstanceState.containsKey(CURRENTLY_SELECTED_ITEM_KEY))
            setActivatedPosition(savedInstanceState.getInt(CURRENTLY_SELECTED_ITEM_KEY));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks");
        }
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sNoopCallbacks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSelectedPostion!=ListView.INVALID_POSITION){
            outState.putInt(CURRENTLY_SELECTED_ITEM_KEY,mSelectedPostion);
        }
        outState.putParcelableArrayList(SAVED_DOCS,mDocuments);

    }

    public void setActivateOnItemClick(boolean activateOnItemClick){
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    @Override
    public NotesAdapter getListAdapter() {
        return (NotesAdapter)super.getListAdapter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallbacks.onItemSelected(getListAdapter().getItem(position));
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mSelectedPostion,false);
        } else {
            getListView().setItemChecked(position,true);
        }
        mSelectedPostion = position;
    }

}
