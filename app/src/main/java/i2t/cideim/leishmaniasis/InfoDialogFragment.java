package i2t.cideim.leishmaniasis;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import i2t.cideim.R;

/**
 * Created by Leonardo.
 * Represents the information menu (?).
 */

public class InfoDialogFragment extends DialogFragment {

    private TextView dialogTitle;
    private TextView dialogDescription;

    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;
    private ImageButton imageButton6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View rootView = inflater.inflate(R.layout.dialog_info, container, false);
        dialogTitle = (TextView) rootView.findViewById(R.id.textViewInfoTitle);
        dialogDescription = (TextView) rootView.findViewById(R.id.textViewInfoDescription);

        imageButton1 = (ImageButton) rootView.findViewById(R.id.imageView1);
        imageButton2 = (ImageButton) rootView.findViewById(R.id.imageView2);
        imageButton3 = (ImageButton) rootView.findViewById(R.id.imageView3);
        imageButton4 = (ImageButton) rootView.findViewById(R.id.imageView4);
        imageButton5 = (ImageButton) rootView.findViewById(R.id.imageView5);
        imageButton6 = (ImageButton) rootView.findViewById(R.id.imageView6);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(R.string.info_ulceras_title, R.string.info_ulceras_description);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(R.string.info_lesiones_title, R.string.info_lesiones_description);
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(R.string.info_localizacion_title, R.string.info_localizacion_description);
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(R.string.info_actividades_title, R.string.info_actividades_description);
            }
        });

        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(R.string.info_antecedentes_title, R.string.info_antecedentes_description);
            }
        });

        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(R.string.info_manta_title, R.string.info_manta_description);
            }
        });

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setText(int titleRes, int descriptionRes) {
        dialogTitle.setText(titleRes);
        dialogDescription.setText(descriptionRes);
    }

}
