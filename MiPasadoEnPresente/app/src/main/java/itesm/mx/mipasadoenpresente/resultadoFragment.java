package itesm.mx.mipasadoenpresente;


import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;
import static itesm.mx.mipasadoenpresente.R.id.et_fecha;
import static itesm.mx.mipasadoenpresente.R.id.et_nombre;


/**
 * A simple {@link Fragment} subclass.
 */
public class resultadoFragment extends Fragment implements View.OnClickListener {

    private static final String CORRECTAS_TAG = "correctas";
    private static final String TOTAL_TAG = "total";
    Button btn_volver, btn_salir;
    TextView tv_resultado;
    int correctas,total;
    SharedPreferences prefs;

    public resultadoFragment() {
        // Required empty public constructor
    }

    public static resultadoFragment newInstance(int correctas, int total){
        resultadoFragment fragment = new resultadoFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(CORRECTAS_TAG, correctas);
        bundle.putInt(TOTAL_TAG, total);
        fragment.setArguments(bundle);

        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart(){
        super.onStart();
        prefs = getActivity().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        if(getArguments() != null){
            correctas = getArguments().getInt(CORRECTAS_TAG);
            total = getArguments().getInt(TOTAL_TAG);

            tv_resultado.setText(correctas + "/" + total);

            JSONObject record = new JSONObject();
            JSONArray arr = new JSONArray();
            try {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());

                String historial = prefs.getString("historial", "[]");

                arr = new JSONArray(historial);

                record.put("puntuacion", correctas + "/" + total);
                record.put("fecha", formattedDate);

                arr.put(record);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("historial",arr.toString());
            editor.commit();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultado, container, false);

        btn_volver = (Button) view.findViewById(R.id.btn_volver);
        btn_salir = (Button) view.findViewById(R.id.btn_salir);
        tv_resultado = (TextView) view.findViewById(R.id.tv_resultado);
        btn_volver.setOnClickListener(this);
        btn_salir.setOnClickListener(this);
        return view ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_volver:
                Intent dificultad_activity = new Intent(getContext(), dificultadActivity.class);
                startActivity(dificultad_activity);
                break;
            case R.id.btn_salir:
                Intent menu_intent = new Intent(getContext(), MainActivity.class);
                startActivity(menu_intent);
                break;
        }
    }
}
