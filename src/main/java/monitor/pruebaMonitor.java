/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monitor;

import monitor.vistas.MonitorUI;
import monitor.modelo.Monitor;
import javax.swing.SwingUtilities;

public class pruebaMonitor {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Monitor monitor = new Monitor();
                monitor.iniciarMonitor();
                monitor.listening();

                MonitorUI ui = new MonitorUI(monitor);
                ui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
