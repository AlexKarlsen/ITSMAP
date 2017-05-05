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
        int id = 0;
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
            id = FromActivityShowProfile.getIntExtra("Id", -1);
            android = FromActivityShowProfile.getStringExtra("Android");
        }

        //Retrieve from saveState
        if(savedInstanceState != null) {
            name = savedInstanceState.getString(PROFILE_NAME);
            id = savedInstanceState.getInt(PROFILE_ID);
        }

        //Set UI elements
        NameEditText.setText(name);

        //If id=-1 then the text field was empty and should remain that way. (Stupid conversion to int instead of keeping it as a string)
        if(id == -1)
            IdEditText.setText("");
        else
            IdEditText.setText(String.valueOf(id));

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
        intent.putExtra("Name", NameEditText.getText().toString());
        
        //Converting from string to integer, is done due to assignment requirements
        //Redundant conversion from string to int
        intent.putExtra("Id", Integer.parseInt(IdEditText.getText().toString()));

        //No conversion to boolean because i want to keep the language independent and a true/false boolean is not
        if (YesRadioButton.isChecked() == true) intent.putExtra("Android", getResources().getString(R.string.yes));
        else if (NoRadioButton.isChecked() == true) intent.putExtra("Android", getResources().getString(R.string.no));

        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(PROFILE_NAME, NameEditText.getText().toString());
        
        //Converting to int required
        outState.putInt(PROFILE_ID, Integer.parseInt(IdEditText.getText().toString()));
        super.onSaveInstanceState(outState);
    }
}
