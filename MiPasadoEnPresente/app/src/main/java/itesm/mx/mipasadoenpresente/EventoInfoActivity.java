package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EventoInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_imagenes;
    private Button btn_zoom;
    private Button btn_editEvento;
    private TextView tv_nombre;
    private TextView tv_fecha;
    private TextView tv_lugar;
    private TextView tv_comentarios;
    private TextView tv_descripcion;
    private TextView tv_personasAsociadas;
    private TextView tv_categoria;
    private ImageView iv_expanded_image_event;
    ArrayList<byte[]> list_imagenes_evento = new ArrayList<byte[]>();

    PhotoViewAttacher pAttacher;

    private boolean zoomed = false;

    int indice = 0;
    GestureDetectorCompat mDetector;

    EventoOperations operations;
    Evento actual_evento = null;
    private long id_evento;
    private boolean existe = false;

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
            }
        }

        MyGestureListener myGestureListener = new MyGestureListener(getApplicationContext());
        mDetector = new GestureDetectorCompat(this, myGestureListener);
        iv_imagenes.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });;

        iv_expanded_image_event.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

       // btn_audio.setOnClickListener(this);
        btn_editEvento.setOnClickListener(this);
        btn_zoom.setOnClickListener(this);
        iv_expanded_image_event.setOnClickListener(this);

    }

    private void zoom() {
        if (!zoomed) {
            iv_expanded_image_event.setVisibility(View.VISIBLE);
            iv_imagenes.setVisibility(View.GONE);
            btn_zoom.setText("Reducir Imagen");
            zoomed = true;
            pAttacher = new PhotoViewAttacher(iv_expanded_image_event);
            pAttacher.update();
        } else {
            iv_expanded_image_event.setVisibility(View.GONE);
            iv_imagenes.setVisibility(View.VISIBLE);
            btn_zoom.setText("Ampliar Imagen");
            zoomed = false;
        }
    }

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
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ampliar_evento_imagen:
                zoom();
                break;
            case R.id.btn_audio:

                Toast.makeText(this, "Reproduciendo Audio",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_editar:
                Intent intent = new Intent(getApplicationContext(), EditEventoActivity.class);
                intent.putExtra("ID", actual_evento.getId());
                startActivity(intent);
                break;
            case R.id.expanded_image:
                zoom();
                break;
            default:
                break;
        }
    }

    public void setImagenEvento(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_evento.get(index);
            iv_imagenes.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
            iv_expanded_image_event.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
        }

    }

    public class MyGestureListener implements GestureDetector.OnGestureListener {
        String LISTENER_TAG = "Listener: ";

        public MyGestureListener(Context applicationContext) {
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() > e2.getX()) {
                indice = indice - 1;
                if (indice < 0) {
                    indice = list_imagenes_evento.size()-1;
                }
            }else if (e1.getX() < e2.getX()){
                indice = indice + 1;
                if (indice >= list_imagenes_evento.size()) {
                    indice = 0;
                }
            }

            setImagenEvento(indice);
            return true;
        }
    }

}
