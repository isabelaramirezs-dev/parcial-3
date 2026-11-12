import java.util.List;

public class PruebasEcoRuta {

    private static int pasadas = 0;
    private static int total = 0;

    public static void main(String[] args) {
        Empresa empresa = new Empresa("EcoRuta");
        empresa.registrarAportante(new Aportante("1001", "Ana Torres", "ana@correo.com"));

        prueba1(empresa);
        prueba2(empresa);
        prueba3(empresa);
        prueba4(empresa);
        prueba5(empresa);

        System.out.println("\n===== RESUMEN: " + pasadas + " de " + total + " verificaciones correctas =====");
    }

    private static void prueba1(Empresa empresa) {
        System.out.println("\n===== PRUEBA 1: Registro de una entrega =====");

        boolean creada = empresa.crearEntrega("E01", "1001");
        Entrega e01 = empresa.buscarEntrega("E01");
        verificar("Crear E01 con aportante 1001", "true", String.valueOf(creada));
        verificar("Estado de E01", "ABIERTA", String.valueOf(e01.getEstado()));
        verificar("Cantidad de lotes de E01", "0", String.valueOf(e01.getLotes().size()));
        verificar("Comprobante de E01", "null", String.valueOf(e01.getComprobante()));

        boolean repetida = empresa.crearEntrega("E01", "1001");
        verificar("Crear otra entrega con código E01 (repetido)", "false", String.valueOf(repetida));

        boolean desconocido = empresa.crearEntrega("E99", "9999");
        verificar("Crear E99 con aportante 9999 (desconocido)", "false", String.valueOf(desconocido));
        verificar("E99 no debe existir", "null", String.valueOf(empresa.buscarEntrega("E99")));

        verificar("E01 se conserva", "true", String.valueOf(empresa.buscarEntrega("E01") == e01));
    }

    private static void prueba2(Empresa empresa) {
        System.out.println("\n===== PRUEBA 2: Validación de lotes =====");

        empresa.crearEntrega("E02", "1001");
        Entrega e02 = empresa.buscarEntrega("E02");

        boolean ok = empresa.agregarLote("E02", new LotePapelCarton("L01", "Cajas", 5, false));
        verificar("Agregar L01 con 5 kg", "true", String.valueOf(ok));

        boolean repetido = empresa.agregarLote("E02", new LotePapelCarton("L01", "Otro", 3, false));
        verificar("Agregar otro lote con código L01 (repetido)", "false", String.valueOf(repetido));

        boolean pesoCero = empresa.agregarLote("E02", new LotePapelCarton("L02", "Sin peso", 0, false));
        verificar("Agregar L02 con 0 kg", "false", String.valueOf(pesoCero));

        List<Lote> lotes = e02.getLotes();
        verificar("Cantidad de lotes de E02", "1", String.valueOf(lotes.size()));
        verificar("Único lote conservado", "L01", lotes.get(0).getCodigo());
    }

    private static void prueba3(Empresa empresa) {
        System.out.println("\n===== PRUEBA 3: Rechazo de una entrega vacía =====");

        empresa.crearEntrega("E03", "1001");
        Entrega e03 = empresa.buscarEntrega("E03");

        boolean aceptada = empresa.aceptarEntrega("E03");
        verificar("Aceptar E03 sin lotes", "false", String.valueOf(aceptada));
        verificar("Estado de E03", "ABIERTA", String.valueOf(e03.getEstado()));
        verificar("Comprobante de E03", "null", String.valueOf(e03.getComprobante()));
    }

    private static void prueba4(Empresa empresa) {
        System.out.println("\n===== PRUEBA 4: Aceptación y cálculo con los tres tipos =====");

        empresa.crearEntrega("E04", "1001");
        Lote papel = new LotePapelCarton("L01", "Papel clasificado", 10, true);
        Lote metal = new LoteMetal("L02", "Aluminio", 5, TipoMetal.ALUMINIO);
        Lote electronico = new LoteElectronico("L03", "Electrónicos", 4, 2);

        empresa.agregarLote("E04", papel);
        empresa.agregarLote("E04", metal);
        empresa.agregarLote("E04", electronico);

        verificar("Valor del lote de papel y cartón (10 kg clasificado)", "6000", String.valueOf(papel.calcularValor()));
        verificar("Valor del lote de metal (5 kg aluminio)", "11000", String.valueOf(metal.calcularValor()));
        verificar("Valor del lote electrónico (4 kg, 2 equipos)", "7200", String.valueOf(electronico.calcularValor()));

        boolean aceptada = empresa.aceptarEntrega("E04");
        Entrega e04 = empresa.buscarEntrega("E04");
        Comprobante c = empresa.consultarComprobante("E04");

        verificar("Aceptar E04", "true", String.valueOf(aceptada));
        verificar("Estado de E04", "ACEPTADA", String.valueOf(e04.getEstado()));
        verificar("Peso total del comprobante", "19", String.valueOf(c.pesoTotal()));
        verificar("Valor total del comprobante", "24200", String.valueOf(c.valorTotal()));
    }

    private static void prueba5(Empresa empresa) {
        System.out.println("\n===== PRUEBA 5: Consulta de etiquetas =====");

        List<String> etiquetas = empresa.consultarEtiquetas("E04");
        verificar("Cantidad de etiquetas de E04", "2", String.valueOf(etiquetas.size()));

        String etiquetaMetal = etiquetas.get(0);
        String etiquetaElectronico = etiquetas.get(1);
        System.out.println("   Etiquetas obtenidas: " + etiquetas);

        verificar("Etiqueta de metal contiene código L02 y ALUMINIO", "true",
                String.valueOf(etiquetaMetal.contains("L02") && etiquetaMetal.contains("ALUMINIO")));
        verificar("Etiqueta electrónica contiene código L03 y 2 equipos", "true",
                String.valueOf(etiquetaElectronico.contains("L03") && etiquetaElectronico.contains("2")));
    }

    private static void verificar(String descripcion, String esperado, String obtenido) {
        total++;
        boolean ok = esperado.equals(obtenido);
        if (ok) {
            pasadas++;
        }
        System.out.println("- Entrada:   " + descripcion);
        System.out.println("  Esperado:  " + esperado);
        System.out.println("  Obtenido:  " + obtenido);
        System.out.println("  Resultado: " + (ok ? "CORRECTO" : "FALLÓ"));
    }
}
