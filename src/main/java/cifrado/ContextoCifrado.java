package cifrado;

import javax.crypto.spec.SecretKeySpec;

public class ContextoCifrado {
    private EstrategiaCifrado estrategia;

    public ContextoCifrado(EstrategiaCifrado estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaCifrado estrategia) {
        this.estrategia = estrategia;
    }

    public String cifrarMensaje(String mensaje, SecretKeySpec clave) throws Exception {
        return estrategia.cifrar(mensaje, clave);
    }

    public String descifrarMensaje(String mensajeCifrado, SecretKeySpec clave) throws Exception {
        return estrategia.descifrar(mensajeCifrado, clave);
    }
    
    public SecretKeySpec crearClave(String clave) throws Exception{
        byte[] claveBytes = clave.getBytes("UTF-8");  
        byte[] claveAjustada = new byte[16];          
        System.arraycopy(claveBytes, 0, claveAjustada, 0, Math.min(claveBytes.length, claveAjustada.length)); // esto es pq la clave es mayimo 16 bytes
        return new SecretKeySpec(claveAjustada, "AES"); 
    }
}