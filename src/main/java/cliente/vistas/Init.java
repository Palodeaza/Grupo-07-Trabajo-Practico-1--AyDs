package vistas;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

public class Init extends javax.swing.JFrame {

    public Init() {
        initComponents();
        
        newContactButton = new AnimatedLabel("Nuevo Contacto");
        bg.add(newContactButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, 130, 60));
        newConvButton = new AnimatedLabel("Nuevo Chat");
        bg.add(newConvButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 130, 60));
        sendMsgTxtButton = new AnimatedLabel("Enviar");
        bg.add(sendMsgTxtButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 440, 90, 60));
        
        msgScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        DefaultListModel<String> modeloConversaciones = new DefaultListModel<>();
        chatList.setModel(modeloConversaciones); 
        chatList.setCellRenderer(new ConversacionRenderer());
        chatList.setFixedCellHeight(50);
        chatList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                int index = getChatList().locationToIndex(evt.getPoint());
                ((ConversacionRenderer) getChatList().getCellRenderer()).setHoveredIndex(index);
                getChatList().repaint();
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                bg.requestFocusInWindow();
            }
        });
        chatList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String sel = chatList.getSelectedValue();
                selectedContactLabel.setText(sel != null ? "  " + sel : "");
            }
        });
        msgTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (msgTextField.getText().trim().isEmpty()) {
                    msgTextField.setText("  Mensaje...");
                    msgTextField.setForeground(Color.GRAY);
                }
            }
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (msgTextField.getText().trim().equals("Mensaje...") || msgTextField.getText().trim().isEmpty()) {
                    msgTextField.setText("  ");
                    msgTextField.setForeground(Color.BLACK);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        bg = new javax.swing.JPanel();
        msgTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatList = new javax.swing.JList<>();
        msgScrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        selectedContactLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        msgTextField.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        msgTextField.setForeground(new java.awt.Color(204, 204, 204));
        msgTextField.setText("  Mensaje...");
        msgTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        msgTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                msgTextFieldMouseClicked(evt);
            }
        });
        msgTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msgTextFieldActionPerformed(evt);
            }
        });
        bg.add(msgTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 440, 350, 60));

        jScrollPane1.setViewportView(chatList);

        bg.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 260, 350));

        msgScrollPane.setAutoscrolls(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        msgScrollPane.setViewportView(jPanel1);

        bg.add(msgScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 440, 390));

        selectedContactLabel.setBackground(new java.awt.Color(0, 0, 102));
        selectedContactLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        selectedContactLabel.setForeground(new java.awt.Color(255, 255, 255));
        selectedContactLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedContactLabel.setOpaque(true);
        bg.add(selectedContactLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 440, 50));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SMI");
        bg.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 50, 40));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jLabel3.setOpaque(true);
        bg.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, 160, -1));

        jLabel1.setBackground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("jLabel1");
        jLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jLabel1.setOpaque(true);
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 440));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void msgTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msgTextFieldActionPerformed
        sendMsgTxtButton.dispatchEvent(new java.awt.event.MouseEvent(
            sendMsgTxtButton,
            java.awt.event.MouseEvent.MOUSE_CLICKED,
            System.currentTimeMillis(),
            0, 0, 0, 1, false
        ));

        msgTextField.setText("  ");

        SwingUtilities.invokeLater(() -> {
            if (!msgTextField.requestFocusInWindow()) {
                msgTextField.requestFocus();
            }
        });
    }//GEN-LAST:event_msgTextFieldActionPerformed

    private void msgTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_msgTextFieldMouseClicked
        if (getMsgTextField().getText().equals("  Mensaje...")) {
            getMsgTextField().setText("  ");
            getMsgTextField().setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_msgTextFieldMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Init().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JList<String> chatList;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane msgScrollPane;
    private javax.swing.JTextField msgTextField;
    private javax.swing.JLabel selectedContactLabel;
    // End of variables declaration//GEN-END:variables
    private AnimatedLabel newContactButton;
    private AnimatedLabel newConvButton;
    private AnimatedLabel sendMsgTxtButton;

    public javax.swing.JList<String> getChatList() {
        return chatList;
    }

    public javax.swing.JScrollPane getMsgScrollPane() {
        return msgScrollPane;
    }

    public javax.swing.JTextField getMsgTextField() {
        return msgTextField;
    }

    public javax.swing.JLabel getNewContactButton() {
        return newContactButton;
    }

    public javax.swing.JLabel getNewConvButton() {
        return newConvButton;
    }

    public javax.swing.JLabel getSendMsgTxtButton() {
        return sendMsgTxtButton;
    }

    public javax.swing.JPanel getChatPanel() {
        return jPanel1;
    }

    public void addChatBubble(String texto, String hora, boolean esPropio) {

        JLabel mensajeLabel = new JLabel("<html><body style='width:200px; margin:2px; padding:2px;'>"
                                         + texto + "</body></html>");
        mensajeLabel.setForeground(Color.WHITE);
        mensajeLabel.setFont(new Font("Roboto", Font.PLAIN, 14));

        JLabel horaLabel = new JLabel(hora);
        horaLabel.setForeground(Color.GRAY);
        horaLabel.setFont(new Font("Roboto", Font.ITALIC, 10));
        horaLabel.setVisible(false);

        RoundedPanel bubblePanel = new RoundedPanel(15, new Color(0, 0, 102));
        bubblePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        bubblePanel.add(mensajeLabel);
        bubblePanel.setOpaque(false);

        bubblePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                horaLabel.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                horaLabel.setVisible(false);
            }
        });

        JPanel wrapper = new JPanel(new FlowLayout(
                esPropio ? FlowLayout.RIGHT : FlowLayout.LEFT,
                5, 2));
        wrapper.setOpaque(false);
        if (esPropio) {
            wrapper.add(horaLabel);
            wrapper.add(bubblePanel);
        } else {
            wrapper.add(bubblePanel);
            wrapper.add(horaLabel);
        }

        getChatPanel().add(wrapper);
        getChatPanel().revalidate();
        getChatPanel().repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar v = getMsgScrollPane().getVerticalScrollBar();
            v.setValue(v.getMaximum());
        });
    }
    
    public void actualizaChats(List<String> listaConexiones) {
        DefaultListModel<String> modeloConversaciones = (DefaultListModel<String>) chatList.getModel();
        modeloConversaciones.clear(); // Limpiar la lista actual
        for (String conexion : listaConexiones) {
            modeloConversaciones.addElement(conexion); // Agregar nuevas conversaciones
        }
        chatList.revalidate(); // Vuelve a validar la lista
        chatList.repaint(); // Redibuja la lista
    }
}
