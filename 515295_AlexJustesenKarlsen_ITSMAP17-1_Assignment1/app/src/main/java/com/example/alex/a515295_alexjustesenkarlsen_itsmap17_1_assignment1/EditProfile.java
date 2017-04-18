package com.example.alex.a515295_alexjustesenkarlsen_itsmap17_1_assignment1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class EditProfile extends AppCompatActivity {

    //Saved state keys
    static final String PROFILE_NAME = "Name";
    static final String PROFILE_ID = "Id";
    static final String ANDROID = "Android";

    //UI elements
    EditText NameEditText;
    EditText IdEditText;
    RadioButton YesRadioButton;
    RadioButton NoRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Variables to acquire from intent or saveState
        String name = "default";
        String id = "default";
        String android = "default";

        //Get references to UI elements
        NameEditText = (EditText) findViewById(R.id.editName);
        IdEditText = (EditText) findViewById(R.id.editId);
        YesRadioButton = (RadioButton) findViewById(R.id.radioYes);
        NoRadioButton = (RadioButton) findViewById(R.id.radioNo);

        //Get Intent and it's values
        Intent FromActivityShowProfile = getIntent();
        if(FromActivityShowProfile != null) {
            name = FromActivityShowProfile.getStringExtra("Name");
            id = FromActivityShowProfile.getStringExtra("Id");
            android = FromActivityShowProfile.getStringExtra("Android");
        }

        //Retrieve from saveState
        if(savedInstanceState != null) {
            name = savedInstanceState.getString(PROFILE_NAME);
            id = savedInstanceState.getString(PROFILE_ID);
        }

        //Set UI elements
        NameEditText.setText(name);
        IdEditText.setText(id);

        if(android.equals(getResources().getString(R.string.yes))) {
            YesRadioButton.setChecked(true);
        }
        else if (android.equals(getResources().getString(R.string.no))) {
            NoRadioButton.setChecked(true);
        }

        //Handling save event
        Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        //Handle cancel event
        Button cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });


    }

    private void Cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void Save()
    {
        Intent intent = new Intent();
        intent.putExtra("Name",NameEditText.getText().toString());
        intent.putExtra("Id",IdEditText.getText().toString());

        if (YesRadioButton.isChecked() == true) intent.putExtra("Android", getResources().getString(R.string.yes));
        else if (NoRadioButton.isChecked() == true) intent.putExtra("Android", getResources().getString(R.string.no));

        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(PROFILE_NAME, NameEditText.getText().toString());
        outState.putString(PROFILE_ID, IdEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
