package persistencia;

public class FabricaPersistenciaJson implements IFabricaPersistencia {

    @Override
    public GuardadorMensaje crearGuardadorMensaje(String usuario) {
        return new GuardadorMensajeJson(usuario);
    }
}