package persistencia;

public class FabricaPersistencia {

    public static IFabricaPersistencia obtenerFabrica(String formato) {
        if (formato == null) {
            throw new IllegalArgumentException("El formato no puede ser nulo");
        }
        switch (formato.toLowerCase()) {
            case "json" -> {
                System.out.println("Creo una fabrica json");
                return new FabricaPersistenciaJson();
            }
            case "xml" -> {
                System.out.println("Creo una fabrica xml");
                return new FabricaPersistenciaXml();
            }
            case "texto plano" -> {
                System.out.println("Creo una fabrica de txt");
                return new FabricaPersistenciaTexto();
            }
            default -> throw new IllegalArgumentException("Formato desconocido: " + formato);
        }
    }
}