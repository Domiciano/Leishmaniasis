package i2t.cideim.leishmaniasis;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import i2t.cideim.R;
import i2t.cideim.custom.TriangleView;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.dto.Constants;
import i2t.cideim.dto.Document;
import i2t.cideim.dto.UlcerImgDTO;
import i2t.cideim.dto.Usuario;
import i2t.cideim.model.Evaluation;
import i2t.cideim.model.LiderComunitario;
import i2t.cideim.model.Patient;
import i2t.cideim.model.UlcerImg;
import i2t.cideim.snd.services.WebserviceConsumer;
import i2t.cideim.util.LeishConstants;

/**
 * Created by Leonardo.
 * This activity allows the users to login.
 */

public class MainActivity extends Activity implements WebserviceConsumer.ServerResponseReceiver{

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
        //Se consulta si está en dB y se ingresa
        if (user != null) {

            //[ ] Subir info a db tanto usuario como lista de documentos y pacientes, luego ir a la otra activity

            Intent intent = new Intent(this, PatientsActivity.class);
            intent.putExtra("userId", user.getIdentification());
            startActivity(intent);

        }
        //Si no está en dB se consulta al server
        else {
            WebserviceConsumer.GetUserByCedula controller = new WebserviceConsumer
                    .GetUserByCedula(editTextName.getText().toString());
            controller.setObserver(this);
            controller.start();
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

    @Override
    public void onServerResponse(Object object) {
        try {
            if(object instanceof String){
                String texto = (String) object;
                switch (texto){
                    case Constants.IOEX:
                        //NO HAY INTERNET
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Informar que no existe
                                textViewTitle.setText(R.string.no_internet_connection);
                            }
                        });
                        break;
                }
            }else if (object instanceof Usuario) {
                Usuario user = (Usuario) object;
                if (user.name != null) {

                    //[OK] Primero se almacena en db al usuario
                    LiderComunitario liderComunitario = new LiderComunitario(user.id, user.nationalId, user.name, user.lastname);
                    db.addUser(liderComunitario);

                    //[OK] Como existe en base de datos, se debe descargar la info
                    WebserviceConsumer.GetDocumentListOfUserByCedula controller = new WebserviceConsumer
                            .GetDocumentListOfUserByCedula(user.nationalId);
                    controller.setObserver(this);
                    controller.start();

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Informar que no existe
                            textViewTitle.setText(R.string.login_user_does_not_exist);
                        }
                    });
                }
            } else if (object instanceof Document[]) {
                Document[] docs = (Document[]) object;
                //[ ] Almacenar en DB local y pasar a la siguiente actividad
                if (docs.length == 0) return;

                //PACIENTES
                String evaluadorId = docs[0].evaluadorId;
                Patient[] patients = Document.findPatientsOfList(docs);
                for (int i = 0; i < patients.length; i++) {
                    db.addPatient(patients[i], evaluadorId);
                }

                //EVALUACIONES
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                for (int i = 0; i < docs.length; i++) {
                    Document actual = docs[i];
                    Evaluation evaluacion = new Evaluation(actual.id, actual.ulcerasBordesElevados, actual.lesionesAgrupadas, actual.localizacionCabeza, actual.localizacionTronco, actual.localizacionBrazoIzquierdo, actual.localizacionBrazoDerecho, actual.localizacionPiernaIzquierda, actual.localizacionPiernaDerecha, actual.actividadRiesgo, actual.antecedentes, actual.contactoManta, actual.date, actual.umbral, actual.puntaje, true);
                    db.addEvaluation(evaluacion, actual.pacienteId);
                    for (int j = 0; j < actual.fotoLesiones.size(); j++) {
                        UlcerImgDTO imgDTO = actual.fotoLesiones.get(j);
                        UlcerImg ulcerIMG = new UlcerImg(imgDTO.id, imgDTO.bodyLocation, format.parse(imgDTO.imgDate), imgDTO.filename, imgDTO.url, "0");
                        db.addUlcerIMG(ulcerIMG, actual.id);
                    }
                }

                Intent intent = new Intent(this, PatientsActivity.class);
                intent.putExtra("userId", evaluadorId);
                startActivity(intent);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
