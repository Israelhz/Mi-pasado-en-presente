/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static itesm.mx.mipasadoenpresente.R.raw.correct;

/**
 * A simple {@link Fragment} subclass.
 */
public class juego1_fragment extends Fragment implements View.OnClickListener{

    final static String PREGUNTAS_INDEX = "Preguntas_index";
    final static String PREGUNTA_ACTUAL_INDEX = "Pregunta_actual_index";
    final static String DIFICULTAD_INDEX = "Dificultad_index";
    final static String CORRECTAS_TAG = "Correctas_tag";
    final static  String PERSONAS_TAG = "Personas_tag";
    final static  String EVENTOS_TAG = "Eventos_tag";

    TextView tv_preguntas, tv_pregunta;
    Button btn_opcion1, btn_opcion2, btn_opcion3, btn_opcion4;
    ImageView iv_persona;

    int preguntas, pregunta_actual, dificultad, respuesta, correctas;
    long persona_evento_id_respuesta;
    PersonaOperations personaOperations;
    ArrayList<Persona> lista_personas;
    EventoOperations eventoOperations;
    ArrayList<Evento> lista_eventos;
    Boolean primer_intento = true;
    Toast toast;
    View view;

    int sonido = 1;
    SharedPreferences prefs;

    Boolean personas, eventos;
    Boolean desconocido = false;

    int selected = 0;

    public juego1_fragment() {
        // Required empty public constructor
    }

    public static juego1_fragment newInstance(int preguntas, int pregunta_actual, int dificultad, int correctas, Boolean personas,Boolean eventos){
        juego1_fragment fragment = new juego1_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PREGUNTAS_INDEX, preguntas);
        bundle.putInt(PREGUNTA_ACTUAL_INDEX, pregunta_actual);
        bundle.putInt(DIFICULTAD_INDEX, dificultad);
        bundle.putInt(CORRECTAS_TAG, correctas);
        bundle.putBoolean(PERSONAS_TAG, personas);
        bundle.putBoolean(EVENTOS_TAG, eventos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(getArguments() != null){
            preguntas = getArguments().getInt(PREGUNTAS_INDEX);
            pregunta_actual = getArguments().getInt(PREGUNTA_ACTUAL_INDEX);
            dificultad = getArguments().getInt(DIFICULTAD_INDEX);
            correctas = getArguments().getInt(CORRECTAS_TAG);
            personas = getArguments().getBoolean(PERSONAS_TAG);
            eventos = getArguments().getBoolean(EVENTOS_TAG);

            personaOperations = new PersonaOperations(getContext());
            personaOperations.open();

            eventoOperations = new EventoOperations(getContext());
            eventoOperations.open();

            lista_eventos = eventoOperations.getAllEventos();
            lista_personas = personaOperations.getAllPersonas();

            if (pregunta_actual > preguntas){
                personaOperations.close();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, resultadoFragment.newInstance(correctas, preguntas, dificultad), "Resultado tag");
                ft.commit();
            }
        }

        // Esconde botones de acuerdo a la dificultad
        switch (dificultad){
            case 1:
                btn_opcion3.setVisibility(View.GONE);
                btn_opcion4.setVisibility(View.GONE);
                break;
            case 2:
                btn_opcion4.setVisibility(View.GONE);
                break;
        }

        prefs = getActivity().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        sonido = prefs.getInt("audio", 1);


        randomQuestion();

