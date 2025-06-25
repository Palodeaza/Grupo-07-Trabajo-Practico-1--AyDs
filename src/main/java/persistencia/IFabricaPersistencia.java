package persistencia;

public interface IFabricaPersistencia {
    GuardadorMensaje crearGuardadorMensaje(String usuario);
    GuardadorContacto crearGuardadorContacto(String usuario);
}