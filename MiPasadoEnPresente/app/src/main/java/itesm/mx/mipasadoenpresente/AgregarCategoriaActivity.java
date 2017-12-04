/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase para la vista de agregar categoria
 */
public class AgregarCategoriaActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_categoria; // Cuadro de texto para el nombre de categoria
    Button btn_agregar; // Botón para agregar categorias
    SharedPreferences prefs; // Para obtener las SharedPreferences
    String categorias; // Para obtener las categorias actuales

    /**
     * OnCreate
     * Inicializa los elementos de la pantalla y obtiene las categorias actuales
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        btn_agregar = (Button) findViewById(R.id.btn_agregar);
        et_categoria = (EditText) findViewById(R.id.et_categoria);

        categorias = prefs.getString("categorias", "");

        btn_agregar.setOnClickListener(this);
    }

    /**
     * Añade funcionalidad al botón de Back en la barra de la app
     * @return
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * Maneja el evento de click para el botón de agregar categoría
     * @param v la vista a la que se dio clic
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agregar:
                if(!et_categoria.getText().toString().equals("")){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("categorias",categorias + et_categoria.getText().toString() + "/");
                    editor.commit();
                    Toast.makeText(this, "Categoría agregada", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(this, "Campo no puede ir vacío", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
