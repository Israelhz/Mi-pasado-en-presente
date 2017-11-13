package itesm.mx.mipasadoenpresente;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class PersonasListActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "PERSONAS_LIST";

    private PersonaOperations personaOps;
    private ArrayList<Persona> personasList;

    private ListView listViewPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas_list);

        listViewPersonas = (ListView) findViewById(R.id.list_personas);

        personaOps = new PersonaOperations(this);
        personaOps.open();

        setListViewPersonas();
    }

    private void setListViewPersonas() {
        Bundle data = getIntent().getExtras();

        if (data != null) {
            if (data.get("Category").equals("Todos"))
                personasList = personaOps.getAllPersonas();
            else
                personasList = personaOps.getPersonasByCategory(String.valueOf(data.get("Category")));
        }

        PersonaAdapter personasAdapter = new PersonaAdapter(this, personasList);
        listViewPersonas.setAdapter(personasAdapter);
    }

    @Override
    public void onResume() {
        personaOps.open();
        super.onResume();
        Log.d(DEBUG_TAG, "onResume() has been called.");
    }

    @Override
    public void onPause() {
        personaOps.close();
        super.onPause();
        Log.d(DEBUG_TAG, "onPause() has been called.");
    }
}
