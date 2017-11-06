package itesm.mx.mipasadoenpresente;

import java.util.ArrayList;

/**
 * Created by israel on 05/11/2017.
 */

public class Evento {

    private long id;
    private String nombre;
    private String categoria;
    private String lugar;
    private String fecha;
    private String descripcion;
    private String comentarios;
    private ArrayList<String> personas_asociadas;
    private ArrayList<byte[]> imagenes;

    public Evento(long id, String nombre, String categoria, String lugar, String fecha, String descripcion, String comentarios, ArrayList<String> personas_asociadas, ArrayList<byte[]> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.lugar = lugar;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.comentarios = comentarios;
        this.personas_asociadas = personas_asociadas;
        this.imagenes = imagenes;
    }

    public Evento(String nombre, String categoria, String lugar, String fecha, String descripcion, String comentarios, ArrayList<String> personas_asociadas, ArrayList<byte[]> imagenes) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.lugar = lugar;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.comentarios = comentarios;
        this.personas_asociadas = personas_asociadas;
        this.imagenes = imagenes;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<String> getPersonas_asociadas() {
        return personas_asociadas;
    }

    public void setPersonas_asociadas(ArrayList<String> personas_asociadas) {
        this.personas_asociadas = personas_asociadas;
    }

    public ArrayList<byte[]> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<byte[]> imagenes) {
        this.imagenes = imagenes;
    }


}
