/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felis
 */
public class pruebaMonitor {
    public static void main(String[] args) {
            Monitor monitor;

        try {
            monitor = new Monitor();
            monitor.iniciarMonitor();
            monitor.listening();
            System.out.println("YA INICIADO");
        } catch (IOException ex) {
            Logger.getLogger(pruebaMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
}
