package itesm.mx.mipasadoenpresente;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class PersonasListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "PERSONAS_LIST";

    private PersonaOperations personaOps;
    private ArrayList<Persona> personasList;
    private Button btn_agregar;
    private ListView listViewPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas_list);

        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_agregar = (Button) findViewById(R.id.btn_agregar);
        listViewPersonas = (ListView) findViewById(R.id.list_personas);

        personaOps = new PersonaOperations(this);
        personaOps.open();
//        personaOps.deletePersonas();
        setListViewPersonas();
        btn_agregar.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PersonaInfoActivity.class);//To View mode
//                Intent intent = new Intent(getApplicationContext(), EditPersonaActivity.class);//Edit mode
                intent.putExtra("ID", personasList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        personaOps.open();
        setListViewPersonas();
        super.onResume();
        Log.d(DEBUG_TAG, "onResume() has been called.");
    }

    @Override
    public void onPause() {
        personaOps.close();
        super.onPause();
        Log.d(DEBUG_TAG, "onPause() has been called.");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_agregar:
                Intent intent = new Intent(getApplicationContext(), EditPersonaActivity.class);
                startActivity(intent);
                break;
        }
    }
}
