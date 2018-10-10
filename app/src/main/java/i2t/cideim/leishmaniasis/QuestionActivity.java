package i2t.cideim.leishmaniasis;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import i2t.cideim.R;
import i2t.cideim.bodylocations.CuerpoHumanoActivity;
import i2t.cideim.hisopo.CuerpoHumanoHisopoActivity;
import i2t.cideim.util.LeishConstants;

/**
 * Created by Leonardo.
 * Activity to answer a question in an evaluation.
 */

public class QuestionActivity extends Activity {

    public static final int QUESTION_ACTIVIDADES = 1;
    public static final int QUESTION_ANTECEDENTES = 2;
    public static final int QUESTION_MANTA = 3;
    public static final int QUESTION_LESIONES = 4;
    public static final int QUESTION_LOCALIZACION = 5;
    public static final int QUESTION_ULCERAS = 6;
    public static final int QUESTION_HISOPO = 7;

    private int question;
    private static ToggleButton buttonYes;
    private static ToggleButton buttonNo;
    private static Button buttonSave;
    private static ImageView imageViewCheck;
    private int currentAnswer; // 0: none; 1: yes; -1: no

    private static ToggleButton tbHead;
    private static ToggleButton tbBody;
    private static ToggleButton tbLeftArm;
    private static ToggleButton tbRightArm;
    private static ToggleButton tbLeftLeg;
    private static ToggleButton tbRightLeg;

    private static ImageView ulceraHead;
    private static ImageView ulceraBody;
    private static ImageView ulceraLeftArm;
    private static ImageView ulceraRightArm;
    private static ImageView ulceraLeftLeg;
    private static ImageView ulceraRightLeg;

    private static final int REQUEST_TAKE_PHOTO = 99;
    private File tmpFile;

    private GuaralizadorFragment fragment = null;

    private static int photoCount = 0; // Number of photos taken by the user
    private static TextView photoCounter; // Photo number field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        currentAnswer = 0;
        question = getIntent().getIntExtra("question", 0);
        if (savedInstanceState == null) {

            switch (question) {
                case QUESTION_ULCERAS:
                    fragment = new QuestionUlcerasFragment();
                    fragment.setYesScore(R.integer.ulceras_si);
                    fragment.setNoScore(R.integer.ulceras_no);
                    break;
                case QUESTION_ACTIVIDADES:
                    fragment = new QuestionActividadesFragment();
                    fragment.setYesScore(R.integer.actividades_si);
                    fragment.setNoScore(R.integer.actividades_no);
                    break;
                case QUESTION_ANTECEDENTES:
                    fragment = new QuestionAntecedentesFragment();
                    fragment.setYesScore(R.integer.antecedentes_si);
                    fragment.setNoScore(R.integer.antecedentes_no);
                    break;
                case QUESTION_MANTA:
                    fragment = new QuestionMantaFragment();
                    fragment.setYesScore(R.integer.manta_si);
                    fragment.setNoScore(R.integer.manta_no);
                    break;
                case QUESTION_LESIONES:
                    fragment = new QuestionLesionesFragment();
                    fragment.setYesScore(R.integer.lesiones_si);
                    fragment.setNoScore(R.integer.lesiones_no);
                    break;
                case QUESTION_LOCALIZACION:
                    QuestionLocalizacionFragment fragmentL = new QuestionLocalizacionFragment();
                    fragment = fragmentL;
                    currentAnswer = 1;
                    break;
                case QUESTION_HISOPO:
                    fragment = new QuestionHisopoFragment();
                    fragment.setYesScore(R.integer.hisopo_no);
                    fragment.setNoScore(R.integer.hisopo_si);
                    break;
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public void onButtonYesClick(View view) {
        buttonSave.setVisibility(View.VISIBLE);
        currentAnswer = 1;
        buttonYes.setChecked(true);
        buttonNo.setChecked(false);
    }

    public void onButtonNoClick(View view) {
        buttonSave.setVisibility(View.VISIBLE);
        currentAnswer = -1;
        buttonNo.setChecked(true);
        buttonYes.setChecked(false);
    }

    public void onButtonSaveClick(View view) {
        if (currentAnswer != 0) {
            photoCount = 0;
            if (question == QUESTION_LOCALIZACION) {
                sendResultBack(tbHead.isChecked(), tbBody.isChecked(),
                        tbLeftArm.isChecked(), tbRightArm.isChecked(),
                        tbLeftLeg.isChecked(), tbRightLeg.isChecked());
            } else {
                sendResultBack();
            }
        }
    }

    public void sendResultBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("question", question);
        returnIntent.putExtra("answer", currentAnswer == 1 ? true : false);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void sendResultBack(boolean head, boolean body, boolean leftArm,
                               boolean rightArm, boolean leftLeg, boolean rightLeg) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("question", question);
        returnIntent.putExtra("head", head);
        returnIntent.putExtra("body", body);
        returnIntent.putExtra("leftArm", leftArm);
        returnIntent.putExtra("rightArm", rightArm);
        returnIntent.putExtra("leftLeg", leftLeg);
        returnIntent.putExtra("rightLeg", rightLeg);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onButtonCameraClick(View view) {
        //dispatchTakePictureIntent();
        Intent i = new Intent(this, CuerpoHumanoActivity.class);
        startActivity(i);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Show a message
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String patientId = getIntent().getStringExtra("patientId");

        // Create a file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String fileName = patientId + "_" + timeStamp + "_";
        // Private directory
        File storageDir = getExternalFilesDir(null);
        // Create the file
        tmpFile = File.createTempFile(fileName, ".jpg", storageDir);

        return tmpFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                photoCount++;
                photoCounter.setVisibility(View.VISIBLE);
                photoCounter.setText(photoCount + "");
                // The adapter is reloaded onStart()
            } else {
                // Delete file
                tmpFile.delete();
            }
        }
    }

