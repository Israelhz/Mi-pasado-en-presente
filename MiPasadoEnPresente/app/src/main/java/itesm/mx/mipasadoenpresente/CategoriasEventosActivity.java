package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CategoriasEventosActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String DEBUG_TAG = "CATEGORIAS_EVENTOS";

    private Button btnPersonales;
    private Button btnEpoca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_eventos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.e(DEBUG_TAG, "ERROR: Missing Images byte array");
        btnPersonales= (Button) findViewById(R.id.btn_personales);
        btnEpoca = (Button) findViewById(R.id.btn_epoca);

        btnPersonales.setOnClickListener(this);
        btnEpoca.setOnClickListener(this);
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
            default:
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

    private void startAct(String category) {
        Intent intent = new Intent(getApplicationContext(), EventosListActivity.class);
        intent.putExtra("Category", category);
        startActivity(intent);
    }
}
