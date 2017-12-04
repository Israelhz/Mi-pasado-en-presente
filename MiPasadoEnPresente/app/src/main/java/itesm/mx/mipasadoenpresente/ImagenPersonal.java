/*
*    Mi-pasado-en-presente
*    Copyright (C) 2017
*
*    Full disclosure can be found at the LICENSE file in the root folder of the GitHub repository.
*/
package itesm.mx.mipasadoenpresente;


/**
 * Clase para manejar las imagenes del usuario
 */
public class ImagenPersonal {
    private long id;
    private byte[] imagen;

    public ImagenPersonal(long id, byte[] imagen) {
        this.id = id;
        this.imagen = imagen;
    }

    public ImagenPersonal(byte[] imagen) {
        this.imagen = imagen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }



}
