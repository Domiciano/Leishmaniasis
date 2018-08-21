package i2t.cideim.hisopo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import i2t.cideim.R;

public class PreguntaHisopoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregunta_hisopo);
    }

    public void terminarToma(View view) {
        finish();
    }
}
