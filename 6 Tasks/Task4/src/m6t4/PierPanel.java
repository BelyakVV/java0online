package m6t4;

/**
 *
 * @author aabyodj
 */
public class PierPanel extends javax.swing.JPanel {
    
//    private MainForm port;
    private final Dockman dockman;
    private Ship ship = null;
    private boolean isLoading;

    /**
     * Creates new form PierPanel
     */
    public PierPanel() {        
        initComponents();
        dockman = new Dockman(this);
        dockman.start();
    }
    
    public boolean isFree() {
        return null == ship;
    }
    
    public boolean acceptShip(Ship newShip) {
        if (!isFree()) return false;
        ship = newShip;        
        MainForm port = (MainForm) getTopLevelAncestor();
        isLoading = port.isFull();
        btnUnmoor.setEnabled(true);
        lblShipName.setText(ship.name);
        showLoad();
        return true;
    }
    
    public Ship getShip() {
        return ship;
    }
    
    public void proceed() {
        if (isFree()) {
            return;
        }
        MainForm port = (MainForm) getTopLevelAncestor();
        if (isLoading) {
            if (ship.isFull()) {
                unmoor();
            } else {
                if (port.takeOne()) {
                    ship.loadOne();
                } else {
                    return; //The port warehouse is empty
                }
            }
        } else {
            
            //Unloading
            if (ship.isEmpty()) {
                isLoading = true;
                proceed();
                return;
                
            //The ship is not empty
            } else {
                if (port.putOne()) {
                    ship.unloadOne();
                } else {
                    return; //The port warehouse is full
                }
            }
        }
        showLoad();
    }

    private void unmoor() {
        if (null == ship) return;
        ship.depart();
        ship = null;
        this.btnUnmoor.setEnabled(false);
        lblShipName.setText("НЕТ");
        showLoad();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button1 = new java.awt.Button();
        java.awt.Label lblShip = new java.awt.Label();
        java.awt.Label lblLoad = new java.awt.Label();
        btnUnmoor = new java.awt.Button();

        button1.setLabel("Отшвартовать");

        setBorder(javax.swing.BorderFactory.createTitledBorder("Причал"));

        lblShip.setText("Пришвартованное судно:");

        lblShipName.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblShipName.setText("НЕТ");

        lblLoad.setText("Загрузка судна:");

        lblShipLoad.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblShipLoad.setText("0/0");

        btnUnmoor.setEnabled(false);
        btnUnmoor.setLabel("Отшвартовать");
        btnUnmoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnmoorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblShip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblShipName, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblShipLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUnmoor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblShip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblShipName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblShipLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUnmoor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUnmoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnmoorActionPerformed
        unmoor();
    }//GEN-LAST:event_btnUnmoorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btnUnmoor;
    private java.awt.Button button1;
    final java.awt.Label lblShipLoad = new java.awt.Label();
    final java.awt.Label lblShipName = new java.awt.Label();
    // End of variables declaration//GEN-END:variables

    private void showLoad() {
        if (isFree()) {
            lblShipLoad.setText("0/0");
        } else {
            StringBuilder result = new StringBuilder();
            if (isLoading || ship.isEmpty()) {
                result.append('↑');
            } else {
                result.append('↓');
            }
            result.append(ship.getCurrentLoad())
                    .append('/')
                    .append(ship.capacity);
            lblShipLoad.setText(result.toString());
//            lblShipLoad.paint(this.getGraphics());
            lblShipLoad.repaint();
        }
    }
}
