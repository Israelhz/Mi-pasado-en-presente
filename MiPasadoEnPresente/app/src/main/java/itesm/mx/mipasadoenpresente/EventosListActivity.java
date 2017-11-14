package itesm.mx.mipasadoenpresente;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by juanc on 11/13/2017.
 */

public class EventosListActivity extends AppCompatActivity {

    private EventoOperations EventoOps;
    private ArrayList<Evento> EventosList;

    private ListView listViewEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_list);

        listViewEventos = (ListView) findViewById(R.id.list_eventos);

        EventoOps = new EventoOperations(this);
        EventoOps.open();

        setListViewEventos();
    }

    private void setListViewEventos() {
        Bundle data = getIntent().getExtras();

        if (data != null) {
                EventosList = EventoOps.getAllEventos();

        }

        EventoAdapter eventosAdapter = new EventoAdapter(this, EventosList);
        listViewEventos.setAdapter(eventosAdapter);
    }
}
