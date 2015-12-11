package com.example.jared.movietrailers;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_VIDEOID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION ="description";
    public static final String KEY_THUMBNAIL ="thumbnail";
    public static final String KEY_VIDEO ="vidoe";
    public static final String KEY_RATING ="rating";
    public static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "movie_trailers";
    private static final String DATABASE_TABLE = "videos";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table videos (_id integer primary key autoincrement,"
                    + "title text not null,description text not null, "
                    + "thumbnail text, vidoe text, rating integer);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }//end method onUpgrade
    }

    //open the database
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //insert a contact into the database
    public long insertVideo(String title,String description, String thumbnail, String video)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_THUMBNAIL, thumbnail);
        initialValues.put(KEY_VIDEO, video);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
//
//    //delete a particular contact
//    public boolean deleteContact(long rowId)
//    {
//        return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + rowId,null) >0;
//    }

    //retrieve all the contacts
    public Cursor getAllVideos()
    {
        return db.query(DATABASE_TABLE,new String[]{KEY_VIDEOID,KEY_TITLE,
                KEY_DESCRIPTION, KEY_THUMBNAIL, KEY_VIDEO, KEY_RATING},null,null,null,null,null);
    }
//
//    //retrieve a single contact
//    public Cursor getContact(long rowId) throws SQLException
//    {
//        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                KEY_NAME,KEY_EMAIL},KEY_ROWID + "=" + rowId,null,null,null,null,null);
//        if(mCursor != null)
//        {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }
//
//    //updates a contact
//    public boolean updateContact(long rowId,String name,String email)
//    {
//        ContentValues cval = new ContentValues();
//        cval.put(KEY_NAME, name);
//        cval.put(KEY_EMAIL, email);
//        return db.update(DATABASE_TABLE, cval, KEY_ROWID + "=" + rowId,null) >0;
//    }

}//end class DBAdapter













