/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Using Photoview library to zoom images
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.media.Image;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

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

    private ImageButton ib_arrow_left;
    private ImageButton ib_arrow_right;
    private Button btn_zoom;
    private ImageView iv_expanded_image;
    private boolean zoomed = false;
    PhotoViewAttacher pAttacher;

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


            btn_editar.setOnClickListener(this);
            ib_arrow_left.setOnClickListener(this);
            ib_arrow_right.setOnClickListener(this);
            btn_zoom.setOnClickListener(this);
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
        ib_arrow_left = (ImageButton) findViewById(R.id.imageBtn_left);
        ib_arrow_right = (ImageButton) findViewById(R.id.imageBtn_right);
        btn_zoom = (Button) findViewById(R.id.btn_ampliar_imagen);
        iv_expanded_image = (ImageView) findViewById(R.id.expanded_image);
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
        }else{
            listImagenesPersonales = new ArrayList<ImagenPersonal>();
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

            case R.id.imageBtn_left:
                if(listImagenesPersonales.size()>0){
                    indice = indice - 1;
                    if (indice < 0) {
                        indice = listImagenesPersonales.size() - 1;
                    }
                    setImagenPersonalView(indice);
                }

                break;

            case R.id.imageBtn_right:
                if(listImagenesPersonales.size() > 0){
                    indice = indice + 1;
                    if (indice >= listImagenesPersonales.size()) {
                        indice = 0;
                    }
                    setImagenPersonalView(indice);
                }

                break;
            case R.id.btn_ampliar_imagen:
                zoom();
                break;
        }
    }

    /**
     * Función para dar zoom a la imagen
     */
    private void zoom() {
        if (!zoomed) {
            iv_expanded_image.setVisibility(View.VISIBLE);
            iv_imagen.setVisibility(View.GONE);
            ib_arrow_left.setVisibility(View.GONE);
            ib_arrow_right.setVisibility(View.GONE);
            btn_zoom.setText("Reducir Imagen");
            zoomed = true;
            pAttacher = new PhotoViewAttacher(iv_expanded_image);
            pAttacher.update();
        } else {
            iv_expanded_image.setVisibility(View.GONE);
            iv_imagen.setVisibility(View.VISIBLE);
            ib_arrow_left.setVisibility(View.VISIBLE);
            ib_arrow_right.setVisibility(View.VISIBLE);
            btn_zoom.setText("Ampliar Imagen");
            zoomed = false;
        }
    }

    public void setImagenPersonalView(int index){
        ImagenPersonal imagen = listImagenesPersonales.get(index);
        iv_imagen.setImageBitmap(BitmapFactory.decodeByteArray(imagen.getImagen(), 0, imagen.getImagen().length));
        iv_expanded_image.setImageBitmap(BitmapFactory.decodeByteArray(imagen.getImagen(), 0, imagen.getImagen().length));

    }


}
