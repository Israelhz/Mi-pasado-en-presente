/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by israel on 07/11/2017.
 */

public class ImagenPersonalDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ImagenPersonalDB.db";
    private static final int DATABASE_VERSION = 1;

    public ImagenPersonalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGEN_PERSONAL_TABLE = "CREATE TABLE " +
                DataBaseSchema.ImagenPersonalTable.TABLE_NAME +
                "(" +
                DataBaseSchema.ImagenPersonalTable._ID + " INTEGER PRIMARY KEY," +
                DataBaseSchema.ImagenPersonalTable.COLUMN_NAME_IMAGEN + " BLOB" +
                ")";
        Log.i("ImagenPersonal onCreate", CREATE_IMAGEN_PERSONAL_TABLE);
        db.execSQL(CREATE_IMAGEN_PERSONAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DELETE_PRODUCTS_TABLE = "DROP TABLE IF EXISTS " +
                DataBaseSchema.ImagenPersonalTable.TABLE_NAME;
        db.execSQL(DELETE_PRODUCTS_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

