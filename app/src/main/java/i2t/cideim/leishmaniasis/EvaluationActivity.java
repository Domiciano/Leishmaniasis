package i2t.cideim.leishmaniasis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.UUID;

import i2t.cideim.R;
import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.model.Evaluation;

/**
 * Created by Leonardo.
 * Main activity for evaluations.
 */

public class EvaluationActivity extends Activity {

    // Visual components
    private TextView textViewTitle;
    private TextView textViewPatientName;
    private TextView textViewScoreUlceras;
    private TextView textViewScoreLesiones;
    private TextView textViewScoreLocalizacion;
    private TextView textViewScoreActividades;
    private TextView textViewScoreAntecedentes;
    private TextView textViewScoreManta;
    private TextView textViewScore;
    private Button buttonSave;

    // Buttons
    private View imageViewCheckUlceras;
    private View imageViewCheckHisopo;
    private View imageViewCheckLesiones;
    private View imageViewCheckLocalizacion;
    private View imageViewCheckActividades;
    private View imageViewCheckAntecedentes;
    private View imageViewCheckManta;

    // Which questions were answered
    private boolean answeredActividades;
    private boolean answeredAntecedentes;
    private boolean answeredManta;
    private boolean answeredLesiones;
    private boolean answeredLocalizacion;
    private boolean answeredUlceras;
    private boolean answeredHisopo;

    private int currentScore;
    public static Evaluation currentEvaluation;
    private UUID patientUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewScoreUlceras = (TextView) findViewById(R.id.textViewScoreUlceras);
        textViewScoreLesiones = (TextView) findViewById(R.id.textViewScoreLesiones);
        textViewScoreLocalizacion = (TextView) findViewById(R.id.textViewScoreLocalizacion);
        textViewScoreActividades = (TextView) findViewById(R.id.textViewScoreActividades);
        textViewScoreAntecedentes = (TextView) findViewById(R.id.textViewScoreAntecedentes);
        textViewScoreManta = (TextView) findViewById(R.id.textViewScoreManta);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        imageViewCheckHisopo = findViewById(R.id.imageViewHisopo);
        imageViewCheckUlceras = findViewById(R.id.imageViewIcon);
        imageViewCheckLesiones = findViewById(R.id.imageViewCheckLesiones);
        imageViewCheckLocalizacion = findViewById(R.id.imageViewCheckLocalizacion);
        imageViewCheckActividades = findViewById(R.id.imageViewCheckActividades);
        imageViewCheckAntecedentes = findViewById(R.id.imageViewCheckAntecedentes);
        imageViewCheckManta = findViewById(R.id.imageViewCheckManta);

        Resources res = getResources();
        currentScore = 0;

        String patientName = getIntent().getStringExtra("patientName");
        patientUUID = UUID.fromString(getIntent().getStringExtra("patientUUID"));
        currentEvaluation = new Evaluation();
        currentEvaluation.setEvaluationParameters(res.getInteger(R.integer.ulceras_si), res.getInteger(R.integer.ulceras_no), res.getInteger(R.integer.lesiones_si), res.getInteger(R.integer.lesiones_no),
                res.getInteger(R.integer.localizacion_brazos_si), res.getInteger(R.integer.localizacion_brazos_no_piernas_no), res.getInteger(R.integer.localizacion_brazos_no_piernas_si),
                res.getInteger(R.integer.actividades_si), res.getInteger(R.integer.actividades_no), res.getInteger(R.integer.antecedentes_si), res.getInteger(R.integer.antecedentes_no),
                res.getInteger(R.integer.manta_si), res.getInteger(R.integer.manta_no));


