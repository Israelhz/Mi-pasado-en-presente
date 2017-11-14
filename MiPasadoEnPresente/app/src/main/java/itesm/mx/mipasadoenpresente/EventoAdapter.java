package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by juanc on 11/7/2017.
 */

public class EventoAdapter extends ArrayAdapter<Evento> {
    private Context context;

    public EventoAdapter(Context context,ArrayList<Evento> Eventos){
        super(context,0,Eventos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.row_eventos,parent,false);
        }

        TextView tvNombre= (TextView) convertView.findViewById(R.id.tv_eNombre);
        TextView tvLugar = (TextView) convertView.findViewById(R.id.tv_eLugar);
        //ImageView ivImagen = (ImageView) convertView.findViewById(R.id.iv_eImagen);

        Evento evento = getItem(position);
        tvNombre.setText(evento.getNombre());
        tvLugar.setText(evento.getLugar());

       // byte[] image = evento.getPicture();
        /*if(image!=null){
            Bitmap bmimage = BitmapFactory.decodeByteArray(image,0,image.length);
            ivImagen.setImageBitmap(bmimage);
        }*/

        return convertView;
    }
}
