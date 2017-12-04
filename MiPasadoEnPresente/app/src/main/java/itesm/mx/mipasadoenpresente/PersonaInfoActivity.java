/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
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

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.LENGTH_LONG;

public class PersonaInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_AUDIO = 0, AGREGAR_IMAGEN = 1, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    private ImageView iv_imagenes, iv_expanded_image;
    private Button btn_zoom, btn_editar,btn_play;
    private TextView et_nombre, et_fecha, et_comentarios, tv_categoria;
    private ImageButton ib_arrow_left;
    private ImageButton ib_arrow_right;

    ArrayList<byte[]> list_imagenes_persona = new ArrayList<byte[]>();

    private boolean zoomed = false;
    int indice = 0;
    GestureDetectorCompat mDetector;

    PersonaOperations operations;
    Persona actual_persona = null;
    private long id_persona;
    private boolean existe = false;

    PhotoViewAttacher pAttacher;


    String audio_path = "";

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

                existe = true;
            }
        }

        checa_permisos();
        btn_editar.setOnClickListener(this);
        btn_zoom.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        ib_arrow_left.setOnClickListener(this);
        ib_arrow_right.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        actual_persona = operations.getPersona(id_persona);
        list_imagenes_persona = actual_persona.getImagenes();
        setImagenPersona(list_imagenes_persona.size()-1);
        et_nombre.setText(actual_persona.getNombre());
        tv_categoria.setText(actual_persona.getCategoria());
        et_fecha.setText(actual_persona.getFecha_cumpleanos());
        et_comentarios.setText(actual_persona.getComentarios());
        audio_path = actual_persona.getAudio();

        existe = true;
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
        btn_zoom = (Button) findViewById(R.id.btn_ampliar_imagen_persona);
        et_nombre = (TextView) findViewById(R.id.et_nombre);
        et_fecha = (TextView) findViewById(R.id.et_fecha);
        et_comentarios = (TextView) findViewById(R.id.et_comentarios);
        tv_categoria = (TextView) findViewById(R.id.tv_categoria);
        btn_editar = (Button) findViewById(R.id.btn_editar);
        btn_play = (Button) findViewById(R.id.btn_play);
        iv_expanded_image = (ImageView) findViewById(R.id.expanded_image);

        ib_arrow_left = (ImageButton) findViewById(R.id.imageBtn_left);
        ib_arrow_right = (ImageButton) findViewById(R.id.imageBtn_right);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void zoom() {
        if (!zoomed) {
            iv_expanded_image.setVisibility(View.VISIBLE);
            iv_imagenes.setVisibility(View.GONE);
            ib_arrow_left.setVisibility(View.GONE);
            ib_arrow_right.setVisibility(View.GONE);
            btn_zoom.setText("Reducir Imagen");
            zoomed = true;
            pAttacher = new PhotoViewAttacher(iv_expanded_image);
            pAttacher.update();
        } else {
            iv_expanded_image.setVisibility(View.GONE);
            iv_imagenes.setVisibility(View.VISIBLE);
            ib_arrow_left.setVisibility(View.VISIBLE);
            ib_arrow_right.setVisibility(View.VISIBLE);
            btn_zoom.setText("Ampliar Imagen");
            zoomed = false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ampliar_imagen_persona:
                zoom();
                break;
            case R.id.btn_editar:
                if (existe) {
                    finish();
                    Intent intent_edit = new Intent(getApplicationContext(), EditPersonaActivity.class);//Edit mode
                    intent_edit.putExtra("ID", actual_persona.getId());
                    startActivity(intent_edit);
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
            case R.id.imageBtn_left:
                Log.d("Arrow", "Left ");
                indice = indice - 1;
                if (indice < 0) {
                    indice = list_imagenes_persona.size()-1;
                }
                setImagenPersona(indice);
                break;
            case R.id.imageBtn_right:
                Log.d("Arrow", "Right ");
                indice = indice + 1;
                if (indice >= list_imagenes_persona.size()) {
                    indice = 0;
                }
                setImagenPersona(indice);
                break;
            default:
                break;
        }
    }

    public void setImagenPersona(int index){
        if(index >= 0){
            byte[] imagen = list_imagenes_persona.get(index);
            iv_imagenes.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
            iv_expanded_image.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
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
}
