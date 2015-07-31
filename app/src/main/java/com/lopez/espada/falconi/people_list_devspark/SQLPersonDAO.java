package com.lopez.espada.falconi.people_list_devspark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 31/07/15.
 */
public class SQLPersonDAO implements PersonDAO {

    private SQLiteDatabase database;
    private PersonDBHelper dbHelper;
    private String[] allColumns = {
            PersonDBHelper.COLUMN_ID,
            PersonDBHelper.COLUMN_NAME,
            PersonDBHelper.COLUMN_EMAIL,
            PersonDBHelper.COLUMN_PHONE,
            PersonDBHelper.COLUMN_ADDRESS,
            PersonDBHelper.COLUMN_DOB
    };

    public SQLPersonDAO(Context context) {
        dbHelper = new PersonDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public Person savePerson(Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonDBHelper.COLUMN_NAME, person.getName());
        values.put(PersonDBHelper.COLUMN_EMAIL, person.getEmail());
        values.put(PersonDBHelper.COLUMN_PHONE, person.getPhone());
        values.put(PersonDBHelper.COLUMN_ADDRESS, person.getAddress());
        values.put(PersonDBHelper.COLUMN_DOB, person.getDob());
        // Oh my SQLite lord, please save this person from the futility of thy life.
        long insertId = database.insert(PersonDBHelper.TABLE_PERSON, null,
                values);

        Cursor cursor = database.query(PersonDBHelper.TABLE_PERSON,
                allColumns, PersonDBHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Person newPerson = cursorToPerson(cursor);
        cursor.close();
        return newPerson;
    }

    @Override
    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<Person>();

        Cursor cursor = database.query(PersonDBHelper.TABLE_PERSON,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Person person = cursorToPerson(cursor);
            persons.add(person);
            cursor.moveToNext();
        }
        cursor.close();
        return persons;
    }

    /*public void deletePerson(Person person) {
        long id = person.getId();
        database.delete(PersonDBHelper.TABLE_PERSON, PersonDBHelper.COLUMN_ID
                + " = " + id, null);
    }*/

    private Person cursorToPerson(Cursor cursor) {
        Person person = new Person();
        person.setId(cursor.getInt(cursor.getColumnIndex(PersonDBHelper.COLUMN_ID)));
        person.setName(cursor.getString(cursor.getColumnIndex(PersonDBHelper.COLUMN_NAME)));
        person.setAddress(cursor.getString(cursor.getColumnIndex(PersonDBHelper.COLUMN_ADDRESS)));
        person.setEmail(cursor.getString(cursor.getColumnIndex(PersonDBHelper.COLUMN_EMAIL)));
        person.setPhone(cursor.getString(cursor.getColumnIndex(PersonDBHelper.COLUMN_PHONE)));
        person.setDob(cursor.getString(cursor.getColumnIndex(PersonDBHelper.COLUMN_DOB)));
        return person;
    }
}
