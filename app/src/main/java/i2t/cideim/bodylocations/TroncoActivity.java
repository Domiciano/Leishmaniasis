package i2t.cideim.bodylocations;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import i2t.cideim.util.ImageUtils;
import i2t.cideim.util.LeishConstants;


/**
 * Created by Domiciano on 01/06/2016.
 */
public class TroncoActivity extends AppCompatActivity {

    int display_height, display_width;
    ImageView tronco_frente, tronco_espalda;

    RelativeLayout region_tronco;

    HashMap<Integer, Button> id_zonas;
    ArrayList<Integer> zonas_afectadas;
    HashMap<Button, Integer> botones_lesion;
    HashMap<Integer, Button> noisel_senotob;

    boolean modo_nueva_lesion = false;

    //ManejadorBD db;
    //Paciente paciente;

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
        setContentView(R.layout.tronco_activity);

        //db = new ManejadorBD(this);
        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","");
        //paciente = db.buscarPaciente(id);


        format = new SimpleDateFormat("yyyy-MM-dd");
        fecha_fotos = format.format(Calendar.getInstance().getTime());

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
        id_zonas.put(51, ((Button) findViewById(R.id.bp51)));
        id_zonas.put(52, ((Button) findViewById(R.id.bp52)));
        id_zonas.put(53, ((Button) findViewById(R.id.bp53)));
        id_zonas.put(54, ((Button) findViewById(R.id.bp54)));
        id_zonas.put(55, ((Button) findViewById(R.id.bp55)));
        id_zonas.put(56, ((Button) findViewById(R.id.bp56)));
        id_zonas.put(57, ((Button) findViewById(R.id.bp57)));
        id_zonas.put(58, ((Button) findViewById(R.id.bp58)));
        id_zonas.put(59, ((Button) findViewById(R.id.bp59)));
        id_zonas.put(60, ((Button) findViewById(R.id.bp60)));
        id_zonas.put(61, ((Button) findViewById(R.id.bp61)));
        id_zonas.put(62, ((Button) findViewById(R.id.bp62)));
        id_zonas.put(63, ((Button) findViewById(R.id.bp63)));
        id_zonas.put(64, ((Button) findViewById(R.id.bp64)));
        id_zonas.put(65, ((Button) findViewById(R.id.bp65)));
        id_zonas.put(66, ((Button) findViewById(R.id.bp66)));
        id_zonas.put(67, ((Button) findViewById(R.id.bp67)));
        id_zonas.put(68, ((Button) findViewById(R.id.bp68)));
        id_zonas.put(69, ((Button) findViewById(R.id.bp69)));
        id_zonas.put(70, ((Button) findViewById(R.id.bp70)));
        id_zonas.put(71, ((Button) findViewById(R.id.bp71)));
        id_zonas.put(72, ((Button) findViewById(R.id.bp72)));
        id_zonas.put(73, ((Button) findViewById(R.id.bp73)));
        id_zonas.put(74, ((Button) findViewById(R.id.bp74)));


        //ToDO: Estos IDs de zonas afectadas deben venir del XML de la tabla con la variable InjuriesPerZone
        zonas_afectadas = new ArrayList<>();


