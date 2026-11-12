import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Empresa {

    private String nombre;
    private Map<String, Aportante> aportantes;
    private Map<String, Entrega> entregas;

    public Empresa(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la empresa es obligatorio");
        }
        this.nombre = nombre;
        this.aportantes = new HashMap<>();
        this.entregas = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public boolean registrarAportante(Aportante aportante) {
        if (aportante == null || aportante.identificacion() == null
                || aportante.identificacion().isBlank()) {
            return false;
        }
        if (aportantes.containsKey(aportante.identificacion())) {
            return false;
        }
        aportantes.put(aportante.identificacion(), aportante);
        return true;
    }

    public boolean existeAportante(String identificacion) {
        return aportantes.containsKey(identificacion);
    }

    public boolean crearEntrega(String codigo, String identificacion) {
        if (codigo == null || codigo.isBlank() || identificacion == null) {
            return false;
        }
        if (entregas.containsKey(codigo)) {
            return false;
        }
        Aportante aportante = aportantes.get(identificacion);
        if (aportante == null) {
            return false;
        }
        entregas.put(codigo, new Entrega(codigo, LocalDate.now(), aportante));
        return true;
    }

    public boolean agregarLote(String codigoEntrega, Lote lote) {
        Entrega entrega = entregas.get(codigoEntrega);
        if (entrega == null) {
            return false;
        }
        return entrega.agregarLote(lote);
    }

    public boolean aceptarEntrega(String codigoEntrega) {
        Entrega entrega = entregas.get(codigoEntrega);
        if (entrega == null) {
            return false;
        }
        return entrega.aceptar();
    }

    public Comprobante consultarComprobante(String codigoEntrega) {
        Entrega entrega = entregas.get(codigoEntrega);
        if (entrega == null) {
            return null;
        }
        return entrega.getComprobante();
    }

    public List<String> consultarEtiquetas(String codigoEntrega) {
        Entrega entrega = entregas.get(codigoEntrega);
        if (entrega == null || entrega.getEstado() != EstadoEntrega.ACEPTADA) {
            return new LinkedList<>();
        }
        return entrega.obtenerEtiquetas();
    }

    public long valorAcumulado(String identificacion) {
        long total = 0;
        for (Entrega e : entregas.values()) {
            Comprobante c = e.getComprobante();
            if (c != null && c.identificacionAportante().equals(identificacion)) {
                total += c.valorTotal();
            }
        }
        return total;
    }

    public Entrega buscarEntrega(String codigo) {
        return entregas.get(codigo);
    }
}
