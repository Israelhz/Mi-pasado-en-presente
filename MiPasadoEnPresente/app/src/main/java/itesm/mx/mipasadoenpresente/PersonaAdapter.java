package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jibril on 11/12/17.
 */

public class PersonaAdapter extends ArrayAdapter<Persona> {

    private static final String DEBUG_TAG = "PERSONA_ADAPTER";

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
//        ivImagePersona.setImageResource(persona.getImagenes());//Images are a byteArray
        Log.e(DEBUG_TAG, "Images array missing");

        return convertView;
    }
}
