package prueba;

import modelo.Server;

public class pruebaServer { // esto podria estar adentro del server // termine agregandolo en server, si quieren borren esto
    public static void main(String[] args) {
        Server server = new Server();
        server.iniciarServidor();
    }
}
