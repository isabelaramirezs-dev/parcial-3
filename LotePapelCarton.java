public class LotePapelCarton extends Lote {

    private boolean clasificado;

    public LotePapelCarton(String codigo, String descripcion, int pesoKg, boolean clasificado) {
        super(codigo, descripcion, pesoKg);
        this.clasificado = clasificado;
    }

    @Override
    public long calcularValor() {
        long valorPorKg = 500;
        if (clasificado) {
            valorPorKg += 100;
        }
        return getPesoKg() * valorPorKg;
    }
}
