/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanc on 11/7/2017.
 */

public class EventoAdapter extends ArrayAdapter<Evento> {
    private Context context;

    private static final String DEBUG_TAG = "EVENTO_ADAPTER";
    private ArrayList<byte[]> imagenes_evento;

    public EventoAdapter(Context context,ArrayList<Evento> Eventos){
        super(context,0,Eventos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.row_evento,parent,false);
        }

        TextView tvNombre= (TextView) convertView.findViewById(R.id.text_nombre_evento);
        //TextView tvLugar = (TextView) convertView.findViewById(R.id.text_nombre_lugar);
        ImageView ivImageEvento = (ImageView) convertView.findViewById(R.id.image_evento);


        Evento evento = getItem(position);
        tvNombre.setText(evento.getNombre());
        //tvLugar.setText(evento.getLugar());

        imagenes_evento = evento.getImagenes();
        if(imagenes_evento.size() > 0){
            byte[] imagen = imagenes_evento.get(imagenes_evento.size()-1);
            ivImageEvento.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
        }


        return convertView;
    }
}
