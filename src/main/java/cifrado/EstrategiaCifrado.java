package cifrado;

import javax.crypto.spec.SecretKeySpec;

public interface EstrategiaCifrado {
    String cifrar(String mensaje, SecretKeySpec clave) throws Exception;
    String descifrar(String mensajeCifrado, SecretKeySpec clave) throws Exception;
}