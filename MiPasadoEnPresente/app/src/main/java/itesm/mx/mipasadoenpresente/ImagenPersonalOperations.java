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
 * Clase para manejar la base de datos de imagenes personales
 */
public class ImagenPersonalOperations {

    private SQLiteDatabase db;
    private ImagenPersonalDBHelper dbHelper;
    private ImagenPersonal imagenPersonal;

    public ImagenPersonalOperations(Context context) { dbHelper = new ImagenPersonalDBHelper(context); }
    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("SQLOPEN", e.toString());
        }

    }

    public void close() { db.close(); }

    public long addImagenPersonal(ImagenPersonal imagenPersonal) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.ImagenPersonalTable.COLUMN_NAME_IMAGEN, imagenPersonal.getImagen());

            newRowId = db.insert(DataBaseSchema.ImagenPersonalTable.TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e("SQLADD", e.toString());
        }
        return newRowId;
    }

    public boolean deleteImagenPersonal(String imagen) {
        boolean result = false;
        String query = "Select * FROM " + DataBaseSchema.ImagenPersonalTable.TABLE_NAME +
                " WHERE " + DataBaseSchema.ImagenPersonalTable.COLUMN_NAME_IMAGEN +
                " = \"" + imagen + "\"";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int id = Integer.parseInt(cursor.getString(0));
                db.delete(DataBaseSchema.ImagenPersonalTable.TABLE_NAME,
                        DataBaseSchema.ImagenPersonalTable._ID + " = ?",
                        new String[]{String.valueOf(id)});
                result = true;
            }
            cursor.close();
        } catch (SQLiteException e){
            Log.e("SQLDELETE", e.toString());
        }
        return result;
    }

    public ArrayList<ImagenPersonal> getAllImagenesPersonales() {
        ArrayList<ImagenPersonal> listaImagenes = new ArrayList<ImagenPersonal>();
        String selectQuery = "SELECT * FROM " + DataBaseSchema.ImagenPersonalTable.TABLE_NAME;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    imagenPersonal = new ImagenPersonal(Integer.parseInt(cursor.getString(0)), cursor.getBlob(1));
                    listaImagenes.add(imagenPersonal);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("SQList", e.toString());
        }
        return listaImagenes;
    }
}
