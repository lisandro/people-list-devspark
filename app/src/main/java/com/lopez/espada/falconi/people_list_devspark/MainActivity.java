package com.lopez.espada.falconi.people_list_devspark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final String PERSON = "PERSON";
    public static final String PERSON_POSITION = "POSITION";

    private static final int NEW_EDIT_PERSON = 1;

    private ArrayList<Person> personList;
    private PersonListAdapter personListAdapter;
    private ListView personListView;

    private SQLPersonDAO dao; // I don't think this should be on the main thread D=

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new SQLPersonDAO(this);
        dao.open();

        bindViews();

        personList = new ArrayList<Person>();
        for(Person p : dao.getAllPersons()) {
            personList.add(p);
        }
        personListAdapter = new PersonListAdapter(this, personList);
        personListView.setAdapter(personListAdapter);
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }

    private void bindViews() {
        personListView = (ListView) findViewById(R.id.person_list_view);
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MainActivity", "click on list");
                Intent intent = new Intent(MainActivity.this, NewEditPerson.class);
                intent.putExtra(PERSON, personList.get(position));
                intent.putExtra(PERSON_POSITION, position);
                startActivityForResult(intent, NEW_EDIT_PERSON);
            }
        });
        Button addPersonButton = (Button) findViewById(R.id.add_person);
        addPersonButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewEditPerson.class);
                startActivityForResult(intent, NEW_EDIT_PERSON);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("MainActivity", "onActivityResult " + requestCode + " " + resultCode);
        if (resultCode == RESULT_OK && requestCode == NEW_EDIT_PERSON) {
            Person person = intent.getParcelableExtra(PERSON);
            int position = intent.getIntExtra(PERSON_POSITION, -1);
            if (position != -1) {
                personList.set(position,person);
                dao.updatePerson(person);
            } else {
                personList.add(person);
                dao.savePerson(person);
            }
            personListAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
