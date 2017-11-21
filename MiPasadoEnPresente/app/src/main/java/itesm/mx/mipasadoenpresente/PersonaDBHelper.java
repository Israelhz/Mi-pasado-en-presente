package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jibril on 11/12/17.
 */

public class PersonaDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Persona.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DEBUG_TAG = "PERSONA_DBHELPER";

    public PersonaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PERSONA_TABLE = "CREATE TABLE " +
                DataBaseSchema.PersonaTable.TABLE_NAME +
                "(" +
                DataBaseSchema.PersonaTable._ID + " INTEGER PRIMARY KEY," +
                DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE + " TEXT," +
                DataBaseSchema.PersonaTable.COLUMN_NAME_CATEGORIA + " TEXT," +
                DataBaseSchema.PersonaTable.COLUMN_NAME_FECHACUMPLEANOS + " TEXT," +
                DataBaseSchema.PersonaTable.COLUMN_NAME_COMENTARIOS + " TEXT, " +
                DataBaseSchema.PersonaTable.COLUMN_NAME_AUDIO + " BLOB" +
                ")";

        Log.i(DEBUG_TAG, CREATE_PERSONA_TABLE);
        db.execSQL(CREATE_PERSONA_TABLE);

        String CREATE_PERSONAIMAGEN_TABLE = "CREATE TABLE " +
                DataBaseSchema.PersonaImagenTable.TABLE_NAME +
                "(" +
                DataBaseSchema.PersonaImagenTable._ID + " INTEGER PRIMARY KEY," +
                DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IDPERSONA + " TEXT," +
                DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IMAGEN + " BLOB" +
                ")";

        Log.i(DEBUG_TAG, CREATE_PERSONAIMAGEN_TABLE);
        db.execSQL(CREATE_PERSONAIMAGEN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DELETE_PERSONA_TABLE = "DROP TABLE IF EXISTS " +
                DataBaseSchema.PersonaTable.TABLE_NAME;
        db.execSQL(DELETE_PERSONA_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
