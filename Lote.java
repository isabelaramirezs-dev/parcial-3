public abstract class Lote {

    private String codigo;
    private String descripcion;
    private int pesoKg;

    public Lote(String codigo, String descripcion, int pesoKg) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del lote es obligatorio");
        }
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.pesoKg = pesoKg;
    }

    public abstract long calcularValor();

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPesoKg() {
        return pesoKg;
    }
}
