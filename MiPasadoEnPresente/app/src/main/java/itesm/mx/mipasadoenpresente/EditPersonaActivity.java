package itesm.mx.mipasadoenpresente;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.widget.Toast.LENGTH_LONG;

public class EditPersonaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_AUDIO = 0, AGREGAR_IMAGEN = 1, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private static final String RECORD_TAG = "Record";
    private ArrayList<String> array_relacion;
    private ImageView iv_imagenes;
    private Button btn_agregar, btn_guardar, btn_grabar,btn_play, btn_borrar;
    private EditText et_nombre, et_fecha, et_comentarios;
    private Spinner spinner;
    byte[] byteArray;
    Bitmap bitmap;
    MediaRecorder recorder;
    ArrayList<byte[]> list_imagenes_persona = new ArrayList<byte[]>();
    TextView tv_relacion;

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

    String categoria = "Todos";
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

        array_relacion = new ArrayList<String>();
        array_relacion.add("Hermano");
        array_relacion.add("Tio");
        array_relacion.add("Sobrino");
        array_relacion.add("Hijo");
        array_relacion.add("Amigo");
        array_relacion.add("Vecino");

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String categorias = prefs.getString("categorias", "");
        ArrayList<String> arr = new ArrayList<>(Arrays.asList(categorias.split("/")));

        for(final String s : arr){
            array_relacion.add(s);
        }

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
                tv_relacion.setText(actual_persona.getCategoria());
                et_fecha.setText(actual_persona.getFecha_cumpleanos());
                et_comentarios.setText(actual_persona.getComentarios());
                audio_path = actual_persona.getAudio();

                Log.i("audio", " = " + actual_persona.getAudio());
                existe = true;
                spinner.setVisibility(View.GONE);
            }else if(data.get("Categoria") != null){
                btn_borrar.setVisibility(View.GONE);
                if(!data.get("Categoria").equals("Todos")){
                    spinner.setVisibility(View.GONE);
                    tv_relacion.setText(data.getString("Categoria"));
                    categoria = data.getString("Categoria");
                }else{
                    tv_relacion.setVisibility(View.GONE);
                }
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
        btn_grabar.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_borrar.setOnClickListener(this);
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
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_fecha = (EditText) findViewById(R.id.et_fecha);
        et_comentarios = (EditText) findViewById(R.id.et_comentarios);
        spinner = (Spinner) findViewById(R.id.spinner_relacion);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_grabar = (Button) findViewById(R.id.btn_grabar);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_borrar = (Button) findViewById(R.id.btn_borrar);
        tv_relacion = (TextView) findViewById(R.id.tv_relacion);
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
                if(et_nombre.getText().toString().trim().equals("") || list_imagenes_persona.size() == 0){
                    Toast.makeText(this, "El nombre y  la imagen no pueden estar vacíos",
                            LENGTH_LONG).show();
                }else{
                    String nombre = et_nombre.getText().toString().trim();
                    String fecha = et_fecha.getText().toString();
                    String comentarios = et_comentarios.getText().toString();
                    String relacion = "";
                    if(categoria.equals("Todos")){
                        relacion = spinner.getSelectedItem().toString();
                    }else{
                        relacion = categoria;
                    }

                    Persona new_persona = new Persona(nombre, relacion, fecha, comentarios, list_imagenes_persona, audio_path);
                    if(existe){
                        operations.updatePersona(id_persona, new_persona);
                    }else{
                        operations.addPersona(new_persona);
                    }

                    Toast.makeText(this, "Se han guardado los datos de la persona",
                            LENGTH_LONG).show();
                    finish();
                }

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
            case R.id.btn_borrar:
                finish();
                operations.deleteEvento(id_persona);
                Toast.makeText(this, "Se han borrado los datos de la persona",
                        Toast.LENGTH_LONG).show();
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

        try {
            btn_grabar.setText("Grabar");
            recording = false;
            //stopping recorder
            recorder.stop();
            recorder.release();
            //after stopping the recorder, create the sound file and add it to media library.
            addRecordingToMediaLibrary();
        } catch (Exception e) {
            Toast.makeText(this, "Sonido no grabado. No hay permisos de acceso. Favor de habilitar los permisos necesarios en ajustes.", Toast.LENGTH_LONG).show();
            return;
        }

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
