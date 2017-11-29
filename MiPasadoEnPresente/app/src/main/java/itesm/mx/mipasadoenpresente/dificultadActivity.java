package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Clase para manejar la vista de seleccionar dificultad del juego
 */
public class dificultadActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_facil, btn_intermedio, btn_dificil, btn_comenzar, btn_menos, btn_mas, btn_configurar, btn_historial;
    TextView tv_cantPreguntas, tv_dificultad;
    int dificultad = 1;
    int cantPreguntas = 3;
    CheckBox check_personas, check_eventos;

    ArrayList<Persona> personas;
    ArrayList<Evento> eventos;
    EventoOperations eventoOperations;
    PersonaOperations operations;

    /**
     * onCreate
     * Inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dificultad);

        operations = new PersonaOperations(this);
        operations.open();

        eventoOperations = new EventoOperations(this);
        eventoOperations.open();

        personas = operations.getAllPersonas();
        eventos = eventoOperations.getAllEventos();

        btn_facil = (Button) findViewById(R.id.btn_facil);
        btn_intermedio = (Button) findViewById(R.id.btn_intermedio);
        btn_dificil = (Button) findViewById(R.id.btn_dificil);
        btn_comenzar = (Button) findViewById(R.id.btn_comenzar);
        btn_menos = (Button) findViewById(R.id.btn_menos);
        btn_mas = (Button) findViewById(R.id.btn_mas);
        tv_cantPreguntas = (TextView) findViewById(R.id.tv_cantpreguntas);
        tv_dificultad = (TextView) findViewById(R.id.tv_dificultad);
        btn_configurar = (Button) findViewById(R.id.btn_configurar);
        check_personas = (CheckBox) findViewById(R.id.check_personas);
        check_eventos = (CheckBox) findViewById(R.id.check_eventos);
        btn_historial = (Button) findViewById(R.id.btn_historial);

        tv_cantPreguntas.setText(String.valueOf(cantPreguntas));
        btn_facil.setOnClickListener(this);
        btn_intermedio.setOnClickListener(this);
        btn_dificil.setOnClickListener(this);
        btn_comenzar.setOnClickListener(this);
        btn_menos.setOnClickListener(this);
        btn_mas.setOnClickListener(this);
        btn_configurar.setOnClickListener(this);
        btn_historial.setOnClickListener(this);
    }

    /**
     * Maneja los clics en la pantalla
     * @param v
     */
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
            case R.id.btn_comenzar: // Verifica que existan al menos 4 personas o 4 eventos para jugar
                Boolean juego_personas = check_personas.isChecked();
                Boolean juego_eventos = check_eventos.isChecked();
                Boolean datos_validos = false;
                String message = "";
                if(juego_personas && !juego_eventos){
                    message = "Se necesitan al menos 4 personas para jugar";
                    datos_validos = personas.size() >= 4;
                }

                if(!juego_personas && juego_eventos){
                    message = "Se necesitan al menos 4 eventos para jugar";
                    datos_validos = eventos.size() >= 4;
                }

                if(juego_personas && juego_eventos){
                    message = "Se necesitan al menos 4 personas y 4 eventos para jugar";
                    datos_validos = personas.size() >= 4 && eventos.size() >= 4;
                }

                if(!juego_eventos && !juego_personas){
                    message = "Seleccione si desea practicar personas o eventos";
                    datos_validos = false;
                }

                if(!datos_validos){
                    Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
                }else {
                    Intent juego_activity = new Intent(getApplicationContext(), juegoActivity.class);
                    juego_activity.putExtra("CantPreguntas", cantPreguntas);
                    juego_activity.putExtra("Dificultad", dificultad);
                    juego_activity.putExtra("Personas", juego_personas);
                    juego_activity.putExtra("Eventos", juego_eventos);
                    startActivity(juego_activity);
                }
                break;
            case R.id.btn_menos: // Reduce la cantidad de preguntas
                if(cantPreguntas > 1 && cantPreguntas < 15){
                    cantPreguntas--;
                }
                tv_cantPreguntas.setText(String.valueOf(cantPreguntas));
                break;
            case R.id.btn_mas: // Aumenta la cantidad de preguntas

                if(cantPreguntas < 15){
                    cantPreguntas++;
                }
                tv_cantPreguntas.setText(String.valueOf(cantPreguntas));
                break;
            case R.id.btn_configurar: // Inicializa actividad de configurar
                Intent configurar_activity = new Intent(getApplicationContext(), ConfigurarJuegoActivity.class);
                startActivity(configurar_activity);
                break;
            case R.id.btn_historial: // Inicializa actividad de historial
                Intent historial_activity = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(historial_activity);

                break;
        }
    }
}
