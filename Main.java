import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static Empresa empresa = new Empresa("EcoRuta");

    public static void main(String[] args) {
        empresa.registrarAportante(new Aportante("1001", "Ana Torres", "ana@correo.com"));
        empresa.registrarAportante(new Aportante("1002", "Supermercado Central", "central@correo.com"));
        empresa.registrarAportante(new Aportante("1003", "Colegio San José", "colegio@correo.com"));

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1:
                    crearEntrega();
                    break;
                case 2:
                    agregarLote();
                    break;
                case 3:
                    aceptarEntrega();
                    break;
                case 4:
                    consultarComprobante();
                    break;
                case 5:
                    consultarEtiquetas();
                    break;
                case 6:
                    consultarValorAcumulado();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== " + empresa.getNombre().toUpperCase() + " =====");
        System.out.println("1. Crear entrega");
        System.out.println("2. Agregar lote a una entrega");
        System.out.println("3. Aceptar entrega");
        System.out.println("4. Consultar comprobante");
        System.out.println("5. Consultar etiquetas");
        System.out.println("6. Consultar valor acumulado por aportante");
        System.out.println("0. Salir");
    }

    private static void crearEntrega() {
        String codigo = leerTexto("Código de la entrega: ");
        String identificacion = leerTexto("Identificación del aportante: ");

        if (codigo.isEmpty()) {
            System.out.println("Rechazado: el código de la entrega no puede estar vacío.");
        } else if (empresa.buscarEntrega(codigo) != null) {
            System.out.println("Rechazado: el código de entrega ya existe.");
        } else if (!empresa.existeAportante(identificacion)) {
            System.out.println("Rechazado: identificación de aportante desconocida.");
        } else if (empresa.crearEntrega(codigo, identificacion)) {
            System.out.println("Entrega " + codigo + " creada en estado ABIERTA.");
        } else {
            System.out.println("Rechazado: no se pudo crear la entrega.");
        }
    }

    private static void agregarLote() {
        String codigoEntrega = leerTexto("Código de la entrega: ");
        Entrega entrega = empresa.buscarEntrega(codigoEntrega);
        if (entrega == null) {
            System.out.println("Rechazado: la entrega no existe.");
            return;
        }
        if (entrega.getEstado() == EstadoEntrega.ACEPTADA) {
            System.out.println("Rechazado: la entrega ya fue aceptada.");
            return;
        }

        System.out.println("Tipo de lote: 1. Papel y cartón  2. Metal  3. Residuo electrónico");
        int tipo = leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo de lote no válido.");
            return;
        }

        String codigoLote = leerTexto("Código del lote: ");
        if (codigoLote.isEmpty()) {
            System.out.println("Rechazado: el código del lote no puede estar vacío.");
            return;
        }
        String descripcion = leerTexto("Descripción: ");
        int peso = leerEntero("Peso en kg: ");
        if (peso <= 0) {
            System.out.println("Rechazado: el peso debe ser un entero positivo.");
            return;
        }

        Lote lote = null;
        if (tipo == 1) {
            String respuesta = leerTexto("¿Está clasificado? (s/n): ");
            boolean clasificado = respuesta.equalsIgnoreCase("s");
            lote = new LotePapelCarton(codigoLote, descripcion, peso, clasificado);
        } else if (tipo == 2) {
            int tipoMetal = leerEntero("Metal: 1. ALUMINIO  2. ACERO: ");
            if (tipoMetal == 1) {
                lote = new LoteMetal(codigoLote, descripcion, peso, TipoMetal.ALUMINIO);
            } else if (tipoMetal == 2) {
                lote = new LoteMetal(codigoLote, descripcion, peso, TipoMetal.ACERO);
            } else {
                System.out.println("Tipo de metal no válido.");
                return;
            }
        } else {
            int equipos = leerEntero("Equipos completos (>= 0): ");
            if (equipos < 0) {
                System.out.println("Rechazado: los equipos no pueden ser negativos.");
                return;
            }
            lote = new LoteElectronico(codigoLote, descripcion, peso, equipos);
        }

        if (empresa.agregarLote(codigoEntrega, lote)) {
            System.out.println("Lote " + codigoLote + " agregado a la entrega " + codigoEntrega + ".");
        } else {
            System.out.println("Rechazado: el código de lote ya existe en la entrega o el peso total sería demasiado grande.");
        }
    }

    private static void aceptarEntrega() {
        String codigo = leerTexto("Código de la entrega: ");
        Entrega entrega = empresa.buscarEntrega(codigo);
        if (entrega == null) {
            System.out.println("Rechazado: la entrega no existe.");
        } else if (entrega.getEstado() == EstadoEntrega.ACEPTADA) {
            System.out.println("Rechazado: la entrega ya fue aceptada.");
        } else if (empresa.aceptarEntrega(codigo)) {
            System.out.println("Entrega aceptada.");
            mostrarComprobante(empresa.consultarComprobante(codigo));
        } else {
            System.out.println("Rechazado: la entrega no tiene lotes.");
        }
    }

    private static void consultarComprobante() {
        String codigo = leerTexto("Código de la entrega: ");
        Comprobante c = empresa.consultarComprobante(codigo);
        if (c == null) {
            System.out.println("No hay comprobante para esa entrega.");
        } else {
            mostrarComprobante(c);
        }
    }

    private static void mostrarComprobante(Comprobante c) {
        System.out.println("Entrega: " + c.codigoEntrega());
        System.out.println("Aportante: " + c.identificacionAportante());
        System.out.println("Peso total: " + c.pesoTotal() + " kg");
        System.out.println("Valor total: $" + c.valorTotal());
    }

    private static void consultarEtiquetas() {
        String codigo = leerTexto("Código de la entrega: ");
        List<String> etiquetas = empresa.consultarEtiquetas(codigo);
        if (etiquetas.isEmpty()) {
            System.out.println("No hay etiquetas (la entrega no existe, no está aceptada o no tiene lotes etiquetables).");
        } else {
            for (String e : etiquetas) {
                System.out.println(e);
            }
        }
    }

    private static void consultarValorAcumulado() {
        String identificacion = leerTexto("Identificación del aportante: ");
        System.out.println("Valor acumulado: $" + empresa.valorAcumulado(identificacion));
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero.");
            }
        }
    }
}
