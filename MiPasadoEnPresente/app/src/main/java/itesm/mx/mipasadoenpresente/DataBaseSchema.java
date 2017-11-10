package itesm.mx.mipasadoenpresente;

import android.provider.BaseColumns;

/**
 * Created by israel on 05/11/2017.
 */

public final class DataBaseSchema {
    private DataBaseSchema() {}

    public static class PersonaTable implements BaseColumns {
        public static final String TABLE_NAME = "Persona";
        public static final String COLUMN_NAME_NOMBRE = "Nombre";
        public static final String COLUMN_NAME_CATEGORIA = "Categoria";
        public static final String COLUMN_NAME_FECHACUMPLEANOS = "FechaCumpleanos";
        public static final String COLUMN_NAME_COMENTARIOS = "Comentarios";
        public static final String COLUMN_NAME_IMAGENES = "Imagenes";
    }

    public static class EventoTable implements BaseColumns {
        public static final String TABLE_NAME = "Evento";
        public static final String COLUMN_NAME_NOMBRE = "Nombre";
        public static final String COLUMN_NAME_CATEGORIA = "Categoria";
        public static final String COLUMN_NAME_LUGAR = "Lugar";
        public static final String COLUMN_NAME_FECHA = "Fecha";
        public static final String COLUMN_NAME_DESCRIPCION = "Descripcion";
        public static final String COLUMN_NAME_COMENTARIOS = "Comentarios";
        public static final String COLUMN_NAME_PERSONASASOCIADAS = "PersonasAsociadas";
        public static final String COLUMN_NAME_IMAGENES = "Imagenes";
    }

    public static class CategoriaTable implements BaseColumns {
        public static final String TABLE_NAME = "Categoria";
        public static final String COLUMN_NAME_NOMBRE = "Nombre";
    }

    public static class ImagenPersonalTable implements BaseColumns {
        public static final String TABLE_NAME = "ImagenPersonal";
        public static final String COLUMN_NAME_IMAGEN = "Imagen";
    }
}