/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoriasPersonasActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "CATEGORIAS_PERSONAS";

    private Button btnTodo;
    private Button btnHermanos;
    private Button btnTios;
    private Button btnSobrinos;
    private Button btnHijos;
    private Button btnAmigos;
    private Button btnVecinos;
    private ImageButton btnSearch;
    private Button btnOtros;
    private Button btnAgregar;
    private TextView tvSearch;
    private ViewGroup layout_categorias;

    private String categorias;
    private ArrayList<String> arr;
    private ArrayList<String> initial_arr;
    SharedPreferences prefs;

    String categoria = "Todos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_personas);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Oculta teclado al iniciar activity

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnTodo = (Button) findViewById(R.id.btn_todo);
        btnHermanos = (Button) findViewById(R.id.btn_hermanos);
        btnTios = (Button) findViewById(R.id.btn_tios);
        btnSobrinos = (Button) findViewById(R.id.btn_sobrinos);
        btnHijos = (Button) findViewById(R.id.btn_hijos);
        btnAmigos = (Button) findViewById(R.id.btn_amigos);
        btnVecinos = (Button) findViewById(R.id.btn_vecinos);
        btnSearch = (ImageButton) findViewById(R.id.imageButton_search);
        tvSearch = (TextView) findViewById(R.id.editText_search_persona);
        btnOtros = (Button) findViewById(R.id.btn_otros);
        btnAgregar = (Button) findViewById(R.id.btn_agregar);
        layout_categorias = (ViewGroup) findViewById(R.id.layout_categorias);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        categorias = prefs.getString("categorias", "");
        arr = new ArrayList<>(Arrays.asList(categorias.split("/")));
        initial_arr = new ArrayList<String>();

        update_categorias();

        btnTodo.setOnClickListener(this);
        btnHermanos.setOnClickListener(this);
        btnTios.setOnClickListener(this);
        btnSobrinos.setOnClickListener(this);
        btnHijos.setOnClickListener(this);
        btnAmigos.setOnClickListener(this);
        btnVecinos.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnOtros.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
    }

    public void update_categorias(){
        categorias = prefs.getString("categorias", "");
        arr = new ArrayList<>(Arrays.asList(categorias.split("/")));

        for(final String s : arr){
            if(s != "" && !initial_arr.contains(s)){
                Button btn = new Button(this);
                btn.setTextSize(28);
                btn.setText(s);
                btn.setTextColor(getResources().getColor(android.R.color.white));
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,0,0,30);
                btn.setLayoutParams(lp);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAct(s);
                    }
                });
                layout_categorias.addView(btn);
                initial_arr.add(s);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        update_categorias();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_todo:
                getByTodo();
                break;
            case R.id.btn_hermanos:
                getByHermanos();
                break;
            case R.id.btn_tios:
                getByTios();
                break;
            case R.id.btn_sobrinos:
                getBySobrinos();
                break;
            case R.id.btn_hijos:
                getByHijos();
                break;
            case R.id.btn_amigos:
                getByAmigos();
                break;
            case R.id.btn_vecinos:
                getByVecinos();
                break;
            case R.id.imageButton_search:
                getBySearch();
                break;
            case R.id.btn_otros:
                startAct("Otros");
                break;
            case R.id.btn_agregar:
                Intent intent = new Intent(getApplicationContext(), AgregarCategoriaActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getByTodo() {
        startAct("Todos");
        Log.d(DEBUG_TAG, "getByTodo()");
    }
    private void getByHermanos() {
        startAct("Hermano");
        categoria="Hermano";
        Log.d(DEBUG_TAG, "getByHermanos()");
    }
    private void getByTios() {
        startAct("Tio");
        categoria="Tio";
        Log.d(DEBUG_TAG, "getByTios()");
    }
    private void getBySobrinos() {
        startAct("Sobrino");
        categoria="Sobrino";
        Log.d(DEBUG_TAG, "getBySobrinos()");
    }
    private void getByHijos() {
        startAct("Hijo");
        categoria="Hijo";
        Log.d(DEBUG_TAG, "getByHijos()");
    }
    private void getByAmigos() {
        startAct("Amigo");
        categoria="Amigo";
        Log.d(DEBUG_TAG, "getByAmigos()");
    }
    private void getByVecinos() {
        startAct("Vecino");
        categoria="Vecino";
        Log.d(DEBUG_TAG, "getByVecinos()");
    }
    private void getBySearch() {
        startAct("Search");
        categoria="Hermano";
        Log.d(DEBUG_TAG, "getBySearch()");
    }

    private void startAct(String category) {
        Intent intent = new Intent(getApplicationContext(), PersonasListActivity.class);
        intent.putExtra("Category", category);
        intent.putExtra("Search", tvSearch.getText().toString());
        startActivity(intent);
    }
}
