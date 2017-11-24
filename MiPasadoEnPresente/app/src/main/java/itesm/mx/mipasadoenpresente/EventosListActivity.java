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
 * Created by juanc on 11/13/2017.
 */

public class EventosListActivity extends AppCompatActivity implements View.OnClickListener {

    private EventoOperations EventoOps;
    private ArrayList<Evento> EventosList;
    private Button btn_agregar;
    private ListView listViewEventos;
    private static final String DEBUG_TAG = "EVENTOS_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_list);

        btn_agregar = (Button) findViewById(R.id.btn_agregar);
        listViewEventos = (ListView) findViewById(R.id.list_eventos);

        EventoOps = new EventoOperations(this);
        EventoOps.open();

        setListViewEventos();
        btn_agregar.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setListViewEventos() {
        Bundle data = getIntent().getExtras();

        if (data != null) {
                    EventosList = EventoOps.getEventosByCategory(String.valueOf(data.get("Category")));

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

    @Override
    public void onResume() {
        EventoOps.open();
        setListViewEventos();
        super.onResume();
        Log.d(DEBUG_TAG, "onResume() has been called.");
    }

    @Override
    public void onPause() {
        EventoOps.close();
        super.onPause();
        Log.d(DEBUG_TAG, "onPause() has been called.");
    }

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
