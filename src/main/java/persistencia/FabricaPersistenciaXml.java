package persistencia;

public class FabricaPersistenciaXml implements IFabricaPersistencia {

    @Override
    public GuardadorMensaje crearGuardadorMensaje(String usuario) {
        return new GuardadorMensajeXml(usuario);
    }
    
    @Override
    public GuardadorContacto crearGuardadorContacto(String usuario) {
        return new GuardadorContactoXml(usuario);
    }
}