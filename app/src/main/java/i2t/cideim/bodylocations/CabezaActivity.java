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
public class CabezaActivity extends AppCompatActivity {

    int display_height, display_width;
    ImageView cabeza_frente, cabeza_espalda;

    RelativeLayout region_cabeza;

    HashMap<Integer, Button> id_zonas;
    ArrayList<Integer> zonas_afectadas;
    HashMap<Button, Integer> botones_lesion;
    HashMap<Integer, Button> noisel_senotob;


    boolean modo_nueva_lesion = false;


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
        setContentView(R.layout.cabeza_activity);

        format = new SimpleDateFormat("yyyy-MM-dd");
        fecha_fotos = format.format(Calendar.getInstance().getTime());

        modo_nueva_lesion = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("modo_nueva_lesion", false);

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
        id_zonas.put(1, ((Button) findViewById(R.id.bp1)));
        id_zonas.put(2, ((Button) findViewById(R.id.bp2)));
        id_zonas.put(3, ((Button) findViewById(R.id.bp3)));
        id_zonas.put(4, ((Button) findViewById(R.id.bp4)));
        id_zonas.put(5, ((Button) findViewById(R.id.bp5)));
        id_zonas.put(6, ((Button) findViewById(R.id.bp6)));
        id_zonas.put(7, ((Button) findViewById(R.id.bp7)));
        id_zonas.put(8, ((Button) findViewById(R.id.bp8)));
        id_zonas.put(9, ((Button) findViewById(R.id.bp9)));
        id_zonas.put(10, ((Button) findViewById(R.id.bp10)));
        id_zonas.put(11, ((Button) findViewById(R.id.bp11)));
        id_zonas.put(12, ((Button) findViewById(R.id.bp12)));
        id_zonas.put(13, ((Button) findViewById(R.id.bp13)));
        id_zonas.put(14, ((Button) findViewById(R.id.bp14)));
        id_zonas.put(15, ((Button) findViewById(R.id.bp15)));
        id_zonas.put(16, ((Button) findViewById(R.id.bp16)));
        id_zonas.put(17, ((Button) findViewById(R.id.bp17)));
        id_zonas.put(18, ((Button) findViewById(R.id.bp18)));


        //ToDO: Estos IDs de zonas afectadas deben venir del XML de la tabla con la variable InjuriesPerZone
        zonas_afectadas = new ArrayList<>();
        //ToDO: Cargar las lesiones de la base de datos