    public void onBackPressed() {
        // Do nothing when pressed
    }

    /* Shows videos or images */
    public void onButtonPlayPressed(View view) {

        switch (question) {
            case QUESTION_ACTIVIDADES:
                this.showVideo(R.raw.video_actividades);
                break;
            case QUESTION_ULCERAS:
                Integer[] imgsu = new Integer[]{R.drawable.ulceras1, R.drawable.ulceras2};
                this.showImages(imgsu);
                break;
            case QUESTION_LESIONES:
                Integer[] imgsl = new Integer[]{R.drawable.agrupadas1, R.drawable.agrupadas2};
                this.showImages(imgsl);
                break;
            case QUESTION_ANTECEDENTES:
                Integer[] imgsa = new Integer[]{R.drawable.antecedentes1, R.drawable.antecedentes2, R.drawable.antecedentes3};
                this.showImages(imgsa);
                break;
            case QUESTION_MANTA:
                this.showVideo(R.raw.video_manta);
                break;
        }
    }

    public void showVideo(int videoResId) {
        VideoDialogFragment videoDialog = new VideoDialogFragment();
        videoDialog.setVideoResId(videoResId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, videoDialog)
                .addToBackStack(null).commit();
    }

    public void showImages(Integer[] imgs) {
        ImagesDialogFragment imagesDialog = new ImagesDialogFragment();
        imagesDialog.setImagesResList(imgs);
        imagesDialog.show(getFragmentManager(), "Images");
    }

    public static class GuaralizadorFragment extends Fragment {

        private int yesScore;
        private int noScore;

        public int getYesScore() {
            return yesScore;
        }

        public void setYesScore(int yesScore) {
            this.yesScore = yesScore;
        }

        public int getNoScore() {
            return noScore;
        }

        public void setNoScore(int noScore) {
            this.noScore = noScore;
        }

        public GuaralizadorFragment() {

        }

        public int getValueFromAnswer(boolean answer) {
            return answer ? getYesScore() : getNoScore();
        }
    }

    public static class QuestionUlcerasFragment extends GuaralizadorFragment {

        public QuestionUlcerasFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_question_ulceras, container, false);
            buttonYes = (ToggleButton) rootView.findViewById(R.id.buttonYes);
            buttonNo = (ToggleButton) rootView.findViewById(R.id.buttonNo);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
            imageViewCheck = (ImageView) rootView.findViewById(R.id.imageViewCheck);
            photoCounter = (TextView) rootView.findViewById(R.id.numFotos);

