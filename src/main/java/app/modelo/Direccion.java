package app.modelo;

public class Direccion {
    private int id;
    private String pais;
    private String ccaa;
    private String provincia;
    private String ciudad;
    private String calle;
    private int cp;

    public Direccion(int id, String pais, String ccaa, String provincia, String ciudad, String calle, int cp) {
        this.id = id;
        this.pais = pais;
        this.ccaa = ccaa;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.calle = calle;
        this.cp = cp;
    }

    public int getId() { return id; }
    public String getPais() { return pais; }
    public String getCcaa() { return ccaa; }
    public String getProvincia() { return provincia; }
    public String getCiudad() { return ciudad; }
    public String getCalle() { return calle; }
    public int getCp() { return cp; }

    @Override
    public String toString() {
        return calle + "\n" +
                cp + " " +
                ciudad + "\n" +
                provincia + " " +
                ccaa + "\n" +
                pais;
    }
}