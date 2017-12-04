/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Clase para manejar la vista de seleccionar categoria de eventos
 */
public class CategoriasEventosActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String DEBUG_TAG = "CATEGORIAS_EVENTOS";

    private Button btnPersonales;
    private Button btnEpoca;
    private ImageButton btnSearch;
    private TextView tvSearch;

    /**
     * OnCreate
     * Inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_eventos);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.e(DEBUG_TAG, "ERROR: Missing Images byte array");
        btnPersonales= (Button) findViewById(R.id.btn_personales);
        btnEpoca = (Button) findViewById(R.id.btn_epoca);
        btnSearch = (ImageButton) findViewById(R.id.imageButton_search);
        tvSearch = (TextView) findViewById(R.id.editText_search_evento);

        btnPersonales.setOnClickListener(this);
        btnEpoca.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    /**
     * Añade funcionalidad al botón de regreso
     * @return
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * Maneja los clics en la pantalla
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_personales:
                getByPersonales(); // Obtiene los eventos personales
                break;
            case R.id.btn_epoca:
                getByEpoca(); // Obtiene los eventos de la epoca
                break;
            case R.id.imageButton_search:
                getBySearch(); // Obtiene los eventos que contenga la busqueda
                break;
        }
    }

    /**
     * getByPersonales
     * Obtiene los eventos personales
     */
    private void getByPersonales() {
        startAct("Personales");
        Log.d(DEBUG_TAG, "getByPersonales()");
    }

    /**
     * getByEpoca
     * Obtiene los eventos de la época
     */
    private void getByEpoca() {
        startAct("Epoca");
        Log.d(DEBUG_TAG, "getByEpoca()");
    }

    /**
     * getBySearch
     * Obtiene los eventos según a busqueda
     */
    private void getBySearch() {
        startAct("Search");
        Log.d(DEBUG_TAG, "getBySearch()");
    }

    /**
     * startAct
     * Inicializa actividad de lista de eventos con la categoria correspondiente
     */
    private void startAct(String category) {
        Intent intent = new Intent(getApplicationContext(), EventosListActivity.class);
        intent.putExtra("Category", category);
        intent.putExtra("Search", tvSearch.getText().toString());
        startActivity(intent);
    }
}
