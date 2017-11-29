package itesm.mx.mipasadoenpresente;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Clase para manejar la vista de configurar el juego
 */
public class ConfigurarJuegoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_audio;
    Button btn_opcion1, btn_opcion2, btn_opcion3, btn_regresar;
    int audio = 1;
    SharedPreferences prefs;

    /**
     * onCreate
     * Inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_juego);

        btn_opcion1 = (Button) findViewById(R.id.btn_sonido1);
        btn_opcion2 = (Button) findViewById(R.id.btn_sonido2);
        btn_opcion3 = (Button) findViewById(R.id.btn_sonido3);
        btn_regresar = (Button) findViewById(R.id.btn_regresar);
        tv_audio = (TextView) findViewById(R.id.tv_audio);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        audio = prefs.getInt("audio", 1);

        updateAudioLabel();

        btn_opcion1.setOnClickListener(this);
        btn_opcion2.setOnClickListener(this);
        btn_opcion3.setOnClickListener(this);
        btn_regresar.setOnClickListener(this);
    }

    /**
     * Actualiza el texto de sonido actual
     */
    public void updateAudioLabel(){
        tv_audio.setText("Sonido actual: " + audio);
    }

    /**
     * Actualiza las preferencias del usuario con el nuevo sonido
     */
    public void updatePreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("audio",audio);
        editor.commit();
        Toast.makeText(this, "Se ha cambiado el sonido del juego",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Reproduce el sonido seleccionado
     */
    public void playAudio(){
        int id = R.raw.correct;
        switch(audio){
            case 1:
                id = R.raw.correct;
                break;
            case 2:
                id = R.raw.tada;
                break;
            case 3:
                id = R.raw.win;
                break;
        }
        MediaPlayer player;
        player = MediaPlayer.create(this, id);
        player.start();
    }

    /**
     * Maneja los clics en la pantalla
     * @param v la vista a la que se dio clic
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_sonido1:
                audio = 1;
                playAudio();
                updateAudioLabel();
                updatePreferences();
                break;
            case R.id.btn_sonido2:
                audio = 2;
                playAudio();
                updateAudioLabel();
                updatePreferences();
                break;
            case R.id.btn_sonido3:
                audio = 3;
                playAudio();
                updateAudioLabel();
                updatePreferences();
                break;
            case R.id.btn_regresar:
                finish();
                updatePreferences();
                break;
        }
    }
}
