package i2t.cideim.bodylocations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import i2t.cideim.dialogs.BooleanAnswerDialog;
import i2t.cideim.leishmaniasis.EvaluationActivity;
import i2t.cideim.leishmaniasis.MainActivity;
import i2t.cideim.R;
import i2t.cideim.model.Hisopo;
import i2t.cideim.model.UlcerImg;


public class CuerpoHumanoActivity extends AppCompatActivity implements View.OnTouchListener {

    boolean focus = true;
    ImageView cuerpo;
    TextView subtitulo_cuerpo, label_cabeza, label_tronco, label_brazo_izquierdo;
    TextView label_brazo_derecho, label_pierna_izquierda, label_pierna_derecha;
    TextView label_mano_derecha, label_mano_izquierda;

    static final int BRAZO_DERECHO = 0;
    static final int BRAZO_IZQUIERDO = 1;
    static final int CABEZA = 2;
    static final int TRONCO = 3;
    static final int PIERNA_DERECHA = 4;
    static final int PIERNA_IZQUIERDA = 5;
    static final int MANO_DERECHA = 6;
    static final int MANO_IZQUIERDA = 7;

    String[] partes = new String[]{"BRAZO DERECHO", "BRAZO IZQUIERDO", "CABEZA", "TRONCO", "PIERNA DERECHA", "PIERNA IZQUIERDA", "NO_PART"};
    TextView[] labels = new TextView[8];

    int alto;
    int ancho;

    int display_height;
    int display_width;

    float text_label_size = 0;

    View stick1, stick2, stick3, stick4, stick5, stick6, stick7, stick8;

    TextView lesion_cabeza, lesion_tronco, lesion_brazo_der, lesion_brazo_izq, lesion_pierna_der, lesion_pierna_izq, lesion_mano_der, lesion_mano_izq;

