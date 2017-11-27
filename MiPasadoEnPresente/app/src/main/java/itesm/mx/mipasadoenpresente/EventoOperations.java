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
 * Created by juanc on 11/7/2017.
 */

public class EventoOperations {
    private SQLiteDatabase db;
    private EventoDBHelper dbHelper;
    private Evento evento;
    private static final String DEBUG_TAG = "EVENTO_OP";

    public EventoOperations(Context context) {dbHelper= new EventoDBHelper(context);}

    public void open() throws SQLException {
        try{
            db= dbHelper.getWritableDatabase();
        } catch (SQLException e){
            Log.e("SQLOPEN",e.toString());
        }
    }
    public void close(){
        db.close();
    }

    public long addEvento(Evento evento){
        long newRowId=0;
        try{
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_NOMBRE,evento.getNombre());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_CATEGORIA,evento.getCategoria());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_LUGAR,evento.getLugar());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_FECHA,evento.getFecha());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_DESCRIPCION,evento.getDescripcion());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_COMENTARIOS,evento.getComentarios());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_PERSONASASOCIADAS,evento.getPersonas_asociadas());
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_AUDIO,evento.getAudio());

            newRowId= db.insert(DataBaseSchema.EventoTable.TABLE_NAME,null,values);

            ArrayList<byte[]> list_imagenes = evento.getImagenes();
            for(byte[] imagen : list_imagenes){
                addEventoImagen(imagen, newRowId);
            }
        }catch(SQLException e){
            Log.e("SQLADD",e.toString());
        }
        return newRowId;
    }

    public boolean deleteEvento(String EventoName){
        boolean result=false;

        String query="SELECT * FROM "+DataBaseSchema.EventoTable.TABLE_NAME+
                " WHERE " + DataBaseSchema.EventoTable.COLUMN_NAME_NOMBRE +
                " = \"" + EventoName + "\"";

        try{
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                int id = Integer.parseInt(cursor.getString(0));
                db.delete(DataBaseSchema.EventoTable.TABLE_NAME,DataBaseSchema.EventoTable._ID + "= ?", new String[]{String.valueOf(id)});
                result = true;
            }
            cursor.close();
        } catch(SQLiteException e){
            Log.e("SQLDELETE",e.toString());
        }
        return result;
    }

    public ArrayList<Evento> getAllEventos() {

        ArrayList<Evento> listaEventos = new ArrayList<Evento>();

        String query = "Select * FROM " + DataBaseSchema.EventoTable.TABLE_NAME;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    long idEvento = Integer.parseInt(cursor.getString(0));
                    ArrayList<byte[]> imagenes = getImagenes(idEvento);
                    Evento evento = new  Evento(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            imagenes,
                            cursor.getString(8)
                    );

                    listaEventos.add(evento);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e("SQLFIND", "ErrorAllList: " + e.toString());
        }
        return listaEventos;
    }

    public ArrayList<Evento> getEventosByCategory(String categoria) {

        ArrayList<Evento> listaEventos = new ArrayList<Evento>();

        String query = "Select * FROM " + DataBaseSchema.EventoTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.EventoTable.COLUMN_NAME_CATEGORIA +
                " = \"" + categoria + "\"";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    long idEvento = Integer.parseInt(cursor.getString(0));
                    ArrayList<byte[]> imagenes = getImagenes(idEvento);
                    Evento evento = new Evento(
                            Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            imagenes,
                            cursor.getString(8)
                    );
                    listaEventos.add(evento);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorByCategoryList: " + e.toString());
        }
        return listaEventos;
    }

    public Evento getEvento(long id) {
        Evento evento = null;
        String query = "Select * FROM " + DataBaseSchema.EventoTable.TABLE_NAME + " WHERE ROWID=" + id ;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    long idEvento = Integer.parseInt(cursor.getString(0));
                    ArrayList<byte[]> imagenes = getImagenes(idEvento);
                    evento = new Evento(
                            Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            imagenes,
                            cursor.getString(8)

                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e(DEBUG_TAG, "ErrorAllList: " + e.toString());
        }
        return evento;
    }

    public void updateEvento(long id, Evento evento){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_NOMBRE,evento.getNombre());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_CATEGORIA,evento.getCategoria());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_LUGAR,evento.getLugar());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_FECHA,evento.getFecha());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_DESCRIPCION,evento.getDescripcion());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_COMENTARIOS,evento.getComentarios());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_PERSONASASOCIADAS,evento.getPersonas_asociadas());
        cv.put(DataBaseSchema.EventoTable.COLUMN_NAME_AUDIO,evento.getAudio());
        db.update(DataBaseSchema.EventoTable.TABLE_NAME, cv, "ROWID=" + id, null);
        Log.d("UPDATE", "UPDATED!");
    }

    public long addEventoImagen(byte[] imagen, long idEvento) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.EventoImagenTable.COLUMN_NAME_IDEVENTO, idEvento);
            values.put(DataBaseSchema.EventoImagenTable.COLUMN_NAME_IMAGEN, imagen);

            newRowId = db.insert(DataBaseSchema.EventoImagenTable.TABLE_NAME, null, values);

        } catch (SQLException e) {
            Log.e("SQLADD", e.toString());
        }
        return newRowId;
    }

    public ArrayList<byte[]> getImagenes(long idEvento) {
        ArrayList<byte[]> imagenes = new ArrayList<byte[]>();
        long id = 0;
        String query = "Select * FROM " + DataBaseSchema.EventoImagenTable.TABLE_NAME + " WHERE " + DataBaseSchema.EventoImagenTable.COLUMN_NAME_IDEVENTO + "=" + idEvento;
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
