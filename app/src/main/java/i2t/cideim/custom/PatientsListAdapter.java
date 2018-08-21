package i2t.cideim.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import i2t.cideim.R;
import i2t.cideim.model.Evaluation;
import i2t.cideim.model.Patient;

/**
 * Created by Leonardo.
 * List adapter for the PatientsActivity.
 */

public class PatientsListAdapter extends ArrayAdapter<Patient> {

    private final List<Patient> patients;

    public PatientsListAdapter(Context context, List<Patient> objects) {
        super(context, R.layout.patients_list_item_layout, objects);
        patients = objects;
    }

    /* Returns a custom view that shows the patient's name and his last evaluation's value */
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.patients_list_item_layout, parent, false);
        TextView textViewName = (TextView) rootView.findViewById(R.id.textViewText);
        TextView textViewId = (TextView) rootView.findViewById(R.id.textViewId);
        TextView textViewScore = (TextView) rootView.findViewById(R.id.textViewScore);

        // Patient's name
        Patient p = patients.get(position);
        textViewName.setText(p.getName() + " " + p.getLastName());
        textViewId.setText(p.getIdentification());

        // Last evaluation's value
        List<Evaluation> evaluations = p.getEvaluationList();
        textViewScore.setText(evaluations.size() == 0 ? "n/a" : String.valueOf((int) evaluations.get(evaluations.size() - 1).getScore()));

        return rootView;
    }

}
