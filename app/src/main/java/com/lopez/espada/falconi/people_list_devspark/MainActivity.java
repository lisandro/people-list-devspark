package com.lopez.espada.falconi.people_list_devspark;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {//ActionBarActivity

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
       /* Person p1 = new Person();
        p1.setName("Jose Hardcodeado");
        personList.add(p1);*/
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("MainActivity", "select on list");
        if (id == R.id.add_person) {
            Intent intent = new Intent(this, NewEditPerson.class);
            startActivityForResult(intent, NEW_EDIT_PERSON);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("MainActivity", "onActivityResult " + requestCode + " " + resultCode);
        if (resultCode == RESULT_OK && requestCode == NEW_EDIT_PERSON) {
            Person person = intent.getParcelableExtra(PERSON);
            int position = intent.getIntExtra(PERSON_POSITION, -1);
            if (position != -1) {
                personList.set(position,person);
            } else {
                personList.add(person);
            }
            dao.savePerson(person);
            personListAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
