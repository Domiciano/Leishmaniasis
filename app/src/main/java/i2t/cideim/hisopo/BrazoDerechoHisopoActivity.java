package i2t.cideim.hisopo;

import android.content.Intent;
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
import i2t.cideim.custom.EntryDialog;
import i2t.cideim.dialogs.BooleanAnswerDialog;
import i2t.cideim.leishmaniasis.EvaluationActivity;
import i2t.cideim.leishmaniasis.MainActivity;
import i2t.cideim.model.Hisopo;


/**
 * Created by Domiciano on 23/06/2016.
 */
public class BrazoDerechoHisopoActivity extends AppCompatActivity implements EntryDialog.OnDialogDismiss{

    int display_height, display_width;
    ImageView brazo_frente, brazo_espalda;
    RelativeLayout region_brazo_der;

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
        setContentView(R.layout.brazo_der_activity);
        findViewById(R.id.root).setBackgroundColor(getResources().getColor(R.color.color_purple_lesiones));

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id","");



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

        botones_lesion = new HashMap<>();
        noisel_senotob = new HashMap<>();

        //ToDO: Inicializar cada zona con su respectivo ID
        id_zonas = new HashMap<>();
        id_zonas.put(19, ((Button) findViewById(R.id.bp19)));
        id_zonas.put(20, ((Button) findViewById(R.id.bp20)));
        id_zonas.put(21, ((Button) findViewById(R.id.bp21)));
        id_zonas.put(22, ((Button) findViewById(R.id.bp22)));
        id_zonas.put(23, ((Button) findViewById(R.id.bp23)));
        id_zonas.put(24, ((Button) findViewById(R.id.bp24)));
        id_zonas.put(25, ((Button) findViewById(R.id.bp25)));
        //id_zonas.put(26, ((Button) findViewById(R.id.bp26)));
        id_zonas.put(27, ((Button) findViewById(R.id.bp27)));
        id_zonas.put(28, ((Button) findViewById(R.id.bp28)));
        id_zonas.put(29, ((Button) findViewById(R.id.bp29)));
        id_zonas.put(30, ((Button) findViewById(R.id.bp30)));
        id_zonas.put(31, ((Button) findViewById(R.id.bp31)));
        id_zonas.put(32, ((Button) findViewById(R.id.bp32)));
        id_zonas.put(33, ((Button) findViewById(R.id.bp33)));
        //id_zonas.put(34, ((Button) findViewById(R.id.bp34)));


        //ToDO: Estos IDs de zonas afectadas deben venir del XML de la tabla con la variable InjuriesPerZone
        zonas_afectadas = new ArrayList<>();