       /*
        Gson gson = new Gson();
        String json_lista = PreferenceManager.getDefaultSharedPreferences(this).getString("lista_bl","[]");
        ArrayList<Integer> bodyLocation = gson.fromJson(json_lista, new TypeToken<ArrayList<Integer>>(){}.getType());
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tronco);
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



        region_tronco = (RelativeLayout) findViewById(R.id.region_tronco_cuerpo);
        tronco_frente = (ImageView) findViewById(R.id.tronco_frente);
        tronco_espalda = (ImageView) findViewById(R.id.tronco_espalda);


        int width_troncos = (display_width-150)/2;
        LinearLayout.LayoutParams p_espalda = new LinearLayout.LayoutParams(width_troncos, (int)(2.25*width_troncos));
        p_espalda.setMargins(50,0,0,0);
        LinearLayout.LayoutParams p_frente = new LinearLayout.LayoutParams(width_troncos,(int)(2.25*width_troncos));
        tronco_espalda.setLayoutParams(p_espalda);
        tronco_frente.setLayoutParams(p_frente);

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

    String foto_code="error";
    File foto=null;
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
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String path = preferences.getString("last_foto", "NO_FOTO");

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FileOutputStream fos = new FileOutputStream(new File(path));
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                if (!path.equals("NO_FOTO")) {
                    preferences.edit().putString("parte_actual", "Lesiones tronco")
                            .putString("body_name", "cabeza").apply();
                    Intent i = new Intent(this, VistaPreviaFotoActivity.class);
                    i.putExtra("foto_path", path);
                    startActivity(i);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    boolean control = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(control){
            control = false;

            Button bp1 = (Button) findViewById(R.id.bp51);
            bp1.setX(tronco_frente.getX() + (int)(0*tronco_frente.getWidth()));
            bp1.setY(tronco_frente.getY() + (int)(0.221*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p10 = new RelativeLayout.LayoutParams((int)(0.5*tronco_frente.getWidth()), (int)(0.15*tronco_frente.getHeight()));
            bp1.setLayoutParams(p10);

            Button bp2 = (Button) findViewById(R.id.bp52);
            bp2.setX(tronco_frente.getX() + (int)(0*tronco_frente.getWidth()));
            bp2.setY(tronco_frente.getY() + (int)(0.372*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p11 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.135*tronco_espalda.getHeight()));
            bp2.setLayoutParams(p11);

            Button bp3 = (Button) findViewById(R.id.bp53);
            bp3.setX(tronco_frente.getX() + (int)(0*tronco_frente.getWidth()));
            bp3.setY(tronco_frente.getY() + (int)(0.508*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p12 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp3.setLayoutParams(p12);

            Button bp4 = (Button) findViewById(R.id.bp54);
            bp4.setX(tronco_frente.getX() + (int)(0*tronco_frente.getWidth()));
            bp4.setY(tronco_frente.getY() + (int)(0.658*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p13 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.134*tronco_espalda.getHeight()));
            bp4.setLayoutParams(p13);

            Button bp5 = (Button) findViewById(R.id.bp55);
            bp5.setX(tronco_frente.getX() + (int)(0*tronco_frente.getWidth()));
            bp5.setY(tronco_frente.getY() + (int)(0.792*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p14 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.14*tronco_espalda.getHeight()));
            bp5.setLayoutParams(p14);

            Button bp6 = (Button) findViewById(R.id.bp56);
            bp6.setX(tronco_frente.getX() + (int)(0*tronco_frente.getWidth()));
            bp6.setY(tronco_frente.getY() + (int)(0.932*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p15 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp6.setLayoutParams(p15);

            Button bp7 = (Button) findViewById(R.id.bp57);
            bp7.setX(tronco_frente.getX() + (int)(0.505*tronco_frente.getWidth()));
            bp7.setY(tronco_frente.getY() + (int)(0.221*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p16 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp7.setLayoutParams(p16);

            Button bp8 = (Button) findViewById(R.id.bp58);
            bp8.setX(tronco_frente.getX() + (int)(0.505*tronco_frente.getWidth()));
            bp8.setY(tronco_frente.getY() + (int)(0.372*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p17 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.135*tronco_espalda.getHeight()));
            bp8.setLayoutParams(p17);

            Button bp9 = (Button) findViewById(R.id.bp59);
            bp9.setX(tronco_frente.getX() + (int)(0.505*tronco_frente.getWidth()));
            bp9.setY(tronco_frente.getY() + (int)(0.508*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p18 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp9.setLayoutParams(p18);

            Button bp10 = (Button) findViewById(R.id.bp60);
            bp10.setX(tronco_frente.getX() + (int)(0.505*tronco_frente.getWidth()));
            bp10.setY(tronco_frente.getY() + (int)(0.658*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p19 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.134*tronco_espalda.getHeight()));
            bp10.setLayoutParams(p19);

            Button bp11 = (Button) findViewById(R.id.bp61);
            bp11.setX(tronco_frente.getX() + (int)(0.505*tronco_frente.getWidth()));
            bp11.setY(tronco_frente.getY() + (int)(0.792*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p20 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.14*tronco_espalda.getHeight()));
            bp11.setLayoutParams(p20);


            Button bp12 = (Button) findViewById(R.id.bp62);
            bp12.setX(tronco_frente.getX() + (int)(0.505*tronco_frente.getWidth()));
            bp12.setY(tronco_frente.getY() + (int)(0.932*tronco_frente.getHeight()));
            RelativeLayout.LayoutParams p21 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp12.setLayoutParams(p21);


            //----------------------

            Button bp13 = (Button) findViewById(R.id.bp63);
            bp13.setX(tronco_espalda.getX() + (int)(0*tronco_espalda.getWidth()));
            bp13.setY(tronco_espalda.getY() + (int)(0.221*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p22 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp13.setLayoutParams(p22);

            Button bp14 = (Button) findViewById(R.id.bp64);
            bp14.setX(tronco_espalda.getX() + (int)(0*tronco_espalda.getWidth()));
            bp14.setY(tronco_espalda.getY() + (int)(0.372*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p23 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.135*tronco_espalda.getHeight()));
            bp14.setLayoutParams(p23);

            Button bp15 = (Button) findViewById(R.id.bp65);
            bp15.setX(tronco_espalda.getX() + (int)(0*tronco_espalda.getWidth()));
            bp15.setY(tronco_espalda.getY() + (int)(0.508*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p24 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp15.setLayoutParams(p24);

            Button bp16 = (Button) findViewById(R.id.bp66);
            bp16.setX(tronco_espalda.getX() + (int)(0*tronco_espalda.getWidth()));
            bp16.setY(tronco_espalda.getY() + (int)(0.658*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p25 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.134*tronco_espalda.getHeight()));
            bp16.setLayoutParams(p25);

            Button bp17 = (Button) findViewById(R.id.bp67);
            bp17.setX(tronco_espalda.getX() + (int)(0*tronco_espalda.getWidth()));
            bp17.setY(tronco_espalda.getY() + (int)(0.792*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p26 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.14*tronco_espalda.getHeight()));
            bp17.setLayoutParams(p26);

            Button bp18 = (Button) findViewById(R.id.bp68);
            bp18.setX(tronco_espalda.getX() + (int)(0*tronco_espalda.getWidth()));
            bp18.setY(tronco_espalda.getY() + (int)(0.932*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p27 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp18.setLayoutParams(p27);


            Button bp19 = (Button) findViewById(R.id.bp69);
            bp19.setX(tronco_espalda.getX() + (int)(0.505*tronco_espalda.getWidth()));
            bp19.setY(tronco_espalda.getY() + (int)(0.221*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p28 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp19.setLayoutParams(p28);

            Button bp20 = (Button) findViewById(R.id.bp70);
            bp20.setX(tronco_espalda.getX() + (int)(0.505*tronco_espalda.getWidth()));
            bp20.setY(tronco_espalda.getY() + (int)(0.372*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p29 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.135*tronco_espalda.getHeight()));
            bp20.setLayoutParams(p29);

            Button bp21 = (Button) findViewById(R.id.bp71);
            bp21.setX(tronco_espalda.getX() + (int)(0.505*tronco_espalda.getWidth()));
            bp21.setY(tronco_espalda.getY() + (int)(0.508*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p30 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp21.setLayoutParams(p30);

            Button bp22 = (Button) findViewById(R.id.bp72);
            bp22.setX(tronco_espalda.getX() + (int)(0.505*tronco_espalda.getWidth()));
            bp22.setY(tronco_espalda.getY() + (int)(0.658*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p31 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.134*tronco_espalda.getHeight()));
            bp22.setLayoutParams(p31);

            Button bp23 = (Button) findViewById(R.id.bp73);
            bp23.setX(tronco_espalda.getX() + (int)(0.505*tronco_espalda.getWidth()));
            bp23.setY(tronco_espalda.getY() + (int)(0.792*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p32 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.14*tronco_espalda.getHeight()));
            bp23.setLayoutParams(p32);

            Button bp24 = (Button) findViewById(R.id.bp74);
            bp24.setX(tronco_espalda.getX() + (int)(0.505*tronco_espalda.getWidth()));
            bp24.setY(tronco_espalda.getY() + (int)(0.932*tronco_espalda.getHeight()));
            RelativeLayout.LayoutParams p33 = new RelativeLayout.LayoutParams((int)(0.5*tronco_espalda.getWidth()), (int)(0.15*tronco_espalda.getHeight()));
            bp24.setLayoutParams(p33);


            ubicarLesiones();
            showVolver();
        }
    }

    public void openManoActivity(View view){

    }

    int lesiones_terminadas=0;
    private void ponerPunto(int id_zona, Button zona, int i) {
        Button b = new Button(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(40,40);
        b.setLayoutParams(p);

        b.setBackgroundResource(R.drawable.bt_lesion_terminado);

        float alfa = zona.getX();
        float beta = zona.getY();
        float gamma = zona.getLayoutParams().width;
        float delta = zona.getLayoutParams().height;

        double offsetx=0, offsety=0;
        if(id_zona == 1){offsetx=0.15; offsety=0.15;}
        else if(id_zona == 2){offsetx=-0.15; offsety=0.13;}


        b.setX(zona.getX() + (int)(zona.getLayoutParams().width*(0.5+offsetx)) - 20);
        b.setY(zona.getY() + (int)(zona.getLayoutParams().height*(0.5+offsety)) - 20);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentarAbrirCamara(v);
            }
        });
        botones_lesion.put(b, id_zona);
        noisel_senotob.put(id_zona, b);
        region_tronco.addView(b);
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
            region_tronco.removeView(((Button) pair.getKey()));
        }
    }

    boolean volver_showed = false;

    public void showVolver() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("t_ok", true).commit();
        volver_showed = true;
        Button brazo_der_boton_volver = (Button) findViewById(R.id.tronco_boton_volver);
        brazo_der_boton_volver.setVisibility(View.VISIBLE);
        if(modo_nueva_lesion) ((Button) findViewById(R.id.tronco_boton_volver)).setText("SIGUIENTE");
    }

    public void hideVolver() {
        Button brazo_der_boton_volver = (Button) findViewById(R.id.tronco_boton_volver);
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
            foto_code = "GUARAL_DT" + fecha_fotos + "DT" + "CC" + cedula + "CC_" + "BP" + id_zona + "BP_" + UUID.randomUUID().toString();
            foto = new File(Environment.getExternalStorageDirectory() + "/"+LeishConstants.FOLDER+"/" + foto_code + ".jpg");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putString("last_foto", foto.toString()).putString("foto_code", foto_code.toString())
                    .putInt("id_zona", id_zona).apply();

            Uri uri = ImageUtils.getImageContentUri(this, foto);

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

        for (int i = 51; i <= 74; i = i + 1) {
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
        if (lesiones_terminadas == zonas_afectadas.size()  && !modo_nueva_lesion) showVolver();
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
            if(b!=null) {
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
                    foto_code = "GUARAL_DT" + fecha_fotos + "DT" + "CC" + cedula + "CC_" + "BP" + id_zona + "BP_" + UUID.randomUUID().toString();
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
