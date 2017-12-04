/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditarInfoActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_guardar;
    Button btn_agregar_imagen;
    EditText et_nombre;
    EditText et_fecha;
    EditText et_comentario;
    ImageView iv_imagen;
    SharedPreferences prefs;
    private ImageButton ib_arrow_left;
    private ImageButton ib_arrow_right;

    byte[] byteArray;
    Bitmap bitmap;


    ArrayList<ImagenPersonal> listImagenesPersonales;

    ImagenPersonalOperations operations;

    int indice = 0;
    GestureDetectorCompat mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        operations = new ImagenPersonalOperations(this);
        operations.open();

        setViews();
        setInfo();

        btn_guardar.setOnClickListener(this);
        btn_agregar_imagen.setOnClickListener(this);
        ib_arrow_left.setOnClickListener(this);
        ib_arrow_right.setOnClickListener(this);
    }

    public void setViews(){
        btn_agregar_imagen = (Button) findViewById(R.id.btn_agregar_imagen);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_fecha = (EditText) findViewById(R.id.et_fecha);
        et_comentario = (EditText) findViewById(R.id.et_comentario);
        iv_imagen = (ImageView) findViewById(R.id.iv_imagenes);
        ib_arrow_left = (ImageButton) findViewById(R.id.imageBtn_left);
        ib_arrow_right = (ImageButton) findViewById(R.id.imageBtn_right);

    }

    public void setInfo(){
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String name = prefs.getString("nombre", "");
        String fecha = prefs.getString("fecha", "");
        String comentario = prefs.getString("comentarios", "");
        et_nombre.setText(name);
        et_fecha.setText(fecha);
        et_comentario.setText(comentario);

        listImagenesPersonales = operations.getAllImagenesPersonales();

        if (listImagenesPersonales.size() > 0){
            setImagenPersonalView(0);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_guardar:
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nombre",et_nombre.getText().toString());
                editor.putString("fecha",et_fecha.getText().toString());
                editor.putString("comentarios",et_comentario.getText().toString());
                editor.commit();
                Toast.makeText(this, "Se han guardado los datos",
                        Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btn_agregar_imagen:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escoger imagen"), 1);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK)
        {
            Uri selectedimg = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                ImagenPersonal imagenPersonal = new ImagenPersonal(byteArray);
                operations.addImagenPersonal(imagenPersonal);

                listImagenesPersonales.add(imagenPersonal);
                indice = listImagenesPersonales.size()-1;
                setImagenPersonalView(indice);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void setImagenPersonalView(int index){
        ImagenPersonal imagen = listImagenesPersonales.get(index);
        iv_imagen.setImageBitmap(BitmapFactory.decodeByteArray(imagen.getImagen(), 0, imagen.getImagen().length));
    }

}
