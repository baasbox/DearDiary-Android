package com.baasbox.deardiary.ui;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import com.baasbox.deardiary.model.Contract;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
class NotesAdapter extends SimpleCursorAdapter {

    private static final String[] FROM ={
            Contract.Notes._TITLE,
            Contract.Notes._TITLE
    };

    private static final int[] TO = {
        android.R.id.text1,
        android.R.id.text2
    };
    public NotesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2, null, FROM, TO, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }
}
