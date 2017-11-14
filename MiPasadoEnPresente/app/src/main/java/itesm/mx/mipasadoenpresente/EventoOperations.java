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
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_PERSONASASOCIADAS,"Faltan personas asociadas");
            values.put(DataBaseSchema.EventoTable.COLUMN_NAME_IMAGENES,"Faltan imagenes");
            newRowId= db.insert(DataBaseSchema.EventoTable.TABLE_NAME,null,values);
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

    public Evento findEvento(String EventoName){
        String query= "SELECT * FROM "+DataBaseSchema.EventoTable.TABLE_NAME+" WHERE "+
                DataBaseSchema.EventoTable.COLUMN_NAME_NOMBRE +
                " = \""+ EventoName+ "\"";
        try{
            Cursor cursor = db.rawQuery(query,null);
            evento=null;
            if(cursor.moveToFirst()){
                evento = new  Evento(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        null,
                        null);

            };
            cursor.close();
        }catch(SQLiteException e){
            Log.e("SQLFIND",e.toString());
        }
        return evento;
    }

    public ArrayList<Evento> getAllEventos() {

        ArrayList<Evento> listaEventos = new ArrayList<Evento>();

        String query = "Select * FROM " + DataBaseSchema.EventoTable.TABLE_NAME;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Evento evento = new  Evento(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            null,
                            null);

                    listaEventos.add(evento);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e("SQLFIND", "ErrorAllList: " + e.toString());
        }
        return listaEventos;
    }

}