        /*
        Gson gson = new Gson();
        String json_lista = PreferenceManager.getDefaultSharedPreferences(this).getString("lista_bl","[]");
        ArrayList<Integer> bodyLocation = gson.fromJson(json_lista, new TypeToken<ArrayList<Integer>>(){}.getType());
        */



        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cabeza);
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


        region_cabeza = (RelativeLayout) findViewById(R.id.region_cabeza);
        cabeza_frente = (ImageView) findViewById(R.id.cabeza_frente);
        cabeza_espalda = (ImageView) findViewById(R.id.cabeza_espalda);

        int width_cabezas = (display_width - 100) / 2;
        LinearLayout.LayoutParams p_espalda = new LinearLayout.LayoutParams(width_cabezas, (int) (1.43 * width_cabezas));
        p_espalda.setMargins(50, 0, 0, 0);
        LinearLayout.LayoutParams p_frente = new LinearLayout.LayoutParams((int) (0.926 * width_cabezas), (int) (1.43 * width_cabezas));

        cabeza_espalda.setLayoutParams(p_espalda);
        cabeza_frente.setLayoutParams(p_frente);

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
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String path = preferences.getString("last_foto", "NO_FOTO");

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FileOutputStream fos = new FileOutputStream(new File(path));
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                if (!path.equals("NO_FOTO")) {
                    preferences.edit().putString("parte_actual", "Lesiones cabeza")
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
        if (control) {
            control = false;
            Button bp1 = (Button) findViewById(R.id.bp1);
            bp1.setX(cabeza_frente.getX() + (int) (0 * cabeza_frente.getWidth()));
            bp1.setY(cabeza_frente.getY() + (int) (0.12 * cabeza_frente.getHeight()));
            RelativeLayout.LayoutParams p10 = new RelativeLayout.LayoutParams((int) (0.5 * cabeza_frente.getWidth()), (int) (0.55 * cabeza_frente.getWidth()));
            bp1.setLayoutParams(p10);

            Button bp2 = (Button) findViewById(R.id.bp2);
            bp2.setX(cabeza_frente.getX() + (int) (0.464 * cabeza_espalda.getWidth()));
            bp2.setY(cabeza_frente.getY() + (int) (0.12 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p11 = new RelativeLayout.LayoutParams((int) (0.5 * cabeza_frente.getWidth()), (int) (0.55 * cabeza_frente.getWidth()));
            bp2.setLayoutParams(p11);

            Button bp3 = (Button) findViewById(R.id.bp3);
            bp3.setX(cabeza_frente.getX() + (int) (0.09 * cabeza_espalda.getWidth()));
            bp3.setY(cabeza_frente.getY() + (int) (0.4742 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p12 = new RelativeLayout.LayoutParams((int) (0.38 * cabeza_frente.getWidth()), (int) (0.16 * cabeza_frente.getWidth()));
            bp3.setLayoutParams(p12);

            Button bp4 = (Button) findViewById(R.id.bp4);
            bp4.setX(cabeza_frente.getX() + (int) (0.5 * cabeza_espalda.getWidth()));
            bp4.setY(cabeza_frente.getY() + (int) (0.4742 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p13 = new RelativeLayout.LayoutParams((int) (0.38 * cabeza_frente.getWidth()), (int) (0.16 * cabeza_frente.getWidth()));
            bp4.setLayoutParams(p13);

            Button bp5 = (Button) findViewById(R.id.bp5);
            bp5.setX(cabeza_frente.getX() + (int) (0.79 * cabeza_espalda.getWidth()));
            bp5.setY(cabeza_frente.getY() + (int) (0.46 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p14 = new RelativeLayout.LayoutParams((int) (0.15 * cabeza_frente.getWidth()), (int) (0.33 * cabeza_frente.getWidth()));
            bp5.setLayoutParams(p14);

            Button bp6 = (Button) findViewById(R.id.bp6);
            bp6.setX(cabeza_frente.getX() + (int) (0 * cabeza_espalda.getWidth()));
            bp6.setY(cabeza_frente.getY() + (int) (0.46 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p15 = new RelativeLayout.LayoutParams((int) (0.15 * cabeza_frente.getWidth()), (int) (0.33 * cabeza_frente.getWidth()));
            bp6.setLayoutParams(p15);

            Button bp7 = (Button) findViewById(R.id.bp7);
            bp7.setX(cabeza_frente.getX() + (int) (0.334 * cabeza_espalda.getWidth()));
            bp7.setY(cabeza_frente.getY() + (int) (0.4742 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p16 = new RelativeLayout.LayoutParams((int) (0.295 * cabeza_frente.getWidth()), (int) (0.375 * cabeza_frente.getWidth()));
            bp7.setLayoutParams(p16);

            Button bp8 = (Button) findViewById(R.id.bp8);
            bp8.setX(cabeza_frente.getX() + (int) (0.334 * cabeza_espalda.getWidth()));
            bp8.setY(cabeza_frente.getY() + (int) (0.72 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p17 = new RelativeLayout.LayoutParams((int) (0.295 * cabeza_frente.getWidth()), (int) (0.22 * cabeza_frente.getWidth()));
            bp8.setLayoutParams(p17);

            Button bp9 = (Button) findViewById(R.id.bp9);
            bp9.setX(cabeza_frente.getX() + (int) (0.55 * cabeza_espalda.getWidth()));
            bp9.setY(cabeza_frente.getY() + (int) (0.58 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p18 = new RelativeLayout.LayoutParams((int) (0.28 * cabeza_frente.getWidth()), (int) (0.48 * cabeza_frente.getWidth()));
            bp9.setLayoutParams(p18);

            Button bp10 = (Button) findViewById(R.id.bp10);
            bp10.setX(cabeza_frente.getX() + (int) (0.13 * cabeza_espalda.getWidth()));
            bp10.setY(cabeza_frente.getY() + (int) (0.58 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p19 = new RelativeLayout.LayoutParams((int) (0.28 * cabeza_frente.getWidth()), (int) (0.48 * cabeza_frente.getWidth()));
            bp10.setLayoutParams(p19);

            Button bp11 = (Button) findViewById(R.id.bp11);
            bp11.setX(cabeza_frente.getX() + (int) (0.038 * cabeza_espalda.getWidth()));
            bp11.setY(cabeza_frente.getY() + (int) (0.755 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p20 = new RelativeLayout.LayoutParams((int) (0.46 * cabeza_frente.getWidth()), (int) (0.56 * cabeza_frente.getWidth()));
            bp11.setLayoutParams(p20);

            Button bp12 = (Button) findViewById(R.id.bp12);
            bp12.setX(cabeza_frente.getX() + (int) (0.463 * cabeza_espalda.getWidth()));
            bp12.setY(cabeza_frente.getY() + (int) (0.78 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p21 = new RelativeLayout.LayoutParams((int) (0.46 * cabeza_frente.getWidth()), (int) (0.51 * cabeza_frente.getWidth()));
            bp12.setLayoutParams(p21);

            Button bp13 = (Button) findViewById(R.id.bp13);
            bp13.setX(cabeza_frente.getX() + (int) (0.33 * cabeza_espalda.getWidth()));
            bp13.setY(cabeza_frente.getY() + (int) (0.862 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p22 = new RelativeLayout.LayoutParams((int) (0.3 * cabeza_frente.getWidth()), (int) (0.135 * cabeza_frente.getWidth()));
            bp13.setLayoutParams(p22);

            Button bp14 = (Button) findViewById(R.id.bp14);
            bp14.setX(cabeza_espalda.getX() + (int) (0.075 * cabeza_espalda.getWidth()));
            bp14.setY(cabeza_espalda.getY() + (int) (0.12 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p23 = new RelativeLayout.LayoutParams((int) (0.89 * cabeza_frente.getWidth()), (int) (1.16 * cabeza_frente.getWidth()));
            bp14.setLayoutParams(p23);

            Button bp15 = (Button) findViewById(R.id.bp15);
            bp15.setX(cabeza_espalda.getX() + (int) (0.812 * cabeza_espalda.getWidth()));
            bp15.setY(cabeza_espalda.getY() + (int) (0.47 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p24 = new RelativeLayout.LayoutParams((int) (0.13 * cabeza_frente.getWidth()), (int) (0.3 * cabeza_frente.getWidth()));
            bp15.setLayoutParams(p24);

            Button bp16 = (Button) findViewById(R.id.bp16);
            bp16.setX(cabeza_espalda.getX() + (int) (0.015 * cabeza_espalda.getWidth()));
            bp16.setY(cabeza_espalda.getY() + (int) (0.47 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p25 = new RelativeLayout.LayoutParams((int) (0.13 * cabeza_frente.getWidth()), (int) (0.3 * cabeza_frente.getWidth()));
            bp16.setLayoutParams(p25);

            Button bp17 = (Button) findViewById(R.id.bp17);
            bp17.setX(cabeza_espalda.getX() + (int) (0 * cabeza_espalda.getWidth()));
            bp17.setY(cabeza_espalda.getY() + (int) (0.87 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p26 = new RelativeLayout.LayoutParams((int) (0.5 * cabeza_frente.getWidth()), (int) (0.38 * cabeza_frente.getWidth()));
            bp17.setLayoutParams(p26);

            Button bp18 = (Button) findViewById(R.id.bp18);
            bp18.setX(cabeza_espalda.getX() + (int) (0.462 * cabeza_espalda.getWidth()));
            bp18.setY(cabeza_espalda.getY() + (int) (0.87 * cabeza_espalda.getHeight()));
            RelativeLayout.LayoutParams p27 = new RelativeLayout.LayoutParams((int) (0.57 * cabeza_frente.getWidth()), (int) (0.38 * cabeza_frente.getWidth()));
            bp18.setLayoutParams(p27);

            ubicarLesiones();
            showVolver();
        }
    }

    int tamano_lesion;
    int lesiones_terminadas = 0;

    private void ponerPunto(int id_zona, Button zona, int i) {
        tamano_lesion = 60;
        Button b = new Button(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(tamano_lesion, tamano_lesion);
        b.setLayoutParams(p);

        b.setBackgroundResource(R.drawable.bt_lesion_terminado);
        /*
        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BP"+zonas_afectadas.get(i), false)){
            b.setBackgroundResource(R.drawable.bt_lesion);
        }else{
            lesiones_terminadas++;
            if(lesiones_terminadas == zonas_afectadas.size() && !modo_nueva_lesion) showVolver();
            b.setBackgroundResource(R.drawable.bt_lesion_terminado);
        }
        */

        double offsetx = 0, offsety = 0;
        if (id_zona == 1) {
            offsetx = 0.15;
            offsety = 0.15;
        } else if (id_zona == 2) {
            offsetx = -0.15;
            offsety = 0.13;
        } else if (id_zona == 9) {
            offsetx = 0.1;
            offsety = -0.13;
        } else if (id_zona == 10) {
            offsetx = -0.1;
            offsety = -0.13;
        } else if (id_zona == 11) {
            offsetx = 0.1;
            offsety = 0.2;
        } else if (id_zona == 12) {
            offsetx = -0.1;
            offsety = 0.2;
        } else if (id_zona == 17) {
            offsetx = 0.2;
            offsety = -0;
        } else if (id_zona == 18) {
            offsetx = -0.2;
            offsety = 0;
        }

        b.setX(zona.getX() + (int) (zona.getLayoutParams().width * (0.5 + offsetx)) - (tamano_lesion / 2));
        b.setY(zona.getY() + (int) (zona.getLayoutParams().height * (0.5 + offsety)) - (tamano_lesion / 2));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentarAbrirCamara(v);
            }
        });
        botones_lesion.put(b, id_zona);
        noisel_senotob.put(id_zona, b);
        region_cabeza.addView(b);
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
                    foto = new File(Environment.getExternalStorageDirectory() + "/"+ LeishConstants.FOLDER+"/" + foto_code + ".jpg");
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
            if (b == null) continue;
            ponerPunto(id_zona, b, i);
            b.setVisibility(View.VISIBLE);
        }

        verifyFotos();
        if (modo_nueva_lesion) activar_modo_nueva_lesion();
    }

    public void borrarShared() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
    }

    private void clearLesiones() {
        Iterator it = botones_lesion.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            region_cabeza.removeView(((Button) pair.getKey()));
        }
        region_cabeza.invalidate();
    }


    boolean volver_showed;

    public void showVolver() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("c_ok", true).commit();
        volver_showed = true;
        Button brazo_der_boton_volver = (Button) findViewById(R.id.cabeza_boton_volver);
        brazo_der_boton_volver.setVisibility(View.VISIBLE);
        if (modo_nueva_lesion)
            ((Button) findViewById(R.id.cabeza_boton_volver)).setText("SIGUIENTE");
    }

    public void hideVolver() {
        Button brazo_der_boton_volver = (Button) findViewById(R.id.cabeza_boton_volver);
        brazo_der_boton_volver.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        clearLesiones();
        control = true;
        if (modo_nueva_lesion) hideVolver();
    }


    @Override
    public void onBackPressed() {
        if (!modo_nueva_lesion) doVolver(null);
        else finish();
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
        for (int i = 1; i <= 18; i = i + 1) {
            Button b = noisel_senotob.get(i);
            if (b == null) continue;
            b.setBackgroundResource(R.drawable.bt_lesion_terminado);
            /*
            if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BP"+i, false)){
                b.setBackgroundResource(R.drawable.bt_lesion);
            }else{
                b.setBackgroundResource(R.drawable.bt_lesion_terminado);
            }
            */
        }


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
                    showVolver();
                }
            });
        }

        Iterator it2 = botones_lesion.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            ((Button) pair.getKey()).setOnClickListener(null);
            ((Button) id_zonas.get((Integer) pair.getValue())).setOnClickListener(null);
        }

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
        int boton = -1;
        Iterator it = id_zonas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (((Button) pair.getValue()).getAlpha() == 1) {
                boton = (Integer) pair.getKey();
            }
        }
        return boton;
    }
}

