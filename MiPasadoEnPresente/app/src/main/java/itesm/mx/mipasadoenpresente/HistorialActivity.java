/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistorialActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences prefs;
    ViewGroup layout_records;
    Button btn_regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout_records = (ViewGroup) findViewById(R.id.layout_records);
        btn_regresar = (Button) findViewById(R.id.btn_regresar);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String historial = prefs.getString("historial", "[]");

        TextView header = new TextView(this);
        header.setTextSize(20);
        header.setText("Preguntas \n correctas       Fecha          Dificultad");
        header.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout_records.addView(header);
        JSONArray records = new JSONArray();
        try {
            records = new JSONArray(historial);
            for(int i = 0; i < records.length(); i++){
                JSONObject obj = records.getJSONObject(i);
                if(obj.has("puntuacion") && obj.has("fecha") && obj.has("dificultad")){
                    TextView bt = new TextView(this);
                    bt.setTextSize(20);
                    bt.setText(obj.get("puntuacion") + "             " + obj.get("fecha") + "        " + obj.get("dificultad"));
                    bt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout_records.addView(bt);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_regresar.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_regresar:
                finish();
                break;
        }
    }
}
