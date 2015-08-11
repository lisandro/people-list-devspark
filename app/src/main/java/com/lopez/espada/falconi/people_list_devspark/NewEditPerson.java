package com.lopez.espada.falconi.people_list_devspark;

import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Lisandro Falconi on 28/07/15.
 *
 */
public class NewEditPerson extends AppCompatActivity {
    private Toolbar toolbar;
    private Button savePersonButton;
    private Button cancelPersonButton;
    private EditText personNameField;
    private EditText personPhoneField;
    private EditText personEmailField;
    private EditText personAddressField;
    private EditText personDoBField;
    private Person person;
    private int personPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_form);

        Intent i = getIntent();
        person = i.getParcelableExtra(MainActivity.PERSON);
        personPosition = i.getIntExtra(MainActivity.PERSON_POSITION, -1);

        bindViews();
    }

    private void bindViews() {

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_home_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        personNameField  = (EditText) findViewById(R.id.person_name);
        personPhoneField  = (EditText) findViewById(R.id.person_phone);
        personEmailField  = (EditText) findViewById(R.id.person_email);
        personAddressField = (EditText) findViewById(R.id.person_address);
        personDoBField =  (EditText) findViewById(R.id.person_dob);
        savePersonButton = (Button)findViewById(R.id.save_button);
        cancelPersonButton = (Button)findViewById(R.id.cancel_button);

        if (person != null) {
            personNameField.setText(person.getName());
            personPhoneField.setText(person.getPhone());
            personEmailField.setText(person.getEmail());
            personAddressField.setText(person.getAddress());
            personDoBField.setText(person.getDob());
        }
        cancelPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        savePersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO extract PersonValidator
                if (personNameField.getText() == null ||
                        personNameField.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Dude! What kind of Person doesn't have a name? ",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra(MainActivity.PERSON, buildPersonFromFields());
                returnIntent.putExtra(MainActivity.PERSON_POSITION, personPosition);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private Person buildPersonFromFields() {
        Person person = new Person();
        person.setName(personNameField.getText().toString());
        person.setAddress(personAddressField.getText().toString());
        person.setEmail(personEmailField.getText().toString());
        person.setPhone(personPhoneField.getText().toString());
        person.setDob(personDoBField.getText().toString());
        if (this.person != null) {
            person.setId(this.person.getId());
        }
        return person;
    }

    public void callContact(View view) {
        String phone = personPhoneField.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    public void sendSMS(View view) {
        String phone = personPhoneField.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null));
        startActivity(intent);
    }

    public void sendEmail(View view) {
        String email = personEmailField.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Body");
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {}
    }
}
