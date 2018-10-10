package i2t.cideim.bodylocations;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import i2t.cideim.dialogs.BooleanAnswerDialog;
import i2t.cideim.leishmaniasis.EvaluationActivity;
import i2t.cideim.leishmaniasis.MainActivity;
import i2t.cideim.R;
import i2t.cideim.model.UlcerImg;
import i2t.cideim.util.LeishConstants;


/**
 * Created by Domiciano on 29/06/2016.
 */
public class ManoDerechaActivity extends AppCompatActivity {
    int display_height, display_width;
    ImageView mano_frente, mano_espalda;

    RelativeLayout region_mano_der;

    HashMap<Integer, Button> id_zonas;
    ArrayList<Integer> zonas_afectadas;
    HashMap<Button, Integer> botones_lesion;
    HashMap<Integer, Button> noisel_senotob;


    boolean modo_nueva_lesion=false;



    String fecha_fotos;
    SimpleDateFormat format;

    public ArrayList<Integer> listarBodyLocationsParaPacienteActual() {
        ArrayList<Integer> out = new ArrayList<>();
        String cedula = PreferenceManager.getDefaultSharedPreferences(this).getString("patientID", "");
        if (cedula.isEmpty()) return out;

        for(int i = 0; i< EvaluationActivity.currentEvaluation.getUlcerList().size() ; i++){
            UlcerImg img = EvaluationActivity.currentEvaluation.getUlcerList().get(i);
            int alfa = Integer.parseInt(img.getBodyLocation());
            out.add(alfa);
        }
        return out;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mano_derecha_activity);

        //db = new ManejadorBD(this);
        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","");
        //paciente = db.buscarPaciente(id);


        format = new SimpleDateFormat("yyyy-MM-dd");
        fecha_fotos = format.format(Calendar.getInstance().getTime());
        //fecha_fotos = format.format(Calendar.getInstance().getTime());

        modo_nueva_lesion = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("modo_nueva_lesion",false);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

        botones_lesion = new HashMap<>();
        noisel_senotob = new HashMap<>();

        //ToDO: Inicializar cada zona con su respectivo ID
        id_zonas = new HashMap<>();
        id_zonas.put(107, ((Button) findViewById(R.id.bp107)));
        id_zonas.put(108, ((Button) findViewById(R.id.bp108)));
        id_zonas.put(109, ((Button) findViewById(R.id.bp109)));
        id_zonas.put(110, ((Button) findViewById(R.id.bp110)));
        id_zonas.put(111, ((Button) findViewById(R.id.bp111)));
        id_zonas.put(112, ((Button) findViewById(R.id.bp112)));
        id_zonas.put(113, ((Button) findViewById(R.id.bp113)));
        id_zonas.put(114, ((Button) findViewById(R.id.bp114)));
        id_zonas.put(115, ((Button) findViewById(R.id.bp115)));
        id_zonas.put(116, ((Button) findViewById(R.id.bp116)));
        id_zonas.put(117, ((Button) findViewById(R.id.bp117)));
        id_zonas.put(118, ((Button) findViewById(R.id.bp118)));
        id_zonas.put(119, ((Button) findViewById(R.id.bp119)));
        id_zonas.put(120, ((Button) findViewById(R.id.bp120)));


        //ToDO: Estos IDs de zonas afectadas deben venir del XML de la tabla con la variable InjuriesPerZone
        zonas_afectadas = new ArrayList<>();