        /*
        Gson gson = new Gson();
        String json_lista = PreferenceManager.getDefaultSharedPreferences(this).getString("lista_bl","[]");
        ArrayList<Integer> bodyLocation = gson.fromJson(json_lista, new TypeToken<ArrayList<Integer>>(){}.getType());
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_brazoder);
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



        brazo_frente = (ImageView) findViewById(R.id.brazo_der_frente);
        brazo_espalda = (ImageView) findViewById(R.id.brazo_der_espalda);
        region_brazo_der = (RelativeLayout) findViewById(R.id.region_brazo_der);

        int altura_brazos = (int)((display_height)*0.5);
        LinearLayout.LayoutParams p_espalda = new LinearLayout.LayoutParams((int)(0.14*altura_brazos), altura_brazos);
        p_espalda.setMargins(200,0,0,0);
        LinearLayout.LayoutParams p_frente = new LinearLayout.LayoutParams((int)(0.14*altura_brazos),altura_brazos);

        brazo_espalda.setLayoutParams(p_espalda);
        brazo_frente.setLayoutParams(p_frente);
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

        }
    }

    boolean control = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("Prueba","onWindow");
        if(control){
            control = false;
            Button bp1 = (Button) findViewById(R.id.bp19);
            bp1.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp1.setY(brazo_frente.getY() + (int)(0.08*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(brazo_frente.getWidth()));
            bp1.setLayoutParams(p1);

            Button bp2 = (Button) findViewById(R.id.bp20);
            bp2.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp2.setY(brazo_frente.getY() + (int)(0.218*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.6*brazo_frente.getWidth()));
            bp2.setLayoutParams(p2);

            Button bp3 = (Button) findViewById(R.id.bp21);
            bp3.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp3.setY(brazo_frente.getY() + (int)(0.302*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp3.setLayoutParams(p3);

            Button bp4 = (Button) findViewById(R.id.bp22);
            bp4.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp4.setY(brazo_frente.getY() + (int)(0.408*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp4.setLayoutParams(p4);

            Button bp5 = (Button) findViewById(R.id.bp23);
            bp5.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp5.setY(brazo_frente.getY() + (int)(0.515*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp5.setLayoutParams(p5);

            Button bp6 = (Button) findViewById(R.id.bp24);
            bp6.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp6.setY(brazo_frente.getY() + (int)(0.62*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p6 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp6.setLayoutParams(p6);

            Button bp7 = (Button) findViewById(R.id.bp25);
            bp7.setX(brazo_frente.getX() + (int)(0*brazo_frente.getWidth()));
            bp7.setY(brazo_frente.getY() + (int)(0.728*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p7 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.82*brazo_frente.getWidth()));
            bp7.setLayoutParams(p7);

            Button bp8 = (Button) findViewById(R.id.bp26);
            bp8.setX(brazo_frente.getX() + (int)(0.045*brazo_frente.getWidth()));
            bp8.setY(brazo_frente.getY() + (int)(0.84*brazo_frente.getHeight()));
            RelativeLayout.LayoutParams p8 = new RelativeLayout.LayoutParams((int)(0.95*brazo_frente.getWidth()), (int)(1.8*brazo_frente.getWidth()));
            bp8.setLayoutParams(p8);

            //-----------------
            Button bp9 = (Button) findViewById(R.id.bp27);
            bp9.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp9.setY(brazo_espalda.getY() + (int)(0.08*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p9 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(brazo_frente.getWidth()));
            bp9.setLayoutParams(p9);

            Button bp10 = (Button) findViewById(R.id.bp28);
            bp10.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp10.setY(brazo_espalda.getY() + (int)(0.218*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p10 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.6*brazo_frente.getWidth()));
            bp10.setLayoutParams(p10);

            Button bp11 = (Button) findViewById(R.id.bp29);
            bp11.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp11.setY(brazo_espalda.getY() + (int)(0.302*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p11 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp11.setLayoutParams(p11);

            Button bp12 = (Button) findViewById(R.id.bp30);
            bp12.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp12.setY(brazo_espalda.getY() + (int)(0.408*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p12 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp12.setLayoutParams(p12);

            Button bp13 = (Button) findViewById(R.id.bp31);
            bp13.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp13.setY(brazo_espalda.getY() + (int)(0.515*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p13 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp13.setLayoutParams(p13);

            Button bp14 = (Button) findViewById(R.id.bp32);
            bp14.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp14.setY(brazo_espalda.getY() + (int)(0.62*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p14 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.774*brazo_frente.getWidth()));
            bp14.setLayoutParams(p14);

            Button bp15 = (Button) findViewById(R.id.bp33);
            bp15.setX(brazo_espalda.getX() + (int)(0*brazo_espalda.getWidth()));
            bp15.setY(brazo_espalda.getY() + (int)(0.728*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p15 = new RelativeLayout.LayoutParams((int)(brazo_frente.getWidth()), (int)(0.82*brazo_frente.getWidth()));
            bp15.setLayoutParams(p15);

            Button bp16 = (Button) findViewById(R.id.bp34);
            bp16.setX(brazo_espalda.getX() + (int)(0.045*brazo_espalda.getWidth()));
            bp16.setY(brazo_espalda.getY() + (int)(0.84*brazo_espalda.getHeight()));
            RelativeLayout.LayoutParams p16 = new RelativeLayout.LayoutParams((int)(0.95*brazo_frente.getWidth()), (int)(1.8*brazo_frente.getWidth()));
            bp16.setLayoutParams(p16);
            //-----------------



            ubicarLesiones();
            showVolver();
        }
    }

    int tamano_lesion=60;
    int lesiones_terminadas=0;
    private void ponerPunto(int id_zona, Button zona, int i) {
        Button b = new Button(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(tamano_lesion,tamano_lesion);
        b.setLayoutParams(p);
        b.setBackgroundResource(R.drawable.bt_lesion_terminado);

        double offsetx=0, offsety=0;
        if(id_zona == 19){offsetx=0.1; offsety=0.15;}
        else if(id_zona == 20){offsetx=0.06; offsety=0;}
        else if(id_zona == 21){offsetx=0.08; offsety=0;}
        else if(id_zona == 22){offsetx=0.12; offsety=0;}
        else if(id_zona == 23){offsetx=0.06; offsety=0;}
        else if(id_zona == 25){offsetx=-0.05; offsety=0;}
        else if(id_zona == 26){offsetx=-0.18; offsety=0;}

        else if(id_zona == 27){offsetx=-0.06; offsety=0.15;}
        else if(id_zona == 28){offsetx=-0.04; offsety=0;}
        else if(id_zona == 29){offsetx=-0.08; offsety=0;}
        else if(id_zona == 30){offsetx=-0.13; offsety=0;}
        else if(id_zona == 31){offsetx=-0.05; offsety=0;}
        else if(id_zona == 32){offsetx=0; offsety=0;}
        else if(id_zona == 33){offsetx=0.04; offsety=0;}
        else if(id_zona == 34){offsetx=0.18; offsety=0;}


        b.setX(zona.getX() + (int)(zona.getLayoutParams().width*(0.5+offsetx)) - (tamano_lesion/2));
        b.setY(zona.getY() + (int)(zona.getLayoutParams().height*(0.5+offsety)) - (tamano_lesion/2));


        botones_lesion.put(b, id_zona);
        noisel_senotob.put(id_zona, b);
        region_brazo_der.addView(b);
    }

    private void ubicarLesiones() {
        zonas_afectadas.clear();
        botones_lesion.clear();
        noisel_senotob.clear();

        ArrayList<Integer> bodyLocation = listarBodyLocationsParaPacienteActual();

        Log.e("UBICAR", "SIZE: " + bodyLocation.size());

        if (bodyLocation.size() == 0 && !modo_nueva_lesion) {
            finish();
            return;
        }

        for (int i : bodyLocation) {
            zonas_afectadas.add(i);
            Log.e("UBICAR", "" + i);
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
            Map.Entry pair = (Map.Entry)it.next();
            region_brazo_der.removeView(((Button) pair.getKey()));
        }
    }

    boolean volver_showed;

    public void showVolver(){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("bd_ok", true).commit();
        volver_showed = true;
        Button brazo_der_boton_volver = (Button) findViewById(R.id.brazo_der_boton_volver);
        brazo_der_boton_volver.setVisibility(View.VISIBLE);
        if(modo_nueva_lesion) ((Button) findViewById(R.id.brazo_der_boton_volver)).setText("SIGUIENTE");
    }
    public void hideVolver(){
        Button brazo_der_boton_volver = (Button) findViewById(R.id.brazo_der_boton_volver);
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
        if(!modo_nueva_lesion) doVolver(null);
        else finish();
    }

    public void borrarShared(){
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
    }





    public void doVolver(View v) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("id_zona").commit();
        CuerpoHumanoHisopoActivity.modo_nueva_lesion = false;
        finish();
    }

    public void verifyFotos() {
        for (int i = 19; i <= 33; i = i + 1) {
            Button b = noisel_senotob.get(i);
            if (b == null) continue;
            b.setBackgroundResource(R.drawable.bt_lesion_terminado);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int bodyLoc = botones_lesion.get(view);
                    int numeroHisopos = 0;
                    for (int i = 0; i < EvaluationActivity.currentEvaluation.getHisopoList().size(); i++) {
                        Hisopo h = EvaluationActivity.currentEvaluation.getHisopoList().get(i);
                        int bl = Integer.parseInt(h.getBodyLocation());
                        if (bl == bodyLoc) {
                            numeroHisopos = h.getMuestras();
                        }
                    }
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    EntryDialog dialog = EntryDialog.newInstance("¿Cuántos hisopos obtuvo de esta zona?", "" + numeroHisopos, bodyLoc, "OK");
                    dialog.setOnDialogDismiss(BrazoDerechoHisopoActivity.this);
                    dialog.show(ft, "dialog_fiebre");
                }
            });
        }
    }

    private void showVolverIfIsComplete() {
        for(int i=0 ; i<zonas_afectadas.size() ; i++){
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BP"+zonas_afectadas.get(i), false))
                lesiones_terminadas++;
        }
        if(lesiones_terminadas == zonas_afectadas.size()  && !modo_nueva_lesion) showVolver();
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
                    EntryDialog dialog = EntryDialog.newInstance("¿Cuantas hisopos obtuvo de esta zona?", ""+numeroHisopos, bodyLoc , "OK");
                    dialog.setOnDialogDismiss(BrazoDerechoHisopoActivity.this);
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
