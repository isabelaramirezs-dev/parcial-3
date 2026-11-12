public class LoteMetal extends Lote implements Etiquetable {

    private TipoMetal tipoMetal;

    public LoteMetal(String codigo, String descripcion, int pesoKg, TipoMetal tipoMetal) {
        super(codigo, descripcion, pesoKg);
        if (tipoMetal == null) {
            throw new IllegalArgumentException("El tipo de metal es obligatorio");
        }
        this.tipoMetal = tipoMetal;
    }

    @Override
    public long calcularValor() {
        if (tipoMetal == TipoMetal.ALUMINIO) {
            return getPesoKg() * 2200L;
        }
        return getPesoKg() * 900L;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote: " + getCodigo() + " | Metal: " + tipoMetal;
    }
}
