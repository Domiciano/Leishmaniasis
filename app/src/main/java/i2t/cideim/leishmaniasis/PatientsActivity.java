package i2t.cideim.leishmaniasis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import i2t.cideim.R;
import i2t.cideim.custom.BroadcastConstants;
import i2t.cideim.custom.PatientsListAdapter;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.dto.Constants;
import i2t.cideim.dto.Document;
import i2t.cideim.dto.PatientDTO;
import i2t.cideim.dto.UlcerImgDTO;
import i2t.cideim.dto.Usuario;
import i2t.cideim.model.Evaluation;
import i2t.cideim.model.LiderComunitario;
import i2t.cideim.model.Patient;
import i2t.cideim.model.UlcerImg;
import i2t.cideim.snd.services.WebserviceConsumer;

/**
 * Created by Leonardo.
 * Activity that shows the list of patients.
 */

public class PatientsActivity extends Activity implements WebserviceConsumer.ServerResponseReceiver {

    private TextView textViewTitle;
    private ListView listViewPatients;

    private PatientsListAdapter adapter;
    private int selectedIndex;
    private DatabaseHandler db;

    private LiderComunitario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        listViewPatients = (ListView) findViewById(R.id.listViewPatients);

        db = new DatabaseHandler(this);

        // Listen to broadcasts from the synchronization server
        IntentFilter syncServiceMessageIntentFilter = new IntentFilter(BroadcastConstants.BROADCAST_ACTION);
        SyncServiceMessageReceiver syncServiceMessageReceiver = new SyncServiceMessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(syncServiceMessageReceiver, syncServiceMessageIntentFilter);


    }

    public void onStart() {
        super.onStart();
        user = db.getUserWithMinimizedPatients(getIntent().getStringExtra("userId"));
        textViewTitle.setText(user.getName());
        selectedIndex = -1;
        loadPatients();
    }

    /* Populates the patients list */
    public void loadPatients() {
        adapter = new PatientsListAdapter(this, user.getPatientList());
        listViewPatients.setAdapter(adapter);

        listViewPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                selectedIndex = i;
            }
        });
    }

    /* Opens the activity to create a new patient */
    public void onButtonAddClick(View view) {
        Intent intent = new Intent(PatientsActivity.this,
                CreatePatientActivity.class);
        intent.putExtra("userId", user.getIdentification());
        startActivity(intent);
    }

    /* Opens the activity to evaluate the selected patient */
    public void onButtonEvaluateClick(View view) {
        if (selectedIndex != -1) {
            Patient selectedPatient = adapter.getItem(selectedIndex);
            Intent intent = new Intent(PatientsActivity.this, EvaluationActivity.class);
            intent.putExtra("patientUUID", selectedPatient.getUUIDNumber().toString());
            intent.putExtra("patientName", selectedPatient.getName() + " " + selectedPatient.getLastName());

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            //TODO: VARIABLE REQUERIDA
            sp.edit()
                    .putString("patientID", selectedPatient.getIdentification())
                    .putString("patientUUID", selectedPatient.getUUIDNumber().toString())
                    .apply();

            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.patients_select, Toast.LENGTH_LONG).show();
        }
    }

    /* Opens the activity to view the details of the selected patient */
    public void onButtonViewClick(View view) {
        if (selectedIndex != -1) {

            Patient selectedPatient = adapter.getItem(selectedIndex);
            Intent intent = new Intent(PatientsActivity.this, ViewPatientActivity.class);
            intent.putExtra("patientUUID", selectedPatient.getUUIDNumber().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.patients_select, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            startSyncService();
            return true;
        } else if (id == R.id.action_see_profile) {
            showProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Shows the user profile */
    public void showProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientsActivity.this);
        String profile = getResources().getString(R.string.profile_name) + ": " + user.getName() + "\n" +
                getResources().getString(R.string.profile_lastname) + ": " + user.getLastName() + "\n" +
                getResources().getString(R.string.profile_genre) + ": " + user.getGenre() + "\n" +
                getResources().getString(R.string.profile_id) + ": " + user.getIdentification() + "\n";
        builder.setMessage(profile).setTitle(R.string.profile_title);
        builder.setPositiveButton(R.string.profile_close, null);
        AlertDialog profileDialog = builder.create();
        profileDialog.show();
    }

    /* Starts the synchronization service */
    public void startSyncService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientsActivity.this);
        builder.setMessage(R.string.patients_sync_status_text).setTitle(R.string.patients_sync_status_title);
        builder.setPositiveButton(R.string.patients_sync_close, null);
        dialog = builder.create();
        dialog.show();
        //Intent serviceIntent = new Intent(PatientsActivity.this,
        //        SyncService.class);
        //LiderComunitario syncUser = db.getUserForSync(user.getIdentification());
        //Log.e( "ALERTA","MODELO: "+g.toJson(syncUser) );
        //serviceIntent.putExtra("user", syncUser);
        //startService(serviceIntent);

        LiderComunitario comunitario = db.getUserForSync(user.getIdentification());
        Log.e(">>>", "USER: " + user.getId());
        final Gson g = new Gson();
        //EVALUADOR
        final Usuario u = new Usuario();
        u.name = comunitario.getName();
        u.lastname = comunitario.getLastName();
        u.nationalId = comunitario.getIdentification();
        u.id = comunitario.getId();
        u.reader = true;
        u.writer = true;

        WebserviceConsumer.PostUserByCedula postuser = new WebserviceConsumer.PostUserByCedula(g.toJson(u), u.nationalId);
        postuser.setObserver(new WebserviceConsumer.ServerResponseReceiver() {
            @Override
            public void onServerResponse(Object json) {
                generarDocumentos();
            }
        });
        postuser.start();
    }

    private void generarDocumentos() {
        Gson g = new Gson();
        SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LiderComunitario comunitario = db.getUserForSync(user.getIdentification());

        ArrayList<Document> documentos = new ArrayList<>();
        final ArrayList<Evaluation> evaluaciones = new ArrayList<>();

        for (int paciente = 0; paciente < comunitario.getPatientList().size(); paciente++) {
            //Iteraciones por paciente
            Patient patient = comunitario.getPatientList().get(paciente);
            for (int evaluacion = 0; evaluacion < patient.getEvaluationList().size(); evaluacion++) {
                //Iterciones por evaluacion
                Evaluation evaluation = patient.getEvaluationList().get(evaluacion);
                if (!evaluation.isSyncked()) {
                    evaluaciones.add(evaluation);
                    Document document = new Document();
                    document.id = UUID.randomUUID().toString();
                    document.lastDateUpdate = dateformatter.format(Calendar.getInstance().getTime());
                    document.pacienteId = patient.getUUIDNumber();
                    document.evaluadorId = comunitario.getId();
                    document.date = evaluation.getDate();
                    document.umbral = (int) evaluation.getUmbral();
                    document.puntaje = (int) evaluation.getScore();
                    document.injuryWeeks = patient.getInjuryWeeks();

                    document.lesionesAgrupadas = evaluation.isAgrupadas();
                    document.ulcerasBordesElevados = evaluation.isUlceras();
                    document.localizacionCabeza = evaluation.isLesionesH();
                    document.localizacionTronco = evaluation.isLesionesB();
                    document.localizacionBrazoIzquierdo = evaluation.isLesionesLA();
                    document.localizacionBrazoDerecho = evaluation.isLesionesRA();
                    document.localizacionPiernaIzquierda = evaluation.isLesionesLL();
                    document.localizacionPiernaDerecha = evaluation.isLesionesRL();
                    document.actividadRiesgo = evaluation.isActividades();
                    document.antecedentes = evaluation.isAntecedentes();
                    document.contactoManta = evaluation.isManta();

                    document.cantidadFoto = db.getAllUlcerImgByEval(evaluation.getUUIDNumber()).size();
                    document.latitud = Double.parseDouble(patient.getLat());
                    document.longitud = Double.parseDouble(patient.getLng());
                    ;

                    document.numeroTotalLesiones = db.getNumeroLesiones(evaluation.getUUIDNumber());
                    document.numeroHisopos = db.getAllUlcerImgByEval(evaluation.getUUIDNumber()).size();

                    List<UlcerImg> imgs = db.getAllUlcerImgByEval(evaluation.getUUIDNumber());
                    ArrayList<UlcerImgDTO> fotoLesiones = new ArrayList<>();
                    for (int j = 0; j < imgs.size(); j++) {
                        UlcerImgDTO img = new UlcerImgDTO();
                        img.id = imgs.get(j).getImgUUID();
                        img.filename = imgs.get(j).getImgUUID();
                        img.imgDate = dateformatter.format(imgs.get(j).getImgDate());
                        img.url = "-";
                        img.patientId = patient.getUUIDNumber();
                        img.raterId = comunitario.getId();
                        img.evaluationId = evaluation.getUUIDNumber();

                        fotoLesiones.add(img);
                    }
                    document.fotoLesiones = fotoLesiones;


                    PatientDTO patientDTO = new PatientDTO();
                    patientDTO.id = patient.getUUIDNumber();
                    patientDTO.cedula = patient.getIdentification();
                    patientDTO.name = patient.getName();
                    patientDTO.lastName = patient.getLastName();
                    patientDTO.documentType = patient.getDocumentType();
                    patientDTO.gender = "" + patient.getGenre();
                    patientDTO.currentAddress = patient.getAddress();
                    patientDTO.phone = patient.getPhone();
                    patientDTO.birthday = patient.getBirthday();
                    patientDTO.province = patient.getProvince();
                    patientDTO.municipality = patient.getMunicipality();
                    patientDTO.lane = patient.getLane();
                    patientDTO.contactName = patient.getContactName();
                    patientDTO.contactLastName = patient.getContactLastName();
                    patientDTO.contactPhone = patient.getContactPhone();
                    patientDTO.contactCurrentAddress = patient.getContactAddress();
                    patientDTO.injuryWeeks = patient.getInjuryWeeks();

                    document.patient = patientDTO;
                    documentos.add(document);
                }
            }
        }

        Log.e("ALERTA", "DOC: " + g.toJson(documentos));
        db.actualizarEvaluaciones(evaluaciones);

        /*
        WebserviceConsumer.PostListDocument documentProc = new WebserviceConsumer.PostListDocument(documentos);
        documentProc.setObserver(new WebserviceConsumer.ServerResponseReceiver() {
            @Override
            public void onServerResponse(Object json) {
                if (json instanceof String) {
                    String respuesta = (String) json;
                    switch (respuesta) {
                        case Constants.OK:
                            //MARCAR EVALUACIONES PARA NO VOLVER A MANDAR
                            db.actualizarEvaluaciones(evaluaciones);
                            break;
                        case Constants.IOEX:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PatientsActivity.this, "Error al subir los documentos", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }
                }
            }
        });
        documentProc.start();
        */

    }

    @Override
    public void onServerResponse(Object obj) {
        if (obj instanceof String) {

        } else if (obj instanceof Usuario) {

        } else if (obj instanceof Document) {

        }
    }

    // Broadcast receiver for receiving status updates from the SyncService
    private class SyncServiceMessageReceiver extends BroadcastReceiver {
        private SyncServiceMessageReceiver() {
        }

        /* Shows a dialog with information from the synchronization service */
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(BroadcastConstants.EXTENDED_DATA_STATUS);

            if (dialog.isShowing()) {
                dialog.setTitle(R.string.patients_sync_result);
                dialog.setMessage(message);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientsActivity.this);
                builder.setMessage(message).setTitle(R.string.patients_sync_result);
                builder.setPositiveButton(R.string.patients_sync_close, null);
                dialog = builder.create();
                dialog.show();
            }
        }
    }

    private AlertDialog dialog;
}
