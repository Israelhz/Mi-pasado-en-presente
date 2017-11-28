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

public class CategoriasEventosActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String DEBUG_TAG = "CATEGORIAS_EVENTOS";

    private Button btnPersonales;
    private Button btnEpoca;
    private ImageButton btnSearch;
    private TextView tvSearch;

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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_personales:
                getByPersonales();
                break;
            case R.id.btn_epoca:
                getByEpoca();
                break;
            case R.id.imageButton_search:
                getBySearch();
                break;
        }
    }

    private void getByPersonales() {
        startAct("Personales");
        Log.d(DEBUG_TAG, "getByPersonales()");
    }
    private void getByEpoca() {
        startAct("Epoca");
        Log.d(DEBUG_TAG, "getByEpoca()");
    }

    private void getBySearch() {
        startAct("Search");
        Log.d(DEBUG_TAG, "getBySearch()");
    }

    private void startAct(String category) {
        Intent intent = new Intent(getApplicationContext(), EventosListActivity.class);
        intent.putExtra("Category", category);
        intent.putExtra("Search", tvSearch.getText().toString());
        startActivity(intent);
    }
}
