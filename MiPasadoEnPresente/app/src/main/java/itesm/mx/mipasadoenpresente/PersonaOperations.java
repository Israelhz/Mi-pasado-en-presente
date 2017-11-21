package itesm.mx.mipasadoenpresente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jibril on 11/12/17.
 */

public class PersonaOperations {

    private SQLiteDatabase db;
    private PersonaDBHelper dbHelper;
    private static final String DEBUG_TAG = "PERSONA_OP";

    public PersonaOperations(Context context) {
        dbHelper = new PersonaDBHelper(context);
    }

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
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_AUDIO, persona.getAudio());

            newRowId = db.insert(DataBaseSchema.PersonaTable.TABLE_NAME, null, values);

            ArrayList<byte[]> list_imagenes = persona.getImagenes();
            for(byte[] imagen : list_imagenes){
                addPersonaImagen(imagen, newRowId);
            }
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
                    long idPersona = Integer.parseInt(cursor.getString(0));
                    ArrayList<byte[]> imagenes = getImagenes(idPersona);
                    Persona persona = new Persona(
                            idPersona,
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            imagenes,
                            cursor.getBlob(5)
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
                    long idPersona = Integer.parseInt(cursor.getString(0));
                    ArrayList<byte[]> imagenes = getImagenes(idPersona);
                    Persona persona = new Persona(
                            idPersona,
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            imagenes,
                            cursor.getBlob(5)
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

    public Persona getPersona(long id) {
        Persona persona = null;
        String query = "Select * FROM " + DataBaseSchema.PersonaTable.TABLE_NAME + " WHERE ROWID=" + id ;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    long idPersona = Integer.parseInt(cursor.getString(0));
                    ArrayList<byte[]> imagenes = getImagenes(idPersona);
                    persona = new Persona(
                            idPersona,
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            imagenes,
                            cursor.getBlob(5)
                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
        return persona;
    }

    public void updatePersona(long id, Persona persona){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE,persona.getNombre());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_CATEGORIA,persona.getCategoria());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_FECHACUMPLEANOS,persona.getFecha_cumpleanos());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_COMENTARIOS,persona.getComentarios());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_AUDIO,persona.getAudio());
        db.update(DataBaseSchema.PersonaTable.TABLE_NAME, cv, "ROWID=" + id, null);
        Log.d("UPDATE", "UPDATED!");
    }

    public long addPersonaImagen(byte[] imagen, long idPersona) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IDPERSONA, idPersona);
            values.put(DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IMAGEN, imagen);

            newRowId = db.insert(DataBaseSchema.PersonaImagenTable.TABLE_NAME, null, values);

        } catch (SQLException e) {
            Log.e("SQLADD", e.toString());
        }
        return newRowId;
    }

    public void addPersonaAudio(byte[] audio, long idPersona) {
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.PersonaTable.COLUMN_NAME_AUDIO, audio);
            db.update(DataBaseSchema.PersonaTable.TABLE_NAME, values, "ROWID=" + idPersona, null);
        } catch (SQLException e) {
            Log.e("SQLUPDATE", e.toString());
        }
    }

    public ArrayList<byte[]> getImagenes(long idPersona) {
        ArrayList<byte[]> imagenes = new ArrayList<byte[]>();
        long id = 0;
        String query = "Select * FROM " + DataBaseSchema.PersonaImagenTable.TABLE_NAME + " WHERE " + DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IDPERSONA + "=" + idPersona;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    imagenes.add(cursor.getBlob(2));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
        return imagenes;
    }

}
