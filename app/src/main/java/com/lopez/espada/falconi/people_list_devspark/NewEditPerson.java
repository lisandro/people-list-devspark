package com.lopez.espada.falconi.people_list_devspark;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lisandro Falconi on 28/07/15.
 *
 */
public class NewEditPerson extends AppCompatActivity {
    private Toolbar toolbar;
    private Button imagePickerButton;
    private Button savePersonButton;
    private Button cancelPersonButton;
    private EditText personNameField;
    private EditText personPhoneField;
    private EditText personEmailField;
    private EditText personAddressField;
    private EditText personDoBField;
    private ImageView personPhotoView;
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

    /**
     * Convert from bitmap to byte array
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * Convert from byte array to bitmap
     *
     * @param image
     * @return
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
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

    private void bindViews() {
        String defaultPicPath = "com.lopez.espada.falconi.people_list_devspark:drawable/person_default_page";

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

        personPhotoView = (ImageView) findViewById(R.id.imageView);
        imagePickerButton = (Button) findViewById(R.id.imagePickerButton);
        personNameField = (EditText) findViewById(R.id.person_name);
        personPhoneField = (EditText) findViewById(R.id.person_phone);
        personEmailField = (EditText) findViewById(R.id.person_email);
        personAddressField = (EditText) findViewById(R.id.person_address);
        personDoBField = (EditText) findViewById(R.id.person_dob);
        savePersonButton = (Button) findViewById(R.id.save_button);
        cancelPersonButton = (Button) findViewById(R.id.cancel_button);
        personPhotoView.setImageResource(getResources().getIdentifier(defaultPicPath, null, null));

        if (person != null) {
            personNameField.setText(person.getName());
            personPhoneField.setText(person.getPhone());
            personEmailField.setText(person.getEmail());
            personAddressField.setText(person.getAddress());
            personDoBField.setText(person.getDob());
            personPhotoView.setImageBitmap(getImage(person.getPhoto()));
        }
        cancelPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        imagePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
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
        if (personPhotoView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) personPhotoView.getDrawable()).getBitmap();
            person.setPhoto(getBytes(bitmap));
        }

        if (this.person != null) {
            person.setId(this.person.getId());
        }
        return person;
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
        } catch (android.content.ActivityNotFoundException ignored) {
        }
    }

    //Profile image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);
            personPhotoView.setImageURI(imageUri);
            Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickProfileImage.jpeg"));
        }

        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Save the selected image as profile picture
     * TO USE in the future
     * @param sourceUri
     */
    private void saveFile(Uri sourceUri) {
        String sourceFilename = sourceUri.getPath();
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "profilePic.jpeg";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException ignored) {

        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // Collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // Collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // The main intent is the last in the list so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Profile picture");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
}
