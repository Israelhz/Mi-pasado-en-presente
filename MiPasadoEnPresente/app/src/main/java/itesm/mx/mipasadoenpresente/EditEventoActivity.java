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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by juanc on 11/20/2017.
 */

public class EditEventoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String RECORD_TAG = "RECORDING";
    private String[] array_categoria;
    private static final int AGREGAR_IMAGEN = 1;
    private ImageView iv_imagenes;

    private Button btn_agregar, btn_guardar, btn_grabar, btn_play, btn_borrar;
    private EditText et_nombre, et_fecha, et_lugar, et_comentarios, et_descripcion, et_personasAsociadas;

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

    String audio_path = "";

    private boolean recording = false;

    public static final int RequestPermissionCode = 1;
    MediaRecorder recorder;
    File audiofile = null;


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
                    if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(actual_evento.getCategoria()));
                    {
                        spinner.setSelection(i);
                        break;
                    }
                }

                et_fecha.setText(actual_evento.getFecha());
                et_lugar.setText(actual_evento.getLugar());
                et_descripcion.setText(actual_evento.getDescripcion());
                et_comentarios.setText(actual_evento.getComentarios());
                et_personasAsociadas.setText(actual_evento.getPersonas_asociadas());
                audio_path = actual_evento.getAudio();
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
        btn_guardar.setOnClickListener(this);
        btn_agregar.setOnClickListener(this);

        btn_borrar.setOnClickListener(this);

        btn_grabar.setOnClickListener(this);
        btn_play.setOnClickListener(this);
    }

    private boolean checa_permisos() {
        Activity activity = this;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            return true;
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, RequestPermissionCode);
        return false;

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

        btn_borrar = (Button) findViewById(R.id.btn_borrar);

        btn_grabar = (Button) findViewById(R.id.btn_grabar);
        btn_play = (Button) findViewById(R.id.btn_play);
    }

    public void startRecording() throws IOException {
        btn_grabar.setText("Detener");
        recording = true;
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".3gp", dir);
        } catch (IOException e) {
            Log.e(RECORD_TAG, "external storage access error" + e.toString());
            return;
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }

    public void stopRecording() {
        btn_grabar.setText("Grabar");
        recording = false;
        //stopping recorder
        recorder.stop();
        recorder.release();
        //after stopping the recorder, create the sound file and add it to media library.
        addRecordingToMediaLibrary();
    }

    public void play() throws IOException {
        Uri myUri = Uri.parse(audio_path); // initialize Uri here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getApplicationContext(), myUri);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    protected void addRecordingToMediaLibrary() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        audio_path = newUri.toString();

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Se ha grabado el sonido", Toast.LENGTH_LONG).show();
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
                if(et_nombre.getText().toString().equals("") || list_imagenes_evento.size() == 0){
                    Toast.makeText(this, "El nombre y  la imagen no pueden estar vacíos",
                            LENGTH_LONG).show();
                }else{
                    String nombre = et_nombre.getText().toString();
                    String fecha = et_fecha.getText().toString();
                    String lugar = et_lugar.getText().toString();

                    String descripcion = et_descripcion.getText().toString();
                    String categoria = spinner.getSelectedItem().toString();
                    String comentarios = et_comentarios.getText().toString();
                    String personasAsociadas = et_personasAsociadas.getText().toString();

                    Evento new_evento = new Evento(nombre, categoria, lugar, fecha, descripcion, comentarios, personasAsociadas, list_imagenes_evento, audio_path);
                    if(existe){
                        operations.updateEvento(id_evento, new_evento);
                    }else{
                        operations.addEvento(new_evento);
                    }
                    Toast.makeText(this, "Se han guardado los datos del evento",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

                break;

            case R.id.btn_borrar:
                String nombre2 = et_nombre.getText().toString();
                operations.deleteEvento(nombre2);
                Toast.makeText(this, "Se han borrado los datos del evento",
                        Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(getApplicationContext(), CategoriasEventosActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_grabar:
                try {
                    if(recording){
                        stopRecording();
                    }else {
                        Toast.makeText(this, "Grabando sonido, pulse Detener para parar la grabación",
                                LENGTH_LONG).show();
                        startRecording();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
