package i2t.cideim.leishmaniasis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import i2t.cideim.R;
import i2t.cideim.custom.TriangleView;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.model.Patient;
import i2t.cideim.util.ListernerPosicion;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

import java.util.*;

/**
 * Created by Leonardo and Camilo.
 * This activity allows to create new patients.
 */

public class CreatePatientActivity extends Activity {

    // Editable fields
    private TextView textViewTitle;
    public TextView tv_location;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextId;
    private ToggleButton toggleButtonM;
    private ToggleButton toggleButtonF;
    private ToggleButton toggleButtonTI;
    private ToggleButton toggleButtonCC;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextBirthday;
    private Spinner spinnerDepartment;
    private Spinner spinnerMunicipality;
    private Spinner spinnerLane;
    private EditText editTextContactName;
    private EditText editTextContactLastName;
    private EditText editTextContactPhone;
    private EditText editTextContactAddress;
    private EditText editTextInjuryWeeks;

    // Triangles (UX components)
    private TriangleView triangleName;
    private TriangleView triangleLastName;
    private TriangleView triangleDoc;
    private TriangleView triangleId;
    private TriangleView triangleAddress;
    private TriangleView trianglePhone;
    private TriangleView triangleBirthday;
    private TriangleView triangleDepartment;
    private TriangleView triangleMunicipality;
    private TriangleView triangleLane;
    private TriangleView triangleCName;
    private TriangleView triangleCLastName;
    private TriangleView triangleCPhone;
    private TriangleView triangleCAddress;
    private TriangleView triangleInjuryWeeks;

    private String userId;
    private DatabaseHandler db;

