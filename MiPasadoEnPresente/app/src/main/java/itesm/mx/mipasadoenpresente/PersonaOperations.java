package itesm.mx.mipasadoenpresente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jibril on 11/12/17.
 */

public class PersonaOperations {

    private SQLiteDatabase db;
    private PersonaDBHelper dbHelper;

    private static final String DEBUG_TAG = "PERSONA_OP";

    public PersonaOperations(Context context) { dbHelper = new PersonaDBHelper(context); }

    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("SQLOPEN", e.toString());
        }
    }

    public void close() { db.close(); }

    public long addPersona(Persona persona) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE, persona.getNombre());
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_CATEGORIA, persona.getCategoria());
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_FECHACUMPLEANOS, persona.getFecha_cumpleanos());
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_COMENTARIOS, persona.getComentarios());
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_IMAGENES, "Missing imagenes array");//CANNOT RESOLVE ARRAYLIST

            Log.w(DEBUG_TAG, "Missing to implement imagenes ArrayList.");//Needs Resolve IMAGENES.

            newRowId = db.insert(DataBaseSchema.PersonaTable.TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(DEBUG_TAG, "ErrorAddPersona: " +  e.toString());
        }
        return newRowId;
    }

    public void deletePersonas() { db.execSQL("delete from " + DataBaseSchema.PersonaTable.TABLE_NAME); }

    public ArrayList<Persona> getPersonasByCategory(String categoria) {

        ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

        String query = "Select * FROM " + DataBaseSchema.PersonaTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.PersonaTable.COLUMN_NAME_CATEGORIA +
                " = \"" + categoria + "\"";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Persona persona = new Persona(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            null
                    );
                    listaPersonas.add(persona);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorByCategoryList: " + e.toString());
        }
        return listaPersonas;
    }

    public ArrayList<Persona> getAllPersonas() {

        ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

        String query = "Select * FROM " + DataBaseSchema.PersonaTable.TABLE_NAME;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Persona persona = new Persona(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            null
                    );
                    listaPersonas.add(persona);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
        return listaPersonas;
    }
}
