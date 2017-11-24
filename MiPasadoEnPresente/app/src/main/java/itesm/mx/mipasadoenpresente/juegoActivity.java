package itesm.mx.mipasadoenpresente;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class juegoActivity extends AppCompatActivity {

    int cantPreguntas;
    int dificultad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        Bundle data = getIntent().getExtras();

        if (data != null) {
            cantPreguntas = (Integer) data.get("CantPreguntas");
            dificultad = (Integer) data.get("Dificultad");
        }

        if (findViewById(R.id.fragment_container) != null){
            juego1_fragment juego = juego1_fragment.newInstance(cantPreguntas,1, dificultad, 0);

            FragmentTransaction transact = getSupportFragmentManager().beginTransaction();
            transact.replace(R.id.fragment_container, juego, "TAG_JUEGO");
            transact.commit();
        }
    }
}
