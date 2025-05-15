package app.modelo;

public class Etiqueta {
    private int id;
    private String etiqueta;

    public Etiqueta(int id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public int getId() { return id; }
    public String getEtiqueta() { return etiqueta; }

    @Override
    public String toString() {
        return etiqueta;
    }
}