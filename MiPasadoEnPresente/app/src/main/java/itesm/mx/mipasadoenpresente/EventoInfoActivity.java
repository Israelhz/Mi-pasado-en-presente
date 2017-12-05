/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Using PhotoView library to zoom images
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Clase para manejar la vista de información de evento
 */
public class EventoInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_imagenes;
    private Button btn_zoom;
    private Button btn_editEvento;
    private Button btn_audio;
    private TextView tv_nombre;
    private TextView tv_fecha;
    private TextView tv_lugar;
    private TextView tv_comentarios;
    private TextView tv_descripcion;
    private TextView tv_personasAsociadas;
    private TextView tv_categoria;
    private ImageView iv_expanded_image_event;
    private ImageButton ib_arrow_left;
    private ImageButton ib_arrow_right;

    ArrayList<byte[]> list_imagenes_evento = new ArrayList<byte[]>();

    PhotoViewAttacher pAttacher;

    String audio_path = "";
    private boolean zoomed = false;

    int indice = 0;

    EventoOperations operations;
    Evento actual_evento = null;
    private long id_evento;
    private boolean existe = false;

    /**
     * Inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

        operations = new EventoOperations(this);
        operations.open();

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();

        Bundle data = getIntent().getExtras();

        if (data != null) {
            if (data.get("ID") != null){
                id_evento= data.getLong("ID");
                actual_evento= operations.getEvento(id_evento);
                list_imagenes_evento = actual_evento.getImagenes();
                setImagenEvento(list_imagenes_evento.size()-1);
                tv_nombre.setText(actual_evento.getNombre());
                tv_categoria.setText(actual_evento.getCategoria());
                tv_fecha.setText(actual_evento.getFecha());
                tv_lugar.setText(actual_evento.getLugar());
                tv_descripcion.setText(actual_evento.getDescripcion());
                tv_comentarios.setText(actual_evento.getComentarios());
                tv_personasAsociadas.setText(actual_evento.getPersonas_asociadas());
                existe = true;
                audio_path = actual_evento.getAudio();
            }
        }

        btn_audio.setOnClickListener(this);
        btn_editEvento.setOnClickListener(this);
        btn_zoom.setOnClickListener(this);
        iv_expanded_image_event.setOnClickListener(this);
        ib_arrow_left.setOnClickListener(this);
        ib_arrow_right.setOnClickListener(this);

    }

    /**
     * Al volver a la actividad vuelve a actualizar el evento
     */
    @Override
    public void onResume(){
        super.onResume();

        actual_evento= operations.getEvento(id_evento);
        list_imagenes_evento = actual_evento.getImagenes();
        setImagenEvento(list_imagenes_evento.size()-1);
        tv_nombre.setText(actual_evento.getNombre());
        tv_categoria.setText(actual_evento.getCategoria());
        tv_fecha.setText(actual_evento.getFecha());
        tv_lugar.setText(actual_evento.getLugar());
        tv_descripcion.setText(actual_evento.getDescripcion());
        tv_comentarios.setText(actual_evento.getComentarios());
        tv_personasAsociadas.setText(actual_evento.getPersonas_asociadas());
        existe = true;
        audio_path = actual_evento.getAudio();
    }

    /**
     * Función para dar zoom a la imagen
     */
    private void zoom() {
        if (!zoomed) {
            iv_expanded_image_event.setVisibility(View.VISIBLE);
            iv_imagenes.setVisibility(View.GONE);
            ib_arrow_left.setVisibility(View.GONE);
            ib_arrow_right.setVisibility(View.GONE);
            btn_zoom.setText("Reducir Imagen");
            zoomed = true;
            pAttacher = new PhotoViewAttacher(iv_expanded_image_event);
            pAttacher.update();
        } else {
            iv_expanded_image_event.setVisibility(View.GONE);
            iv_imagenes.setVisibility(View.VISIBLE);
            ib_arrow_left.setVisibility(View.VISIBLE);
            ib_arrow_right.setVisibility(View.VISIBLE);
            btn_zoom.setText("Ampliar Imagen");
            zoomed = false;
        }
    }

    /**
     * Reproduce el audio
     * @throws IOException
     */
    public void play() throws IOException {
        Uri myUri = Uri.parse(audio_path); // initialize Uri here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getApplicationContext(), myUri);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    /**
     * Inicializa elementos de la pantalla
     */
    public void setViews(){
        iv_imagenes = (ImageView) findViewById(R.id.iv_imagenes_evento);
        btn_editEvento = (Button) findViewById(R.id.btn_editar);
        tv_nombre = (TextView) findViewById(R.id.text_nombre);
        tv_fecha = (TextView) findViewById(R.id.text_fecha);
        tv_lugar = (TextView) findViewById(R.id.text_lugar);
        iv_expanded_image_event = (ImageView) findViewById(R.id.expanded_image);
        list_imagenes_evento = new ArrayList<byte[]>();
        tv_comentarios= (TextView) findViewById(R.id.text_comentario);
        tv_descripcion = (TextView) findViewById(R.id.text_descripcion);
        tv_personasAsociadas = (TextView) findViewById(R.id.text_personas_relacionadas);
        tv_categoria = (TextView) findViewById(R.id.text_categoria);
        btn_zoom = (Button) findViewById(R.id.btn_ampliar_evento_imagen);
        ib_arrow_left = (ImageButton) findViewById(R.id.imageBtn_left);
        ib_arrow_right = (ImageButton) findViewById(R.id.imageBtn_right);
        btn_audio = (Button) findViewById(R.id.btn_audio);
    }

    /** Agrega funcionalidad al botón de Back
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * Maneja los clics en la pantalla
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ampliar_evento_imagen:
                zoom();
                break;
            case R.id.btn_audio:
                if(audio_path.equals("")){
                    Toast.makeText(this, "No hay un sonido asociado", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        Toast.makeText(this, "Reproduciendo audio",
                                LENGTH_LONG).show();
                        play();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_editar:
                finish();
                Intent intent = new Intent(getApplicationContext(), EditEventoActivity.class);
                intent.putExtra("ID", actual_evento.getId());
                startActivity(intent);
                break;
            case R.id.imageBtn_left:
                Log.d("Arrow", "Left ");
                indice = indice - 1;
                if (indice < 0) {
                    indice = list_imagenes_evento.size()-1;
                }
                setImagenEvento(indice);
                break;
            case R.id.imageBtn_right:
                Log.d("Arrow", "Right ");
                indice = indice + 1;
                if (indice >= list_imagenes_evento.size()) {
                    indice = 0;
                }
                setImagenEvento(indice);
                break;
            default:
                break;
        }
    }

    /**
     * Cambia la imagen actual
     * @param index
     */
    public void setImagenEvento(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_evento.get(index);
            iv_imagenes.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
            iv_expanded_image_event.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
        }

    }
}
