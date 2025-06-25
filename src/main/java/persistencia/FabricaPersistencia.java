package persistencia;

public class FabricaPersistencia {
    
    public static IFabricaPersistencia obtenerFabrica(String tipo, String usuario) {
        IFabricaPersistencia fabrica;
        GuardadorMensaje guardadorNuevo;
        GuardadorContacto guardadorContactoNuevo;

        switch (tipo.toLowerCase()) {
            case "json" -> {
                fabrica = new FabricaPersistenciaJson();
                guardadorNuevo = fabrica.crearGuardadorMensaje(usuario);
                guardadorContactoNuevo = fabrica.crearGuardadorContacto(usuario);
                SincronizadorPersistencia.sincronizar(usuario, "json", guardadorNuevo, guardadorContactoNuevo);
                return fabrica;
            }
            case "xml" -> {
                fabrica = new FabricaPersistenciaXml();
                guardadorNuevo = fabrica.crearGuardadorMensaje(usuario);
                guardadorContactoNuevo = fabrica.crearGuardadorContacto(usuario);
                SincronizadorPersistencia.sincronizar(usuario, "xml", guardadorNuevo, guardadorContactoNuevo);
                return fabrica;
            }
            case "texto plano" -> {
                fabrica = new FabricaPersistenciaTexto();
                guardadorNuevo = fabrica.crearGuardadorMensaje(usuario);
                guardadorContactoNuevo = fabrica.crearGuardadorContacto(usuario);
                SincronizadorPersistencia.sincronizar(usuario, "txt", guardadorNuevo, guardadorContactoNuevo);
                return fabrica;
            }
            default -> throw new IllegalArgumentException("Tipo de persistencia no soportado: " + tipo);
        }
    }
}