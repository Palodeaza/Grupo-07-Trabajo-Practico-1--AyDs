package vistas;

import java.awt.Color;
import java.util.List;
import javax.swing.DefaultListModel;

public class Init extends javax.swing.JFrame {

    public Init() {
        initComponents();
        newContactButton = new AnimatedLabel("Nuevo Contacto");
        bg.add(newContactButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, 130, 60));

        newConvButton = new AnimatedLabel("Nuevo Chat");
        bg.add(newConvButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 130, 60));

        sendMsgTxtButton = new AnimatedLabel("Enviar");
        bg.add(sendMsgTxtButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 440, 90, 60));
        DefaultListModel<String> modeloConversaciones = new DefaultListModel<>();
        DefaultListModel<String> mensajesList = new DefaultListModel<>();
        chatList.setModel(modeloConversaciones); // Configurar modelo dinámico
        chatList.setCellRenderer(new ConversacionRenderer());
        chatList.setFixedCellHeight(30);
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 700, 450));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Init.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Init.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Init.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Init.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Init().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JList<String> chatList;
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
    /**
     * @return the chatList
     */
    public javax.swing.JList<String> getChatList() {
        return chatList;
    }

    /**
     * @return the msgScrollPane
     */
    public javax.swing.JScrollPane getMsgScrollPane() {
        return msgScrollPane;
    }

    /**
     * @return the msgTextField
     */
    public javax.swing.JTextField getMsgTextField() {
        return msgTextField;
    }

    /**
     * @return the newContactButton
     */
    public javax.swing.JLabel getNewContactButton() {
        return newContactButton;
    }

    /**
     * @return the newConvButton
     */
    public javax.swing.JLabel getNewConvButton() {
        return newConvButton;
    }

    /**
     * @return the sendMsgTxtButton
     */
    public javax.swing.JLabel getSendMsgTxtButton() {
        return sendMsgTxtButton;
    }

    public javax.swing.JPanel getChatPanel() {
        return jPanel1;
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
