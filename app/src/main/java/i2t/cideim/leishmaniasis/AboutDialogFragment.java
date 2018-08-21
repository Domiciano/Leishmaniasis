package i2t.cideim.leishmaniasis;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import i2t.cideim.R;

/**
 * Created by Leonardo.
 * Represents the about menu (i).
 */

public class AboutDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        final View rootView = inflater.inflate(R.layout.dialog_about, container, false);

        final TextView title = (TextView) rootView.findViewById(R.id.textViewDialogTitle);
        final TextView description = (TextView) rootView.findViewById(R.id.textViewDialogDescription);

        final ListView listView = (ListView) rootView.findViewById(R.id.listViewAbout);

        List<AboutListItem> faq = new ArrayList<AboutListItem>();

        faq.add(new AboutListItem(R.string.about_faq1_title, R.string.about_faq1_description));
        faq.add(new AboutListItem(R.string.about_faq2_title, R.string.about_faq2_description));
        faq.add(new AboutListItem(R.string.about_faq3_title, R.string.about_faq3_description));
        faq.add(new AboutListItem(R.string.about_faq4_title, R.string.about_faq4_description));
        faq.add(new AboutListItem(R.string.about_faq5_title, R.string.about_faq5_description));
        faq.add(new AboutListItem(R.string.about_faq6_title, R.string.about_faq6_description));

        AboutListAdapter adapter = new AboutListAdapter(getActivity(), faq);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AboutListItem item = (AboutListItem) adapterView.getItemAtPosition(i);
                title.setText(item.getTitle());
                description.setText(item.getDescription());
                view.setSelected(true);
                ((ScrollView) rootView.findViewById(R.id.scrollView)).fullScroll(ScrollView.FOCUS_UP);
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

    /* Represents an option in the menu */
    public class AboutListItem {
        private int title;
        private int description;

        public AboutListItem(int title, int description) {
            this.title = title;
            this.description = description;
        }

        public int getTitle() {
            return title;
        }

        public int getDescription() {
            return description;
        }
    }

    /* Represents the list of options in the menu */
    public class AboutListAdapter extends ArrayAdapter<AboutListItem> {
        List<AboutListItem> items;

        public AboutListAdapter(Context context, List<AboutListItem> objects) {
            super(context, R.layout.dialog_about_list_item_layout, objects);
            items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.dialog_about_list_item_layout, parent, false);

            TextView textView = (TextView) rootView.findViewById(R.id.textViewText);
            AboutListItem item = items.get(position);
            textView.setText(item.getTitle());

            return rootView;
        }
    }

}
