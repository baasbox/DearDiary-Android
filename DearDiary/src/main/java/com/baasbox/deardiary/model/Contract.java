package com.baasbox.deardiary.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.view.ContextThemeWrapper;

/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public final class Contract  {
    private Contract(){}

    public final static String AUTHORITY ="com.baasbox.deardiary.provider";

    public final static Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public final static class Notes {
        public final static String PATH = "notes";

        public final static Uri CONTENT_URI = Contract.CONTENT_URI.buildUpon().appendPath(PATH).build();

        public final static String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/vnd.deardiary";
        public final static String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/vnd.deardiary";

        public final static String _ID = BaseColumns._ID;
        public final static String _TITLE = "title";
        public final static String _CONTENT = "content";

        public static Note loadNote(ContentResolver cr,Uri uri){
            Cursor c = null;
            try{
                c= cr.query(uri,null,null,null,null);
                if(c!=null && c.moveToFirst()){
                    Note n = new Note();
                    n.setTitle(c.getString(c.getColumnIndexOrThrow(_TITLE)));
                    n.setNote(c.getString(c.getColumnIndexOrThrow(_CONTENT)));
                    n.setDate(new Time());
                    return n;
                }
                return null;
            }finally {
                if(c!=null && !c.isClosed()){
                    c.close();
                }
            }

        }
    }
}
