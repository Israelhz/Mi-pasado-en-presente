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

public class AgregarCategoriaActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_categoria;
    Button btn_agregar;
    SharedPreferences prefs;
    String categorias;
    ArrayList<String> arr_categorias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_categoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        btn_agregar = (Button) findViewById(R.id.btn_agregar);
        et_categoria = (EditText) findViewById(R.id.et_categoria);

        categorias = prefs.getString("categorias", "");

        arr_categorias = new ArrayList<>(Arrays.asList(categorias.split(",")));

        btn_agregar.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agregar:
                if(!et_categoria.getText().toString().equals("")){
                    SharedPreferences.Editor editor = prefs.edit();
                    arr_categorias.add(et_categoria.getText().toString());
                    editor.putString("categorias",arr_categorias.toString());
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