    boolean modo_nueva_lesion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuerpo_humano_activity);


        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

        focus = true;

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cuerpo_humano);
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


        cuerpo = (ImageView) findViewById(R.id.cuerpo);

        subtitulo_cuerpo = (TextView) findViewById(R.id.subtitulo_cuerpo);
        label_brazo_derecho = (TextView) findViewById(R.id.label_brazo_derecho);
        label_brazo_izquierdo = (TextView) findViewById(R.id.label_brazo_izquierdo);
        label_cabeza = (TextView) findViewById(R.id.label_cabeza);
        label_tronco = (TextView) findViewById(R.id.label_tronco);
        label_pierna_derecha = (TextView) findViewById(R.id.label_pierna_derecha);
        label_pierna_izquierda = (TextView) findViewById(R.id.label_pierna_izquierda);
        label_mano_derecha = (TextView) findViewById(R.id.label_mano_derecha);
        label_mano_izquierda = (TextView) findViewById(R.id.label_mano_izquierda);

        labels[0] = label_brazo_derecho;
        labels[1] = label_brazo_izquierdo;
        labels[2] = label_cabeza;
        labels[3] = label_tronco;
        labels[4] = label_pierna_derecha;
        labels[5] = label_pierna_izquierda;
        labels[6] = label_mano_derecha;
        labels[7] = label_mano_izquierda;

        cuerpo.setOnTouchListener(this);

        double escala = 0.5;
        int ancho = (int) (display_height * 0.3 * escala);
        int alto = (int) (display_height * escala);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
        cuerpo.setLayoutParams(params);

        stick1 = findViewById(R.id.stick1);
        stick2 = findViewById(R.id.stick2);
        stick3 = findViewById(R.id.stick3);
        stick4 = findViewById(R.id.stick4);
        stick5 = findViewById(R.id.stick5);
        stick6 = findViewById(R.id.stick6);
        stick7 = findViewById(R.id.stick7);
        stick8 = findViewById(R.id.stick8);

        lesion_cabeza = (TextView) findViewById(R.id.lesion_cabeza);
        lesion_tronco = (TextView) findViewById(R.id.lesion_tronco);
        lesion_brazo_der = (TextView) findViewById(R.id.lesion_brazo_der);
        lesion_brazo_izq = (TextView) findViewById(R.id.lesion_brazo_izq);
        lesion_pierna_der = (TextView) findViewById(R.id.lesion_pierna_der);
        lesion_pierna_izq = (TextView) findViewById(R.id.lesion_pierna_izq);
        lesion_mano_der = (TextView) findViewById(R.id.lesion_mano_der);
        lesion_mano_izq = (TextView) findViewById(R.id.lesion_mano_izq);


        lesion_cabeza.setVisibility(View.GONE);
        lesion_tronco.setVisibility(View.GONE);
        lesion_brazo_der.setVisibility(View.GONE);
        lesion_brazo_izq.setVisibility(View.GONE);
        lesion_pierna_der.setVisibility(View.GONE);
        lesion_pierna_izq.setVisibility(View.GONE);
        lesion_mano_der.setVisibility(View.GONE);
        lesion_mano_izq.setVisibility(View.GONE);




    }

    private void ponerIndicadoresDeLesionTerminada() {
        try {
            hideAllLesiones();
            ArrayList<Integer> imagenes = listarBodyLocationsParaPacienteActual();
            ponerIndicadoresDeLesion(imagenes);
        } catch (Exception e) {
            Log.e("ERROR", e.getLocalizedMessage());
        }
    }

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

    private void ponerIndicadoresDeLesion(List<Integer> bodyLocation) {
        try {
            for (Integer bl : bodyLocation) {
                if (bl >= 1 && bl <= 18)
                    showLesion(CABEZA);
                else if (bl >= 19 && bl <= 34)
                    showLesion(BRAZO_DERECHO);
                else if (bl >= 35 && bl <= 50)
                    showLesion(BRAZO_IZQUIERDO);
                else if (bl >= 51 && bl <= 74)
                    showLesion(TRONCO);
                else if (bl >= 75 && bl <= 90)
                    showLesion(PIERNA_DERECHA);
                else if (bl >= 91 && bl <= 106)
                    showLesion(PIERNA_IZQUIERDA);
                else if (bl >= 107 && bl <= 120)
                    showLesion(MANO_DERECHA);
                else if (bl >= 121 && bl <= 134)
                    showLesion(MANO_IZQUIERDA);
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getLocalizedMessage());
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

    @Override
    protected void onResume() {
        super.onResume();
        //hideButtons();
        focus = true;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (focus) {
            focus = false;

            ancho = cuerpo.getWidth();
            alto = cuerpo.getHeight();
            adjustSticks();
            adjustLabels();
            initLesiones();




            ponerIndicadoresDeLesionTerminada();

            if (modo_nueva_lesion) activar_modo_nueva_lesion();
            else desactivar_modo_nueva_lesion();
        }
    }


    //Estas variables controlan el listener onTouch del diagrama
    boolean cabeza_active = false, brazo_der_active = false, brazo_izq_active = false;
    boolean tronco_active = false, pierna_der_active = false, pierna_izq_active = false;
    boolean mano_der_active = false, mano_izq_active = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int parte = getParteDelCuerpo(event.getX() / ancho, event.getY() / alto);
                showPartLabel(parte);
                break;
            case MotionEvent.ACTION_MOVE:
                parte = getParteDelCuerpo(event.getX() / ancho, event.getY() / alto);
                showPartLabel(parte);
                break;
            case MotionEvent.ACTION_UP:
                parte = getParteDelCuerpo(event.getX() / ancho, event.getY() / alto);
                if (parte == CABEZA && cabeza_active) {
                    Intent i = new Intent(this, CabezaActivity.class);
                    startActivity(i);
                } else if (parte == TRONCO && tronco_active) {
                    Intent i = new Intent(this, TroncoActivity.class);
                    startActivity(i);
                } else if (parte == BRAZO_DERECHO && brazo_der_active) {
                    Intent i = new Intent(this, BrazoDerechoActivity.class);
                    startActivity(i);
                } else if (parte == BRAZO_IZQUIERDO && brazo_izq_active) {
                    Intent i = new Intent(this, BrazoIzquierdoActvity.class);
                    startActivity(i);
                } else if (parte == PIERNA_DERECHA && pierna_der_active) {
                    Intent i = new Intent(this, PiernaDerechaActivity.class);
                    startActivity(i);
                } else if (parte == PIERNA_IZQUIERDA && pierna_izq_active) {
                    Intent i = new Intent(this, PiernaIzquierdaActivity.class);
                    startActivity(i);
                } else if (parte == MANO_DERECHA && mano_der_active) {
                    Intent i = new Intent(this, ManoDerechaActivity.class);
                    startActivity(i);
                } else if (parte == MANO_IZQUIERDA && mano_izq_active) {
                    Intent i = new Intent(this, ManoIzquierdaActivity.class);
                    startActivity(i);
                }
                return false;
        }
        return true;
    }

    private void showPartLabel(int parte) {
        if (parte <= 7) {
            labels[parte].setTextSize(20);
            labels[parte].setTypeface(null, Typeface.BOLD_ITALIC);
            for (int i = 0; i < labels.length; i++) {
                if (i != parte) {
                    labels[i].setTextSize(16);
                    labels[parte].setTypeface(null, Typeface.ITALIC);
                }
            }
        }
    }

    private int getParteDelCuerpo(float x, float y) {
        if (x < 0.25 && x > 0 && y < 0.5 && y > 0.185) {
            return BRAZO_DERECHO;
        } else if (x < 0.7 && x > 0.3 && y < 0.18 && y >= 0) {
            return CABEZA;
        } else if (x <= 1 && x > 0.75 && y < 0.5 && y > 0.185) {
            return BRAZO_IZQUIERDO;
        } else if (x <= 0.75 && x >= 0.25 && y <= 0.55 && y >= 0.18) {
            return TRONCO;
        } else if (x <= 0.5 && x >= 0.2 && y <= 1 && y > 0.55) {
            return PIERNA_DERECHA;
        } else if (x <= 0.8 && x > 0.5 && y <= 1 && y > 0.55) {
            return PIERNA_IZQUIERDA;
        } else if (x < 0.25 && x > 0 && y <= 0.6 && y > 0.5) {
            return MANO_DERECHA;
        } else if (x <= 1 && x > 0.75 && y <= 0.6 && y > 0.5) {
            return MANO_IZQUIERDA;
        }
        return 8;
    }

    private void adjustSticks() {

        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(subtitulo_cuerpo.getWidth(), subtitulo_cuerpo.getHeight());
        par.setMargins(0, 0, 0, 40);
        subtitulo_cuerpo.setLayoutParams(par);

        stick1.setX(cuerpo.getX() + (int) (0.6 * cuerpo.getWidth()));
        stick1.setY(cuerpo.getY() + (int) (0.08 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams((int) (0.5 * cuerpo.getWidth()), 2);
        stick1.setLayoutParams(p1);

        stick2.setX(cuerpo.getX() + (int) (0.5 * cuerpo.getWidth()));
        stick2.setY(cuerpo.getY() + (int) (0.23 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams((int) (0.60 * cuerpo.getWidth()), 2);
        stick2.setLayoutParams(p2);

        stick3.setX(cuerpo.getX() + (int) (0.9 * cuerpo.getWidth()));
        stick3.setY(cuerpo.getY() + (int) (0.38 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams((int) (0.34 * cuerpo.getWidth()), 2);
        stick3.setLayoutParams(p3);

        stick4.setX(cuerpo.getX() - (int) (0.1 * cuerpo.getWidth()));
        stick4.setY(cuerpo.getY() + (int) (0.34 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams((int) (0.25 * cuerpo.getWidth()), 2);
        stick4.setLayoutParams(p4);

        stick5.setX(cuerpo.getX() - (int) (0.07 * cuerpo.getWidth()));
        stick5.setY(cuerpo.getY() + (int) (0.83 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams((int) (0.4 * cuerpo.getWidth()), 2);
        stick5.setLayoutParams(p5);

        stick6.setX(cuerpo.getX() + (int) (0.65 * cuerpo.getWidth()));
        stick6.setY(cuerpo.getY() + (int) (0.73 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p6 = new RelativeLayout.LayoutParams((int) (0.36 * cuerpo.getWidth()), 2);
        stick6.setLayoutParams(p6);

        stick7.setX(cuerpo.getX() - (int) (0.25 * cuerpo.getWidth()));
        stick7.setY(cuerpo.getY() + (int) (0.54 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p7 = new RelativeLayout.LayoutParams((int) (0.36 * cuerpo.getWidth()), 2);
        stick7.setLayoutParams(p7);

        stick8.setX(cuerpo.getX() + (int) (0.89 * cuerpo.getWidth()));
        stick8.setY(cuerpo.getY() + (int) (0.54 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p8 = new RelativeLayout.LayoutParams((int) (0.36 * cuerpo.getWidth()), 2);
        stick8.setLayoutParams(p8);
    }

    private void adjustLabels() {
        label_cabeza.setX(cuerpo.getX() + (int) (1.15 * cuerpo.getWidth()));
        label_cabeza.setY(cuerpo.getY() + (int) (0.05 * cuerpo.getHeight()));

        label_tronco.setX(cuerpo.getX() + (int) (1.15 * cuerpo.getWidth()));
        label_tronco.setY(cuerpo.getY() + (int) (0.2 * cuerpo.getHeight()));

        label_brazo_izquierdo.setX(cuerpo.getX() + (int) (1.28 * cuerpo.getWidth()));
        label_brazo_izquierdo.setY(cuerpo.getY() + (int) (0.35 * cuerpo.getHeight()));

        label_brazo_derecho.setX(cuerpo.getX() - (int) (1 * cuerpo.getWidth()));
        label_brazo_derecho.setY(cuerpo.getY() + (int) (0.3 * cuerpo.getHeight()));

        label_pierna_izquierda.setX(cuerpo.getX() + (int) (1.05 * cuerpo.getWidth()));
        label_pierna_izquierda.setY(cuerpo.getY() + (int) (0.7 * cuerpo.getHeight()));

        label_pierna_derecha.setX(cuerpo.getX() - (int) (0.8 * cuerpo.getWidth()));
        label_pierna_derecha.setY(cuerpo.getY() + (int) (0.8 * cuerpo.getHeight()));

        label_mano_izquierda.setX(cuerpo.getX() + (int) (1.2 * cuerpo.getWidth()));
        label_mano_izquierda.setY(cuerpo.getY() + (int) (0.5 * cuerpo.getHeight()));

        label_mano_derecha.setX(cuerpo.getX() - (int) (1 * cuerpo.getWidth()));
        label_mano_derecha.setY(cuerpo.getY() + (int) (0.5 * cuerpo.getHeight()));
    }


    private void removeListernerToAllLabels() {
        label_brazo_derecho.setOnTouchListener(null);
        label_brazo_izquierdo.setOnTouchListener(null);
        label_cabeza.setOnTouchListener(null);
        label_tronco.setOnTouchListener(null);
        label_mano_derecha.setOnTouchListener(null);
        label_mano_izquierda.setOnTouchListener(null);
        label_pierna_derecha.setOnTouchListener(null);
        label_pierna_izquierda.setOnTouchListener(null);
    }

    private void setListernerToLabels(int parte) {
        switch (parte) {
            case CABEZA:
                label_cabeza.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_cabeza.setScaleX(1.2f);
                            label_cabeza.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_cabeza.setScaleX(1);
                            label_cabeza.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), CabezaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case TRONCO:
                label_tronco.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_tronco.setScaleX(1.2f);
                            label_tronco.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_tronco.setScaleX(1);
                            label_tronco.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), TroncoActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case BRAZO_DERECHO:
                label_brazo_derecho.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_brazo_derecho.setScaleX(1.2f);
                            label_brazo_derecho.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_brazo_derecho.setScaleX(1);
                            label_brazo_derecho.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), BrazoDerechoActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case BRAZO_IZQUIERDO:
                label_brazo_izquierdo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_brazo_izquierdo.setScaleX(1.2f);
                            label_brazo_izquierdo.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_brazo_izquierdo.setScaleX(1);
                            label_brazo_izquierdo.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), BrazoIzquierdoActvity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case PIERNA_DERECHA:
                label_pierna_derecha.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_pierna_derecha.setScaleX(1.2f);
                            label_pierna_derecha.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_pierna_derecha.setScaleX(1);
                            label_pierna_derecha.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), PiernaDerechaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case PIERNA_IZQUIERDA:
                label_pierna_izquierda.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_pierna_izquierda.setScaleX(1.2f);
                            label_pierna_izquierda.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_pierna_izquierda.setScaleX(1);
                            label_pierna_izquierda.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), PiernaIzquierdaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case MANO_DERECHA:
                label_mano_derecha.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_mano_derecha.setScaleX(1.2f);
                            label_mano_derecha.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_mano_derecha.setScaleX(1);
                            label_mano_derecha.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), ManoDerechaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case MANO_IZQUIERDA:
                label_mano_izquierda.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_mano_izquierda.setScaleX(1.2f);
                            label_mano_izquierda.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_mano_izquierda.setScaleX(1);
                            label_mano_izquierda.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), ManoIzquierdaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

        }
    }

    private void initLesiones() {
        lesion_cabeza.setX(cuerpo.getX() + (int) (0.42 * cuerpo.getWidth()));
        lesion_cabeza.setY(cuerpo.getY() + (int) (0.05 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_cabeza.setLayoutParams(p1);

        lesion_tronco.setX(cuerpo.getX() + (int) (0.42 * cuerpo.getWidth()));
        lesion_tronco.setY(cuerpo.getY() + (int) (0.21 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_tronco.setLayoutParams(p2);

        lesion_brazo_der.setX(cuerpo.getX() + (int) (0.05 * cuerpo.getWidth()));
        lesion_brazo_der.setY(cuerpo.getY() + (int) (0.36 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_brazo_der.setLayoutParams(p3);

        lesion_brazo_izq.setX(cuerpo.getX() + (int) (0.79 * cuerpo.getWidth()));
        lesion_brazo_izq.setY(cuerpo.getY() + (int) (0.36 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_brazo_izq.setLayoutParams(p4);

        lesion_pierna_der.setX(cuerpo.getX() + (int) (0.27 * cuerpo.getWidth()));
        lesion_pierna_der.setY(cuerpo.getY() + (int) (0.71 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_pierna_der.setLayoutParams(p5);

        lesion_pierna_izq.setX(cuerpo.getX() + (int) (0.58 * cuerpo.getWidth()));
        lesion_pierna_izq.setY(cuerpo.getY() + (int) (0.71 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p6 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_pierna_izq.setLayoutParams(p6);

        lesion_mano_der.setX(cuerpo.getX() + (int) (0.02 * cuerpo.getWidth()));
        lesion_mano_der.setY(cuerpo.getY() + (int) (0.53 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p7 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_mano_der.setLayoutParams(p7);

        lesion_mano_izq.setX(cuerpo.getX() + (int) (0.82 * cuerpo.getWidth()));
        lesion_mano_izq.setY(cuerpo.getY() + (int) (0.53 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p8 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_mano_izq.setLayoutParams(p8);


    }

    boolean lesion_presente_bd = false, lesion_presente_bi = false, lesion_presente_c = false;
    boolean lesion_presente_t = false, lesion_presente_md = false, lesion_presente_mi = false;
    boolean lesion_presente_pd = false, lesion_presente_pi = false;

    private void showLesion(int parte) {
        switch (parte) {
            case BRAZO_DERECHO:
                lesion_brazo_der.setVisibility(View.VISIBLE);
                lesion_brazo_der.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(BRAZO_DERECHO);
                brazo_der_active = true;
                lesion_presente_bd = true;
                break;
            case BRAZO_IZQUIERDO:
                lesion_brazo_izq.setVisibility(View.VISIBLE);
                lesion_brazo_izq.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(BRAZO_IZQUIERDO);
                brazo_izq_active = true;
                lesion_presente_bi = true;
                break;
            case CABEZA:
                lesion_cabeza.setVisibility(View.VISIBLE);
                lesion_cabeza.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(CABEZA);
                cabeza_active = true;
                lesion_presente_c = true;
                break;
            case TRONCO:
                lesion_tronco.setVisibility(View.VISIBLE);
                lesion_tronco.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(TRONCO);
                tronco_active = true;
                lesion_presente_t = true;
                break;
            case PIERNA_DERECHA:
                lesion_pierna_der.setVisibility(View.VISIBLE);
                lesion_pierna_der.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(PIERNA_DERECHA);
                pierna_der_active = true;
                lesion_presente_pd = true;
                break;
            case PIERNA_IZQUIERDA:
                lesion_pierna_izq.setVisibility(View.VISIBLE);
                lesion_pierna_izq.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(PIERNA_IZQUIERDA);
                pierna_izq_active = true;
                lesion_presente_pi = true;
                break;
            case MANO_DERECHA:
                lesion_mano_der.setVisibility(View.VISIBLE);
                lesion_mano_der.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(MANO_DERECHA);
                mano_der_active = true;
                lesion_presente_md = true;
                break;
            case MANO_IZQUIERDA:
                lesion_mano_izq.setVisibility(View.VISIBLE);
                lesion_mano_izq.setBackgroundResource(R.drawable.bt_lesion_terminado);
                setListernerToLabels(MANO_IZQUIERDA);
                mano_izq_active = true;
                lesion_presente_mi = true;
                break;
        }
    }

    private void hideAllLesiones() {
        lesion_brazo_der.setVisibility(View.INVISIBLE);
        brazo_der_active = false;
        lesion_presente_bd = false;

        lesion_brazo_izq.setVisibility(View.INVISIBLE);
        brazo_izq_active = false;
        lesion_presente_bi = false;

        lesion_cabeza.setVisibility(View.INVISIBLE);
        cabeza_active = false;
        lesion_presente_c = false;

        lesion_tronco.setVisibility(View.INVISIBLE);
        tronco_active = false;
        lesion_presente_t = false;

        lesion_pierna_der.setVisibility(View.INVISIBLE);
        pierna_der_active = false;
        lesion_presente_pd = false;

        lesion_pierna_izq.setVisibility(View.INVISIBLE);
        pierna_izq_active = false;
        lesion_presente_pi = false;

        lesion_mano_der.setVisibility(View.INVISIBLE);
        mano_der_active = false;
        lesion_presente_md = false;

        lesion_mano_izq.setVisibility(View.INVISIBLE);
        mano_izq_active = false;
        lesion_presente_mi = false;

        removeListernerToAllLabels();
    }



    private void showButtons() {
        LinearLayout botones_cuerpo = (LinearLayout) findViewById(R.id.botones_cuerpo);
        botones_cuerpo.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        LinearLayout botones_cuerpo = (LinearLayout) findViewById(R.id.botones_cuerpo);
        botones_cuerpo.setVisibility(View.GONE);
    }


    public void addNewLesion(View v) {
        activar_modo_nueva_lesion();
    }

    private void backNewLesion() {

        desactivar_modo_nueva_lesion();
    }

    @Override
    public void onBackPressed() {
        if (!modo_nueva_lesion) {
            finish();
            borrarPreferences();
        } else backNewLesion();
    }

    public void activar_modo_nueva_lesion() {
        hideButtons();
        if (lesion_presente_c)
            lesion_cabeza.setVisibility(View.GONE);
        if (lesion_presente_t)
            lesion_tronco.setVisibility(View.GONE);
        if (lesion_presente_md)
            lesion_mano_der.setVisibility(View.GONE);
        if (lesion_presente_mi)
            lesion_mano_izq.setVisibility(View.GONE);
        if (lesion_presente_bd)
            lesion_brazo_der.setVisibility(View.GONE);
        if (lesion_presente_bi)
            lesion_brazo_izq.setVisibility(View.GONE);
        if (lesion_presente_pd)
            lesion_pierna_der.setVisibility(View.GONE);
        if (lesion_presente_pi)
            lesion_pierna_izq.setVisibility(View.GONE);

        cabeza_active = true;
        setListernerToLabels(CABEZA);

        tronco_active = true;
        setListernerToLabels(TRONCO);

        mano_der_active = true;
        setListernerToLabels(MANO_DERECHA);

        mano_izq_active = true;
        setListernerToLabels(MANO_IZQUIERDA);

        brazo_der_active = true;
        setListernerToLabels(BRAZO_DERECHO);

        brazo_izq_active = true;
        setListernerToLabels(BRAZO_IZQUIERDO);

        pierna_der_active = true;
        setListernerToLabels(PIERNA_DERECHA);

        pierna_izq_active = true;
        setListernerToLabels(PIERNA_IZQUIERDA);

        subtitulo_cuerpo.setText("Seleccione el lugar donde se encuentra la nueva lesión");
        modo_nueva_lesion = true;

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("modo_nueva_lesion", true).commit();
    }

    public void desactivar_modo_nueva_lesion() {
        modo_nueva_lesion = false;
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("modo_nueva_lesion").commit();
        if (!modo_nueva_lesion) showButtons();

        if (lesion_presente_c) lesion_cabeza.setVisibility(View.VISIBLE);
        if (lesion_presente_t) lesion_tronco.setVisibility(View.VISIBLE);
        if (lesion_presente_md) lesion_mano_der.setVisibility(View.VISIBLE);
        if (lesion_presente_mi) lesion_mano_izq.setVisibility(View.VISIBLE);
        if (lesion_presente_bd) lesion_brazo_der.setVisibility(View.VISIBLE);
        if (lesion_presente_bi) lesion_brazo_izq.setVisibility(View.VISIBLE);
        if (lesion_presente_pd) lesion_pierna_der.setVisibility(View.VISIBLE);
        if (lesion_presente_pi) lesion_pierna_izq.setVisibility(View.VISIBLE);

        //Inhabilitar zonas en el diagrama
        if (!lesion_presente_c) {
            cabeza_active = false;
            label_cabeza.setOnTouchListener(null);
        }
        if (!lesion_presente_t) {
            tronco_active = false;
            label_tronco.setOnTouchListener(null);
        }
        if (!lesion_presente_md) {
            mano_der_active = false;
            label_mano_derecha.setOnTouchListener(null);
        }
        if (!lesion_presente_mi) {
            mano_izq_active = false;
            label_mano_izquierda.setOnTouchListener(null);
        }
        if (!lesion_presente_bd) {
            brazo_der_active = false;
            label_brazo_derecho.setOnTouchListener(null);
        }
        if (!lesion_presente_bi) {
            brazo_izq_active = false;
            label_brazo_izquierdo.setOnTouchListener(null);
        }
        if (!lesion_presente_pd) {
            pierna_der_active = false;
            label_pierna_derecha.setOnTouchListener(null);
        }
        if (!lesion_presente_pi) {
            pierna_izq_active = false;
            label_pierna_izquierda.setOnTouchListener(null);
        }

        subtitulo_cuerpo.setText("Seleccione la zona del cuerpo donde se encuentran ubicadas las lesiones para fotografiarla.");

    }

    public void terminarToma(View v) {
        /*
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("toma_fotos_terminada", true)
                .putBoolean("F"+id,true)
                .apply();
        */
        finish();
        borrarPreferences();
    }

    private void borrarPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();


        edit.remove("modo_nueva_lesion");
        edit.remove("last_foto");
        edit.remove("id_zona");
        edit.remove("parte_actual");
        edit.remove("foto_path");
        edit.remove("prueba_c");
        edit.remove("prueba_t");
        edit.remove("prueba_bd");
        edit.remove("prueba_bi");
        edit.remove("prueba_mi");
        edit.remove("prueba_md");
        edit.remove("prueba_pd");
        edit.remove("prueba_pi");
        edit.remove("c_ok");
        edit.remove("t_ok");
        edit.remove("bd_ok");
        edit.remove("bi_ok");
        edit.remove("pd_ok");
        edit.remove("pi_ok");
        edit.remove("md_ok");
        edit.remove("mi_ok");
        edit.remove("body_name");
        edit.remove("foto_path");
        edit.remove("modo_eliminar_foto");
        for (int i = 1; i <= 134; i++) {
            edit.remove("BP" + i);
        }
        edit.commit();

    }

}
