/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
 */

package itesm.mx.mipasadoenpresente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_informacion;
    Button btn_personas;
    Button btn_eventos;
    Button btn_juegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_informacion = (Button) findViewById(R.id.btn_info);
        btn_personas = (Button) findViewById(R.id.btn_personas);
        btn_eventos = (Button) findViewById(R.id.btn_eventos);
        btn_juegos = (Button) findViewById(R.id.btn_juego);

        btn_informacion.setOnClickListener(this);
        btn_personas.setOnClickListener(this);
        btn_eventos.setOnClickListener(this);
        btn_juegos.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_info:
                Intent info_activity = new Intent(getApplicationContext(), InformacionActivity.class);
                startActivity(info_activity);
                break;
            case R.id.btn_personas:
                Intent personas_activity = new Intent(getApplicationContext(), CategoriasPersonasActivity.class);
                startActivity(personas_activity);
                break;
            case R.id.btn_eventos:
                Intent eventos_activity = new Intent(getApplicationContext(), CategoriasEventosActivity.class);
                startActivity(eventos_activity);
                break;
            case R.id.btn_juego:
                Intent juegos_activity = new Intent(getApplicationContext(), dificultadActivity.class);
                startActivity(juegos_activity);
                break;
        }

    }
}
