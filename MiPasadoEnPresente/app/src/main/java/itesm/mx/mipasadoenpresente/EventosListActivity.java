package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Clase para manejar la lista de eventos
 */
public class EventosListActivity extends AppCompatActivity implements View.OnClickListener {

    private EventoOperations EventoOps;
    private ArrayList<Evento> EventosList;
    private Button btn_agregar;
    private ListView listViewEventos;
    private static final String DEBUG_TAG = "EVENTOS_LIST";

    /**
     * Inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_list);
        // Despliega el botón de Back en action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_agregar = (Button) findViewById(R.id.btn_agregar);
        listViewEventos = (ListView) findViewById(R.id.list_eventos);

        EventoOps = new EventoOperations(this);
        EventoOps.open();

        setListViewEventos();
        btn_agregar.setOnClickListener(this);
    }

    /**
     * Agrega funcionalidad al botón de back
     * @return
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * Obtiene información del intent y despliega la lista de eventos correspondiente
     */
    private void setListViewEventos() {
        Bundle data = getIntent().getExtras();

        if (data != null) {
            if (data.get("Category").equals("Search")) {
                EventosList = EventoOps.getEventosBySearch(String.valueOf(data.get("Search")));
            }else{
                EventosList = EventoOps.getEventosByCategory(String.valueOf(data.get("Category")));
            }
        }
        EventoAdapter eventosAdapter = new EventoAdapter(this, EventosList);
        listViewEventos.setAdapter(eventosAdapter);
        listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EventoInfoActivity.class);
                intent.putExtra("ID", EventosList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * Actualiza lista de eventos al volver a la actividad
     *
     */
    @Override
    public void onResume() {
        EventoOps.open();
        setListViewEventos();
        super.onResume();
        Log.d(DEBUG_TAG, "onResume() has been called.");
    }

    /**
     * Cierra las operaciones mientras este en pausa
     */
    @Override
    public void onPause() {
        EventoOps.close();
        super.onPause();
        Log.d(DEBUG_TAG, "onPause() has been called.");
    }

    /**
     * Maneja el evento de clic en agregar evento
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_agregar:
                Intent intent = new Intent(getApplicationContext(), EditEventoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
