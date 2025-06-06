package persistencia;

public class FabricaPersistenciaTexto implements IFabricaPersistencia {

    @Override
    public GuardadorMensaje crearGuardadorMensaje(String usuario) {
        return new GuardadorMensajeTexto(usuario);
    }
}