package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class dificultadActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_facil, btn_intermedio, btn_dificil, btn_comenzar, btn_menos, btn_mas;
    TextView tv_cantPreguntas, tv_dificultad;
    int dificultad = 1;
    int cantPreguntas = 3;

    ArrayList<Persona> personas;
    PersonaOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dificultad);

        operations = new PersonaOperations(this);
        operations.open();

        personas = operations.getAllPersonas();

        btn_facil = (Button) findViewById(R.id.btn_facil);
        btn_intermedio = (Button) findViewById(R.id.btn_intermedio);
        btn_dificil = (Button) findViewById(R.id.btn_dificil);
        btn_comenzar = (Button) findViewById(R.id.btn_comenzar);
        btn_menos = (Button) findViewById(R.id.btn_menos);
        btn_mas = (Button) findViewById(R.id.btn_mas);
        tv_cantPreguntas = (TextView) findViewById(R.id.tv_cantpreguntas);
        tv_dificultad = (TextView) findViewById(R.id.tv_dificultad);

        tv_cantPreguntas.setText(String.valueOf(cantPreguntas));
        btn_facil.setOnClickListener(this);
        btn_intermedio.setOnClickListener(this);
        btn_dificil.setOnClickListener(this);
        btn_comenzar.setOnClickListener(this);
        btn_menos.setOnClickListener(this);
        btn_mas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_facil:
                dificultad = 1;
                tv_dificultad.setText("Dificultad: Fácil");
                break;
            case R.id.btn_intermedio:
                tv_dificultad.setText("Dificultad: Intermedia");
                dificultad = 2;
                break;
            case R.id.btn_dificil:
                tv_dificultad.setText("Dificultad: Difícil");
                dificultad = 3;
                break;
            case R.id.btn_comenzar:
                if(personas.size() < 4){
                    Toast.makeText(this, "Se necesitan al menos 4 personas o 4 eventos para jugar",
                            Toast.LENGTH_LONG).show();
                }else {
                    Intent juego_activity = new Intent(getApplicationContext(), juegoActivity.class);
                    juego_activity.putExtra("CantPreguntas", cantPreguntas);
                    juego_activity.putExtra("Dificultad", dificultad);
                    startActivity(juego_activity);
                }
                break;
            case R.id.btn_menos:
                if(cantPreguntas > 1 && cantPreguntas < 15){
                    cantPreguntas--;
                }
                tv_cantPreguntas.setText(String.valueOf(cantPreguntas));
                break;
            case R.id.btn_mas:

                if(cantPreguntas < 15){
                    cantPreguntas++;
                }
                tv_cantPreguntas.setText(String.valueOf(cantPreguntas));
                break;
        }
    }
}
