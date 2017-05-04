package com.example.emil_.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class EditMainActivity extends AppCompatActivity {

    // Views
    EditText nameEditText;
    EditText idEditText;
    RadioButton trueRadioButton;
    RadioButton falseRadioButton;

    // Saved instances keys. Also used in the intents passed between the activities
    static final String NAME_KEY = "name_key";
    static final String ID_KEY = "id_key";
    static final String ANDROID_KEY = "android_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_main);

        // Get initial values from previous activity
        nameEditText = (EditText) findViewById(R.id.NameEditText);
        idEditText = (EditText) findViewById(R.id.IdEditText);
        trueRadioButton = (RadioButton) findViewById(R.id.TrueRadioButton);
        falseRadioButton = (RadioButton) findViewById(R.id.FalseRadioButton);

        Intent fromActivityMain = getIntent();
        String name = fromActivityMain.getStringExtra(NAME_KEY);
        String id = fromActivityMain.getStringExtra(ID_KEY);
        String android = fromActivityMain.getStringExtra(ANDROID_KEY);

        nameEditText.setText(name);
        idEditText.setText(id);
        if (android.equals(getResources().getString(R.string.trueword))) {
            trueRadioButton.setChecked(true);
        }
        else if (android.equals(getResources().getString(R.string.falseword))) {
            falseRadioButton.setChecked(true);
        }

        // Event handlers
        Button saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
    }

    public void Save(){
        Intent intent = new Intent(this, EditMainActivity.class);
        intent.putExtra(NAME_KEY, nameEditText.getText().toString());
        intent.putExtra(ID_KEY, idEditText.getText().toString());

        String android;
        if (trueRadioButton.isChecked())
            android = getResources().getString(R.string.trueword);
        else if (falseRadioButton.isChecked())
            android = getResources().getString(R.string.falseword);
        else
            android = getResources().getString(R.string.defaultinput);

        intent.putExtra(ANDROID_KEY, android);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void Cancel(){
        setResult(RESULT_CANCELED);
        finish();
    }
}
