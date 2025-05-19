package persistencia;

public class FabricaPersistenciaJson implements IFabricaPersistencia {

    @Override
    public GuardadorMensaje crearGuardadorMensaje() {
        return new GuardadorMensajeJson();
    }
}