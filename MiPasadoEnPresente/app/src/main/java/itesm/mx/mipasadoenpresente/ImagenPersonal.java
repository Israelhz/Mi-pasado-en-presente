package itesm.mx.mipasadoenpresente;

/**
 * Created by israel on 05/11/2017.
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
