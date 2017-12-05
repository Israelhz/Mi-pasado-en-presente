/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
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
 * Clase para realizar operaciones sobre la base de datos de personas
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

    /** Añade una nueva persona a la base de datos **/
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

    /**
     * Elimina base de datos de personas
     */
    public void deletePersonas() {
//        db.execSQL("delete from " + DataBaseSchema.PersonaTable.TABLE_NAME);
        db.execSQL("drop table " + DataBaseSchema.PersonaTable.TABLE_NAME);
        db.execSQL("drop database Persona");
    }

    /**
     * Obtiene a las personas de una categoria dada
     * @param categoria
     * @return
     */
    public ArrayList<Persona> getPersonasByCategory(String categoria) {

        ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

        String query = "Select * FROM " + DataBaseSchema.PersonaTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.PersonaTable.COLUMN_NAME_CATEGORIA +
                " = \"" + categoria + "\"" + " ORDER BY " + DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE + " ASC";
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
                            cursor.getString(5)
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

    /**
     * Obtiene todas las personas de la base de datos
     * @return
     */
    public ArrayList<Persona> getAllPersonas() {

        ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

        String query = "Select * FROM " + DataBaseSchema.PersonaTable.TABLE_NAME + " ORDER BY " + DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE + " ASC";
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
                            cursor.getString(5)
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

    /**
     * Obtiene a las personas que su nombre incluya el nombre que se pasa como parámetro
     * @param name
     * @return
     */
    public ArrayList<Persona> getPersonasBySearch(String name) {

        ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

        String query = "Select * FROM " + DataBaseSchema.PersonaTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE +
                "  LIKE \'%" + name + "%\'" + " ORDER BY " + DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE + " ASC";
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
                            cursor.getString(5)
                    );

                    listaPersonas.add(persona);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
//        Log.e("SQLSEARCH", query);
//        Log.e("SQLSEARCH", " " + listaPersonas.get(0).getNombre());

        return listaPersonas;
    }

    /**
     * Elimina una persona por su nombre
     * @param idPersona
     * @return
     */
    public void deleteEvento(long idPersona){
        String query="DELETE FROM "+DataBaseSchema.PersonaTable.TABLE_NAME+
                " WHERE " + DataBaseSchema.PersonaTable._ID +
                " = \"" + idPersona + "\"";

        db.execSQL(query);

        query = "DELETE FROM " + DataBaseSchema.PersonaImagenTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IDPERSONA +
                " = \"" + idPersona + "\"";

        db.execSQL(query);
    }

    public void deleteImagens(long idPersona) {
        String query;
        query = "DELETE FROM " + DataBaseSchema.PersonaImagenTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IDPERSONA +
                " = \"" + idPersona + "\"";

        db.execSQL(query);
    }
    /**
     * Obtiene una persona por medio de su id
     * @param id
     * @return
     */
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
                            cursor.getString(5)
                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
        return persona;
    }

    /**
     * Actualiza los datos de una persona incluyendo sus imagenes
     * @param id
     * @param persona
     */
    public void updatePersona(long id, Persona persona){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_NOMBRE,persona.getNombre());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_CATEGORIA,persona.getCategoria());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_FECHACUMPLEANOS,persona.getFecha_cumpleanos());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_COMENTARIOS,persona.getComentarios());
        cv.put(DataBaseSchema.PersonaTable.COLUMN_NAME_AUDIO,persona.getAudio());

        deleteImagens(id);//Avoids duplication of images.
        for(byte[] imagen : persona.getImagenes()){
                addPersonaImagen(imagen, id);
        }
        db.update(DataBaseSchema.PersonaTable.TABLE_NAME, cv, "ROWID=" + id, null);
        Log.d("UPDATE", "UPDATED!");
    }

    /**
     * Añade una nueva imagen a la persona
     * @param imagen
     * @param idPersona
     * @return
     */
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

    /**
     * Verifica si la imagen ya existe
     * @param imagen
     * @param idPersona
     * @return
     */
    public Boolean existsPersonaImagen(byte[] imagen, long idPersona) {

        String query = "Select * FROM " + DataBaseSchema.PersonaImagenTable.TABLE_NAME + " WHERE " + DataBaseSchema.PersonaImagenTable.COLUMN_NAME_IDPERSONA + "=" + idPersona;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    if(imagen == cursor.getBlob(2))
                        return true;
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
        return false;
    }

    /**
     * Obtiene todas las imagenes de una persona
     * @param idPersona
     * @return
     */
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
