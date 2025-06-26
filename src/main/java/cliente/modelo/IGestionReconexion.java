package cliente.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cliente.modelo.IMensaje;
import cliente.controlador.IGestionInterfaz;
import cliente.modelo.IGestionRed;
import persistencia.GuardadorMensaje;

public interface IGestionReconexion {

    public boolean reconectarBackup(IGestionRed gestor);
    public void usuarioOnline(IGestionInterfaz controlador, IGestionRed gestor, String emisor, String serverN);

}