        /*
        Gson gson = new Gson();
        String json_lista = PreferenceManager.getDefaultSharedPreferences(this).getString("lista_bl","[]");
        ArrayList<Integer> bodyLocation = gson.fromJson(json_lista, new TypeToken<ArrayList<Integer>>(){}.getType());
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_manoder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        region_mano_der = (RelativeLayout) findViewById(R.id.region_mano_der);
        mano_frente = (ImageView) findViewById(R.id.mano_der_frente);
        mano_espalda = (ImageView) findViewById(R.id.mano_der_espalda);

        int altura_brazos = (int) ((display_width) * 0.45);
        LinearLayout.LayoutParams p_espalda = new LinearLayout.LayoutParams(altura_brazos, altura_brazos);
        p_espalda.setMargins((int) (0.05 * display_width), 0, 0, 0);
        LinearLayout.LayoutParams p_frente = new LinearLayout.LayoutParams(altura_brazos, altura_brazos);

        mano_espalda.setLayoutParams(p_espalda);
        mano_frente.setLayoutParams(p_frente);

        hideVolver();

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
                    if (salida.equals("SI")) {
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

    String foto_code = "error";
    File foto = null;

    public void openCamera(View view) {
        int id_zona = botones_lesion.get(view);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id_zona", id_zona).commit();
        Intent i = new Intent(this, ThumbnailsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String path = preferences.getString("last_foto", "NO_FOTO");
            if (!path.equals("NO_FOTO")) {
                preferences.edit().putString("parte_actual", "Lesiones mano derecha")
                        .putString("body_name", "mano_der").commit();

                Intent i = new Intent(this, VistaPreviaFotoActivity.class);
                i.putExtra("foto_path", path);
                startActivity(i);
            }
        }
    }

    boolean control = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (control) {
            control = false;

            Button bp1 = (Button) findViewById(R.id.bp107);
            bp1.setX(mano_espalda.getX() + (int) (0.36 * mano_espalda.getWidth()));
            bp1.setY(mano_espalda.getY() + (int) (0.88 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p10 = new RelativeLayout.LayoutParams((int) (0.375 * mano_frente.getWidth()), (int) (0.27 * mano_frente.getHeight()));
            bp1.setLayoutParams(p10);

            Button bp2 = (Button) findViewById(R.id.bp108);
            bp2.setX(mano_espalda.getX() + (int) (0.275 * mano_espalda.getWidth()));
            bp2.setY(mano_espalda.getY() + (int) (0.485 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p11 = new RelativeLayout.LayoutParams((int) (0.49 * mano_espalda.getWidth()), (int) (0.41 * mano_espalda.getHeight()));
            bp2.setLayoutParams(p11);

            Button bp3 = (Button) findViewById(R.id.bp109);
            bp3.setX(mano_espalda.getX() + (int) (0.07 * mano_espalda.getWidth()));
            bp3.setY(mano_espalda.getY() + (int) (0.24 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p12 = new RelativeLayout.LayoutParams((int) (0.3 * mano_espalda.getWidth()), (int) (0.35 * mano_espalda.getHeight()));
            bp3.setLayoutParams(p12);

            Button bp4 = (Button) findViewById(R.id.bp110);
            bp4.setX(mano_espalda.getX() + (int) (0.01 * mano_espalda.getWidth()));
            bp4.setY(mano_espalda.getY() + (int) (0.45 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p13 = new RelativeLayout.LayoutParams((int) (0.29 * mano_espalda.getWidth()), (int) (0.29 * mano_espalda.getHeight()));
            bp4.setLayoutParams(p13);

            Button bp5 = (Button) findViewById(R.id.bp111);
            bp5.setX(mano_espalda.getX() + (int) (0.25 * mano_espalda.getWidth()));
            bp5.setY(mano_espalda.getY() + (int) (0.16 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p14 = new RelativeLayout.LayoutParams((int) (0.24 * mano_espalda.getWidth()), (int) (0.35 * mano_espalda.getHeight()));
            bp5.setLayoutParams(p14);

            Button bp6 = (Button) findViewById(R.id.bp112);
            bp6.setX(mano_espalda.getX() + (int) (0.505 * mano_espalda.getWidth()));
            bp6.setY(mano_espalda.getY() + (int) (0.165 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p15 = new RelativeLayout.LayoutParams((int) (0.16 * mano_espalda.getWidth()), (int) (0.45 * mano_espalda.getHeight()));
            bp6.setLayoutParams(p15);

            Button bp7 = (Button) findViewById(R.id.bp113);
            bp7.setX(mano_espalda.getX() + (int) (0.68 * mano_espalda.getWidth()));
            bp7.setY(mano_espalda.getY() + (int) (0.5 * mano_espalda.getHeight()));
            RelativeLayout.LayoutParams p16 = new RelativeLayout.LayoutParams((int) (0.31 * mano_espalda.getWidth()), (int) (0.24 * mano_espalda.getHeight()));
            bp7.setLayoutParams(p16);
            //-----------------------------------

            Button bp8 = (Button) findViewById(R.id.bp114);
            bp8.setX(mano_frente.getX() + (int) (0.27 * mano_frente.getWidth()));
            bp8.setY(mano_frente.getY() + (int) (0.88 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p17 = new RelativeLayout.LayoutParams((int) (0.375 * mano_frente.getWidth()), (int) (0.27 * mano_frente.getHeight()));
            bp8.setLayoutParams(p17);

            Button bp9 = (Button) findViewById(R.id.bp115);
            bp9.setX(mano_frente.getX() + (int) (0.24 * mano_frente.getWidth()));
            bp9.setY(mano_frente.getY() + (int) (0.485 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p18 = new RelativeLayout.LayoutParams((int) (0.49 * mano_espalda.getWidth()), (int) (0.41 * mano_espalda.getHeight()));
            bp9.setLayoutParams(p18);

            Button bp10 = (Button) findViewById(R.id.bp116);
            bp10.setX(mano_frente.getX() + (int) (0.63 * mano_frente.getWidth()));
            bp10.setY(mano_frente.getY() + (int) (0.24 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p19 = new RelativeLayout.LayoutParams((int) (0.3 * mano_espalda.getWidth()), (int) (0.35 * mano_espalda.getHeight()));
            bp10.setLayoutParams(p19);

            Button bp11 = (Button) findViewById(R.id.bp117);
            bp11.setX(mano_frente.getX() + (int) (0.705 * mano_frente.getWidth()));
            bp11.setY(mano_frente.getY() + (int) (0.45 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p20 = new RelativeLayout.LayoutParams((int) (0.29 * mano_espalda.getWidth()), (int) (0.29 * mano_espalda.getHeight()));
            bp11.setLayoutParams(p20);

            Button bp12 = (Button) findViewById(R.id.bp118);
            bp12.setX(mano_frente.getX() + (int) (0.515 * mano_frente.getWidth()));
            bp12.setY(mano_frente.getY() + (int) (0.16 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p21 = new RelativeLayout.LayoutParams((int) (0.24 * mano_espalda.getWidth()), (int) (0.35 * mano_espalda.getHeight()));
            bp12.setLayoutParams(p21);

            Button bp13 = (Button) findViewById(R.id.bp119);
            bp13.setX(mano_frente.getX() + (int) (0.34 * mano_frente.getWidth()));
            bp13.setY(mano_frente.getY() + (int) (0.165 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p22 = new RelativeLayout.LayoutParams((int) (0.16 * mano_espalda.getWidth()), (int) (0.45 * mano_espalda.getHeight()));
            bp13.setLayoutParams(p22);

            Button bp14 = (Button) findViewById(R.id.bp120);
            bp14.setX(mano_frente.getX() + (int) (0.02 * mano_frente.getWidth()));
            bp14.setY(mano_frente.getY() + (int) (0.5 * mano_frente.getHeight()));
            RelativeLayout.LayoutParams p23 = new RelativeLayout.LayoutParams((int) (0.31 * mano_espalda.getWidth()), (int) (0.24 * mano_espalda.getHeight()));
            bp14.setLayoutParams(p23);

            ubicarLesiones();
            showVolver();
        }
    }

    public void openManoActivity(View view) {

    }

    int lesiones_terminadas = 0;

    private void ponerPunto(int id_zona, Button zona, int i) {
        Button b = new Button(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(40, 40);
        b.setLayoutParams(p);
        b.setBackgroundResource(R.drawable.bt_lesion_terminado);

        float alfa = zona.getX();
        float beta = zona.getY();
        float gamma = zona.getLayoutParams().width;
        float delta = zona.getLayoutParams().height;

        b.setX(zona.getX() + zona.getLayoutParams().width / 2 - 20);
        b.setY(zona.getY() + zona.getLayoutParams().height / 2 - 20);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentarAbrirCamara(v);
            }
        });
        botones_lesion.put(b, id_zona);
        noisel_senotob.put(id_zona, b);
        region_mano_der.addView(b);
    }

    private void ubicarLesiones() {
        zonas_afectadas.clear();
        botones_lesion.clear();
        noisel_senotob.clear();

        ArrayList<Integer> bodyLocation = listarBodyLocationsParaPacienteActual();
        if(bodyLocation.size() == 0 && !modo_nueva_lesion){
            finish();
            return;
        }
        for (int i : bodyLocation)
            zonas_afectadas.add(i);

        for (int i = 0; i < zonas_afectadas.size(); i++) {
            int id_zona = zonas_afectadas.get(i);
            Button b = id_zonas.get(id_zona);
            if(b == null) continue;
            b.setVisibility(View.VISIBLE);
            ponerPunto(id_zona, b, i);
        }

        verifyFotos();
        if (modo_nueva_lesion) activar_modo_nueva_lesion();
    }

    private void clearLesiones() {
        Iterator it = botones_lesion.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            region_mano_der.removeView(((Button) pair.getKey()));
        }
    }

    boolean volver_showed = false;

    public void showVolver() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("md_ok", true).commit();
        volver_showed = true;
        Button brazo_der_boton_volver = (Button) findViewById(R.id.mano_der_boton_volver);
        brazo_der_boton_volver.setVisibility(View.VISIBLE);
        if(modo_nueva_lesion) ((Button) findViewById(R.id.mano_der_boton_volver)).setText("SIGUIENTE");
    }

    public void hideVolver() {
        Button brazo_der_boton_volver = (Button) findViewById(R.id.mano_der_boton_volver);
        brazo_der_boton_volver.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearLesiones();
        control = true;
        if(modo_nueva_lesion) hideVolver();
    }

    @Override
    public void onBackPressed() {
        if (!modo_nueva_lesion) doVolver(null);
        else finish();
    }


    public void borrarShared() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
    }


    public void doVolver(View v) {
        if (!modo_nueva_lesion) {
            if (volver_showed) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().remove("id_zona").commit();
                finish();
            } else {
                finish();
            }
        } else {
            int id_zona = getSelectedPart();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String cedula = sp.getString("patientID", "UNKNOWN");
            foto_code = "DT" + fecha_fotos + "DT" + "CC" + cedula + "CC_" + "BP" + id_zona + "BP_" + UUID.randomUUID().toString();
            foto = new File(Environment.getExternalStorageDirectory() + "/"+LeishConstants.FOLDER+"/" + foto_code + ".jpg");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putString("last_foto", foto_code.toString())
                    .putInt("id_zona", id_zona).apply();

            Uri uri = Uri.fromFile(foto);


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        12);
            } else {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, 10);
            }


        }
    }

    public void verifyFotos() {
        /*Menos VUELTAS QUE EL DE ABAJO
        for(int i=0 ; i<zonas_afectadas.size() ; i++){
            Button b = noisel_senotob.get(zonas_afectadas.get(i));
            if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BP"+zonas_afectadas.get(i), false)){
                b.setBackgroundResource(R.drawable.bt_lesion);
            }else{
                b.setBackgroundResource(R.drawable.bt_lesion_terminado);
            }
            //RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(tamano_lesion,tamano_lesion);
            //b.setLayoutParams(p);
        }
        */

