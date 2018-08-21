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

import i2t.cideim.R;
import i2t.cideim.custom.BroadcastConstants;
import i2t.cideim.custom.PatientsListAdapter;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.model.LiderComunitario;
import i2t.cideim.model.Patient;

/**
 * Created by Leonardo.
 * Activity that shows the list of patients.
 */

public class PatientsActivity extends Activity {

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
        Intent serviceIntent = new Intent(PatientsActivity.this,
                SyncService.class);
        LiderComunitario syncUser = db.getUserForSync(user.getIdentification());
        Gson g = new Gson();
        Log.e( "ALERTA","MODELO: "+g.toJson(syncUser) );
        serviceIntent.putExtra("user", syncUser);
        //startService(serviceIntent);
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
