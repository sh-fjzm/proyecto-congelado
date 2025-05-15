package app.modelo;

public class EmpresaEtiqueta {
    private int idEmpresa;
    private int idEtiqueta;

    public EmpresaEtiqueta(int idEmpresa, int idEtiqueta) {
        this.idEmpresa = idEmpresa;
        this.idEtiqueta = idEtiqueta;
    }

    public int getIdEmpresa() { return idEmpresa; }
    public int getIdEtiqueta() { return idEtiqueta; }

    @Override
    public String toString() {
        return "Empresa ID: " + idEmpresa + " - Etiqueta ID: " + idEtiqueta;
    }
}