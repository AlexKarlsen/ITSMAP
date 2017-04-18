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

    EditText editTextName;
    EditText editTextId;
    EditText editTextAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        editTextName = (EditText) findViewById(R.id.editProfileName);
        editTextName.setFocusable(false);
        editTextName.setEnabled(false);

        editTextId = (EditText) findViewById(R.id.editProfileldNumber);
        editTextId.setFocusable(false);
        editTextId.setEnabled(false);

        editTextAndroid = (EditText) findViewById(R.id.IsDeveloper);
        editTextAndroid.setFocusable(false);
        editTextAndroid.setEnabled(false);

        Button editButton = (Button) findViewById(R.id.Edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToEditProfile();
            }
        });

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
        intent.putExtra("Name", editTextName.getText().toString());
        intent.putExtra("Id", editTextId.getText().toString());
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
            editTextId.setText(data.getStringExtra("Id"));
            editTextAndroid.setText(data.getStringExtra("Android"));
        }
    }



}