        for (int i = 107; i <= 120; i = i + 1) {
            Button b = noisel_senotob.get(i);
            if (b == null) continue;

            b.setBackgroundResource(R.drawable.bt_lesion_terminado);
        }


    }

    private void showVolverIfIsComplete() {
        for (int i = 0; i < zonas_afectadas.size(); i++) {
            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BP" + zonas_afectadas.get(i), false))
                lesiones_terminadas++;
        }
        if (lesiones_terminadas == zonas_afectadas.size() && !modo_nueva_lesion) showVolver();
    }

    ArrayList<Button> botones;

    private void activar_modo_nueva_lesion() {
        //Activar listeners: todos menos las lesiones
        botones = new ArrayList<>();

        Iterator it = id_zonas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            final Button b = (Button) pair.getValue();
            b.setVisibility(View.VISIBLE);
            b.setAlpha(0);
            botones.add(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int j=0 ; j<botones.size() ; j++){botones.get(j).setAlpha(0);}
                    b.setAlpha(1);
                    showVolver();
                }
            });
        }

        Iterator it2 = botones_lesion.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry)it2.next();
            ((Button) pair.getKey()).setOnClickListener(null);
            ((Button) id_zonas.get((Integer) pair.getValue())).setOnClickListener(null);
        }

        for(int i : zonas_afectadas) {
            Button b = id_zonas.get(i);
            if(b != null) {
                b.setVisibility(View.VISIBLE);
                b.setAlpha(1);
            }
        }

    }

    public int getSelectedPart() {
        int boton=-1;
        Iterator it = id_zonas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(((Button) pair.getValue()).getAlpha() == 1){
                boton = (Integer) pair.getKey();
            }
        }
        return boton;
    }


    View actual_button;

    private void intentarAbrirCamara(View v) {
        actual_button = v;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    11);
        } else {
            openCamera(actual_button);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(actual_button);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 12: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    int id_zona = getSelectedPart();
                    //ToDO: Guardar en base de datos la nueva lesion
                    //String cedula = paciente.getCedula();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    String cedula = sp.getString("patientID", "UNKNOWN");
                    foto_code = "DT" + fecha_fotos + "DT" + "CC" + cedula + "CC_" + "BP" + id_zona + "BP_" + UUID.randomUUID().toString();
                    foto = new File(Environment.getExternalStorageDirectory() + "/"+LeishConstants.FOLDER+"/" + foto_code + ".jpg");
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putString("last_foto", foto.toString()).putString("foto_code", foto_code.toString())
                            .putInt("id_zona", id_zona).apply();

                    Uri uri = Uri.fromFile(foto);

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(i, 10);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

}