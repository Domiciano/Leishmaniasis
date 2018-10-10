package i2t.cideim.leishmaniasis;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import i2t.cideim.R;
import i2t.cideim.custom.TriangleView;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.model.LiderComunitario;
import i2t.cideim.util.LeishConstants;

/**
 * Created by Leonardo.
 * This activity allows the users to login.
 */

public class MainActivity extends Activity {

    private TextView textViewTitle;
    private EditText editTextName;
    //private EditText editTextId;
    private TriangleView triangleName;
    private TriangleView triangleId;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
            }, 11);
        }else{
            crearCarpetaFotos();
        }

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        editTextName = (EditText) findViewById(R.id.editTextName);
        //editTextId = (EditText) findViewById(R.id.editTextId);
        triangleName = (TriangleView) findViewById(R.id.triangleAbout);
        triangleId = (TriangleView) findViewById(R.id.triangleId);

        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleName.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        /*
        editTextId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleId.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
        */

        db = new DatabaseHandler(this);
    }

    private void crearCarpetaFotos() {
        File carpeta = new File(Environment.getExternalStorageDirectory()+"/"+ LeishConstants.FOLDER);
        if(!carpeta.exists()) carpeta.mkdirs();
    }

    /* Shows an activity to create a new user */
    public void onButtonAddClick(View view) {
        Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
        startActivity(intent);
    }

    /* Performs a simple login procedure, that consist in comparing the name and the id of the user */
    public void onButtonEnterClick(View view) {
        LiderComunitario user = db.getMinimizedUser(editTextName.getText().toString());
        if (user != null) {
            //String name = editTextName.getText().toString().toLowerCase();
            //if (user.getName().toLowerCase().equals(name)) {
            Intent intent = new Intent(this, PatientsActivity.class);
            intent.putExtra("userId", user.getIdentification());
            startActivity(intent);
            //} else {
            //    textViewTitle.setText(R.string.login_invalid_data);
            //}
        } else {
            textViewTitle.setText(R.string.login_user_does_not_exist);
        }
    }

    /* Shows the about menu */
    public void onAboutButtonPressed(View view) {
        AboutDialogFragment dialog = new AboutDialogFragment();
        dialog.show(getFragmentManager(), "About");
    }

    /* Shows the information menu */
    public void onInfoButtonPressed(View view) {
        InfoDialogFragment dialog = new InfoDialogFragment();
        dialog.show(getFragmentManager(), "Info");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 11){
            boolean allPermissionsGranted = true;
            for(int i=0 ; i<grantResults.length ; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allPermissionsGranted = false;
                    break;
                }
            }

            if(allPermissionsGranted){
                crearCarpetaFotos();
            }

        }
    }
}
