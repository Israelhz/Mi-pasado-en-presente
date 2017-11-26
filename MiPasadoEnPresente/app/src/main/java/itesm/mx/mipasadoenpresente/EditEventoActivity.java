package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanc on 11/20/2017.
 */

public class EditEventoActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] array_categoria;
    private static final int AGREGAR_IMAGEN = 1;
    private ImageView iv_imagenes;
    private Button btn_agregar;
    private Button btn_guardar;
    private EditText et_nombre;
    private EditText et_fecha;
    private EditText et_lugar;
    private EditText et_comentarios;
    private EditText et_descripcion;
    private EditText et_personasAsociadas;
    private Spinner spinner;
    byte[] byteArray;
    Bitmap bitmap;

    ArrayList<byte[]> list_imagenes_evento= new ArrayList<byte[]>();

    int indice = 0;
    GestureDetectorCompat mDetector;

    EventoOperations operations;

    Evento actual_evento = null;
    private long id_evento;
    private boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_evento);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

        operations = new EventoOperations(this);
        operations.open();

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();

        array_categoria = new String[]{"Personales","Epoca"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, array_categoria);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Bundle data = getIntent().getExtras();

        if (data != null) {
            if (data.get("ID") != null){
                id_evento = data.getLong("ID");
                actual_evento = operations.getEvento(id_evento);
                list_imagenes_evento = actual_evento.getImagenes();
                setImagenEvento(list_imagenes_evento.size()-1);
                et_nombre.setText(actual_evento.getNombre());
                for(int i= 0; i < spinner.getAdapter().getCount(); i++)
                {
                    if(spinner.getAdapter().getItem(i).toString().contains(actual_evento.getCategoria()));
                    {
                        spinner.setSelection(i);
                    }
                }
                et_fecha.setText(actual_evento.getFecha());
                et_lugar.setText(actual_evento.getLugar());
                et_descripcion.setText(actual_evento.getDescripcion());
                et_comentarios.setText(actual_evento.getComentarios());
                et_personasAsociadas.setText(actual_evento.getPersonas_asociadas());
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


        btn_guardar.setOnClickListener(this);
        btn_agregar.setOnClickListener(this);
    }

    public void setViews(){
        iv_imagenes = (ImageView) findViewById(R.id.iv_imagenes_evento);
        btn_agregar = (Button) findViewById(R.id.btn_agregar_imagen_evento);
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_fecha = (EditText) findViewById(R.id.et_fecha);
        et_lugar = (EditText) findViewById(R.id.et_lugar);
        spinner = (Spinner) findViewById(R.id.spinner_relacion);
        list_imagenes_evento = new ArrayList<byte[]>();
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        et_comentarios= (EditText) findViewById(R.id.et_comentarios);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_personasAsociadas = (EditText) findViewById(R.id.et_personas_asociadas);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agregar_imagen_evento:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escoger imagen"), AGREGAR_IMAGEN);
                break;
            case R.id.btn_guardar:
                String nombre = et_nombre.getText().toString();
                String fecha = et_fecha.getText().toString();
                String lugar = et_lugar.getText().toString();

                String descripcion = et_descripcion.getText().toString();
                String categoria = spinner.getSelectedItem().toString();
                String comentarios = et_comentarios.getText().toString();
                String personasAsociadas = et_personasAsociadas.getText().toString();

                Evento new_evento = new Evento(nombre, categoria, lugar, fecha, descripcion, comentarios, personasAsociadas, list_imagenes_evento);
                if(existe){
                    operations.updateEvento(id_evento, new_evento);
                }else{
                    operations.addEvento(new_evento);
                }
                Toast.makeText(this, "Se han guardado los datos del evento",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void setImagenEvento(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_evento.get(index);
            iv_imagenes.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK)
        {
            switch(requestCode){
                case AGREGAR_IMAGEN:
                    Uri selectedimg = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        list_imagenes_evento.add(byteArray);
                        setImagenEvento(list_imagenes_evento.size()-1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

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