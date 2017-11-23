package itesm.mx.mipasadoenpresente;

import java.util.ArrayList;

/**
 * Created by israel on 05/11/2017.
 */

public class Persona {

    private long id;
    private String nombre;
    private String categoria;
    private String fecha_cumpleanos;
    private String comentarios;
    private ArrayList<byte[]> imagenes;
    private byte[] audio;

    public Persona(String nombre, String categoria, String fecha_cumpleanos, String comentarios, ArrayList<byte[]> imagenes) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.fecha_cumpleanos = fecha_cumpleanos;
        this.comentarios = comentarios;
        this.imagenes = imagenes;
        this.audio = null;
    }

    public Persona(long id, String nombre, String categoria, String fecha_cumpleanos, String comentarios, ArrayList<byte[]> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.fecha_cumpleanos = fecha_cumpleanos;
        this.comentarios = comentarios;
        this.imagenes = imagenes;
        this.audio = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha_cumpleanos() {
        return fecha_cumpleanos;
    }

    public void setFecha_cumpleanos(String fecha_cumpleanos) {
        this.fecha_cumpleanos = fecha_cumpleanos;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<byte[]> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<byte[]> imagenes) {
        this.imagenes = imagenes;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

}
