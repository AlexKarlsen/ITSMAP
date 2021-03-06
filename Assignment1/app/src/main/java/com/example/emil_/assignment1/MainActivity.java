package com.example.emil_.assignment1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Request codes
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_EDIT = 2;

    // Saved instances keys. Also used in the intents passed between the activities
    static final String NAME_KEY = "name_key";
    static final String ID_KEY = "id_key";
    static final String ANDROID_KEY = "android_key";
    static final String BITMAP_KEY = "bitmap_key";

    // Views
    TextView nameTextView;
    TextView idTextView;
    TextView androidTextView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.NameTextView);
        idTextView = (TextView) findViewById(R.id.IdTextView);
        androidTextView = (TextView) findViewById(R.id.AndroidTextView);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        // recovering the instance state
        if (savedInstanceState != null) {
            nameTextView.setText(savedInstanceState.getString(NAME_KEY));
            if (savedInstanceState.getInt(ID_KEY) != -1)
                idTextView.setText(String.valueOf(savedInstanceState.getInt(ID_KEY)));
            androidTextView.setText(savedInstanceState.getString(ANDROID_KEY));

            imageButton.setImageBitmap((Bitmap) savedInstanceState.getParcelable(BITMAP_KEY));
        }

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToEdit();
            }});

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    public void NavigateToEdit()
    {
        Intent intent = new Intent(this, EditMainActivity.class);
        intent.putExtra(NAME_KEY, nameTextView.getText().toString());
        intent.putExtra(ID_KEY, getTextViewTextAsInt(idTextView));
        intent.putExtra(ANDROID_KEY, androidTextView.getText().toString());

        startActivityForResult(intent, REQUEST_EDIT);
    }

    // Heavily inspired by
    // https://developer.android.com/training/camera/photobasics.html
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageButton.setImageBitmap(imageBitmap);
        }
        else if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NAME_KEY);
            // Default value should never be needed as the conversion is handled correctly before it is put in the intent
            int id = data.getIntExtra(ID_KEY, -1);
            String android = data.getStringExtra(ANDROID_KEY);

            nameTextView.setText(name);
            if (id != -1)
                idTextView.setText(String.valueOf(id));
            androidTextView.setText(android);
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(NAME_KEY, nameTextView.getText().toString());
        outState.putInt(ID_KEY, getTextViewTextAsInt(idTextView));
        outState.putString(ANDROID_KEY, androidTextView.getText().toString());

        outState.putParcelable(BITMAP_KEY, ((BitmapDrawable)imageButton.getDrawable()).getBitmap());
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    // Helper function to get id from IdTextView as int to ensure conversion failure is handled correctly and consistently.
    // Will return -1 if parse fails.
    public static int getTextViewTextAsInt(TextView textView)
    {
        int returnInt = -1;
        try
        {
            returnInt = Integer.parseInt(textView.getText().toString());
        }
        catch (NumberFormatException e)
        {}

        return returnInt;
    }
}
