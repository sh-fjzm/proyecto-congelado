package app.modelo;

public class Telefono {
    private int id;
    private String telefono;

    public Telefono(int id, String telefono) {
        this.id = id;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public String getTelefono() { return telefono; }

    @Override
    public String toString() {
        return telefono;
    }
}