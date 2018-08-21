package i2t.cideim.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import i2t.cideim.R;


/**
 * Created by Domiciano on 19/05/2016.
 */
public class BooleanAnswerDialog extends DialogFragment implements View.OnClickListener{


    String enunciado;
    OnMyDialogResult mDialogResult;

    public static BooleanAnswerDialog newInstance(String enunciado) {
        BooleanAnswerDialog f = new BooleanAnswerDialog();

        Bundle args = new Bundle();
        args.putString("enunciado", enunciado);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enunciado = getArguments().getString("enunciado");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        d.getWindow().setLayout((int)(screenWidth*0.8), 650);
        return d;
    }

    Button positive, negative;
    TextView texto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.boolean_dialog, container, false);

        positive = (Button) v.findViewById(R.id.boolean_dialog_positive);
        negative = (Button) v.findViewById(R.id.boolean_dialog_negative);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        texto = (TextView) v.findViewById(R.id.boolean_dialog_texto);
        texto.setText(enunciado);
        return v;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        Button b = (Button) v;
        if(b.equals(positive)){
            mDialogResult.finish("SI");
        }else if(b.equals(negative)){
            mDialogResult.finish("NO");
        }

    }

    public interface OnMyDialogResult{
        void finish(String result);
    }
    public void setOnDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

}
