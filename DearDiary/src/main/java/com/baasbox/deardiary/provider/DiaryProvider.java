package com.baasbox.deardiary.provider;

import android.content.*;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.database.DatabaseUtilsCompat;
import com.baasbox.deardiary.model.Contract;

public class DiaryProvider extends ContentProvider {
    private DiaryDbHelper mDb;

    public DiaryProvider() {
    }

    @Override
    public boolean onCreate() {
        mDb = new DiaryDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)){
            case ALL_NOTES: return Contract.Notes.DIR_TYPE;
            case NOTE_BY_ID: return Contract.Notes.ITEM_TYPE;
            default: throw new UnsupportedOperationException("Unsupported uri");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = MATCHER.match(uri);
        switch (match){
            case ALL_NOTES:
                SQLiteDatabase db = mDb.getWritableDatabase();
                long id = db.insert(Contract.Notes.PATH, null, values);
                if (id!=-1){
                    getContext().getContentResolver().notifyChange(uri,null);
                    return ContentUris.withAppendedId(uri,id);
                }
                break;
        }
        return null;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = MATCHER.match(uri);
        switch (match){
            case ALL_NOTES:
                break;
            case NOTE_BY_ID:
                String whereId = Contract.Notes._ID+" = "+ContentUris.parseId(uri);
                selection = DatabaseUtilsCompat.concatenateWhere(selection,whereId);
                break;
        }
        SQLiteDatabase db = mDb.getWritableDatabase();
        int delete = db.delete(Contract.Notes.PATH, selection, selectionArgs);
        if (delete!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return delete;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = MATCHER.match(uri);
        switch (match){
            case ALL_NOTES:
                break;
            case NOTE_BY_ID:
                String whereId = Contract.Notes._ID+" = "+ContentUris.parseId(uri);
                selection = DatabaseUtilsCompat.concatenateWhere(selection,whereId);
                break;
        }
        SQLiteDatabase db = mDb.getWritableDatabase();
        int updates = db.update(Contract.Notes.PATH,values,selection,selectionArgs);
        if (updates!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updates;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        int match = MATCHER.match(uri);
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(Contract.Notes.PATH);
        switch (match) {
            case ALL_NOTES:

                break;
            case NOTE_BY_ID:
                String whereId = Contract.Notes._ID+" = "+ContentUris.parseId(uri);
                sqb.appendWhere(whereId);
                break;
        }
        SQLiteDatabase db = mDb.getReadableDatabase();
        Cursor c = sqb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        if (c!=null){
            c.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return c;
    }


    private final static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int ALL_NOTES = UriMatcher.NO_MATCH+1;
    private final static int NOTE_BY_ID = ALL_NOTES+1;

    static {
        MATCHER.addURI(Contract.AUTHORITY,Contract.Notes.PATH,ALL_NOTES);
        MATCHER.addURI(Contract.AUTHORITY,Contract.Notes.PATH+"/#",NOTE_BY_ID);
    }

    private final static String DB_NAME = "deardiary.db";
    private final static int DB_VERSION = 3;

    //language=SQLite
    private final static String CREATE_TABLE = "create table if not exists "+ Contract.Notes.PATH+
            "("+Contract.Notes._ID+ " integer primary key autoincrement," +
            Contract.Notes._TITLE+" text not null," +
            Contract.Notes._CONTENT+" text not null default (''))";

    //language=SQLite
    private final static String DROP_TABLE = "drop table if exists "+Contract.Notes.PATH;

    private static class DiaryDbHelper extends SQLiteOpenHelper{

        DiaryDbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion>oldVersion){
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }

        }
    }
}
