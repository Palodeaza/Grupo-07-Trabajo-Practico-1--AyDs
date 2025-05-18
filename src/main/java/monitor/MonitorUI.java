package monitor;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorUI extends JFrame {

    private JLabel estadoServidor1;
    private JLabel estadoServidor2;
    private JLabel servidorPrimario;
    private Monitor monitor;

    public MonitorUI(Monitor monitor) {
        this.monitor = monitor;
        setTitle("Monitor de Servidores");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        estadoServidor1 = new JLabel("Servidor 1: Desconocido", SwingConstants.CENTER);
        estadoServidor2 = new JLabel("Servidor 2: Desconocido", SwingConstants.CENTER);
        servidorPrimario = new JLabel("Servidor Primario: Desconocido", SwingConstants.CENTER);

        add(new JLabel("Estado de los Servidores", SwingConstants.CENTER));
        add(estadoServidor1);
        add(estadoServidor2);
        add(servidorPrimario);

        actualizarEstados();
    }

    private void actualizarEstados() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean s1 = monitor.estaActivo(monitor.getIp1(), monitor.getPuerto1());
                boolean s2 = monitor.estaActivo(monitor.getIp2(), monitor.getPuerto2());
                int primario = monitor.getServidorActivo();

                SwingUtilities.invokeLater(() -> {
                    estadoServidor1.setText("Servidor 1: " + (s1 ? "Activo" : "Caído"));
                    estadoServidor1.setForeground(s1 ? Color.GREEN.darker() : Color.RED);

                    estadoServidor2.setText("Servidor 2: " + (s2 ? "Activo" : "Caído"));
                    estadoServidor2.setForeground(s2 ? Color.GREEN.darker() : Color.RED);

                    servidorPrimario.setText("Servidor Primario: " + primario);
                });
            }
        }, 0, 3000); // cada 3 segundos
    }

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