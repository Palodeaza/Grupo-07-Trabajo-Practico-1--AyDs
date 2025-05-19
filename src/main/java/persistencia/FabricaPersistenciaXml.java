package persistencia;

public class FabricaPersistenciaXml implements IFabricaPersistencia {

    @Override
    public GuardadorMensaje crearGuardadorMensaje() {
        return new GuardadorMensajeXml();
    }
}