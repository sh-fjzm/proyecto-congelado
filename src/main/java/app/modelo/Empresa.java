package app.modelo;

import app.modelo.Direccion;
import app.modelo.Email;
import app.modelo.Etiqueta;
import app.modelo.Telefono;

import java.util.List;

public class Empresa {
    private int id;
    private String nombre;
    private Direccion direccion;
    private Telefono telefono;
    private Email email;
    private String linkedinUrl;
    private List<Etiqueta> etiquetas;

    public Empresa(int id, String nombre, Direccion direccion, Telefono telefono, Email email, String linkedinUrl, List<Etiqueta> etiquetas) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.linkedinUrl = linkedinUrl;
        this.etiquetas = etiquetas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Direccion getDireccion() { return direccion; }
    public Telefono getTelefono() { return telefono; }
    public Email getEmail() { return email; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public List<Etiqueta> getEtiquetas() { return etiquetas; }

    @Override
    public String toString() {
        return "Empresa: " + nombre + "\n" +
                "Dirección: " + direccion + "\n" +
                "Teléfono: " + telefono + "\n" +
                "Email: " + email + "\n" +
                "LinkedIn: " + linkedinUrl + "\n" +
                "Etiquetas: " + etiquetas;
    }
}