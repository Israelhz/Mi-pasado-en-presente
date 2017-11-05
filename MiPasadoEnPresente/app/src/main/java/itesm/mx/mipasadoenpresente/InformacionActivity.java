package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InformacionActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_editar;
    TextView tv_nombre;
    TextView tv_fecha;
    TextView tv_comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_editar = (Button) findViewById(R.id.btn_editar);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);
        tv_comentario = (TextView) findViewById(R.id.tv_comentario);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String name = prefs.getString("nombre", "No se ha definido");//"No name defined" is the default value.
        String fecha = prefs.getString("fecha", "No se ha definido"); //0 is the default value.
        String comentario = prefs.getString("comentarios", "No se ha definido");
        tv_nombre.setText(name);
        tv_fecha.setText(fecha);
        tv_comentario.setText(comentario);




        btn_editar.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_editar:
                Intent editar_info_activity = new Intent(getApplicationContext(), EditarInfoActivity.class);
                startActivity(editar_info_activity);
                break;
        }
    }
}
