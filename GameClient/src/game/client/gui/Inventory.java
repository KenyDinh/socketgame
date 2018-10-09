/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.gui;

import game.client.common.CommonDefine;
import game.client.common.CommonMethod;
import game.client.element.Equipment;
import game.client.socket.BoomGameClient;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.DefaultListModel;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class Inventory extends javax.swing.JFrame {

    private int itemSelectedIndex = -1;
    private Equipment selectedItem = null;
    private List<Equipment> listEquipment;
    private BombPreview bp;

    /**
     * Creates new form Inventory
     */
    public Inventory() {
        initComponents();
        initListItem();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/client.png")).getImage());
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
        listItem = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        imgPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        descrip = new javax.swing.JTextArea();
        btnPreview = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Inventory");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        listItem.setBackground(new java.awt.Color(204, 204, 204));
        listItem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        listItem.setForeground(new java.awt.Color(0, 153, 102));
        listItem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listItem.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listItemValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listItem);

        imgPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imgPanelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout imgPanelLayout = new javax.swing.GroupLayout(imgPanel);
        imgPanel.setLayout(imgPanelLayout);
        imgPanelLayout.setHorizontalGroup(
            imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imgPanelLayout.setVerticalGroup(
            imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        descrip.setBackground(new java.awt.Color(204, 204, 204));
        descrip.setColumns(20);
        descrip.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        descrip.setForeground(new java.awt.Color(0, 153, 102));
        descrip.setLineWrap(true);
        descrip.setRows(5);
        descrip.setWrapStyleWord(true);
        jScrollPane2.setViewportView(descrip);

        btnPreview.setText("Preview");
        btnPreview.setToolTipText("Preview Bomb Explode");
        btnPreview.setEnabled(false);
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(imgPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPreview)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        );

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        if (selectedItem != null) {
            bp = new BombPreview(selectedItem.getHorizontalLen(), selectedItem.getVerticalLen(), selectedItem.getSquareLen());
            CommonMethod.setLocationFrame(bp);
            bp.setVisible(true);
        }
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        repaintImg();
    }//GEN-LAST:event_formWindowActivated

    private void imgPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgPanelMouseEntered
        repaintImg();
    }//GEN-LAST:event_imgPanelMouseEntered

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ClientFrame.getClientFrame().setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void listItemValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listItemValueChanged
        int index = listItem.getSelectedIndex();
        if (itemSelectedIndex != index) {
            itemSelectedIndex = index;
            selectedItem = listEquipment.get(itemSelectedIndex);
            repaintImg();
            descrip.setText(null);
            descrip.append("Name: " + selectedItem.getName());
            descrip.append(CommonDefine.BREAK_LINE);
            if (BoomGameClient.isBombItem(selectedItem.getId()) || BoomGameClient.isBomb(selectedItem.getId())) {
                btnPreview.setEnabled(true);
                descrip.append("Damage: " + selectedItem.getDamage());
                descrip.append(CommonDefine.BREAK_LINE);
                descrip.append("Horizontal Size: " + selectedItem.getHorizontalLen());
                descrip.append(CommonDefine.BREAK_LINE);
                descrip.append("Vertial Size: " + selectedItem.getVerticalLen());
                descrip.append(CommonDefine.BREAK_LINE);
                descrip.append("Square Size: " + selectedItem.getSquareLen());
                descrip.append(CommonDefine.BREAK_LINE);
                descrip.append("Recovery Ratio: " + selectedItem.getRecoverRatio());
                descrip.append(CommonDefine.BREAK_LINE);
            }else{
                btnPreview.setEnabled(false);
            }
            descrip.append("Description: " + selectedItem.getDescrip());
            descrip.append(CommonDefine.BREAK_LINE);
        }
    }//GEN-LAST:event_listItemValueChanged

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        ClientFrame.getClientFrame().setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    public void closeInventory() {
        if (bp != null && bp.isVisible()) {
            bp.closePreview();
        }
        ClientFrame.getClientFrame().setEnabled(true);
        this.dispose();
    }

    private void repaintImg() {
        if (selectedItem != null) {
            Graphics g = imgPanel.getGraphics();
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, imgPanel.getWidth(), imgPanel.getHeight());
            g.drawImage(ClientFrame.getClientFrame().getListImageBoom().get("way"), 0, 0, imgPanel.getWidth(), imgPanel.getHeight(), null);
            String key = "item_" + selectedItem.getId();
            if (BoomGameClient.isBombItem(selectedItem.getId())) {
                key = "bomb_" + BoomGameClient.getBombItem(selectedItem.getId());
            } else if (BoomGameClient.isBomb(selectedItem.getId())) {
                key = "bomb_" + selectedItem.getId();
            }
            int scale = 10;
            g.drawImage(ClientFrame.getClientFrame().getListImageBoom().get(key), scale, scale, imgPanel.getWidth() - 2 * scale, imgPanel.getHeight() - 2 * scale, null);
        }
    }

    private void initListItem() {
        if (ClientFrame.getClientFrame().getMapItemOwn().isEmpty()) {
            return;
        }
        listEquipment = new ArrayList<>();
        for (Entry<Integer, Integer> entry : ClientFrame.getClientFrame().getMapItemOwn().entrySet()) {
            Equipment e = ClientFrame.getClientFrame().getEquipmentById(entry.getKey());
            if (e != null) {
                listEquipment.add(e);
            }
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < listEquipment.size(); i++) {
            listModel.add(i, listEquipment.get(i).getName());
        }
        listItem.setModel(listModel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnPreview;
    private javax.swing.JTextArea descrip;
    private javax.swing.JPanel imgPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listItem;
    // End of variables declaration//GEN-END:variables
}