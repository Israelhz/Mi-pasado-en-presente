/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
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
 * Created by Jibril on 11/12/17.
 */

public class PersonaAdapter extends ArrayAdapter<Persona> {

    private static final String DEBUG_TAG = "PERSONA_ADAPTER";
    private ArrayList<byte[]> imagenes_persona;
    public PersonaAdapter (Context context, ArrayList<Persona> personas) {
        super(context, 0, personas);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_personas, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.text_nombre_persona);
        ImageView ivImagePersona = (ImageView) convertView.findViewById(R.id.image_persona);

        Persona persona = getItem(position);
        tvName.setText(persona.getNombre());
        imagenes_persona = persona.getImagenes();
        if(imagenes_persona.size() > 0){
            byte[] imagen = imagenes_persona.get(imagenes_persona.size()-1);
            ivImagePersona.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
        }

        return convertView;
    }
}
