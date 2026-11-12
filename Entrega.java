import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Entrega {

    private String codigo;
    private LocalDate fecha;
    private Aportante aportante;
    private EstadoEntrega estado;
    private List<Lote> lotes;
    private Comprobante comprobante;

    public Entrega(String codigo, LocalDate fecha, Aportante aportante) {
        if (codigo == null || codigo.isBlank() || fecha == null || aportante == null) {
            throw new IllegalArgumentException("Código, fecha y aportante son obligatorios");
        }
        this.codigo = codigo;
        this.fecha = fecha;
        this.aportante = aportante;
        this.estado = EstadoEntrega.ABIERTA;
        this.lotes = new LinkedList<>();
        this.comprobante = null;
    }

    public boolean agregarLote(Lote lote) {
        if (lote == null || estado != EstadoEntrega.ABIERTA) {
            return false;
        }
        if (lote.getPesoKg() <= 0) {
            return false;
        }
        // evita que el peso total desborde un int
        if ((long) calcularPesoTotal() + lote.getPesoKg() > Integer.MAX_VALUE) {
            return false;
        }
        for (Lote l : lotes) {
            if (l.getCodigo().equals(lote.getCodigo())) {
                return false;
            }
        }
        lotes.add(lote);
        return true;
    }

    public boolean aceptar() {
        if (estado == EstadoEntrega.ACEPTADA || lotes.isEmpty()) {
            return false;
        }
        comprobante = new Comprobante(codigo, aportante.identificacion(),
                calcularPesoTotal(), calcularValorTotal());
        estado = EstadoEntrega.ACEPTADA;
        return true;
    }

    public int calcularPesoTotal() {
        int total = 0;
        for (Lote l : lotes) {
            total += l.getPesoKg();
        }
        return total;
    }

    public long calcularValorTotal() {
        long total = 0;
        for (Lote l : lotes) {
            total += l.calcularValor();
        }
        return total;
    }

    public List<String> obtenerEtiquetas() {
        List<String> etiquetas = new LinkedList<>();
        for (Lote l : lotes) {
            if (l instanceof Etiquetable) {
                etiquetas.add(((Etiquetable) l).generarEtiqueta());
            }
        }
        return etiquetas;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Aportante getAportante() {
        return aportante;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public List<Lote> getLotes() {
        return new LinkedList<>(lotes);
    }

    public Comprobante getComprobante() {
        return comprobante;
    }
}
