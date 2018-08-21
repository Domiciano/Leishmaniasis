package i2t.cideim.thumbs;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import i2t.cideim.R;


/**
 * Created by Domiciano on 18/05/2016.
 */
public class ThumbsAdapter extends BaseAdapter {

    ArrayList<Bitmap> fotos;
    ArrayList<String> rutas;
    Context context;
    LayoutInflater inflater=null;

    public ThumbsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        fotos = new ArrayList<>();
        rutas = new ArrayList<>();
    }

    public void addBitmap(Bitmap b, String r){
        fotos.add(b);
        rutas.add(r);
    }

    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public Object getItem(int position) {
        return fotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null) vi = inflater.inflate(R.layout.thumbs_item, null);

        //ARMAR TODOS LOS
        ImageView thumb = (ImageView) vi.findViewById(R.id.foto_item_thumb);
        thumb.setImageBitmap(fotos.get(position));

        return vi;
    }


    public String getRuta(int position) {
        return rutas.get(position);
    }

    public void clear() {
        fotos.clear();
        rutas.clear();
        notifyDataSetChanged();
    }
}
