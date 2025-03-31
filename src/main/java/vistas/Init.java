package vistas;

import java.awt.Color;
import java.util.List;
import javax.swing.DefaultListModel;

public class Init extends javax.swing.JFrame {

    public Init() {
        initComponents();
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
        newContactButton = new javax.swing.JLabel();
        newConvButton = new javax.swing.JLabel();
        sendMsgTxtButton = new javax.swing.JLabel();
        SendMsgColor = new javax.swing.JLabel();
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
        jPanel1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        msgScrollPane.setViewportView(jPanel1);

        bg.add(msgScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 440, 440));

        newContactButton.setBackground(new java.awt.Color(0, 0, 102));
        newContactButton.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        newContactButton.setForeground(new java.awt.Color(255, 255, 255));
        newContactButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newContactButton.setText("Nuevo Contacto");
        newContactButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newContactButton.setOpaque(true);
        newContactButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newContactButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newContactButtonMouseExited(evt);
            }
        });
        bg.add(newContactButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, 130, 60));

        newConvButton.setBackground(new java.awt.Color(0, 0, 102));
        newConvButton.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        newConvButton.setForeground(new java.awt.Color(255, 255, 255));
        newConvButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newConvButton.setText("Nuevo Chat");
        newConvButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        newConvButton.setOpaque(true);
        newConvButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newConvButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newConvButtonMouseExited(evt);
            }
        });
        bg.add(newConvButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 130, 60));

        sendMsgTxtButton.setBackground(new java.awt.Color(0, 0, 102));
        sendMsgTxtButton.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        sendMsgTxtButton.setForeground(new java.awt.Color(255, 255, 255));
        sendMsgTxtButton.setText("     Enviar");
        sendMsgTxtButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sendMsgTxtButton.setOpaque(true);
        sendMsgTxtButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sendMsgTxtButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendMsgTxtButtonMouseExited(evt);
            }
        });
        bg.add(sendMsgTxtButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 442, 90, 60));

        SendMsgColor.setBackground(new java.awt.Color(0, 0, 102));
        SendMsgColor.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        SendMsgColor.setForeground(new java.awt.Color(255, 255, 255));
        SendMsgColor.setOpaque(true);
        bg.add(SendMsgColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 440, 90, 60));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SMI");
        bg.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 60, 40));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jLabel3.setOpaque(true);
        bg.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, 170, -1));

        jLabel1.setBackground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("jLabel1");
        jLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jLabel1.setOpaque(true);
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 700, 510));

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
        // TODO add your handling code here:
    }//GEN-LAST:event_msgTextFieldActionPerformed

    private void newConvButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newConvButtonMouseEntered
        getNewConvButton().setBackground(new Color(0, 102, 204));
    }//GEN-LAST:event_newConvButtonMouseEntered

    private void newConvButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newConvButtonMouseExited
        getNewConvButton().setBackground(new Color(0, 0, 102));
    }//GEN-LAST:event_newConvButtonMouseExited

    private void newContactButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newContactButtonMouseEntered
        getNewContactButton().setBackground(new Color(0, 102, 204));

    }//GEN-LAST:event_newContactButtonMouseEntered

    private void newContactButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newContactButtonMouseExited
        getNewContactButton().setBackground(new Color(0, 0, 102));
    }//GEN-LAST:event_newContactButtonMouseExited

    private void sendMsgTxtButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendMsgTxtButtonMouseEntered
        getSendMsgTxtButton().setBackground(new Color(0, 102, 204));
    }//GEN-LAST:event_sendMsgTxtButtonMouseEntered

    private void sendMsgTxtButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendMsgTxtButtonMouseExited
        getSendMsgTxtButton().setBackground(new Color(0, 0, 102));
    }//GEN-LAST:event_sendMsgTxtButtonMouseExited

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
    private javax.swing.JLabel SendMsgColor;
    private javax.swing.JPanel bg;
    private javax.swing.JList<String> chatList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane msgScrollPane;
    private javax.swing.JTextField msgTextField;
    private javax.swing.JLabel newContactButton;
    private javax.swing.JLabel newConvButton;
    private javax.swing.JLabel sendMsgTxtButton;
    // End of variables declaration//GEN-END:variables

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
    System.out.println("TENGO QUE AGREGAR: " + listaConexiones.toString());
    for (String conexion : listaConexiones) {
        System.out.println("AGREGO LA SIGUIENTE CONEXION A LA PANTALLA: " + conexion);
        modeloConversaciones.addElement(conexion); // Agregar nuevas conversaciones
    }
    chatList.revalidate(); // Vuelve a validar la lista
    chatList.repaint(); // Redibuja la lista
    }
}