        textViewPatientName.setText(patientName);
    }

    @Override
    public void onStart() {
        super.onStart();
        textViewTitle.setText(R.string.evaluation_title);
    }

    public void onButtonUlcerasClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_ulceras);
        startQuestionActivity(QuestionActivity.QUESTION_ULCERAS, patientUUID);
    }

    public void onButtonLesionesClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_lesiones);
        startQuestionActivity(QuestionActivity.QUESTION_LESIONES);
    }

    public void onButtonHisopoClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_lesiones);
        startQuestionActivity(QuestionActivity.QUESTION_HISOPO);
    }

    public void onButtonLocalizacionClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_localizacion);
        startQuestionActivity(QuestionActivity.QUESTION_LOCALIZACION);
    }

    public void onButtonActividadesClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_actividades);
        startQuestionActivity(QuestionActivity.QUESTION_ACTIVIDADES);
    }

    public void onButtonAntecedentesClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_antecedentes);
        startQuestionActivity(QuestionActivity.QUESTION_ANTECEDENTES);
    }

    public void onButtonMantaClick(View view) {
        textViewTitle.setText(R.string.evaluation_subtitle_manta);
        startQuestionActivity(QuestionActivity.QUESTION_MANTA);
    }

    /* Starts a question activity for result */
    public void startQuestionActivity(int question) {
        Intent intent = new Intent(EvaluationActivity.this,
                QuestionActivity.class);
        intent.putExtra("question", question);
        startActivityForResult(intent, question);
    }

    /* Starts a question activity for result. The UUID is used in the Ulceras question */
    public void startQuestionActivity(int question, UUID uuid) {
        Intent intent = new Intent(EvaluationActivity.this,
                QuestionActivity.class);
        intent.putExtra("question", question);
        intent.putExtra("patientId", uuid.toString());
        startActivityForResult(intent, question);
    }

    /* Shows the current score in the GUI */
    public void setScore(int score) {
        currentEvaluation.setScore(score);
        textViewScore.setText(Integer.toString(score));
    }

    /* Saves the evaluation in the database */
    public void onButtonSaveClick(View view) {
        DatabaseHandler db = new DatabaseHandler(this);
        Gson g = new Gson();

        String json = g.toJson(currentEvaluation);
        Log.e("Model","MODELO: "+json);

        db.addEvaluation(currentEvaluation, patientUUID.toString());
        Toast.makeText(this, R.string.evaluation_saved, Toast.LENGTH_LONG).show();
        EvaluationActivity.super.onBackPressed();
    }

    /* Calculates the evaluation's single and total scores */
    public void onNewAnswer() {

        currentScore = 0;

        if (answeredActividades) {
            int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_ACTIVIDADES);
            textViewScoreActividades.setText(Integer.toString(value));
            imageViewCheckActividades.setVisibility(View.VISIBLE);
            currentScore += value;
        }
        if (answeredAntecedentes) {
            int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_ANTECEDENTES);
            textViewScoreAntecedentes.setText(Integer.toString(value));
            imageViewCheckAntecedentes.setVisibility(View.VISIBLE);
            currentScore += value;
        }
        if (answeredManta) {
            int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_MANTA);
            textViewScoreManta.setText(Integer.toString(value));
            imageViewCheckManta.setVisibility(View.VISIBLE);
            currentScore += value;
        }
        if (answeredLesiones) {
            int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_LESIONES);
            textViewScoreLesiones.setText(Integer.toString(value));
            imageViewCheckLesiones.setVisibility(View.VISIBLE);
            currentScore += value;
        }
        if (answeredLocalizacion) {
            int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_LOCALIZACION);
            textViewScoreLocalizacion.setText(Integer.toString(value));
            imageViewCheckLocalizacion.setVisibility(View.VISIBLE);
            currentScore += value;
        }
        if (answeredUlceras) {
            int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_ULCERAS);
            textViewScoreUlceras.setText(Integer.toString(value));
            imageViewCheckUlceras.setVisibility(View.VISIBLE);
            currentScore += value;
        }
        if (answeredHisopo) {
            //int value = currentEvaluation.getEvaluationResult(Evaluation.EVALUATION_ULCERAS);
            //textViewScoreUlceras.setText(Integer.toString(value));
            imageViewCheckHisopo.setVisibility(View.VISIBLE);
            //currentScore += value;
        }

        if (answeredActividades && answeredAntecedentes && answeredManta
                && answeredLesiones && answeredLocalizacion && answeredUlceras && answeredHisopo) {
            buttonSave.setVisibility(View.VISIBLE);
        }

        setScore(currentScore);
    }

    /* Executed when a question was answered */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (data.getIntExtra("question", -1)) {
            case QuestionActivity.QUESTION_ACTIVIDADES:
                currentEvaluation.setActividades(data.getBooleanExtra("answer",
                        false));
                answeredActividades = true;
                onNewAnswer();
                break;
            case QuestionActivity.QUESTION_ANTECEDENTES:
                currentEvaluation.setAntecedentes(data.getBooleanExtra("answer",
                        false));
                answeredAntecedentes = true;
                onNewAnswer();
                break;
            case QuestionActivity.QUESTION_MANTA:
                currentEvaluation.setManta(data.getBooleanExtra("answer", false));
                answeredManta = true;
                onNewAnswer();
                break;
            case QuestionActivity.QUESTION_LESIONES:
                currentEvaluation.setAgrupadas(data
                        .getBooleanExtra("answer", false));
                answeredLesiones = true;
                onNewAnswer();
                break;
            case QuestionActivity.QUESTION_LOCALIZACION:
                currentEvaluation.setLesionesH(data.getBooleanExtra("head", false));
                currentEvaluation.setLesionesB(data.getBooleanExtra("body", false));
                currentEvaluation.setLesionesLA(data.getBooleanExtra("leftArm",
                        false));
                currentEvaluation.setLesionesRA(data.getBooleanExtra("rightArm",
                        false));
                currentEvaluation.setLesionesLL(data.getBooleanExtra("leftLeg",
                        false));
                currentEvaluation.setLesionesRL(data.getBooleanExtra("rightLeg",
                        false));
                answeredLocalizacion = true;
                onNewAnswer();
                break;
            case QuestionActivity.QUESTION_ULCERAS:
                currentEvaluation.setUlceras(data.getBooleanExtra("answer", false));
                answeredUlceras = true;
                onNewAnswer();
                break;

            case QuestionActivity.QUESTION_HISOPO:
                //TODO: Ingresar esta variable con la base cambiada
                //currentEvaluation.setUlceras(data.getBooleanExtra("answer", false));
                answeredHisopo = true;
                onNewAnswer();
                break;



        }
    }

    /* Alerts the user that the unsaved data will be lost */
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.back_alert);
        builder.setPositiveButton(R.string.go_back, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EvaluationActivity.super.onBackPressed();
                    }
                });
        builder.setNegativeButton(R.string.stay, null);
        builder.create().show();
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

}
