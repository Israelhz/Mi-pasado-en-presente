/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;

import java.util.ArrayList;

/**
 * Clase para manejar a las personas
 */
public class Persona {

    private long id;
    private String nombre;
    private String categoria;
    private String fecha_cumpleanos;
    private String comentarios;
    private ArrayList<byte[]> imagenes;
    private String audio;

    public Persona(String nombre, String categoria, String fecha_cumpleanos, String comentarios, ArrayList<byte[]> imagenes, String audio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.fecha_cumpleanos = fecha_cumpleanos;
        this.comentarios = comentarios;
        this.imagenes = imagenes;
        this.audio = audio;
    }

    public Persona(long id, String nombre, String categoria, String fecha_cumpleanos, String comentarios, ArrayList<byte[]> imagenes, String audio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.fecha_cumpleanos = fecha_cumpleanos;
        this.comentarios = comentarios;
        this.imagenes = imagenes;
        this.audio = audio;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

}
