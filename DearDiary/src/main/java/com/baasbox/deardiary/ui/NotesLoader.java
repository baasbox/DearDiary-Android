package com.baasbox.deardiary.ui;

import android.content.Context;
import android.support.v4.content.CursorLoader;
import com.baasbox.deardiary.model.Contract;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
class NotesLoader extends CursorLoader {

    private final static String[] PROJECTION =
            {
                    Contract.Notes._ID,
                    Contract.Notes._TITLE,
                    Contract.Notes._DATE
            };

    private final static String SORT = Contract.Notes._DATE+ " ASC";

    NotesLoader(Context context) {
        super(context, Contract.Notes.CONTENT_URI, PROJECTION, null, null, SORT);
    }
}
