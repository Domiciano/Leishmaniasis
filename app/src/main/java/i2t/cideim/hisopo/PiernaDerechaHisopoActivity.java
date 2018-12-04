package i2t.cideim.hisopo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import i2t.cideim.R;
import i2t.cideim.bodylocations.VistaPreviaFotoActivity;
import i2t.cideim.custom.EntryDialog;
import i2t.cideim.dialogs.BooleanAnswerDialog;
import i2t.cideim.leishmaniasis.EvaluationActivity;
import i2t.cideim.leishmaniasis.MainActivity;
import i2t.cideim.model.Hisopo;


/**
 * Created by Domiciano on 23/06/2016.
 */
public class PiernaDerechaHisopoActivity extends AppCompatActivity implements EntryDialog.OnDialogDismiss{
    int display_height, display_width;
    ImageView pierna_frente, pierna_espalda;

    RelativeLayout region_pierna_der;

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

        for(int i = 0; i< EvaluationActivity.currentEvaluation.getHisopoList().size() ; i++){
            Hisopo h = EvaluationActivity.currentEvaluation.getHisopoList().get(i);
            int alfa = Integer.parseInt(h.getBodyLocation());
            out.add(alfa);
        }
        return out;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pierna_der_activity);
        findViewById(R.id.root).setBackgroundColor(getResources().getColor(R.color.color_purple_lesiones));
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
        id_zonas.put(75, ((Button) findViewById(R.id.bp75)));
        id_zonas.put(76, ((Button) findViewById(R.id.bp76)));
        id_zonas.put(77, ((Button) findViewById(R.id.bp77)));
        id_zonas.put(78, ((Button) findViewById(R.id.bp78)));
        id_zonas.put(79, ((Button) findViewById(R.id.bp79)));
        id_zonas.put(80, ((Button) findViewById(R.id.bp80)));
        id_zonas.put(81, ((Button) findViewById(R.id.bp81)));
        id_zonas.put(82, ((Button) findViewById(R.id.bp82)));
        id_zonas.put(83, ((Button) findViewById(R.id.bp83)));
        id_zonas.put(84, ((Button) findViewById(R.id.bp84)));
        id_zonas.put(85, ((Button) findViewById(R.id.bp85)));
        id_zonas.put(86, ((Button) findViewById(R.id.bp86)));
        id_zonas.put(87, ((Button) findViewById(R.id.bp87)));
        id_zonas.put(88, ((Button) findViewById(R.id.bp88)));
        id_zonas.put(89, ((Button) findViewById(R.id.bp89)));
        id_zonas.put(90, ((Button) findViewById(R.id.bp90)));


        //ToDO: Estos IDs de zonas afectadas deben venir del XML de la tabla con la variable InjuriesPerZone
        zonas_afectadas = new ArrayList<>();


        /*
        Gson gson = new Gson();
        String json_lista = PreferenceManager.getDefaultSharedPreferences(this).getString("lista_bl","[]");
        ArrayList<Integer> bodyLocation = gson.fromJson(json_lista, new TypeToken<ArrayList<Integer>>(){}.getType());
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_piernader);
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


        region_pierna_der = (RelativeLayout) findViewById(R.id.region_pierna_der);
        pierna_frente = (ImageView) findViewById(R.id.pierna_der_frente);
        pierna_espalda = (ImageView) findViewById(R.id.pierna_der_espalda);

        int altura_brazos = (int)((display_height)*0.5);
        LinearLayout.LayoutParams p_espalda = new LinearLayout.LayoutParams((int)(0.29*altura_brazos), altura_brazos);
        p_espalda.setMargins(100,0,0,0);
        LinearLayout.LayoutParams p_frente = new LinearLayout.LayoutParams((int)(0.29*altura_brazos),altura_brazos);

        pierna_espalda.setLayoutParams(p_espalda);
        pierna_frente.setLayoutParams(p_frente);

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
        Intent i = new Intent(this, PreguntaHisopoActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String path = preferences.getString("last_foto","NO_FOTO");
            if(!path.equals("NO_FOTO")) {
                preferences.edit().putString("parte_actual","Lesiones pierna derecha")
                        .putString("body_name", "pierna_der").commit();

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
        if(control){
            control = false;

            Button bp1 = (Button) findViewById(R.id.bp75);
            bp1.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp1.setY(pierna_frente.getY() + (int)(0.085*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p10 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp1.setLayoutParams(p10);

            Button bp2 = (Button) findViewById(R.id.bp76);
            bp2.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp2.setY(pierna_frente.getY() + (int)(0.212*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p11 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp2.setLayoutParams(p11);

            Button bp3 = (Button) findViewById(R.id.bp77);
            bp3.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp3.setY(pierna_frente.getY() + (int)(0.338*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p12 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp3.setLayoutParams(p12);

            Button bp4 = (Button) findViewById(R.id.bp78);
            bp4.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp4.setY(pierna_frente.getY() + (int)(0.465*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p13 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp4.setLayoutParams(p13);

            Button bp5 = (Button) findViewById(R.id.bp79);
            bp5.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp5.setY(pierna_frente.getY() + (int)(0.592*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p14 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp5.setLayoutParams(p14);

            Button bp6 = (Button) findViewById(R.id.bp80);
            bp6.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp6.setY(pierna_frente.getY() + (int)(0.718*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p15 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.12*pierna_frente.getHeight()));
            bp6.setLayoutParams(p15);

            Button bp7 = (Button) findViewById(R.id.bp81);
            bp7.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp7.setY(pierna_frente.getY() + (int)(0.84*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p16 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.118*pierna_frente.getHeight()));
            bp7.setLayoutParams(p16);

            Button bp8 = (Button) findViewById(R.id.bp82);
            bp8.setX(pierna_frente.getX() + (int)(0*pierna_frente.getWidth()));
            bp8.setY(pierna_frente.getY() + (int)(0.96*pierna_frente.getHeight()));
            RelativeLayout.LayoutParams p17 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp8.setLayoutParams(p17);
            //------------------------------------

            Button bp9 = (Button) findViewById(R.id.bp83);
            bp9.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp9.setY(pierna_espalda.getY() + (int)(0.085*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p18 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp9.setLayoutParams(p18);

            Button bp10 = (Button) findViewById(R.id.bp84);
            bp10.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp10.setY(pierna_espalda.getY() + (int)(0.212*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p19 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp10.setLayoutParams(p19);

            Button bp11 = (Button) findViewById(R.id.bp85);
            bp11.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp11.setY(pierna_espalda.getY() + (int)(0.338*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p20 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp11.setLayoutParams(p20);

            Button bp12 = (Button) findViewById(R.id.bp86);
            bp12.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp12.setY(pierna_espalda.getY() + (int)(0.465*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p21 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp12.setLayoutParams(p21);

            Button bp13 = (Button) findViewById(R.id.bp87);
            bp13.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp13.setY(pierna_espalda.getY() + (int)(0.592*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p22 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp13.setLayoutParams(p22);

            Button bp14 = (Button) findViewById(R.id.bp88);
            bp14.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp14.setY(pierna_espalda.getY() + (int)(0.718*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p23 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.12*pierna_frente.getHeight()));
            bp14.setLayoutParams(p23);

            Button bp15 = (Button) findViewById(R.id.bp89);
            bp15.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp15.setY(pierna_espalda.getY() + (int)(0.84*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p24 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.118*pierna_frente.getHeight()));
            bp15.setLayoutParams(p24);

            Button bp16 = (Button) findViewById(R.id.bp90);
            bp16.setX(pierna_espalda.getX() + (int)(0*pierna_espalda.getWidth()));
            bp16.setY(pierna_espalda.getY() + (int)(0.96*pierna_espalda.getHeight()));
            RelativeLayout.LayoutParams p25 = new RelativeLayout.LayoutParams((int)(pierna_frente.getWidth()), (int)(0.125*pierna_frente.getHeight()));
            bp16.setLayoutParams(p25);





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
        if(id_zona == 75){offsetx=0.1; offsety=0;}
        else if(id_zona == 76){offsetx=0.1; offsety=0;}
        else if(id_zona == 77){offsetx=0.1; offsety=0;}
        else if(id_zona == 78){offsetx=0.1; offsety=0;}
        else if(id_zona == 79){offsetx=0.1; offsety=0;}
        else if(id_zona == 80){offsetx=0.15; offsety=0;}
        else if(id_zona == 81){offsetx=0.22; offsety=0.15;}
        else if(id_zona == 82){offsetx=0.2; offsety=0;}

        else if(id_zona == 83){offsetx=-0.12; offsety=0;}
        else if(id_zona == 84){offsetx=-0.12; offsety=0;}
        else if(id_zona == 85){offsetx=-0.12; offsety=0;}
        else if(id_zona == 86){offsetx=-0.12; offsety=0;}
        else if(id_zona == 87){offsetx=-0.12; offsety=0;}
        else if(id_zona == 88){offsetx=-0.18; offsety=0;}
        else if(id_zona == 89){offsetx=-0.22; offsety=0;}
        else if(id_zona == 90){offsetx=-0.22; offsety=0;}


        b.setX(zona.getX() + (int)(zona.getLayoutParams().width*(0.5+offsetx)) - 20);
        b.setY(zona.getY() + (int)(zona.getLayoutParams().height*(0.5+offsety)) - 20);

        botones_lesion.put(b, id_zona);
        noisel_senotob.put(id_zona, b);
        region_pierna_der.addView(b);
    }

    private void ubicarLesiones() {
        zonas_afectadas.clear();
        botones_lesion.clear();
        noisel_senotob.clear();

        ArrayList<Integer> bodyLocation = listarBodyLocationsParaPacienteActual();

        Log.e("UBICAR","SIZE: "+bodyLocation.size());

        if(bodyLocation.size() == 0 && !modo_nueva_lesion){
            finish();
            return;
        }

        for (int i : bodyLocation){
            zonas_afectadas.add(i);
            Log.e("UBICAR",""+i);
        }


        for (int i = 0; i < zonas_afectadas.size(); i++) {
            int id_zona = zonas_afectadas.get(i);
            Button b = id_zonas.get(id_zona);
            if (b == null) continue;
            ponerPunto(id_zona, b, i);
            b.setVisibility(View.VISIBLE);
        }

        verifyFotos();
        if (modo_nueva_lesion) activar_modo_nueva_lesion();
    }

    private void clearLesiones() {
        Iterator it = botones_lesion.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            region_pierna_der.removeView(((Button) pair.getKey()));
        }
    }

    boolean volver_showed = false;

    public void showVolver() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("pd_ok", true).commit();
        volver_showed = true;
        Button brazo_der_boton_volver = (Button) findViewById(R.id.pierna_der_boton_volver);
        brazo_der_boton_volver.setVisibility(View.VISIBLE);
        if(modo_nueva_lesion) ((Button) findViewById(R.id.pierna_der_boton_volver)).setText("SIGUIENTE");
    }

    public void hideVolver() {
        Button brazo_der_boton_volver = (Button) findViewById(R.id.pierna_der_boton_volver);
        brazo_der_boton_volver.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearLesiones();
        control = true;
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
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("id_zona").commit();
        CuerpoHumanoHisopoActivity.modo_nueva_lesion = false;
        finish();
    }


    public void verifyFotos() {
        for (int i = 75; i <= 90; i = i + 1) {
            Button b = noisel_senotob.get(i);
            if (b == null) continue;
            b.setBackgroundResource(R.drawable.bt_lesion_terminado);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int bodyLoc = botones_lesion.get(view);
                    int numeroHisopos = 0;
                    for(int i=0 ; i<EvaluationActivity.currentEvaluation.getHisopoList().size() ; i++){
                        Hisopo h = EvaluationActivity.currentEvaluation.getHisopoList().get(i);
                        int bl = Integer.parseInt(h.getBodyLocation());
                        if(bl == bodyLoc){
                            numeroHisopos = h.getMuestras();
                        }
                    }
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    EntryDialog dialog = EntryDialog.newInstance("¿Cuantas hisopos obtuvo de esta zona?", ""+numeroHisopos, bodyLoc , "OK");
                    dialog.setOnDialogDismiss(PiernaDerechaHisopoActivity.this);
                    dialog.show(ft, "dialog_fiebre");
                }
            });
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
            Map.Entry pair = (Map.Entry) it.next();
            final Button b = (Button) pair.getValue();
            b.setVisibility(View.VISIBLE);
            b.setAlpha(0);
            botones.add(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < botones.size(); j++) {
                        botones.get(j).setAlpha(0);
                    }
                    b.setAlpha(1);

                    int bodyLoc = getSelectedPart();
                    int numeroHisopos = 0;
                    for(int i=0 ; i<EvaluationActivity.currentEvaluation.getHisopoList().size() ; i++){
                        Hisopo h = EvaluationActivity.currentEvaluation.getHisopoList().get(i);
                        int bl = Integer.parseInt(h.getBodyLocation());
                        if(bl == bodyLoc){
                            numeroHisopos = h.getMuestras();
                        }
                    }
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    EntryDialog dialog = EntryDialog.newInstance("¿Cuántos hisopos obtuvo de esta zona?", ""+numeroHisopos, bodyLoc , "OK");
                    dialog.setOnDialogDismiss(PiernaDerechaHisopoActivity.this);
                    dialog.show(ft, "dialog_fiebre");
                }
            });
        }

        /*
        Iterator it2 = botones_lesion.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            ((Button) pair.getKey()).setOnClickListener(null);
            ((Button) id_zonas.get((Integer) pair.getValue())).setOnClickListener(null);
        }
        */

        //Activar colores en zonas afectadas
        for (int i : zonas_afectadas) {
            Button b = id_zonas.get(i);
            if (b != null) {
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

    private void intentarAbrirCamara(View v) {
        openCamera(v);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(null);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void finish(EntryDialog dialog, String sintoma, int bodylocation) {
        dialog.dismiss();

        //Eliminar registro
        for(int i=0 ; i<EvaluationActivity.currentEvaluation.getHisopoList().size() ; i++){
            if( bodylocation == Integer.parseInt(EvaluationActivity.currentEvaluation.getHisopoList().get(i).getBodyLocation()) ){
                EvaluationActivity.currentEvaluation.getHisopoList().remove(i);
            }
        }
        //Agregar uno nuevo
        if(!sintoma.trim().equals("0")){
            Hisopo h = new Hisopo(UUID.randomUUID().toString(), "" + bodylocation, Calendar.getInstance().getTime(), Integer.parseInt(sintoma));
            EvaluationActivity.currentEvaluation.getHisopoList().add(h);
            ubicarLesiones();
        }
    }
}
