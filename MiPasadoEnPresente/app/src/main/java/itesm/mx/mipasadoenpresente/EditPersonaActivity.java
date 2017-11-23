package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EditPersonaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_AUDIO = 0;
    private static final int AGREGAR_IMAGEN = 1;
    private String[] array_relacion;
    private ImageView iv_imagenes;
    private Button btn_agregar;
    private Button btn_guardar;
    private Button btn_grabar;
    private EditText et_nombre;
    private EditText et_fecha;
    private EditText et_comentarios;
    private Spinner spinner;
    byte[] byteArray;
    Bitmap bitmap;

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
        setContentView(R.layout.activity_edit_persona);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

        operations = new PersonaOperations(this);
        operations.open();

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();

        array_relacion = new String[]{"Hermano", "Tio", "Sobrino", "Hijo", "Amigo", "Vecino"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, array_relacion);
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
                id_persona = data.getLong("ID");
                actual_persona = operations.getPersona(id_persona);
                list_imagenes_persona = actual_persona.getImagenes();

                setImagenPersona(list_imagenes_persona.size()-1);
                et_nombre.setText(actual_persona.getNombre());
                for(int i= 0; i < spinner.getAdapter().getCount(); i++)
                {
                    if(spinner.getAdapter().getItem(i).toString().contains(actual_persona.getCategoria()));
                    {
                        spinner.setSelection(i);
                    }
                }
                et_fecha.setText(actual_persona.getFecha_cumpleanos());
                et_comentarios.setText(actual_persona.getComentarios());
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
        btn_grabar.setOnClickListener(this);
    }

    public void setViews(){
        iv_imagenes = (ImageView) findViewById(R.id.iv_imagenes_persona);
        btn_agregar = (Button) findViewById(R.id.btn_agregar_imagen_persona);
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_fecha = (EditText) findViewById(R.id.et_fecha);
        et_comentarios = (EditText) findViewById(R.id.et_comentarios);
        spinner = (Spinner) findViewById(R.id.spinner_relacion);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_grabar = (Button) findViewById(R.id.btn_grabar);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agregar_imagen_persona:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escoger imagen"), AGREGAR_IMAGEN);
                break;
            case R.id.btn_guardar:
                String nombre = et_nombre.getText().toString();
                String fecha = et_fecha.getText().toString();
                String comentarios = et_comentarios.getText().toString();
                String relacion = spinner.getSelectedItem().toString();
                byte[] byteDummy = new byte[5];
                Persona new_persona = new Persona(nombre, relacion, fecha, comentarios, list_imagenes_persona, byteDummy);
                if(existe){
                    operations.updatePersona(id_persona, new_persona);
                }else{
                    operations.addPersona(new_persona);
                }

                Toast.makeText(this, "Se han guardado los datos de la persona",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_grabar:
                Intent intent_audio = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent_audio, SELECT_AUDIO);
                break;
        }
    }

    public void setImagenPersona(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_persona.get(index);
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
                        list_imagenes_persona.add(byteArray);
                        setImagenPersona(list_imagenes_persona.size()-1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SELECT_AUDIO:
                    Log.i("AUDIO","RECORDED");
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