        tv_preguntas.setText("Pregunta " + pregunta_actual + " de " + preguntas);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_juego1_fragment, container, false);

        tv_preguntas = (TextView) view.findViewById(R.id.tv_preguntas);
        tv_pregunta = (TextView) view.findViewById(R.id.tv_pregunta);
        btn_opcion1 = (Button) view.findViewById(R.id.btn_opcion1);
        btn_opcion2 = (Button) view.findViewById(R.id.btn_opcion2);
        btn_opcion3 = (Button) view.findViewById(R.id.btn_opcion3);
        btn_opcion4 = (Button) view.findViewById(R.id.btn_opcion4);
        iv_persona = (ImageView) view.findViewById(R.id.iv_persona);

        btn_opcion1.setOnClickListener(this);
        btn_opcion2.setOnClickListener(this);
        btn_opcion3.setOnClickListener(this);
        btn_opcion4.setOnClickListener(this);
        return view;
    }

    // Función que genera una pregunta de manera aleatoria, eligiendo 4 personas y mostrando la foto de una
    public void randomQuestion(){
        Log.i("random question", " personas= " + personas + " eventos= " + eventos);
        if(personas && !eventos)
            randomQuestionPersona();

        if(eventos && !personas)
            randomQuestionEvento();

        if(eventos && personas){
            int random = randInt(1,3);
            if(random == 1){
                randomQuestionPersona();
            }else{
                randomQuestionEvento();
            }
        }
    }

    public void randomQuestionPersona(){
        selected = 1;
        tv_pregunta.setText("¿Cuál es el nombre de esta persona?");
        ArrayList<Integer> id_personas = new ArrayList<Integer>(); // Arraylist de ids de las personas de las respuestas
        int length = lista_personas.size(); // Longitud de la lista de personas

        // Escoge enteros diferentes dependiendo de la dificultad
        for(int i=0; i<=dificultad; i++){
            int random = randInt(0,length);
            if(!id_personas.contains(random)){
                id_personas.add(random);
            }else {
                i--;
            }
        }

        int id_respuesta = randInt(0, id_personas.size());
        respuesta = id_respuesta;

        Persona persona_respuesta = lista_personas.get(id_personas.get(id_respuesta));
        persona_evento_id_respuesta = persona_respuesta.getId();

        ArrayList<byte[]> persona_imagenes = persona_respuesta.getImagenes();

        int random_imagen = randInt(0, persona_imagenes.size());
        byte[] img = persona_imagenes.get(random_imagen);
        Bitmap bmimage = BitmapFactory.decodeByteArray(img, 0, img.length);
        iv_persona.setImageBitmap(bmimage);

        // Dificultad fácil
        btn_opcion1.setText(lista_personas.get(id_personas.get(0)).getNombre());
        btn_opcion2.setText(lista_personas.get(id_personas.get(1)).getNombre());

        if(dificultad >= 2){ // Dificultad intermedia
            btn_opcion3.setText(lista_personas.get(id_personas.get(2)).getNombre());
        }

        if(dificultad == 3){ // Dificultad difícil
            btn_opcion4.setText(lista_personas.get(id_personas.get(3)).getNombre());
            respuesta_desconocida(true);
        }

    }

    public void respuesta_desconocida(boolean persona){
        int rand = randInt(0,2);
        if(rand == 1){
            desconocido = true;
            if(persona){
                iv_persona.setImageResource(get_id_persona_desconocida());
            }else{
                iv_persona.setImageResource(get_id_evento_desconocido());
            }
            int opcion = randInt(1,5);
            switch (opcion){
                case 1:
                    btn_opcion1.setText("Ninguno");
                    respuesta = 0;
                    break;
                case 2:
                    btn_opcion2.setText("Ninguno");
                    respuesta = 1;
                    break;
                case 3:
                    btn_opcion3.setText("Ninguno");
                    respuesta = 2;
                    break;
                case 4:
                    btn_opcion4.setText("Ninguno");
                    respuesta = 3;
                    break;
            }
        }
    }

    public int get_id_persona_desconocida(){
        int rand = randInt(0,5);
        switch(rand){
            case 0:
                return R.drawable.desconocido1;
            case 1:
                return R.drawable.desconocido2;
            case 2:
                return R.drawable.desconocido3;
            case 3:
                return R.drawable.desconocido4;
            case 4:
                return R.drawable.desconocido5;
        }
        return R.drawable.desconocido5;
    }

    public int get_id_evento_desconocido(){
        int rand = randInt(0,5);
        switch(rand){
            case 0:
                return R.drawable.evento1;
            case 1:
                return R.drawable.evento2;
            case 2:
                return R.drawable.evento3;
            case 3:
                return R.drawable.evento4;
            case 4:
                return R.drawable.evento5;
        }
        return R.drawable.evento5;
    }

    public void randomQuestionEvento(){
        selected = 2;
        tv_pregunta.setText("¿Cuál es el nombre de este evento?");
        ArrayList<Integer> id_eventos = new ArrayList<Integer>(); // Arraylist de ids de las personas de las respuestas
        int length = lista_eventos.size(); // Longitud de la lista de personas

        // Escoge enteros diferentes dependiendo de la dificultad
        for(int i=0; i<=dificultad; i++){
            int random = randInt(0,length);
            if(!id_eventos.contains(random)){
                id_eventos.add(random);
            }else {
                i--;
            }
        }

        int id_respuesta = randInt(0, id_eventos.size());
        respuesta = id_respuesta;

        Evento evento_respuesta = lista_eventos.get(id_eventos.get(id_respuesta));
        persona_evento_id_respuesta = evento_respuesta.getId();

        ArrayList<byte[]> evento_imagenes = evento_respuesta.getImagenes();

        int random_imagen = randInt(0, evento_imagenes.size());
        byte[] img = evento_imagenes.get(random_imagen);
        Bitmap bmimage = BitmapFactory.decodeByteArray(img, 0, img.length);
        iv_persona.setImageBitmap(bmimage);

        // Dificultad fácil
        btn_opcion1.setText(lista_eventos.get(id_eventos.get(0)).getNombre());
        btn_opcion2.setText(lista_eventos.get(id_eventos.get(1)).getNombre());

        if(dificultad >= 2){ // Dificultad intermedia
            btn_opcion3.setText(lista_eventos.get(id_eventos.get(2)).getNombre());
        }

        if(dificultad == 3){ // Dificultad difícil
            btn_opcion4.setText(lista_eventos.get(id_eventos.get(3)).getNombre());
            respuesta_desconocida(false);
        }

    }

    public static int randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min)) + min;

        return randomNum;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_opcion1:
                check_answer(0);
                break;
            case R.id.btn_opcion2:
                check_answer(1);
                break;
            case R.id.btn_opcion3:
                check_answer(2);
                break;
            case R.id.btn_opcion4:
                check_answer(3);
                break;
        }
    }

    public void check_answer(int respuesta_usuario){
        if(respuesta == respuesta_usuario){
            if(primer_intento){
                correctas++;
            }
            showRespuestaCorrecta();
            personaOperations.close();
            if(desconocido){
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, juego1_fragment.newInstance(preguntas, pregunta_actual+1, dificultad, correctas, personas, eventos), "Info persona tag");
                ft.commit();
            }else{
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                if(selected == 1){
                    ft.replace(R.id.fragment_container, InfoJuegoFragment.newInstance(preguntas, pregunta_actual+1, dificultad, persona_evento_id_respuesta, correctas, personas, eventos), "Info persona tag");
                }else {
                    ft.replace(R.id.fragment_container, InfoJuegoEventoFragment.newInstance(preguntas, pregunta_actual+1, dificultad, persona_evento_id_respuesta, correctas, personas, eventos), "Info persona tag");
                }
                ft.commit();
            }

        }else {
            primer_intento = false;
            showRespuestaIncorrecta();
        }
    }

    public void showRespuestaCorrecta(){
        int id = R.raw.correct;
        switch(sonido){
            case 1:
                id = R.raw.correct;
                break;
            case 2:
                id = R.raw.tada;
                break;
            case 3:
                id = R.raw.win;
                break;
        }
        MediaPlayer player;
        player = MediaPlayer.create(getActivity(), id);
        player.start();toast = Toast.makeText(getContext(),"Respuesta correcta", Toast.LENGTH_SHORT);
        toast.show();
        hideToast();
    }

    public void showRespuestaIncorrecta(){
        toast = Toast.makeText(getContext(),"Vuelve a intentarlo", Toast.LENGTH_SHORT);
        toast.show();
        hideToast();
    }
    public void hideToast(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }


}
