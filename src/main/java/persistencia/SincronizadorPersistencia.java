package persistencia;

import cliente.modelo.Contacto;
import cliente.modelo.IMensaje;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SincronizadorPersistencia {

    public static void sincronizar(String usuario, String nuevaExtension, GuardadorMensaje guardadorNuevo, GuardadorContacto guardadorNuevoContacto) {
        String[] extensiones = {"json", "xml", "txt"};

        for (String ext : extensiones) {
            if (!ext.equals(nuevaExtension)) {
                // -------- MIGRAR MENSAJES --------
                File archivoMensajesViejo = new File("persistencia/mensajes" + usuario + "." + ext);
                if (archivoMensajesViejo.exists()) {
                    System.out.println("[SYNC] Migrando mensajes de ." + ext + " a ." + nuevaExtension);
                    GuardadorMensaje guardadorViejo = crearGuardadorTemporalMensaje(ext, usuario);
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
                        if (archivoMensajesViejo.delete()) {
                            System.out.println("[SYNC] Archivo de mensajes eliminado: " + archivoMensajesViejo.getPath());
                        } else {
                            System.out.println("[SYNC] No se pudo eliminar: " + archivoMensajesViejo.getPath());
                        }
                    }
                }

                // -------- MIGRAR CONTACTOS --------
                File archivoContactosViejo = new File("persistencia/contactos" + usuario + "." + ext);
                if (archivoContactosViejo.exists()) {
                    System.out.println("[SYNC] Migrando contactos de ." + ext + " a ." + nuevaExtension);
                    GuardadorContacto guardadorViejoContacto = crearGuardadorTemporalContacto(ext, usuario);
                    if (guardadorViejoContacto != null) {
                        List<Contacto> contactos = guardadorViejoContacto.cargarContactos();
                        for (Contacto c : contactos) {
                            guardadorNuevoContacto.guardarContacto(c);
                        }
                        if (archivoContactosViejo.delete()) {
                            System.out.println("[SYNC] Archivo de contactos eliminado: " + archivoContactosViejo.getPath());
                        } else {
                            System.out.println("[SYNC] No se pudo eliminar: " + archivoContactosViejo.getPath());
                        }
                    }
                }
            }
        }
    }

    private static GuardadorMensaje crearGuardadorTemporalMensaje(String extension, String usuario) {
        return switch (extension) {
            case "json" -> new GuardadorMensajeJson(usuario);
            case "xml" -> new GuardadorMensajeXml(usuario);
            case "txt" -> new GuardadorMensajeTexto(usuario);
            default -> null;
        };
    }

    private static GuardadorContacto crearGuardadorTemporalContacto(String extension, String usuario) {
        return switch (extension) {
            case "json" -> new GuardadorContactoJson(usuario);
            case "xml" -> new GuardadorContactoXml(usuario);
            case "txt" -> new GuardadorContactoTexto(usuario);
            default -> null;
        };
    }
}