package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by juanc on 11/7/2017.
 */

public class EventoDBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME="EventoDB.db";
    private static final int DATABASE_VERSION=1;

    public EventoDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_EVENTO_TABLE= "CREATE TABLE "+
                DataBaseSchema.EventoTable.TABLE_NAME+
                "("+
                DataBaseSchema.EventoTable._ID+" INTEGER PRIMARY KEY,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_NOMBRE +" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_CATEGORIA+" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_LUGAR+" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_FECHA+" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_DESCRIPCION+" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_COMENTARIOS+" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_PERSONASASOCIADAS+" TEXT,"+
                DataBaseSchema.EventoTable.COLUMN_NAME_AUDIO + " TEXT" +
                ")";

        Log.i("Eventohelper onCreate", CREATE_EVENTO_TABLE);
        db.execSQL(CREATE_EVENTO_TABLE);

        String CREATE_EVENTOIMAGEN_TABLA = "CREATE TABLE " +
                DataBaseSchema.EventoImagenTable.TABLE_NAME +
                "(" +
                DataBaseSchema.EventoImagenTable._ID + " INTEGER PRIMARY KEY," +
                DataBaseSchema.EventoImagenTable.COLUMN_NAME_IDEVENTO + " TEXT," +
                DataBaseSchema.EventoImagenTable.COLUMN_NAME_IMAGEN + " BLOB" +
                ")";

        Log.i("Eventohelper onCreate", CREATE_EVENTOIMAGEN_TABLA);
        db.execSQL(CREATE_EVENTOIMAGEN_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String DELETE_EVENTO_TABLE = "DROP TABLE IF EXISTS"+ DataBaseSchema.EventoTable.TABLE_NAME;
        db.execSQL(DELETE_EVENTO_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
