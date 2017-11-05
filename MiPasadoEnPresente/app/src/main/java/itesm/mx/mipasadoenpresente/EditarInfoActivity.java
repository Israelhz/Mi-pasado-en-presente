package itesm.mx.mipasadoenpresente;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditarInfoActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_guardar;
    EditText et_nombre;
    EditText et_fecha;
    EditText et_comentario;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_fecha = (EditText) findViewById(R.id.et_fecha);
        et_comentario = (EditText) findViewById(R.id.et_comentario);


        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String name = prefs.getString("nombre", "");//"No name defined" is the default value.
        String fecha = prefs.getString("fecha", ""); //0 is the default value.
        String comentario = prefs.getString("comentarios", "");
        et_nombre.setText(name);
        et_fecha.setText(fecha);
        et_comentario.setText(comentario);

        btn_guardar.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
                break;
        }
    }
}
