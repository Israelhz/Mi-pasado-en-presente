package itesm.mx.mipasadoenpresente;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.LENGTH_LONG;

public class PersonaInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_AUDIO = 0, AGREGAR_IMAGEN = 1, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private static final String RECORD_TAG = "Record";
    private String[] array_relacion;
    private ImageView iv_imagenes;
    private Button btn_agregar, btn_editar,btn_play;
    private TextView et_nombre, et_fecha, et_comentarios, tv_categoria;
    private Spinner spinner;
    byte[] byteArray;
    Bitmap bitmap;
    MediaRecorder recorder;
    ArrayList<byte[]> list_imagenes_persona = new ArrayList<byte[]>();

    int indice = 0;
    GestureDetectorCompat mDetector;

    PersonaOperations operations;
    Persona actual_persona = null;
    private long id_persona;
    private boolean existe = false;

    String audio_path = "";

    private boolean recording = false;

    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    File audiofile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

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
                et_nombre.setText(actual_persona.getNombre());
                tv_categoria.setText(actual_persona.getCategoria());
                et_fecha.setText(actual_persona.getFecha_cumpleanos());
                et_comentarios.setText(actual_persona.getComentarios());
                audio_path = actual_persona.getAudio();

                Log.i("audio", " = " + actual_persona.getAudio());
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

        checa_permisos();
        btn_editar.setOnClickListener(this);
        btn_agregar.setOnClickListener(this);
        btn_play.setOnClickListener(this);
    }

    private boolean checa_permisos() {
        Activity activity = this;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            return true;
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        return false;

    }

    public void setViews(){
        iv_imagenes = (ImageView) findViewById(R.id.iv_imagenes_persona);
        btn_agregar = (Button) findViewById(R.id.btn_agregar_imagen_persona);
        et_nombre = (TextView) findViewById(R.id.et_nombre);
        et_fecha = (TextView) findViewById(R.id.et_fecha);
        et_comentarios = (TextView) findViewById(R.id.et_comentarios);
        tv_categoria = (TextView) findViewById(R.id.tv_categoria);
        btn_editar = (Button) findViewById(R.id.btn_guardar);
        btn_play = (Button) findViewById(R.id.btn_play);
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
            case R.id.btn_editar:
                if (existe) {
                    Intent intent_edit = new Intent(getApplicationContext(), EditPersonaActivity.class);//Edit mode
                    intent_edit.putExtra("ID", actual_persona.getId());
                    startActivity(intent_edit);
                }
                setViews();
                break;
            case R.id.btn_play:
                if(audio_path == ""){
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
        }
    }

    public void setImagenPersona(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_persona.get(index);
            iv_imagenes.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));

        }

    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void play() throws IOException {
        Uri myUri = Uri.parse(audio_path); // initialize Uri here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getApplicationContext(), myUri);
        mediaPlayer.prepare();
        mediaPlayer.start();

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
