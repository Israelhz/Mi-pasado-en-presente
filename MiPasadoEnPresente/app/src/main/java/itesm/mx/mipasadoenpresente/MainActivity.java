package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_informacion;
    Button btn_personas;
    Button btn_eventos;
    Button btn_juegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_informacion = (Button) findViewById(R.id.btn_info);
        btn_personas = (Button) findViewById(R.id.btn_personas);
        btn_eventos = (Button) findViewById(R.id.btn_eventos);
        btn_juegos = (Button) findViewById(R.id.btn_juego);

        btn_informacion.setOnClickListener(this);
        btn_personas.setOnClickListener(this);
        btn_eventos.setOnClickListener(this);
        btn_juegos.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_info:
                Intent info_activity = new Intent(getApplicationContext(), InformacionActivity.class);
                startActivity(info_activity);
                break;
            case R.id.btn_personas:
                Intent personas_activity = new Intent(getApplicationContext(), CategoriasPersonasActivity.class);
                startActivity(personas_activity);
                break;
            case R.id.btn_eventos:
                Intent eventos_activity = new Intent(getApplicationContext(), CategoriasEventosActivity.class);
                startActivity(eventos_activity);
                break;
            case R.id.btn_juego:
                Intent juegos_activity = new Intent(getApplicationContext(), dificultadActivity.class);
                startActivity(juegos_activity);
                break;
        }

    }
}
