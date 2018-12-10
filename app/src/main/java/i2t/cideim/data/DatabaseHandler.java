package i2t.cideim.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import i2t.cideim.model.Evaluation;
import i2t.cideim.model.Hisopo;
import i2t.cideim.model.LiderComunitario;
import i2t.cideim.model.Patient;
import i2t.cideim.model.UlcerImg;

/**
 * Created by Leonardo.
 * Handles all the data transactions to the database.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Error codes
    public static final int SUCCESS = 1;
    public static final int RECORD_EXIST = 2;
    public static final int UNKNOWN_ERROR = 99;

    // Database
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "leishmaniasis";

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_NAT_ID = "cedula";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER__LAST_NAME = "last_name";
    private static final String KEY_USER_GENRE = "genre";

    // Patients table
    private static final String TABLE_PATIENTS = "patients";
    private static final String KEY_PATIENT_UUID = "uuid";
    private static final String KEY_PATIENT_ID = "id";
    private static final String KEY_PATIENT_NAME = "name";
    private static final String KEY_PATIENT_LAST_NAME = "last_name";
    private static final String KEY_PATIENT_DOCUMENT_TYPE = "document_type";
    private static final String KEY_PATIENT_GENRE = "genre";
    private static final String KEY_PATIENT_ADDRESS = "address";
    private static final String KEY_PATIENT_PHONE = "phone_number";
    private static final String KEY_PATIENT_BIRTHDAY = "birthday"; // dd/MM/aaaa
    private static final String KEY_PATIENT_PROVINCE = "province";
    private static final String KEY_PATIENT_MUNICIPALITY = "municipality";
    private static final String KEY_PATIENT_LANE = "lane";
    private static final String KEY_PATIENT_CONTACT_NAME = "contact_name";
    private static final String KEY_PATIENT_CONTACT_LAST_NAME = "contact_last_name";
    private static final String KEY_PATIENT_CONTACT_PHONE = "contact_phone_number";
    private static final String KEY_PATIENT_CONTACT_ADDRESS = "contact_address";
    private static final String KEY_PATIENT_LAT = "latitude";
    private static final String KEY_PATIENT_LNG = "longitude";
    private static final String KEY_PATIENT_INJURY_WEEKS = "injury_weeks";

    // Evaluations table
    private static final String TABLE_EVALUATIONS = "evaluations";
    private static final String KEY_EVALUATION_UUID = "uuid";
    private static final String KEY_EVALUATION_ULCERAS = "ulceras";
    private static final String KEY_EVALUATION_AGRUPADAS = "agrupadas";
    private static final String KEY_EVALUATION_LESIONES_H = "lesiones_h";
    private static final String KEY_EVALUATION_LESIONES_B = "lesiones_B";
    private static final String KEY_EVALUATION_LESIONES_LA = "lesiones_la";
    private static final String KEY_EVALUATION_LESIONES_RA = "lesiones_ra";
    private static final String KEY_EVALUATION_LESIONES_LL = "lesiones_ll";
    private static final String KEY_EVALUATION_LESIONES_RL = "lesiones_rl";
    private static final String KEY_EVALUATION_ACTIVIDADES = "actividades";
    private static final String KEY_EVALUATION_ANTECEDENTES = "antecedentes";
    private static final String KEY_EVALUATION_MANTA = "manta";
    private static final String KEY_EVALUATION_DATE = "date";
    private static final String KEY_EVALUATION_THRESHOLD = "threshold";
    private static final String KEY_EVALUATION_SCORE = "score";
    private static final String KEY_EVALUATION_SYNCKED = "syncked";

    // Users table
    private static final String TABLE_ULCERIMG = "ulcerImg";
    private static final String KEY_ULCERIMG_UUID = "uuid";
    private static final String KEY_ULCERIMG_BODYLOCATION = "bodylocation";
    private static final String KEY_ULCERIMG_DATE = "imgdate";
    private static final String KEY_ULCERIMG_IMGFORMAT = "imgformat";
    private static final String KEY_ULCERIMG_IMGUUID = "imguuid";
    private static final String KEY_ULCERIMG_INJURIESPERLOCATION = "injuriesperlocation";

    // Users table
    private static final String TABLE_HISOPO = "hisopo";
    private static final String KEY_HISOPO_UUID = "uuid";
    private static final String KEY_HISOPO_BODYLOCATION = "bodylocation";
    private static final String KEY_HISOPO_DATE = "date";
    private static final String KEY_HISOPO_MUESTRAS = "muestras";

    // Relationships
    private static final String FK_USER = "user";
    private static final String FK_PATIENT = "patient";
    private static final String FK_EVALUATION = "evaluation";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Creates the SQLite database */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " TEXT PRIMARY KEY," + KEY_NAT_ID + " TEXT," + KEY_USER_NAME + " TEXT,"
                + KEY_USER__LAST_NAME + " TEXT," + KEY_USER_GENRE + " TEXT" + ")";

        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
                + KEY_PATIENT_UUID + " TEXT PRIMARY KEY," + KEY_PATIENT_ID + " TEXT," + KEY_PATIENT_NAME
                + " TEXT," + KEY_PATIENT_LAST_NAME + " TEXT," + KEY_PATIENT_DOCUMENT_TYPE + " TEXT,"
                + KEY_PATIENT_GENRE + " TEXT," + KEY_PATIENT_ADDRESS + " TEXT,"
                + KEY_PATIENT_PHONE + " TEXT," + KEY_PATIENT_BIRTHDAY + " TEXT,"
                + KEY_PATIENT_PROVINCE + " TEXT," + KEY_PATIENT_MUNICIPALITY
                + " TEXT," + KEY_PATIENT_LANE + " TEXT,"
                + KEY_PATIENT_CONTACT_NAME + " TEXT,"
                + KEY_PATIENT_CONTACT_LAST_NAME + " TEXT,"
                + KEY_PATIENT_CONTACT_PHONE + " TEXT,"
                + KEY_PATIENT_CONTACT_ADDRESS + " TEXT,"
                + KEY_PATIENT_LAT + " TEXT,"
                + KEY_PATIENT_LNG + " TEXT,"
                + KEY_PATIENT_INJURY_WEEKS + " INTEGER," + FK_USER + " TEXT"
                + ")";

        String CREATE_EVALUATIONS_TABLE = "CREATE TABLE " + TABLE_EVALUATIONS
                + "(" + KEY_EVALUATION_UUID
                + " TEXT PRIMARY KEY,"
                + KEY_EVALUATION_ULCERAS + " INTEGER,"
                + KEY_EVALUATION_AGRUPADAS + " INTEGER,"
                + KEY_EVALUATION_LESIONES_H + " INTEGER,"
                + KEY_EVALUATION_LESIONES_B + " INTEGER,"
                + KEY_EVALUATION_LESIONES_LA + " INTEGER,"
                + KEY_EVALUATION_LESIONES_RA + " INTEGER,"
                + KEY_EVALUATION_LESIONES_LL + " INTEGER,"
                + KEY_EVALUATION_LESIONES_RL + " INTEGER,"
                + KEY_EVALUATION_ACTIVIDADES + " INTEGER,"
                + KEY_EVALUATION_ANTECEDENTES + " INTEGER,"
                + KEY_EVALUATION_SYNCKED + " INTEGER,"

                + KEY_EVALUATION_MANTA + " INTEGER," + KEY_EVALUATION_DATE
                + " TEXT," + KEY_EVALUATION_THRESHOLD + " NUMERIC,"
                + KEY_EVALUATION_SCORE + " NUMERIC," + FK_PATIENT + " TEXT" + ")";

        String CREATE_ULCERIMG_TABLE = "CREATE TABLE " + TABLE_ULCERIMG + "("
                + KEY_ULCERIMG_UUID + " TEXT PRIMARY KEY,"
                + KEY_ULCERIMG_BODYLOCATION + " INTEGER,"
                + KEY_ULCERIMG_DATE + " TEXT,"
                + KEY_ULCERIMG_IMGFORMAT + " TEXT,"
                + KEY_ULCERIMG_IMGUUID + " TEXT,"
                + KEY_ULCERIMG_INJURIESPERLOCATION + " TEXT,"
                + FK_EVALUATION + " TEXT"
                + ")";

        String CREATE_HISOPO_TABLE = "CREATE TABLE " + TABLE_HISOPO + "("
                + KEY_HISOPO_UUID + " TEXT PRIMARY KEY,"
                + KEY_HISOPO_BODYLOCATION + " INTEGER,"
                + KEY_HISOPO_DATE + " TEXT,"
                + KEY_HISOPO_MUESTRAS + " INTEGER,"
                + FK_EVALUATION + " TEXT"
                + ")";


        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PATIENTS_TABLE);
        db.execSQL(CREATE_EVALUATIONS_TABLE);
        db.execSQL(CREATE_ULCERIMG_TABLE);
        db.execSQL(CREATE_HISOPO_TABLE);
    }

    /* Handles the changes in the model */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVALUATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ULCERIMG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISOPO);
        onCreate(db);
    }

    /* DEFAULT CRUD */

    /* Adds a new item to a table */
    public int add(String tableName, ContentValues values) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long rowId = db.insertOrThrow(tableName, null, values);
            db.close();
            return rowId == -1 ? DatabaseHandler.UNKNOWN_ERROR : DatabaseHandler.SUCCESS;
        } catch (SQLiteConstraintException e) {
            return DatabaseHandler.RECORD_EXIST;
        }
    }

    /* USERS */

    /* Adds a new item to the users table */
    public int addUser(LiderComunitario liderComunitario) {
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, liderComunitario.getId());
        values.put(KEY_NAT_ID, liderComunitario.getIdentification());
        values.put(KEY_USER_NAME, liderComunitario.getName());
        values.put(KEY_USER__LAST_NAME, liderComunitario.getLastName());
        values.put(KEY_USER_GENRE, String.valueOf(liderComunitario.getGenre()));
        return add(TABLE_USERS, values);
    }

    /* Returns the user with the specified id, only with its name and identification. */
    public LiderComunitario getMinimizedUser(String cedula) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ID, KEY_NAT_ID,
                        KEY_USER_NAME, KEY_USER__LAST_NAME}, KEY_NAT_ID + "=?", new String[]{cedula}, null,
                null, null, null
        );
        LiderComunitario liderComunitario = null;
        if (cursor != null && cursor.moveToFirst()) {
            liderComunitario = new LiderComunitario(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
        db.close();
        return liderComunitario;
    }

    /* Returns a user with all his properties and minimized patients */
    public LiderComunitario getUserWithMinimizedPatients(String cedula) {
        LiderComunitario liderComunitario = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_ID, KEY_NAT_ID, KEY_USER_NAME, KEY_USER__LAST_NAME},
                KEY_NAT_ID + "=?", new String[]{cedula}, null,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            liderComunitario = new LiderComunitario(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            liderComunitario.addAllPatients(getMinimizedPatients(liderComunitario.getIdentification()));
        }

        db.close();
        return liderComunitario;
    }

    /* PATIENTS */

    /* Adds a new item to the patients table */
    public int addPatient(Patient patient, String userId) {
        ContentValues values = new ContentValues();
        values.put(KEY_PATIENT_UUID, patient.getUUIDNumber());
        values.put(KEY_PATIENT_ID, patient.getIdentification());
        values.put(KEY_PATIENT_NAME, patient.getName());
        values.put(KEY_PATIENT_LAST_NAME, patient.getLastName());
        values.put(KEY_PATIENT_DOCUMENT_TYPE, patient.getDocumentType());
        values.put(KEY_PATIENT_GENRE, String.valueOf(patient.getGenre()));
        values.put(KEY_PATIENT_ADDRESS, patient.getAddress());
        values.put(KEY_PATIENT_PHONE, patient.getPhone());
        values.put(KEY_PATIENT_BIRTHDAY, patient.getBirthday());
        values.put(KEY_PATIENT_PROVINCE, patient.getProvince());
        values.put(KEY_PATIENT_MUNICIPALITY, patient.getMunicipality());
        values.put(KEY_PATIENT_LANE, patient.getLane());
        values.put(KEY_PATIENT_CONTACT_NAME, patient.getContactName());
        values.put(KEY_PATIENT_CONTACT_LAST_NAME, patient.getContactLastName());
        values.put(KEY_PATIENT_CONTACT_PHONE, patient.getContactPhone());
        values.put(KEY_PATIENT_CONTACT_ADDRESS, patient.getContactAddress());
        values.put(KEY_PATIENT_LAT, patient.getLat());
        values.put(KEY_PATIENT_LNG, patient.getLng());
        values.put(KEY_PATIENT_INJURY_WEEKS, patient.getInjuryWeeks());
        values.put(FK_USER, userId);
        return add(TABLE_PATIENTS, values);
    }

    /* Returns the list of patients for a user. Each patient with only name, last name, id and his latest evaluation's value */
    public List<Patient> getMinimizedPatients(String userId) {
        List<Patient> patients = new ArrayList<Patient>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_PATIENT_UUID, KEY_PATIENT_ID,
                KEY_PATIENT_NAME, KEY_PATIENT_LAST_NAME}, FK_USER + "=?", new String[]{userId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Patient patient = new Patient(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                Evaluation latestEvaluation = getMinimizedLatestEvaluation(cursor.getString(0));
                if (latestEvaluation != null) {
                    patient.addEvaluation(latestEvaluation);
                }
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        db.close();
        return patients;
    }

    /* Returns the patient with the specified uuid, with all its properties and only the latest evaluation */
    public Patient getFullPatientWithLatestEvaluation(String patientUUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                new String[]{KEY_PATIENT_UUID, KEY_PATIENT_ID, KEY_PATIENT_NAME, KEY_PATIENT_LAST_NAME,
                        KEY_PATIENT_DOCUMENT_TYPE, KEY_PATIENT_GENRE, KEY_PATIENT_ADDRESS, KEY_PATIENT_PHONE,
                        KEY_PATIENT_BIRTHDAY, KEY_PATIENT_PROVINCE, KEY_PATIENT_MUNICIPALITY, KEY_PATIENT_LANE,
                        KEY_PATIENT_CONTACT_NAME, KEY_PATIENT_CONTACT_LAST_NAME, KEY_PATIENT_CONTACT_PHONE,
                        KEY_PATIENT_CONTACT_ADDRESS, KEY_PATIENT_INJURY_WEEKS, KEY_PATIENT_LAT, KEY_PATIENT_LNG},
                KEY_PATIENT_UUID + "=?", new String[]{patientUUID}, null, null, null, null
        );
        Patient patient = null;
        if (cursor != null && cursor.moveToFirst()) {
            patient = new Patient(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5).charAt(0),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15), cursor.getInt(16),
                    Double.parseDouble(cursor.getString(17)), Double.parseDouble(cursor.getString(18)));

            Evaluation latestEvaluation = getFullLatestEvaluation(patientUUID);
            if (latestEvaluation != null) {
                patient.addEvaluation(latestEvaluation);
            }
        }
        db.close();
        return patient;
    }

    /* EVALUATIONS */

    /* Adds an item to the evaluations table */
    public int addEvaluation(Evaluation evaluation, String patientUUID) {
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        ContentValues values = new ContentValues();
        values.put(KEY_EVALUATION_UUID, evaluation.getUUIDNumber());
        values.put(KEY_EVALUATION_SYNCKED, evaluation.isSyncked());
        values.put(KEY_EVALUATION_ULCERAS, evaluation.isUlceras());
        values.put(KEY_EVALUATION_AGRUPADAS, evaluation.isAgrupadas());
        values.put(KEY_EVALUATION_LESIONES_H, evaluation.isLesionesH());
        values.put(KEY_EVALUATION_LESIONES_B, evaluation.isLesionesB());
        values.put(KEY_EVALUATION_LESIONES_LA, evaluation.isLesionesLA());
        values.put(KEY_EVALUATION_LESIONES_RA, evaluation.isLesionesRA());
        values.put(KEY_EVALUATION_LESIONES_LL, evaluation.isLesionesLL());
        values.put(KEY_EVALUATION_LESIONES_RL, evaluation.isLesionesRL());
        values.put(KEY_EVALUATION_ACTIVIDADES, evaluation.isActividades());
        values.put(KEY_EVALUATION_ANTECEDENTES, evaluation.isAntecedentes());
        values.put(KEY_EVALUATION_MANTA, evaluation.isManta());
        values.put(KEY_EVALUATION_DATE, date);
        values.put(KEY_EVALUATION_THRESHOLD, evaluation.getUmbral());
        values.put(KEY_EVALUATION_SCORE, evaluation.getScore());
        values.put(FK_PATIENT, patientUUID);

        //Agregar fotos
        for (UlcerImg img : evaluation.getUlcerList()) {
            int res = addUlcerIMG(img, evaluation.getUUIDNumber());
            Log.e("ALERTA AGREGANDO IMG", ">" + res);
        }
        for (Hisopo his : evaluation.getHisopoList()) {
            addHisopo(his, evaluation.getUUIDNumber());
        }

        return add(TABLE_EVALUATIONS, values);
    }


    /* Returns the latest evaluation for a patient, only with the date and the final score */
    public Evaluation getMinimizedLatestEvaluation(String patientUUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUATIONS, new String[]{KEY_EVALUATION_DATE, KEY_EVALUATION_SCORE},
                FK_PATIENT + "=?", new String[]{patientUUID}, null, null, null, null);
        Evaluation evaluation = null;
        if (cursor != null && cursor.moveToLast()) {
            evaluation = new Evaluation();
            evaluation.setDate(cursor.getString(0));
            evaluation.setScore(cursor.getInt(1));
        }
        db.close();
        return evaluation;
    }

    public Evaluation getEvaluation(String UUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUATIONS, new String[]{KEY_EVALUATION_DATE, KEY_EVALUATION_SCORE, KEY_EVALUATION_UUID},
                KEY_EVALUATION_UUID + "=?", new String[]{UUID}, null, null, null, null);
        Evaluation evaluation = null;
        if (cursor != null && cursor.moveToLast()) {
            evaluation = new Evaluation();
            evaluation.setDate(cursor.getString(0));
            evaluation.setScore(cursor.getInt(1));
            evaluation.setUUIDNumber(cursor.getString(2));
        }
        db.close();
        return evaluation;
    }

    /* Returns the latest evaluation for a patient, with all its properties */
    public Evaluation getFullLatestEvaluation(String patientUUID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUATIONS,
                new String[]{KEY_EVALUATION_UUID, KEY_EVALUATION_ULCERAS, KEY_EVALUATION_AGRUPADAS,
                        KEY_EVALUATION_LESIONES_H, KEY_EVALUATION_LESIONES_B, KEY_EVALUATION_LESIONES_LA,
                        KEY_EVALUATION_LESIONES_RA, KEY_EVALUATION_LESIONES_LL, KEY_EVALUATION_LESIONES_RL,
                        KEY_EVALUATION_ACTIVIDADES, KEY_EVALUATION_ANTECEDENTES, KEY_EVALUATION_MANTA,
                        KEY_EVALUATION_DATE, KEY_EVALUATION_THRESHOLD, KEY_EVALUATION_SCORE, KEY_EVALUATION_SYNCKED},
                FK_PATIENT + "=?", new String[]{patientUUID}, null, null, null, null
        );
        Evaluation evaluation = null;
        if (cursor != null && cursor.moveToLast()) {
            evaluation = new Evaluation(cursor.getString(0), cursor.getInt(1) == 1 ? true : false, cursor.getInt(2) == 1 ? true : false,
                    cursor.getInt(3) == 1 ? true : false, cursor.getInt(4) == 1 ? true : false, cursor.getInt(5) == 1 ? true : false, cursor.getInt(6) == 1 ? true : false,
                    cursor.getInt(7) == 1 ? true : false, cursor.getInt(8) == 1 ? true : false, cursor.getInt(9) == 1 ? true : false, cursor.getInt(10) == 1 ? true : false,
                    cursor.getInt(11) == 1 ? true : false, cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getInt(15) == 1 ? true : false);
        }
        db.close();
        return evaluation;
    }

    /* SYNC */

    /* Returns a user with its patients and evaluations */
    // TODO: Only send non synced information
    public LiderComunitario getUserForSync(String cedula) {
        LiderComunitario liderComunitario = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_ID, KEY_NAT_ID, KEY_USER_NAME, KEY_USER__LAST_NAME},
                KEY_NAT_ID + "=?", new String[]{cedula}, null,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            liderComunitario = new LiderComunitario(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            liderComunitario.addAllPatients(getPatientsForSync(liderComunitario.getIdentification()));
        }

        db.close();
        return liderComunitario;
    }

    /* Returns the whole list of patients for a user */
    private List<Patient> getPatientsForSync(String userId) {
        List<Patient> patients = new ArrayList<Patient>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                new String[]{KEY_PATIENT_UUID, KEY_PATIENT_ID, KEY_PATIENT_NAME, KEY_PATIENT_LAST_NAME,
                        KEY_PATIENT_DOCUMENT_TYPE, KEY_PATIENT_GENRE, KEY_PATIENT_ADDRESS, KEY_PATIENT_PHONE,
                        KEY_PATIENT_BIRTHDAY, KEY_PATIENT_PROVINCE, KEY_PATIENT_MUNICIPALITY, KEY_PATIENT_LANE,
                        KEY_PATIENT_CONTACT_NAME, KEY_PATIENT_CONTACT_LAST_NAME, KEY_PATIENT_CONTACT_PHONE,
                        KEY_PATIENT_CONTACT_ADDRESS, KEY_PATIENT_INJURY_WEEKS, KEY_PATIENT_LAT, KEY_PATIENT_LNG},
                FK_USER + "=?", new String[]{userId}, null, null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Patient patient = new Patient(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5).charAt(0),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),
                        cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15),
                        cursor.getInt(16), Double.parseDouble(cursor.getString(17)),
                        Double.parseDouble(cursor.getString(18)));

                patient.addAllEvaluations(getEvaluationsForSync(cursor.getString(0), userId, patient.getIdentification()));
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        db.close();
        return patients;
    }

    /* Returns the whole list of evaluations for a patient */
    private List<Evaluation> getEvaluationsForSync(String patientUUID, String evaluatorCC, String patientCC) {
        List<Evaluation> evaluations = new ArrayList<Evaluation>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUATIONS,
                new String[]{KEY_EVALUATION_UUID, KEY_EVALUATION_ULCERAS, KEY_EVALUATION_AGRUPADAS,
                        KEY_EVALUATION_LESIONES_H, KEY_EVALUATION_LESIONES_B, KEY_EVALUATION_LESIONES_LA,
                        KEY_EVALUATION_LESIONES_RA, KEY_EVALUATION_LESIONES_LL, KEY_EVALUATION_LESIONES_RL,
                        KEY_EVALUATION_ACTIVIDADES, KEY_EVALUATION_ANTECEDENTES, KEY_EVALUATION_MANTA,
                        KEY_EVALUATION_DATE, KEY_EVALUATION_THRESHOLD, KEY_EVALUATION_SCORE, KEY_EVALUATION_SYNCKED},
                FK_PATIENT + "=?", new String[]{patientUUID}, null, null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Evaluation evaluation = new Evaluation(cursor.getString(0), cursor.getInt(1) == 1 ? true : false, cursor.getInt(2) == 1 ? true : false,
                        cursor.getInt(3) == 1 ? true : false, cursor.getInt(4) == 1 ? true : false, cursor.getInt(5) == 1 ? true : false, cursor.getInt(6) == 1 ? true : false,
                        cursor.getInt(7) == 1 ? true : false, cursor.getInt(8) == 1 ? true : false, cursor.getInt(9) == 1 ? true : false, cursor.getInt(10) == 1 ? true : false,
                        cursor.getInt(11) == 1 ? true : false, cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getInt(15) == 1 ? true : false);
                evaluation.setEvaluadorId(evaluatorCC);
                evaluation.setPacienteId(patientCC);

                evaluation.addAllUlcerImg(getAllUlcerImgByEval(evaluation.getUUIDNumber()));
                evaluation.addAllHisopos(getAllHisoposByEval(evaluation.getUUIDNumber()));

                evaluations.add(evaluation);
            } while (cursor.moveToNext());
        }
        db.close();
        return evaluations;
    }


    /* ULCER IMAGENES */
    public int addUlcerIMG(UlcerImg ulcer, String uuid_eval) {
        ContentValues values = new ContentValues();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        values.put(KEY_ULCERIMG_UUID, ulcer.getUuid()); //TEXT
        values.put(KEY_ULCERIMG_BODYLOCATION, Integer.parseInt(ulcer.getBodyLocation())); //INT
        values.put(KEY_ULCERIMG_DATE, format.format(ulcer.getImgDate())); //TEXT
        values.put(KEY_ULCERIMG_IMGFORMAT, ulcer.getImgFormat()); //TEXT
        values.put(KEY_ULCERIMG_IMGUUID, ulcer.getImgUUID()); //TEXT
        values.put(KEY_ULCERIMG_INJURIESPERLOCATION, ulcer.getInjuriesPerLocation()); //STRING
        values.put(FK_EVALUATION, uuid_eval); //TEXT
        int result = add(TABLE_ULCERIMG, values);
        return result;
    }

    public List<UlcerImg> getAllUlcerImgByEval(String uuidNumber) {
        ArrayList<UlcerImg> imagenes = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ULCERIMG,
                new String[]{KEY_ULCERIMG_UUID,
                        KEY_ULCERIMG_DATE,
                        KEY_ULCERIMG_INJURIESPERLOCATION,
                        KEY_ULCERIMG_IMGFORMAT,
                        KEY_ULCERIMG_BODYLOCATION,
                        KEY_ULCERIMG_IMGUUID},
                FK_EVALUATION + "=?", new String[]{uuidNumber}, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    UlcerImg img = new UlcerImg();
                    img.setUuid(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_UUID)));
                    img.setBodyLocation(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_BODYLOCATION)));
                    String fecha = cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_DATE));
                    img.setImgDate(format.parse(fecha));
                    img.setImgFormat(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_IMGFORMAT)));
                    img.setImgUUID(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_IMGUUID)));
                    img.setInjuriesPerLocation(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_INJURIESPERLOCATION)));
                    imagenes.add(img);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        db.close();
        return imagenes;
    }

    public int addHisopo(Hisopo his, String uuidNumber) {
        ContentValues values = new ContentValues();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        values.put(KEY_HISOPO_UUID, his.getUuid()); //TEXT
        values.put(KEY_HISOPO_BODYLOCATION, Integer.parseInt(his.getBodyLocation())); //INT
        values.put(KEY_HISOPO_DATE, format.format(his.getDate())); //TEXT
        values.put(KEY_HISOPO_MUESTRAS, his.getMuestras()); //INT
        values.put(FK_EVALUATION, uuidNumber); //TEXT
        int result = add(TABLE_HISOPO, values);
        return result;
    }

    public List<Hisopo> getAllHisoposByEval(String uuidNumber) {
        ArrayList<Hisopo> hisopos = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISOPO,
                new String[]{KEY_HISOPO_UUID,
                        KEY_HISOPO_DATE,
                        KEY_HISOPO_MUESTRAS,
                        KEY_HISOPO_BODYLOCATION},
                FK_EVALUATION + "=?", new String[]{uuidNumber}, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    Hisopo his = new Hisopo();
                    his.setUuid(cursor.getString(cursor.getColumnIndex(KEY_HISOPO_UUID)));
                    String fecha = cursor.getString(cursor.getColumnIndex(KEY_HISOPO_DATE));
                    his.setDate(format.parse(fecha));
                    his.setMuestras(cursor.getInt(cursor.getColumnIndex(KEY_HISOPO_MUESTRAS)));
                    his.setBodyLocation(cursor.getString(cursor.getColumnIndex(KEY_HISOPO_BODYLOCATION)));
                    hisopos.add(his);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        db.close();
        return hisopos;
    }

    public void printAllHisopos() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISOPO, null);
        Log.e("ALERTA", "" + cursor);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    UlcerImg img = new UlcerImg();
                    img.setUuid(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_UUID)));
                    img.setBodyLocation(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_BODYLOCATION)));
                    String fecha = cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_DATE));
                    Log.e("ALERTA", "fecha " + fecha);
                    img.setImgDate(format.parse(fecha));
                    img.setImgFormat(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_IMGFORMAT)));
                    img.setImgUUID(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_IMGUUID)));
                    img.setInjuriesPerLocation(cursor.getString(cursor.getColumnIndex(KEY_ULCERIMG_INJURIESPERLOCATION)));
                    Log.e("ULCERS>", "NOMBRE: " + img.getImgUUID() + "   BL:" + img.getBodyLocation());
                } catch (ParseException e) {
                    Log.e("ALERTA", "GARRAFAL" + e.getLocalizedMessage());
                }
            } while (cursor.moveToNext());
        }
        db.close();
    }

    public int getNumeroLesiones(String evalUID) {
        List<UlcerImg> imagenes = getAllUlcerImgByEval(evalUID);
        ArrayList<String> diff = new ArrayList<>();
        if (imagenes.size() == 0) return 0;
        if (imagenes.size() == 1) return 1;
        diff.add(imagenes.get(0).getBodyLocation());
        for (int i = 1; i < imagenes.size(); i++) {
            if (!diff.contains(imagenes.get(i).getBodyLocation())) {
                diff.add(imagenes.get(i).getBodyLocation());
            }
        }
        return diff.size();
    }


    public void actualizarEvaluaciones(ArrayList<Evaluation> evaluaciones) {
        for (int i = 0; i < evaluaciones.size(); i++) {
            Evaluation a = evaluaciones.get(i);
            this.updateEvaluation(a);
        }
    }


    public void updateEvaluation(Evaluation evaluation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EVALUATIONS + " SET " + KEY_EVALUATION_SYNCKED + " = 1 WHERE " + KEY_EVALUATION_UUID + " = '" + evaluation.getUUIDNumber() + "'");
        db.close();
    }
}
