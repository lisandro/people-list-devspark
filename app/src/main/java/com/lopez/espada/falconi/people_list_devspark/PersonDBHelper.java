package com.lopez.espada.falconi.people_list_devspark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gabriel on 31/07/15.
 */
public class PersonDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "persons.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PERSON = "comments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_PHOTO = "photo";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PERSON + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null,"
            + COLUMN_PHONE + " text,"
            + COLUMN_EMAIL + " text,"
            + COLUMN_ADDRESS + " text,"
            + COLUMN_DOB + " text,"
            + COLUMN_PHOTO + " blob"
            +");";

    public PersonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PersonDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);
    }

}