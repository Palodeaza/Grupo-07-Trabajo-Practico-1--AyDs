package cliente.vistas;

import java.awt.Color;

public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                bg.requestFocusInWindow();
            }
        });
        loginButton = new AnimatedButton("INGRESAR");
        loginButton.setBounds(160, 350, 100, 30);
        bg.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 100, 30));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        bg = new javax.swing.JPanel();
        userTxt = new javax.swing.JTextField();
        IniciarSesion = new javax.swing.JLabel();
        Linea = new javax.swing.JLabel();
        Contraseña = new javax.swing.JLabel();
        panelNaranja = new javax.swing.JLabel();
        lineaInicioSesion = new javax.swing.JLabel();
        formatoComboBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        jScrollPane1.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userTxt.setForeground(new java.awt.Color(204, 204, 204));
        userTxt.setText("Ingrese su nombre de usuario...");
        userTxt.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        userTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTxtMouseClicked(evt);
            }
        });
        userTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userTxtActionPerformed(evt);
            }
        });
        bg.add(userTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 320, -1));

        IniciarSesion.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        IniciarSesion.setText("INICIAR SESION");
        bg.add(IniciarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 230, 20));

        Linea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bg.add(Linea, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 268, 320, -1));

        Contraseña.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        Contraseña.setText("Nombre de usuario");
        bg.add(Contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        panelNaranja.setBackground(new java.awt.Color(0, 0, 102));
        panelNaranja.setForeground(new java.awt.Color(255, 153, 0));
        panelNaranja.setOpaque(true);
        bg.add(panelNaranja, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, -10, 250, 520));

        lineaInicioSesion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        bg.add(lineaInicioSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 142, 200, -1));

        formatoComboBox.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        formatoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "JSON", "XML", "Texto plano" }));
        formatoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatoComboBoxActionPerformed(evt);
            }
        });
        bg.add(formatoComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 110, -1));

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel1.setText("Tipo de Persistencia");
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userTxtActionPerformed

    private void userTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTxtMouseClicked
       if (getUserTxt().getText().equals("Ingrese su nombre de usuario...")){
            getUserTxt().setText("");
            getUserTxt().setForeground(Color.BLACK);          
       }
    }//GEN-LAST:event_userTxtMouseClicked

    private void formatoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatoComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formatoComboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Contraseña;
    private javax.swing.JLabel IniciarSesion;
    private javax.swing.JLabel Linea;
    private javax.swing.JPanel bg;
    private javax.swing.JComboBox<String> formatoComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel lineaInicioSesion;
    private javax.swing.JLabel panelNaranja;
    private javax.swing.JTextField userTxt;
    // End of variables declaration//GEN-END:variables
    private AnimatedButton loginButton;

    /**
     * @return the loginButton
     */
    public javax.swing.JButton getLoginButton() {
        return loginButton;
    }

    /**
     * @return the portTxt
     */

    /**
     * @return the userTxt
     */
    public javax.swing.JTextField getUserTxt() {
        return userTxt;
    }

    /**
     * @return the formatoComboBox
     */
    public javax.swing.JComboBox<String> getFormatoComboBox() {
        return formatoComboBox;
    }
}
