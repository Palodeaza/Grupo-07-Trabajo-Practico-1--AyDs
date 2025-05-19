package persistencia;

public class FabricaPersistenciaTexto implements IFabricaPersistencia {

    @Override
    public GuardadorMensaje crearGuardadorMensaje() {
        return new GuardadorMensajeTexto();
    }
}