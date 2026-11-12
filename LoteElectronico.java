public class LoteElectronico extends Lote implements Etiquetable {

    private int equipos;

    public LoteElectronico(String codigo, String descripcion, int pesoKg, int equipos) {
        super(codigo, descripcion, pesoKg);
        if (equipos < 0) {
            throw new IllegalArgumentException("Los equipos no pueden ser negativos");
        }
        this.equipos = equipos;
    }

    @Override
    public long calcularValor() {
        return getPesoKg() * 1400L + equipos * 800L;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote: " + getCodigo() + " | Equipos completos: " + equipos;
    }
}
