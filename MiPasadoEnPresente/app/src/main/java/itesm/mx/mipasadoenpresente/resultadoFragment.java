package itesm.mx.mipasadoenpresente;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class resultadoFragment extends Fragment implements View.OnClickListener {

    private static final String CORRECTAS_TAG = "correctas";
    private static final String TOTAL_TAG = "total";
    Button btn_volver, btn_salir;
    TextView tv_resultado;
    int correctas,total;

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

    @Override
    public void onStart(){
        super.onStart();

        if(getArguments() != null){
            correctas = getArguments().getInt(CORRECTAS_TAG);
            total = getArguments().getInt(TOTAL_TAG);

            tv_resultado.setText(correctas + "/" + total);
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