            RelativeLayout contenedor_foto = (RelativeLayout) rootView.findViewById(R.id.contenedor_foto);
            contenedor_foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CuerpoHumanoActivity.class);
                    startActivity(i);
                }
            });

            for(int i=0 ; i<contenedor_foto.getChildCount() ; i++) {
                contenedor_foto.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), CuerpoHumanoActivity.class);
                        startActivity(i);
                    }
                });
            }


            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int numeroFotos = getNumeroFotosPorPaciente(sp.getString("patientID", "XXXXX"));
            if(numeroFotos>0) imageViewCheck.setVisibility(View.VISIBLE);
            else imageViewCheck.setVisibility(View.INVISIBLE);
        }

        public int getNumeroFotosPorPaciente(String cedula) {
            File carpeta = new File(Environment.getExternalStorageDirectory()+"/"+ LeishConstants.FOLDER+"/");
            if(!carpeta.exists()) return 0;
            int contador = 0;
            for(int i=0 ; i<carpeta.list().length ; i++){
                String filename = carpeta.list()[i];
                if(filename.contains(cedula)){
                    contador++;
                }
            }
            return contador;
        }

    }

    public static class QuestionLesionesFragment extends GuaralizadorFragment {

        public QuestionLesionesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_question_lesiones, container, false);
            buttonYes = (ToggleButton) rootView.findViewById(R.id.buttonYes);
            buttonNo = (ToggleButton) rootView.findViewById(R.id.buttonNo);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
            return rootView;
        }
    }

    public static class QuestionHisopoFragment extends GuaralizadorFragment {

        public QuestionHisopoFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_question_hisopo, container, false);
            buttonYes = (ToggleButton) rootView.findViewById(R.id.buttonYes);
            buttonNo = (ToggleButton) rootView.findViewById(R.id.buttonNo);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);


            RelativeLayout contenedor_foto = (RelativeLayout) rootView.findViewById(R.id.contenedor_hisopito);
            contenedor_foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CuerpoHumanoHisopoActivity.class);
                    startActivity(i);
                }
            });

            for(int i=0 ; i<contenedor_foto.getChildCount() ; i++) {
                contenedor_foto.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), CuerpoHumanoHisopoActivity.class);
                        startActivity(i);
                    }
                });
            }

            return rootView;
        }
    }

    public static class QuestionLocalizacionFragment extends GuaralizadorFragment {
        public QuestionLocalizacionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_question_localizacion, container, false);


            tbHead = (ToggleButton) rootView
                    .findViewById(R.id.toggleButtonHead);
            tbBody = (ToggleButton) rootView
                    .findViewById(R.id.toggleButtonBody);
            tbLeftArm = (ToggleButton) rootView
                    .findViewById(R.id.toggleButtonLeftArm);
            tbRightArm = (ToggleButton) rootView
                    .findViewById(R.id.toggleButtonRightArm);
            tbLeftLeg = (ToggleButton) rootView
                    .findViewById(R.id.toggleButtonLeftLeg);
            tbRightLeg = (ToggleButton) rootView
                    .findViewById(R.id.toggleButtonRightLeg);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);

            ulceraHead = (ImageView) rootView.findViewById(R.id.imageViewUlceraH);
            ulceraBody = (ImageView) rootView.findViewById(R.id.imageViewUlceraB);
            ulceraLeftArm = (ImageView) rootView.findViewById(R.id.imageViewUlceraLA);
            ulceraRightArm = (ImageView) rootView.findViewById(R.id.imageViewUlceraRA);
            ulceraLeftLeg = (ImageView) rootView.findViewById(R.id.imageViewUlceraLL);
            ulceraRightLeg = (ImageView) rootView.findViewById(R.id.imageViewUlceraRL);

            tbHead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ulceraHead.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
            });

            tbBody.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ulceraBody.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
            });

            tbLeftArm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ulceraLeftArm.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
            });

            tbRightArm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ulceraRightArm.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
            });

            tbLeftLeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ulceraLeftLeg.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
            });

            tbRightLeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ulceraRightLeg.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                }
            });


            return rootView;
        }
    }

    public static class QuestionActividadesFragment extends
            GuaralizadorFragment {

        public QuestionActividadesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_question_actividades, container, false);
            buttonYes = (ToggleButton) rootView.findViewById(R.id.buttonYes);
            buttonNo = (ToggleButton) rootView.findViewById(R.id.buttonNo);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
            return rootView;
        }
    }

    public static class QuestionAntecedentesFragment extends
            GuaralizadorFragment {

        public QuestionAntecedentesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_question_antecedentes, container, false);
            buttonYes = (ToggleButton) rootView.findViewById(R.id.buttonYes);
            buttonNo = (ToggleButton) rootView.findViewById(R.id.buttonNo);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
            return rootView;
        }
    }

    public static class QuestionMantaFragment extends GuaralizadorFragment {

        public QuestionMantaFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_question_manta,
                    container, false);
            buttonYes = (ToggleButton) rootView.findViewById(R.id.buttonYes);
            buttonNo = (ToggleButton) rootView.findViewById(R.id.buttonNo);
            buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
            return rootView;
        }
    }
}
