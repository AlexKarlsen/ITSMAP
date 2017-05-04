package com.example.alex.a515295_alexjustesenkarlsen_itsmap17_1_assignment1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ShowProfile extends AppCompatActivity {

    static final int REQ_IMAGE_CAPTURE = 1;
    static final int REQ_EDIT = 2;

    static final String PROFILE_NAME = "Name";
    static final String PROFILE_ID = "Id";
    static final String ANDROID = "Android";

    EditText editTextName, editTextId, editTextAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        //Get references to UI element and disable for inputs
        editTextName = (EditText) findViewById(R.id.editProfileName);
        editTextName.setFocusable(false);
        editTextName.setEnabled(false);

        editTextId = (EditText) findViewById(R.id.editProfileldNumber);
        editTextId.setFocusable(false);
        editTextId.setEnabled(false);

        editTextAndroid = (EditText) findViewById(R.id.IsDeveloper);
        editTextAndroid.setFocusable(false);
        editTextAndroid.setEnabled(false);

        // recovering the instance state
        if (savedInstanceState != null) {
            editTextName.setText(savedInstanceState.getString(PROFILE_NAME));
            editTextId.setText(Integer.toString(savedInstanceState.getInt(PROFILE_ID)));
            editTextAndroid.setText(savedInstanceState.getString(ANDROID));
        }

        //Handle EditButton
        Button editButton = (Button) findViewById(R.id.Edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToEditProfile();
            }
        });

        //Handle ImageButton
        ImageButton startCameraButton = (ImageButton) findViewById(R.id.imageButton);
        startCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartCamera();
            }
        });
    }

    public void NavigateToEditProfile()
    {
        Intent intent = new Intent(ShowProfile.this, EditProfile.class);
        if(editTextName != null)
            intent.putExtra("Name", editTextName.getText().toString());
        if(editTextId != null && !editTextId.getText().toString().equals(""))
            try {
                intent.putExtra("Id", Integer.parseInt(editTextId.getText().toString()));
            }
            catch(NumberFormatException ex)
            {

            }

        if(editTextAndroid != null)
            intent.putExtra("Android", editTextAndroid.getText().toString());

        startActivityForResult(intent, REQ_EDIT);
    }

    public void StartCamera()
    {
        Intent startCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (startCameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(startCameraIntent, REQ_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQ_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap =(Bitmap) extras.get("data");
            ImageButton button = (ImageButton) findViewById(R.id.imageButton);
            button.setImageBitmap(imageBitmap);
        }
        else if (requestCode == REQ_EDIT && resultCode == RESULT_OK)
        {
            editTextName.setText(data.getStringExtra("Name"));
            editTextId.setText(Integer.toString(data.getIntExtra("Id",0)));
            editTextAndroid.setText(data.getStringExtra("Android"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("Name", editTextName.getText().toString());
        outState.putInt("Id", Integer.parseInt(editTextId.getText().toString()));
        outState.putString("Android", editTextAndroid.getText().toString());
        super.onSaveInstanceState(outState);
    }



}
