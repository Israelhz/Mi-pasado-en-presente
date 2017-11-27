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
 * A simple {@link Fragment} subclass.
 */
public class InfoJuegoEventoFragment extends Fragment implements View.OnClickListener{


    final static String PREGUNTAS_INDEX = "Preguntas_index";
    final static String PREGUNTA_ACTUAL_INDEX = "Pregunta_actual_index";
    final static String DIFICULTAD_INDEX = "Dificultad_index";
    final static String CORRECTAS_TAG = "Correctas_tag";
    final static String EVENTO_ID = "Evento_id";
    final static  String PERSONAS_TAG = "Personas_tag";
    final static  String EVENTOS_TAG = "Eventos_tag";

    ImageView iv_respuesta;
    TextView tv_nombre, tv_categoria, tv_fecha, tv_lugar, tv_descripcion, tv_comentarios, tv_personas;
    Button btn_continuar;

    int preguntas, pregunta_actual, dificultad, correctas;

    long respuesta;

    Boolean personas, eventos;

    Evento evento;
    EventoOperations eventoOperations;

    public InfoJuegoEventoFragment() {
        // Required empty public constructor
    }

    public static InfoJuegoEventoFragment newInstance(int preguntas, int pregunta_actual, int dificultad, long respuesta, int correctas, Boolean personas, Boolean eventos){
        InfoJuegoEventoFragment fragment = new InfoJuegoEventoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PREGUNTAS_INDEX, preguntas);
        bundle.putInt(PREGUNTA_ACTUAL_INDEX, pregunta_actual);
        bundle.putInt(DIFICULTAD_INDEX, dificultad);
        bundle.putInt(CORRECTAS_TAG, correctas);
        bundle.putLong(EVENTO_ID, respuesta);
        bundle.putBoolean(PERSONAS_TAG, personas);
        bundle.putBoolean(EVENTOS_TAG, eventos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_juego_evento, container, false);

        iv_respuesta = (ImageView) view.findViewById(R.id.iv_respuesta);
        tv_nombre = (TextView) view.findViewById(R.id.tv_respuesta_nombre);
        tv_categoria = (TextView) view.findViewById(R.id.tv_respuesta_categoria);
        tv_fecha = (TextView) view.findViewById(R.id.tv_respuesta_fecha);
        tv_lugar = (TextView) view.findViewById(R.id.tv_respuesta_lugar);
        tv_descripcion = (TextView) view.findViewById(R.id.tv_respuesta_descripcion);
        tv_comentarios = (TextView) view.findViewById(R.id.tv_respuesta_comentarios);
        tv_personas = (TextView) view.findViewById(R.id.tv_respuesta_personas);
        btn_continuar = (Button) view.findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(getArguments() != null){
            preguntas = getArguments().getInt(PREGUNTAS_INDEX);
            pregunta_actual = getArguments().getInt(PREGUNTA_ACTUAL_INDEX);
            dificultad = getArguments().getInt(DIFICULTAD_INDEX);
            correctas = getArguments().getInt(CORRECTAS_TAG);
            respuesta = getArguments().getLong(EVENTO_ID);
            personas = getArguments().getBoolean(PERSONAS_TAG);
            eventos = getArguments().getBoolean(EVENTOS_TAG);
            eventoOperations = new EventoOperations(getContext());
            eventoOperations.open();
            evento = eventoOperations.getEvento(respuesta);
            setInfo();
        }


    }

    public void setInfo(){
        tv_nombre.setText("Nombre: " + evento.getNombre());
        tv_categoria.setText("Categoría: " + evento.getCategoria());
        tv_fecha.setText("Fecha: " + evento.getFecha());
        tv_lugar.setText("Lugar: " + evento.getLugar());
        tv_descripcion.setText("Descripción: "  + evento.getDescripcion());
        tv_comentarios.setText("Comentarios: " + evento.getComentarios());

        if(evento.getImagenes().size() >= 1){
            int length = evento.getImagenes().size();
            byte[] img = evento.getImagenes().get(length-1);
            Bitmap bmimage = BitmapFactory.decodeByteArray(img, 0, img.length);
            iv_respuesta.setImageBitmap(bmimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continuar:
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                Log.i("change fragment", " personas= " + personas + " eventos= " + eventos);
                ft.replace(R.id.fragment_container, juego1_fragment.newInstance(preguntas, pregunta_actual, dificultad, correctas, personas, eventos), "Info persona tag");
                ft.commit();
                break;
        }
    }

}
