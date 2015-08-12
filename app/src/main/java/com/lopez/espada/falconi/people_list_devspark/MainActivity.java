package com.lopez.espada.falconi.people_list_devspark;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ViewSwitcher;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String PERSON = "PERSON";
    public static final String PERSON_POSITION = "POSITION";

    private static final int NEW_EDIT_PERSON = 1;

    private List<Person> personList;
    private PersonListAdapter personListAdapter;
    private ListView personListView;
    private GridView personGridView;
    private SQLPersonDAO dao; // I don't think this should be on the main thread D=
    private ViewSwitcher viewSwitcher;
    private EditText searchPersonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new SQLPersonDAO(this);
        dao.open();

        bindViews();

        personList = dao.getAllPersons();
        personListAdapter = new PersonListAdapter(this, personList);
        personListView.setAdapter(personListAdapter);
        personGridView.setAdapter(personListAdapter);
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }

    private void bindViews() {
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        Animation slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        viewSwitcher.setInAnimation(slide_in_left);
        viewSwitcher.setOutAnimation(slide_out_right);

        
        personListView = (ListView) findViewById(R.id.person_list_view);
        personGridView = (GridView) findViewById(R.id.person_grid_view);
        personGridView.setNumColumns(2);

        personListView.setOnItemClickListener(getPersonClickListener());
        personGridView.setOnItemClickListener(getPersonClickListener());

        searchPersonButton = (EditText) findViewById(R.id.search_person);
        searchPersonButton.addTextChangedListener(new TextWatcher() {
            //TODO sanitize input (linebreaks)
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MainActivity.this.personListAdapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        Button addPersonButton = (Button) findViewById(R.id.add_person);
        addPersonButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewEditPerson.class);
                startActivityForResult(intent, NEW_EDIT_PERSON);
            }
        });

        RadioButton listViewRadioButton = (RadioButton) findViewById(R.id.linear_layout_rb);
        listViewRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.setDisplayedChild(0);
            }
        });

        RadioButton gridViewRadioButton = (RadioButton) findViewById(R.id.grid_layout_rb);
        gridViewRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.setDisplayedChild(1);
            }
        });
    }

    @NonNull
    private AdapterView.OnItemClickListener getPersonClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewEditPerson.class);
                intent.putExtra(PERSON, (Person)personListAdapter.getItem(position));
                intent.putExtra(PERSON_POSITION, position);
                startActivityForResult(intent, NEW_EDIT_PERSON);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && requestCode == NEW_EDIT_PERSON) {
            Person person = intent.getParcelableExtra(PERSON);
            int position = intent.getIntExtra(PERSON_POSITION, -1);
            if (position != -1) {
                Person oldPerson = (Person)personListAdapter.getItem(position);
                personList.set(personList.indexOf(oldPerson),person);
                dao.updatePerson(person);
            } else {
                personList.add(person);
                dao.savePerson(person);
            }
            searchPersonButton.setText(searchPersonButton.getText());
            // kinda ugly, trigger text changed so it refilters the list
         //   personListAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