    LocationManager locationManager;
    ListernerPosicion locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);

        // Obtains the user id from the intent
        userId = getIntent().getStringExtra("userId");

        // Loads the database
        db = new DatabaseHandler(this);

        // Non editable fields
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);

        // Editable fields
        tv_location = (TextView) findViewById(R.id.tv_location);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextId = (EditText) findViewById(R.id.editTextId);
        toggleButtonM = (ToggleButton) findViewById(R.id.toggleButtonM);
        toggleButtonF = (ToggleButton) findViewById(R.id.toggleButtonF);
        toggleButtonTI = (ToggleButton) findViewById(R.id.toggleButtonTI);
        toggleButtonCC = (ToggleButton) findViewById(R.id.toggleButtonCC);
        triangleDoc = (TriangleView) findViewById(R.id.triangleDoc);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextBirthday = (EditText) findViewById(R.id.editTextBirthday);
        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);
        spinnerMunicipality = (Spinner) findViewById(R.id.spinnerMunicipality);
        spinnerLane = (Spinner) findViewById(R.id.spinnerLane);
        editTextContactName = (EditText) findViewById(R.id.editTextCName);
        editTextContactLastName = (EditText) findViewById(R.id.editTextCLastname);
        editTextContactPhone = (EditText) findViewById(R.id.editTextCPhone);
        editTextContactAddress = (EditText) findViewById(R.id.editTextCAddress);
        editTextInjuryWeeks = (EditText) findViewById(R.id.editTextInjuryWeeks);

        // UX components
        triangleName = (TriangleView) findViewById(R.id.triangleAbout);
        triangleLastName = (TriangleView) findViewById(R.id.triangleLastName);
        triangleId = (TriangleView) findViewById(R.id.triangleId);
        triangleAddress = (TriangleView) findViewById(R.id.triangleAddress);
        trianglePhone = (TriangleView) findViewById(R.id.trianglePhone);
        triangleBirthday = (TriangleView) findViewById(R.id.triangleBirthday);
        triangleDepartment = (TriangleView) findViewById(R.id.triangleDepartment);
        triangleMunicipality = (TriangleView) findViewById(R.id.triangleMunicipality);
        triangleLane = (TriangleView) findViewById(R.id.triangleLane);
        triangleCName = (TriangleView) findViewById(R.id.triangleCName);
        triangleCLastName = (TriangleView) findViewById(R.id.triangleCLastname);
        triangleCPhone = (TriangleView) findViewById(R.id.triangleCPhone);
        triangleCAddress = (TriangleView) findViewById(R.id.triangleCAddress);
        triangleInjuryWeeks = (TriangleView) findViewById(R.id.triangleWeeks);

        // Interaction between components
        createEditTextAndTriangleViewFocusInteraction(editTextName, triangleName);
        createEditTextAndTriangleViewFocusInteraction(editTextLastName, triangleLastName);
        createEditTextAndTriangleViewFocusInteraction(editTextId, triangleId);
        createEditTextAndTriangleViewFocusInteraction(editTextId, triangleDoc);
        createEditTextAndTriangleViewFocusInteraction(editTextAddress, triangleAddress);
        createEditTextAndTriangleViewFocusInteraction(editTextPhone, trianglePhone);
        createEditTextAndTriangleViewFocusInteraction(editTextBirthday, triangleBirthday);
        createEditTextAndTriangleViewFocusInteraction(editTextInjuryWeeks, triangleInjuryWeeks);

        // Birth date picker
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dateFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                editTextBirthday.setText(sdf.format(calendar.getTime()));
            }
        };

        editTextBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    triangleBirthday.setVisibility(View.VISIBLE);
                    DatePickerDialog dateDialog = new DatePickerDialog(CreatePatientActivity.this, dateSetListener,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.MONTH, -1);
                    dateDialog.getDatePicker().setMaxDate(now.getTimeInMillis());
                    dateDialog.show();
                } else {
                    triangleBirthday.setVisibility(View.INVISIBLE);
                }
            }
        });

        spinnerDepartment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleDepartment.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        spinnerMunicipality.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleMunicipality.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        spinnerLane.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triangleLane.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        createEditTextAndTriangleViewFocusInteraction(editTextContactName, triangleCName);
        createEditTextAndTriangleViewFocusInteraction(editTextContactLastName, triangleCLastName);
        createEditTextAndTriangleViewFocusInteraction(editTextContactPhone, triangleCPhone);
        createEditTextAndTriangleViewFocusInteraction(editTextContactAddress, triangleCAddress);

        // Data for the spinners (a.k.a. drop down lists)
        ArrayAdapter<CharSequence> documentTypesAdapter = ArrayAdapter.createFromResource(this,
                R.array.create_patient_document_types, R.layout.spinner_layout);
        ArrayAdapter<CharSequence> departmentsAdapter = ArrayAdapter.createFromResource(this,
                R.array.create_patient_departments, R.layout.spinner_layout);
        ArrayAdapter<CharSequence> municipalitiesAdapter = ArrayAdapter.createFromResource(this,
                R.array.create_patient_municipalities, R.layout.spinner_layout);
        ArrayAdapter<CharSequence> lanesAdapter = ArrayAdapter.createFromResource(this,
                R.array.create_patient_tumaco_lanes, R.layout.spinner_layout);
        documentTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lanesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDepartment.setAdapter(departmentsAdapter);
        spinnerMunicipality.setAdapter(municipalitiesAdapter);
        spinnerLane.setAdapter(lanesAdapter);

        // Spinners interaction
        spinnerDepartment.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!spinnerDepartment.getSelectedItem().toString().equals("Departamento") && adapterContains(spinnerDepartment.getAdapter(), "Departamento")) {

                    int currentPosition = spinnerDepartment.getSelectedItemPosition();

                    String[] s = getResources().getStringArray(R.array.create_patient_departments);
                    List<String> list = new ArrayList<String>();
                    Collections.addAll(list, s);
                    list.remove("Departamento");
                    ArrayAdapter<CharSequence> departmentsAdapter2 = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_layout);
                    for (int i = 0; i < list.size(); i++)
                        departmentsAdapter2.add(list.get(i));

                    spinnerDepartment.setAdapter(departmentsAdapter2);
                    spinnerDepartment.setSelection(currentPosition - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        spinnerMunicipality.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!spinnerMunicipality.getSelectedItem().toString().equals("Municipio") && adapterContains(spinnerMunicipality.getAdapter(), "Municipio")) {

                    int currentPosition = spinnerMunicipality.getSelectedItemPosition();

                    String[] s = getResources().getStringArray(R.array.create_patient_municipalities);
                    List<String> list = new ArrayList<String>();
                    Collections.addAll(list, s);
                    list.remove("Municipio");
                    ArrayAdapter<CharSequence> departmentsAdapter2 = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_layout);
                    for (int i = 0; i < list.size(); i++)
                        departmentsAdapter2.add(list.get(i));

                    spinnerMunicipality.setAdapter(departmentsAdapter2);
                    spinnerMunicipality.setSelection(currentPosition - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        spinnerLane.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!spinnerLane.getSelectedItem().toString().equals("Vereda") && adapterContains(spinnerLane.getAdapter(), "Vereda")) {

                    int currentPosition = spinnerLane.getSelectedItemPosition();

                    String[] s = getResources().getStringArray(R.array.create_patient_tumaco_lanes);
                    List<String> list = new ArrayList<String>();
                    Collections.addAll(list, s);
                    list.remove("Vereda");
                    ArrayAdapter<CharSequence> departmentsAdapter2 = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_layout);
                    for (int i = 0; i < list.size(); i++)
                        departmentsAdapter2.add(list.get(i));

                    spinnerLane.setAdapter(departmentsAdapter2);
                    spinnerLane.setSelection(currentPosition - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new ListernerPosicion(this);
        knowIfLocationAreEnabled();
        startLocation();

    }

    private void knowIfLocationAreEnabled() {
        final Context context = this;
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("Por favor active los servicios de localización");
            dialog.setPositiveButton("Abrir configuración", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogo, int paramInt) {
                    dialogo.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void startLocation() {
        tv_location.setText("Cargando las coordenadas de su ubicación...");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }
    }

    /* Creates the interaction that allows the triangle to be visible when the user is editing the text field */
    private void createEditTextAndTriangleViewFocusInteraction(EditText etView, final TriangleView tView) {
        etView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                tView.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    /* Returns true when the adapter contains a component or false if not */
    public boolean adapterContains(SpinnerAdapter adapter, String element) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(element))
                return true;
        }
        return false;
    }

    /* Toggle button interaction. Genre */
    public void onButtonGenreMClick(View view) {
        toggleButtonF.setChecked(false);
        toggleButtonM.setChecked(true);
    }

    /* Toggle button interaction. Genre */
    public void onButtonGenreFClick(View view) {
        toggleButtonM.setChecked(false);
        toggleButtonF.setChecked(true);
    }

    /* Toggle button interaction. Document type */
    public void onButtonDocTIClick(View view) {
        toggleButtonCC.setChecked(false);
        toggleButtonTI.setChecked(true);
    }

    /* Toggle button interaction. Document type */
    public void onButtonDocCCClick(View view) {
        toggleButtonTI.setChecked(false);
        toggleButtonCC.setChecked(true);
    }

    /* Creates a new patient and saves it to the database */
    public void onButtonCreateClick(View view) {
        // Obtains the data from the editable fields
        final String name = editTextName.getText().toString().toUpperCase();
        final String lastName = editTextLastName.getText().toString().toUpperCase();
        final String identification = editTextId.getText().toString();
        final String birthday = editTextBirthday.getText().toString();
        String weeksText = editTextInjuryWeeks.getText().toString();

        // Validates that the required data is available
        if (name.trim().length() != 0 && lastName.trim().length() != 0 &&
                identification.trim().length() != 0 && birthday.trim().length() != 0 &&
                !spinnerDepartment.getSelectedItem().toString().equals("Departamento") &&
                !spinnerDepartment.getSelectedItem().toString().equals("Municipio") &&
                !spinnerDepartment.getSelectedItem().toString().equals("Vereda") &&
                weeksText.trim().length() != 0) {

            final int injury_weeks = Integer.parseInt(weeksText);

            // Validates that no negative values were introduced in the injury_weeks field
            if (injury_weeks <= 0) {
                textViewTitle.setText(R.string.create_patient_error_invalid_data);
            } else {

                // Shows the confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String dialogMessage = getString(R.string.create_patient_name) + ": " + name + " " + lastName + "\n" +
                        getString(R.string.create_patient_id) + ": " + identification + "\n" +
                        getString(R.string.create_patient__injury_weeks) + ": " + weeksText;
                builder.setMessage(dialogMessage).setTitle(R.string.create_patient_confirm);

                // Creates the patient
                builder.setPositiveButton(R.string.create_patient_confirm_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        char genre = toggleButtonM.isChecked() ? 'M' : 'F';
                        String documentType = toggleButtonTI.isChecked() ? "TI" : "CC";
                        String address = editTextAddress.getText().toString().toUpperCase();
                        String phone = editTextPhone.getText().toString().toUpperCase();
                        String department = ((String) spinnerDepartment.getSelectedItem()).toUpperCase();
                        String municipality = ((String) spinnerMunicipality.getSelectedItem()).toUpperCase();
                        String lane = ((String) spinnerLane.getSelectedItem()).toUpperCase();
                        String contactName = editTextContactName.getText().toString().toUpperCase();
                        String contactLastName = editTextContactLastName.getText().toString().toUpperCase();
                        String contactPhone = editTextContactPhone.getText().toString().toUpperCase();
                        String contactAddress = editTextContactAddress.getText().toString().toUpperCase();

                        Patient patient = new Patient(identification, name, lastName, documentType, genre, address,
                                phone, birthday, department, municipality, lane, contactName, contactLastName, contactPhone,
                                contactAddress, injury_weeks);
                        int result = db.addPatient(patient, userId);
                        if (result == DatabaseHandler.SUCCESS) {
                            CreatePatientActivity.super.onBackPressed();
                        } else {
                            textViewTitle.setText(R.string.create_patient_error);
                        }
                    }
                });

                builder.setNegativeButton(R.string.create_patient_confirm_cancel, null);

                Dialog dialog = builder.create();
                dialog.show();
            }
        } else {
            Toast.makeText(this, R.string.evaluation_fill_fields, Toast.LENGTH_LONG).show();
        }
    }

    /* Alerts the user that the unsaved data will be lost */
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.back_alert);
        builder.setPositiveButton(R.string.go_back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CreatePatientActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton(R.string.stay, null);
        builder.create().show();
    }
}
