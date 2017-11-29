package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Clase para la vista de imagenes personales
 */
public class InformacionActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_editar;
    TextView tv_nombre;
    TextView tv_fecha;
    TextView tv_comentario;
    ImageView iv_imagen;

    ArrayList<ImagenPersonal> listImagenesPersonales;
    ImagenPersonalOperations operations;
    int indice = 0;
    GestureDetectorCompat mDetector;

    /**
     * Inicializa las vistas
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_informacion);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            operations = new ImagenPersonalOperations(this);
            operations.open();

            setViews();
            setInfo();


            InformacionActivity.MyGestureListener myGestureListener = new InformacionActivity.MyGestureListener(getApplicationContext());
            mDetector = new GestureDetectorCompat(this, myGestureListener);
            iv_imagen.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    mDetector.onTouchEvent(event);
                    return true;
                }
            });;


            btn_editar.setOnClickListener(this);
    }

    /**
     * Iniializa las vistas
     */
    public void setViews(){
        btn_editar = (Button) findViewById(R.id.btn_editar);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);
        tv_comentario = (TextView) findViewById(R.id.tv_comentario);
        iv_imagen = (ImageView) findViewById(R.id.iv_imagen);
    }

    /**
     * Actualiza los datos obteniendolos desde SharedPreferences
     */
    public void setInfo(){
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String name = prefs.getString("nombre", "No se ha definido");//"No name defined" is the default value.
        String fecha = prefs.getString("fecha", "No se ha definido"); //0 is the default value.
        String comentario = prefs.getString("comentarios", "No se ha definido");
        tv_nombre.setText(name);
        tv_fecha.setText(fecha);
        tv_comentario.setText(comentario);

        listImagenesPersonales = operations.getAllImagenesPersonales();

        if (listImagenesPersonales.size() > 0){
            setImagenPersonalView(0);
        }

    }

    /**
     * Agrega funcionalidad al botón de back'
     * @return
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * Vuelve a actualizar textos despues de volver a la actividad
     */
    @Override
    public void onResume(){
        setInfo();
        super.onResume();
    }

    /**
     * Maneja el clic de Editar mandandolo a una nueva actividad
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_editar:
                Intent editar_info_activity = new Intent(getApplicationContext(), EditarInfoActivity.class);
                startActivity(editar_info_activity);
                break;
        }
    }

    public void setImagenPersonalView(int index){
        ImagenPersonal imagen = listImagenesPersonales.get(index);
        iv_imagen.setImageBitmap(BitmapFactory.decodeByteArray(imagen.getImagen(), 0, imagen.getImagen().length));
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
                    indice = listImagenesPersonales.size()-1;
                }
            }else if (e1.getX() < e2.getX()){
                indice = indice + 1;
                if (indice >= listImagenesPersonales.size()) {
                    indice = 0;
                }
            }

            setImagenPersonalView(indice);
            return true;
        }
    }
}
