package persistencia;

import cliente.modelo.IMensaje;
import java.io.File;
import java.util.List;
import java.util.Map;

public class SincronizadorPersistencia {

    public static void sincronizar(String usuario, String nuevaExtension, GuardadorMensaje guardadorNuevo) {
        String[] extensiones = {"json", "xml", "txt"};

        for (String ext : extensiones) {
            if (!ext.equals(nuevaExtension)) {
                File archivoViejo = new File("persistencia/mensajes" + usuario + "." + ext);
                if (archivoViejo.exists()) {
                    System.out.println("[SYNC] Migrando mensajes de ." + ext + " a ." + nuevaExtension);
                    GuardadorMensaje guardadorViejo = crearGuardadorTemporal(ext, usuario);
                    if (guardadorViejo != null) {
                        Map<String, List<IMensaje>> mensajes = guardadorViejo.cargarMensajes();
                        for (List<IMensaje> lista : mensajes.values()) {
                            for (IMensaje m : lista) {
                                guardadorNuevo.guardarMensaje(
                                    m.getNombreEmisor(),
                                    m.getIpEmisor(),
                                    m.getMensaje(),
                                    m.getHora(),
                                    m.getReceptor()
                                );
                            }
                        }

                        if (archivoViejo.delete()) {
                            System.out.println("[SYNC] Archivo eliminado: " + archivoViejo.getPath());
                        } else {
                            System.out.println("[SYNC] No se pudo eliminar: " + archivoViejo.getPath());
                        }
                    }
                }
            }
        }
    }

    private static GuardadorMensaje crearGuardadorTemporal(String extension, String usuario) {
        return switch (extension) {
            case "json" -> new GuardadorMensajeJson(usuario);
            case "xml" -> new GuardadorMensajeXml(usuario);
            case "txt" -> new GuardadorMensajeTexto(usuario);
            default -> null;
        };
    }
}