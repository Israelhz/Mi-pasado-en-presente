package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CategoriasPersonasActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "CATEGORIAS_PERSONAS";

    private Button btnTodo;
    private Button btnHermanos;
    private Button btnTios;
    private Button btnSobrinos;
    private Button btnHijos;
    private Button btnAmigos;
    private Button btnVecinos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_personas);

        Log.e(DEBUG_TAG, "ERROR: Missing Images byte array");

        btnTodo = (Button) findViewById(R.id.btn_todo);
        btnHermanos = (Button) findViewById(R.id.btn_hermanos);
        btnTios = (Button) findViewById(R.id.btn_tios);
        btnSobrinos = (Button) findViewById(R.id.btn_sobrinos);
        btnHijos = (Button) findViewById(R.id.btn_hijos);
        btnAmigos = (Button) findViewById(R.id.btn_amigos);
        btnVecinos = (Button) findViewById(R.id.btn_vecinos);

        btnTodo.setOnClickListener(this);
        btnHermanos.setOnClickListener(this);
        btnTios.setOnClickListener(this);
        btnSobrinos.setOnClickListener(this);
        btnHijos.setOnClickListener(this);
        btnAmigos.setOnClickListener(this);
        btnVecinos.setOnClickListener(this);

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
            default:
                break;
        }
    }

    private void getByTodo() {
        startAct("Todos");
        Log.d(DEBUG_TAG, "getByTodo()");
    }
    private void getByHermanos() {
        startAct("Hermanos");
        Log.d(DEBUG_TAG, "getByHermanos()");
    }
    private void getByTios() {
        startAct("Tios");
        Log.d(DEBUG_TAG, "getByTios()");
    }
    private void getBySobrinos() {
        startAct("Sobrinos");
        Log.d(DEBUG_TAG, "getBySobrinos()");
    }
    private void getByHijos() {
        startAct("Hijos");
        Log.d(DEBUG_TAG, "getByHijos()");
    }
    private void getByAmigos() {
        startAct("Amigos");
        Log.d(DEBUG_TAG, "getByAmigos()");
    }
    private void getByVecinos() {
        startAct("Vecinos");
        Log.d(DEBUG_TAG, "getByVecinos()");
    }

    private void startAct(String category) {
        Intent intent = new Intent(getApplicationContext(), PersonasListActivity.class);
        intent.putExtra("Category", category);
        startActivity(intent);
    }
}
