package frank.password_manager.UI;

import frank.password_manager.DAO.UserDAO;
import frank.password_manager.Utils.Tools;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class IniciarSesionPNL extends javax.swing.JPanel {

    private Object username;

    public IniciarSesionPNL() {
        initComponents();
        username = Tools.getUserOrNot();
        if (username != null) {
            userTF.setText(username.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        passLBL = new javax.swing.JLabel();
        iniciarBTN = new javax.swing.JButton();
        userLBL = new javax.swing.JLabel();
        passTF = new javax.swing.JPasswordField();
        userTF = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("INICIAR SESION");

        passLBL.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        passLBL.setText("Contraseña maestra");

        iniciarBTN.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        iniciarBTN.setText("Iniciar");
        iniciarBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarBTNActionPerformed(evt);
            }
        });

        userLBL.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        userLBL.setText("Nombre de usuario");

        passTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        userTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        userTF.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(iniciarBTN)))
                        .addContainerGap(209, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(userTF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                            .addComponent(passLBL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passTF, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(userLBL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(passLBL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(iniciarBTN)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(255, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(408, 408, 408))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(244, 244, 244))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel1)
                .addGap(49, 49, 49)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(146, 146, 146))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void iniciarBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarBTNActionPerformed

        String password = passTF.getPassword().toString();

        UserDAO dao = new UserDAO();

        if (dao.authenticateUser(username.toString(), password)) {
            JOptionPane.showMessageDialog(null, "Se ha registrado exitosamente", "Inicio exitoso", JOptionPane.INFORMATION_MESSAGE);
            Tools.changePanel(new DashboardPNL(), (JPanel) this.getParent());
        }
    }//GEN-LAST:event_iniciarBTNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton iniciarBTN;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel passLBL;
    private javax.swing.JPasswordField passTF;
    private javax.swing.JLabel userLBL;
    private javax.swing.JTextField userTF;
    // End of variables declaration//GEN-END:variables
}
