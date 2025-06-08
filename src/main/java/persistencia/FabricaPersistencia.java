package persistencia;

public class FabricaPersistencia {
    
    public static IFabricaPersistencia obtenerFabrica(String tipo, String usuario) {
        GuardadorMensaje guardadorNuevo;
        IFabricaPersistencia fabrica;

        switch (tipo.toLowerCase()) {
            case "json" -> {
                fabrica = new FabricaPersistenciaJson();
                guardadorNuevo = fabrica.crearGuardadorMensaje(usuario);
                SincronizadorPersistencia.sincronizar(usuario, "json", guardadorNuevo);
                return fabrica;
            }
            case "xml" -> {
                fabrica = new FabricaPersistenciaXml();
                guardadorNuevo = fabrica.crearGuardadorMensaje(usuario);
                SincronizadorPersistencia.sincronizar(usuario, "xml", guardadorNuevo);
                return fabrica;
            }
            case "texto plano" -> {
                fabrica = new FabricaPersistenciaTexto();
                guardadorNuevo = fabrica.crearGuardadorMensaje(usuario);
                SincronizadorPersistencia.sincronizar(usuario, "txt", guardadorNuevo);
                return fabrica;
            }
            default -> throw new IllegalArgumentException("Tipo de persistencia no soportado: " + tipo);
        }
    }
}