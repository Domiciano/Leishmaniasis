package i2t.cideim.leishmaniasis;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import i2t.cideim.R;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.model.Evaluation;
import i2t.cideim.model.Patient;

/**
 * Created by Leonardo.
 * Shows all the data of a patient.
 */

public class ViewPatientActivity extends Activity {

    private TextView textViewName;
    private TextView textViewIdentification;
    private TextView textViewGenre;
    private TextView textViewAddress;
    private TextView textViewPhone;
    private TextView textViewBirthday;
    private TextView textViewDepartment;
    private TextView textViewMunicipality;
    private TextView textViewLane;
    private TextView textViewContactName;
    private TextView textViewContactPhone;
    private TextView textViewContactAddress;
    private TextView textViewInjuryWeeks;

    private TextView textViewEvaluation1;
    private TextView textViewEvaluation2;
    private TextView textViewEvaluation3;
    private TextView textViewEvaluation4;
    private TextView textViewEvaluation5;
    private TextView textViewEvaluation6;

    private UUID patientUUID;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        textViewName = (TextView) findViewById(R.id.textViewText);
        textViewIdentification = (TextView) findViewById(R.id.textViewIdentification);
        textViewGenre = (TextView) findViewById(R.id.textViewGenre);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewPhone = (TextView) findViewById(R.id.textViewPhone);
        textViewBirthday = (TextView) findViewById(R.id.textViewBirthday);
        textViewDepartment = (TextView) findViewById(R.id.textViewDepartment);
        textViewMunicipality = (TextView) findViewById(R.id.textViewMunicipality);
        textViewLane = (TextView) findViewById(R.id.textViewLane);
        textViewContactName = (TextView) findViewById(R.id.textViewContactName);
        textViewContactPhone = (TextView) findViewById(R.id.textViewContactPhone);
        textViewContactAddress = (TextView) findViewById(R.id.textViewContactAddress);
        textViewInjuryWeeks = (TextView) findViewById(R.id.textViewInjuryWeeks);

        textViewEvaluation1 = (TextView) findViewById(R.id.textViewEvaluation1);
        textViewEvaluation2 = (TextView) findViewById(R.id.textViewEvaluation2);
        textViewEvaluation3 = (TextView) findViewById(R.id.textViewEvaluation3);
        textViewEvaluation4 = (TextView) findViewById(R.id.textViewEvaluation4);
        textViewEvaluation5 = (TextView) findViewById(R.id.textViewEvaluation5);
        textViewEvaluation6 = (TextView) findViewById(R.id.textViewEvaluation6);

        patientUUID = UUID.fromString(getIntent().getStringExtra("patientUUID"));
        db = new DatabaseHandler(this);

        fillGUI();
    }

    public void fillGUI() {
        Patient patient = db.getFullPatientWithLatestEvaluation(patientUUID.toString());

        textViewName.setText(patient.getName() + " " + patient.getLastName());
        textViewIdentification.setText(patient.getDocumentType() + " " + patient.getIdentification());
        textViewGenre.setText(getResources().getString(R.string.create_patient_genre) + " " + patient.getGenre());
        textViewAddress.setText(patient.getAddress());
        textViewPhone.setText(patient.getPhone());
        textViewBirthday.setText(getResources().getString(R.string.create_patient_birthdate) + ": " + patient.getBirthday());
        textViewDepartment.setText(patient.getProvince());
        textViewMunicipality.setText(patient.getMunicipality());
        textViewLane.setText(patient.getLane());
        textViewContactName.setText(patient.getContactName() + " " + patient.getContactLastName());
        textViewContactPhone.setText(patient.getContactPhone());
        textViewContactAddress.setText(patient.getContactAddress());
        textViewInjuryWeeks.setText(getResources().getString(R.string.create_patient__injury_weeks) + ": " + patient.getInjuryWeeks());

        // Last evaluation
        List<Evaluation> evaluations = patient.getEvaluationList();

        // Calculates the evaluation results
        if (evaluations.size() != 0) {

            Evaluation evaluation = evaluations.get(evaluations.size() - 1);
            Resources res = getResources();
            evaluation.setEvaluationParameters(res.getInteger(R.integer.ulceras_si), res.getInteger(R.integer.ulceras_no), res.getInteger(R.integer.lesiones_si), res.getInteger(R.integer.lesiones_no),
                    res.getInteger(R.integer.localizacion_brazos_si), res.getInteger(R.integer.localizacion_brazos_no_piernas_no), res.getInteger(R.integer.localizacion_brazos_no_piernas_si),
                    res.getInteger(R.integer.actividades_si), res.getInteger(R.integer.actividades_no), res.getInteger(R.integer.antecedentes_si), res.getInteger(R.integer.antecedentes_no),
                    res.getInteger(R.integer.manta_si), res.getInteger(R.integer.manta_no));

            textViewEvaluation1.setText(getResources().getString(R.string.evaluation_ulceras) + ": " + evaluation.getEvaluationResult(Evaluation.EVALUATION_ULCERAS));
            textViewEvaluation2.setText(getResources().getString(R.string.evaluation_lesiones) + ": " + evaluation.getEvaluationResult(Evaluation.EVALUATION_LESIONES));
            textViewEvaluation3.setText(getResources().getString(R.string.evaluation_localizacion) + ": " + evaluation.getEvaluationResult(Evaluation.EVALUATION_LOCALIZACION));
            textViewEvaluation4.setText(getResources().getString(R.string.evaluation_actividades) + ": " + evaluation.getEvaluationResult(Evaluation.EVALUATION_ACTIVIDADES));
            textViewEvaluation5.setText(getResources().getString(R.string.evaluation_antecedentes) + ": " + evaluation.getEvaluationResult(Evaluation.EVALUATION_ANTECEDENTES));
            textViewEvaluation6.setText(getResources().getString(R.string.evaluation_manta) + ": " + evaluation.getEvaluationResult(Evaluation.EVALUATION_MANTA));
        } else {
            textViewEvaluation1.setText(getResources().getString(R.string.evaluation_ulceras) + ": n/a");
            textViewEvaluation2.setText(getResources().getString(R.string.evaluation_lesiones) + ": n/a");
            textViewEvaluation3.setText(getResources().getString(R.string.evaluation_localizacion) + ": n/a");
            textViewEvaluation4.setText(getResources().getString(R.string.evaluation_actividades) + ": n/a");
            textViewEvaluation5.setText(getResources().getString(R.string.evaluation_antecedentes) + ": n/a");
            textViewEvaluation6.setText(getResources().getString(R.string.evaluation_manta) + ": n/a");
        }
    }

    public void onButtonOkClick(View view) {
        this.onBackPressed();
    }

}
