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

    EditText NameEditText;
    EditText IdEditText;
    RadioButton YesRadioButton;
    RadioButton NoRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Get references to UI elements
        NameEditText = (EditText) findViewById(R.id.editName);
        IdEditText = (EditText) findViewById(R.id.editId);
        YesRadioButton = (RadioButton) findViewById(R.id.radioYes);
        NoRadioButton = (RadioButton) findViewById(R.id.radioNo);

        //Get Intent and it's values
        Intent FromActivityShowProfile = getIntent();
        String name = FromActivityShowProfile.getStringExtra("Name");
        String id = FromActivityShowProfile.getStringExtra("Id");
        String android = FromActivityShowProfile.getStringExtra("Android");

        //Set UI elements
        NameEditText.setText(name);
        IdEditText.setText(id);

        if(android.equals("True")) {
            YesRadioButton.setChecked(true);
        }
        else if (android.equals("False")) {
            NoRadioButton.setChecked(true);
        }

        Button save = (Button) findViewById(R.id.btnSave);
        Button cancel = (Button) findViewById(R.id.btnCancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

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

        if (YesRadioButton.isChecked() == true) intent.putExtra("Android","True");
        else if (NoRadioButton.isChecked() == true) intent.putExtra("Android", "False");

        setResult(RESULT_OK,intent);
        finish();
    }
}
