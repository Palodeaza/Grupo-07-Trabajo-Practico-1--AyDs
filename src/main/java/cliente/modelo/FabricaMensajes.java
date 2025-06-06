
package cliente.modelo;


public class FabricaMensajes implements IFabricaMensajes {

    private static FabricaMensajes instancia;


    private FabricaMensajes() {
    }

    // 3. Método público para obtener la instancia única
    public static FabricaMensajes getInstancia() {
        if (instancia == null) {
            instancia = new FabricaMensajes();
        }
        return instancia;
    }

    // 4. Implementación de los métodos de IFabricaMensajes
    @Override
    public IMensaje creaMensaje() {
        IMensaje m = new Mensaje();
        return m;
    }
}

