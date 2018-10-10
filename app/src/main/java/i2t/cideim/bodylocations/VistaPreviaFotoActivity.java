package i2t.cideim.bodylocations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import i2t.cideim.data.DatabaseHandler;
import i2t.cideim.dialogs.BooleanAnswerDialog;
import i2t.cideim.leishmaniasis.EvaluationActivity;
import i2t.cideim.leishmaniasis.MainActivity;
import i2t.cideim.R;
import i2t.cideim.model.Evaluation;
import i2t.cideim.model.UlcerImg;
import i2t.cideim.util.ImageUtils;


/**
 * Created by Domiciano on 02/06/2016.
 */
public class VistaPreviaFotoActivity extends AppCompatActivity {

    static final boolean MODO_NORMAL=false;
    static final boolean MODO_ELIMINAR=true;

    String foto_path;

    int display_height, display_width;
    Bitmap b;
    ImageView im;

    boolean modo;

    TextView titulo_vista_previa;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);

        db = new DatabaseHandler(this);

        titulo_vista_previa = (TextView) findViewById(R.id.titulo_vista_previa);
        titulo_vista_previa.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("parte_actual","NO_TIT"));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        if(getIntent().getExtras() != null){
            foto_path = getIntent().getExtras().getString("foto_path");
            modo = getIntent().getExtras().getBoolean("modo_eliminar_foto", false);
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_preview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modo == MODO_NORMAL){
                    setToolbarModoNormal();
                }else if(modo == MODO_ELIMINAR){
                    setToolbarModoEliminar();
                }
            }
        });


        Button boton_ok_preview = (Button) findViewById(R.id.boton_ok_preview);
        if(modo == MODO_NORMAL) boton_ok_preview.setText("Añadir Foto");
        else boton_ok_preview.setText("Eliminar Foto");

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;




        im = (ImageView)findViewById(R.id.foto_image);
        intentarMostrarFoto();

    }

    public void intentarMostrarFoto(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    12);
        }else{
            loadFoto();
        }
    }

    public void loadFoto(){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        b = ImageUtils.decodeSampledBitmapFromResource(foto_path, 200,200);

        double proporcion = (double) b.getWidth()/b.getHeight();
        if(proporcion > 1){
            b = Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), matrix, true);
            proporcion = Math.pow(proporcion, -1);
        }

        double scale= 0.6;
        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams((int)(scale*display_width), (int)(scale*display_width/proporcion));
        im.setLayoutParams(p);
        im.setImageBitmap(b);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadFoto();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evaluacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancel_eval) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Está seguro que desea cancelar la evaluación en curso?");
            dialog.show(ft, "dialog_fiebre");
            dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
                @Override
                public void finish(String salida) {
                    if(salida.equals("SI")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doAction(View view) {
        if(modo == MODO_NORMAL) {
            finish();
            Intent i = new Intent(this, ThumbnailsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            //Cómo conseguir EVAL

            int bodylocation = PreferenceManager.getDefaultSharedPreferences(this).getInt("id_zona",-1);
            String nombre_foto = PreferenceManager.getDefaultSharedPreferences(this).getString("foto_code","ERROR");
            //Cómo crear la ulcer
            UlcerImg ulcer = new UlcerImg(UUID.randomUUID().toString(),
                    ""+bodylocation, Calendar.getInstance().getTime(),
                    ".PNG", nombre_foto, "");

            EvaluationActivity.currentEvaluation.addUlcer(ulcer);


        }else if(modo == MODO_ELIMINAR){
            File f = new File(foto_path);
            f.delete();

            //Eliminar la foto del registro
            for(int i=0 ; i<EvaluationActivity.currentEvaluation.getUlcerList().size() ; i++){
                String uuid = EvaluationActivity.currentEvaluation.getUlcerList().get(i).getImgUUID();
                if(foto_path.contains(uuid)){
                    EvaluationActivity.currentEvaluation.getUlcerList().remove(i);
                }
            }

            Intent i = new Intent(this, ThumbnailsActivity.class);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    private void setToolbarModoNormal() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Está seguro que desea descartar la foto actual?");
        dialog.show(ft, "");
        dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
            @Override
            public void finish(String salida) {
                if(salida.equals("SI")) {
                    File f = new File(foto_path);
                    f.delete();
                    finishActivity();
                }
            }
        });
    }

    private void setToolbarModoEliminar() {
        finishActivity();
    }

    private void finishActivity(){
        finish();
    }

    @Override
    public void onBackPressed() {
        if(modo == MODO_NORMAL){
            setToolbarModoNormal();
        }else if(modo == MODO_ELIMINAR){
            setToolbarModoEliminar();
        }
    }
}
