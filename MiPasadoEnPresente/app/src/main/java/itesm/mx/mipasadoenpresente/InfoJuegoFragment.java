/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Fragmento para mostrar la información de la persona después de contestar una pregunta
 */
public class InfoJuegoFragment extends Fragment implements View.OnClickListener {

    final static String PREGUNTAS_INDEX = "Preguntas_index";
    final static String PREGUNTA_ACTUAL_INDEX = "Pregunta_actual_index";
    final static String DIFICULTAD_INDEX = "Dificultad_index";
    final static String CORRECTAS_TAG = "Correctas_tag";
    final static String PERSONA_ID = "Persona_id";
    final static  String PERSONAS_TAG = "Personas_tag";
    final static  String EVENTOS_TAG = "Eventos_tag";

    ImageView iv_respuesta;
    TextView tv_nombre, tv_relacion, tv_fecha, tv_comentarios;
    Button btn_continuar;

    int preguntas, pregunta_actual, dificultad, correctas;

    long respuesta;

    Boolean personas, eventos;

    Persona persona;
    PersonaOperations personaOperations;

    public InfoJuegoFragment() {
        // Required empty public constructor
    }

    /**
     * Devuelve una nueva instancia mandando los elementos de los parametros
     * @param preguntas
     * @param pregunta_actual
     * @param dificultad
     * @param respuesta
     * @param correctas
     * @param personas
     * @param eventos
     * @return
     */
    public static InfoJuegoFragment newInstance(int preguntas, int pregunta_actual, int dificultad, long respuesta, int correctas, Boolean personas, Boolean eventos){
        InfoJuegoFragment fragment = new InfoJuegoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PREGUNTAS_INDEX, preguntas);
        bundle.putInt(PREGUNTA_ACTUAL_INDEX, pregunta_actual);
        bundle.putInt(DIFICULTAD_INDEX, dificultad);
        bundle.putInt(CORRECTAS_TAG, correctas);
        bundle.putLong(PERSONA_ID, respuesta);
        bundle.putBoolean(PERSONAS_TAG, personas);
        bundle.putBoolean(EVENTOS_TAG, eventos);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Inicializa elementos de la pantalla
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_juego, container, false);

        iv_respuesta = (ImageView) view.findViewById(R.id.iv_respuesta_persona);
        tv_nombre = (TextView) view.findViewById(R.id.tv_respuesta_nombre);
        tv_relacion = (TextView) view.findViewById(R.id.tv_respuesta_relacion);
        tv_fecha = (TextView) view.findViewById(R.id.tv_respuesta_fecha);
        tv_comentarios = (TextView) view.findViewById(R.id.tv_respuesta_comentarios);

        btn_continuar = (Button) view.findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(this);
        return view;
    }

    /**
     * Inicializa los parametros
     */
    @Override
    public void onStart(){
        super.onStart();

        if(getArguments() != null){
            preguntas = getArguments().getInt(PREGUNTAS_INDEX);
            pregunta_actual = getArguments().getInt(PREGUNTA_ACTUAL_INDEX);
            dificultad = getArguments().getInt(DIFICULTAD_INDEX);
            correctas = getArguments().getInt(CORRECTAS_TAG);
            respuesta = getArguments().getLong(PERSONA_ID);
            personas = getArguments().getBoolean(PERSONAS_TAG);
            eventos = getArguments().getBoolean(EVENTOS_TAG);
            personaOperations = new PersonaOperations(getContext());
            personaOperations.open();
            persona = personaOperations.getPersona(respuesta);
            setInfo();
        }


    }

    /**
     * Actualiza los valores de los textos
     */
    public void setInfo(){
        tv_nombre.setText("Nombre: " + persona.getNombre());
        tv_fecha.setText("Cumpleaños: " + persona.getFecha_cumpleanos());
        tv_relacion.setText("Relación: " + persona.getCategoria());
        tv_comentarios.setText("Comentarios: " + persona.getComentarios());
        if(persona.getImagenes().size() >= 1){
            int length = persona.getImagenes().size();
            byte[] img = persona.getImagenes().get(length-1);
            Bitmap bmimage = BitmapFactory.decodeByteArray(img, 0, img.length);
            iv_respuesta.setImageBitmap(bmimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continuar:
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, juego1_fragment.newInstance(preguntas, pregunta_actual, dificultad, correctas, personas, eventos), "Info persona tag");
                ft.commit();
                break;
        }
    }
}
