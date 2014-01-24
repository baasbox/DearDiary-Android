package com.baasbox.deardiary.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import com.baasbox.deardiary.R;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class NotesListFragment extends ListFragment {

    private final static String CURRENTLY_SELECTED_ITEM_KEY = "currently_selected_item";

    public interface Callbacks {
        public void onItemSelected(long itemId);
    }

    private final static Callbacks sNoopCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long itemId) {
        }
    };

    private int mSelectedPostion = ListView.INVALID_POSITION;

    private Callbacks mCallbacks;
    private NotesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new NotesAdapter(getActivity());
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(R.id.NOTES_LOADER,null,fLoaderCallbacks);
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
    }

    public void setActivateOnItemClick(boolean activateOnItemClick){
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallbacks.onItemSelected(id);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mSelectedPostion,false);
        } else {
            getListView().setItemChecked(position,true);
        }
        mSelectedPostion = position;
    }


    private final LoaderManager.LoaderCallbacks<Cursor> fLoaderCallbacks  =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    return new NotesLoader(getActivity());
                }
                @Override
                public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                    mAdapter.swapCursor(cursor);

                }
                @Override
                public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    mAdapter.swapCursor(null);
                }
            };
}
