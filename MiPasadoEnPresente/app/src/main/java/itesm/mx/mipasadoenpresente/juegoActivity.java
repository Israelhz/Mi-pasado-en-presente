/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Clase para la vista de juego
 */
public class juegoActivity extends AppCompatActivity {

    int cantPreguntas, dificultad;
    Boolean personas, eventos;

    /**
     * Inicializa vista con un fragmento de pregunta
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            cantPreguntas = (Integer) data.get("CantPreguntas");
            dificultad = (Integer) data.get("Dificultad");
            personas = (Boolean) data.get("Personas");
            eventos = (Boolean) data.get("Eventos");
        }

        if (findViewById(R.id.fragment_container) != null){
            juego1_fragment juego = juego1_fragment.newInstance(cantPreguntas,1, dificultad, 0, personas, eventos);

            FragmentTransaction transact = getSupportFragmentManager().beginTransaction();
            transact.replace(R.id.fragment_container, juego, "TAG_JUEGO");
            transact.commit();
        }
    }

    /**
     * Añade funcionalidad al botón de Back en la barra de la app
     * @return
     */
    @Override
    public void onBackPressed(){
        finish();
        Intent dificultad_activity = new Intent(getApplicationContext(), dificultadActivity.class);
        startActivity(dificultad_activity);
    }

}
