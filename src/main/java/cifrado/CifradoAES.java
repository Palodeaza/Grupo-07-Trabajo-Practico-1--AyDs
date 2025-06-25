/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cifrado;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tomas
 */
public class CifradoAES implements EstrategiaCifrado {
    // google pibe
    @Override
    public String cifrar(String mensaje, SecretKeySpec clave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");           
        cipher.init(Cipher.ENCRYPT_MODE, clave);               
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes("UTF-8")); 
        return Base64.getEncoder().encodeToString(mensajeCifrado);         
    }
    
    @Override
    public String descifrar(String mensajeCifrado, SecretKeySpec clave) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] mensajeBytes = Base64.getDecoder().decode(mensajeCifrado);
            byte[] mensajeDescifrado = cipher.doFinal(mensajeBytes);
            return new String(mensajeDescifrado, "UTF-8");
        } catch (javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException e) {
            return "CLAVE DE CIFRADO ERRÓNEA";
        } catch (Exception e) {
            return "ERROR AL DESCIFRAR MENSAJE";
        }
    }
}
