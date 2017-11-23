package itesm.mx.mipasadoenpresente;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class PersonaInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_imagenes;
    private Button btn_audio;
    private TextView tv_nombre;
    private TextView tv_fecha;
    private TextView tv_comentarios;
    private TextView tv_relacion;
//    byte[] byteArray;
//    Bitmap bitmap;

    ArrayList<byte[]> list_imagenes_persona = new ArrayList<byte[]>();

    int indice = 0;
    GestureDetectorCompat mDetector;

    PersonaOperations operations;
    Persona actual_persona = null;
    private long id_persona;
    private boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

//        Intent intent = new Intent(getApplicationContext(), EditPersonaActivity.class);
//        intent.putExtra("ID", personasList.get(position).getId());
//        startActivity(intent);
        operations = new PersonaOperations(this);
        operations.open();

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();

        Bundle data = getIntent().getExtras();

        if (data != null) {
            if (data.get("ID") != null){
                id_persona = data.getLong("ID");
                actual_persona = operations.getPersona(id_persona);
                list_imagenes_persona = actual_persona.getImagenes();

                setImagenPersona(list_imagenes_persona.size()-1);
                tv_relacion.setText(actual_persona.getCategoria());
                tv_nombre.setText(actual_persona.getNombre());
                tv_fecha.setText(actual_persona.getFecha_cumpleanos());
                tv_comentarios.setText(actual_persona.getComentarios());
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

        btn_audio.setOnClickListener(this);
    }

    public void setViews(){
        iv_imagenes = (ImageView) findViewById(R.id.iv_imagenes_persona);
        tv_relacion = (TextView) findViewById(R.id.text_relacion);
        btn_audio = (Button) findViewById(R.id.btn_audio);
        tv_nombre = (TextView) findViewById(R.id.text_nombre);
        tv_fecha = (TextView) findViewById(R.id.text_fecha);
        tv_comentarios = (TextView) findViewById(R.id.text_comentario);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_audio:

                Toast.makeText(this, "Reproduciendo Audio",
                        Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void setImagenPersona(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_persona.get(index);
            iv_imagenes.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));

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
                    indice = list_imagenes_persona.size()-1;
                }
            }else if (e1.getX() < e2.getX()){
                indice = indice + 1;
                if (indice >= list_imagenes_persona.size()) {
                    indice = 0;
                }
            }

            setImagenPersona(indice);
            return true;
        }
    }

}
