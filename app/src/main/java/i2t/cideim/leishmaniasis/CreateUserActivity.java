package i2t.cideim.leishmaniasis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import i2t.cideim.R;
import i2t.cideim.custom.TriangleView;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.model.LiderComunitario;

/**
 * Created by Leonardo.
 * This activity allows to create a new user (former líder comunitario).
 */

public class CreateUserActivity extends Activity {

    private TextView textViewTitle;
    private EditText editTextName;
    private EditText editTextLastName;
    private ToggleButton toggleButtonM;
    private ToggleButton toggleButtonF;
    private EditText editTextId;
    private TriangleView triangleName;
    private TriangleView triangleLastName;
    private TriangleView triangleId;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Loads the database
        db = new DatabaseHandler(this);

        // Editable fields
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        toggleButtonM = (ToggleButton) findViewById(R.id.toggleButtonM);
        toggleButtonF = (ToggleButton) findViewById(R.id.toggleButtonF);
        editTextId = (EditText) findViewById(R.id.editTextId);

        // UX components
        triangleName = (TriangleView) findViewById(R.id.triangleAbout);
        triangleLastName = (TriangleView) findViewById(R.id.triangleLastName);
        triangleId = (TriangleView) findViewById(R.id.triangleId);

        // Interaction between components
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleName.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        editTextLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleLastName.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        editTextId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleId.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    /* Creates the new user in the database */
    public void onButtonCreateClick(View view) {
        // Obtains the data from the editable fields
        String name = editTextName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String cedula = editTextId.getText().toString();
        char genre = toggleButtonM.isChecked() ? 'M' : 'F';

        // Validates that the required data is available
        if (name.trim().length() != 0 && lastName.trim().length() != 0 && cedula.trim().length() != 0) {

            // Creates the user
            LiderComunitario liderComunitario = new LiderComunitario(cedula, name, lastName);
            int result = db.addUser(liderComunitario);

            if (result == DatabaseHandler.SUCCESS) {
                super.onBackPressed();
            } else if (result == DatabaseHandler.RECORD_EXIST) {
                textViewTitle.setText(R.string.create_user_exist);
            }
        } else {
            textViewTitle.setText(R.string.create_non_valid_data);
        }
    }

    /* Toggle buttons interaction. Genre */
    public void onButtonGenreMClick(View view) {
        toggleButtonF.setChecked(false);
        toggleButtonM.setChecked(true);
    }

    /* Toggle buttons interaction. Genre */
    public void onButtonGenreFClick(View view) {
        toggleButtonM.setChecked(false);
        toggleButtonF.setChecked(true);
    }

    /* Alerts the user that the unsaved data will be lost */
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.back_alert);
        builder.setPositiveButton(R.string.go_back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CreateUserActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton(R.string.stay, null);
        builder.create().show();
    }
}
