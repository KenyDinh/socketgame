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
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class BoomSettingFrame extends javax.swing.JFrame {

    private List<Equipment> listUseItem;
    private List<Equipment> listOwnItem;
    private Equipment selectedItem = null;
    private static final int MAX_ITEM_USE = 3;
    private int currentIndexOwn = -1;
    private int currentIndexUse = -1;

    /**
     * Creates new form BoomSettingFrame
     *
     */
    public BoomSettingFrame(int gameCoin) {
        initComponents();
        CommonMethod.setLocationFrame(this);
        updateItemData();
        tf_gamecoin.setText(String.valueOf(gameCoin).trim());
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/setting.png")).getImage());
    }

    public void disableCoinSetting() {
        tf_gamecoin.setEditable(false);
    }

    private void updateItemData() {
        listUseItem = new ArrayList<>();
        listOwnItem = new ArrayList<>();
        DefaultListModel<String> ownList = new DefaultListModel<>();
        for (Entry<Integer, Integer> entry : ClientFrame.getClientFrame().getMapItemOwn().entrySet()) {
            Equipment e = ClientFrame.getClientFrame().getEquipmentById(entry.getKey());
            if (e != null) {
                String strv = entry.getValue() < 10 ? String.valueOf(entry.getValue()) + " :" : String.valueOf(entry.getValue());
                ownList.addElement(strv + e.getName());
                listOwnItem.add(e);
            }
        }
        ///
        DefaultListModel<String> useList = new DefaultListModel<>();
        for (Entry<Integer, Integer> entry : ClientFrame.getClientFrame().getMapItemUse().entrySet()) {
            Equipment e = ClientFrame.getClientFrame().getEquipmentById(entry.getKey());
            if (e != null) {
                String strv = entry.getValue() < 10 ? String.valueOf(entry.getValue()) + " :" : String.valueOf(entry.getValue());
                useList.addElement(strv + e.getName());
                listUseItem.add(e);
            }
        }
        list_ownItem.setModel(ownList);
        list_useItem.setModel(useList);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        tf_gamecoin = new javax.swing.JTextField();
        label_gc = new javax.swing.JLabel();
        body = new javax.swing.JPanel();
        jscroll_own = new javax.swing.JScrollPane();
        list_ownItem = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_useItem = new javax.swing.JList<>();
        btn_add = new javax.swing.JButton();
        btn_remove = new javax.swing.JButton();
        btn_removeAll = new javax.swing.JButton();
        footer = new javax.swing.JPanel();
        jscroll_use = new javax.swing.JScrollPane();
        descrip = new javax.swing.JTextArea();
        btn_ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Setting");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        label_gc.setText("Game Coin");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(label_gc, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tf_gamecoin, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_gamecoin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_gc))
                .addContainerGap())
        );

        list_ownItem.setForeground(new java.awt.Color(0, 51, 255));
        list_ownItem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_ownItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        list_ownItem.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_ownItemValueChanged(evt);
            }
        });
        jscroll_own.setViewportView(list_ownItem);

        list_useItem.setForeground(new java.awt.Color(0, 153, 102));
        list_useItem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_useItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        list_useItem.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_useItemValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list_useItem);

        btn_add.setText(">>");
        btn_add.setEnabled(false);
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_remove.setText("<<");
        btn_remove.setEnabled(false);
        btn_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removeActionPerformed(evt);
            }
        });

        btn_removeAll.setText("Remove");
        btn_removeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removeAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addComponent(jscroll_own, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_remove, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_removeAll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_add, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jscroll_own)
            .addComponent(jScrollPane2)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btn_add)
                .addGap(18, 18, 18)
                .addComponent(btn_remove)
                .addGap(18, 18, 18)
                .addComponent(btn_removeAll)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        descrip.setEditable(false);
        descrip.setColumns(20);
        descrip.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        descrip.setForeground(new java.awt.Color(0, 102, 255));
        descrip.setLineWrap(true);
        descrip.setRows(5);
        descrip.setWrapStyleWord(true);
        jscroll_use.setViewportView(descrip);

        btn_ok.setText("OK");
        btn_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jscroll_use)
            .addGroup(footerLayout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(btn_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(footerLayout.createSequentialGroup()
                .addComponent(jscroll_use, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ok)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(footer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_okActionPerformed
        String c = tf_gamecoin.getText();
        if (tf_gamecoin.isEditable()) {
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Game coin is not null!");
                return;
            }
            if (!c.matches("[0-9]+")) {
                JOptionPane.showMessageDialog(this, "Game coin is invalid!");
                return;
            }
            if (Integer.parseInt(c) > 100000) {
                JOptionPane.showMessageDialog(this, "Game coin is invalid!");
                return;
            }
            if (Integer.parseInt(c) != ClientFrame.getClientFrame().getgCoin()) {
                ClientFrame.getClientFrame().sendMessage(CommonDefine.BOOM_GAME_GCOIN + CommonDefine.SEPARATOR_KEY_VALUE + c);
            }
        }
        //set item
        ClientFrame.getClientFrame().setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_btn_okActionPerformed

    private void list_ownItemValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_ownItemValueChanged
        if (!list_ownItem.isSelectionEmpty() && currentIndexOwn != list_ownItem.getSelectedIndex()) {
            currentIndexOwn = list_ownItem.getSelectedIndex();
            if (currentIndexOwn >= 0 && currentIndexOwn < listOwnItem.size()) {
                selectedItem = listOwnItem.get(currentIndexOwn);
                if (selectedItem != null) {
                    list_useItem.clearSelection();
                    currentIndexUse = -1;
                    btn_add.setEnabled(true);
                    btn_remove.setEnabled(false);
                    showItemInfo();
                }
            } else {
                currentIndexOwn = -1;
            }
        }
    }//GEN-LAST:event_list_ownItemValueChanged

    private void list_useItemValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_useItemValueChanged
        if (!list_useItem.isSelectionEmpty() && currentIndexUse != list_useItem.getSelectedIndex()) {
            currentIndexUse = list_useItem.getSelectedIndex();
            if (currentIndexUse >= 0 && currentIndexUse < listUseItem.size()) {
                selectedItem = listUseItem.get(currentIndexUse);
                if (selectedItem != null) {
                    list_ownItem.clearSelection();
                    currentIndexOwn = -1;
                    btn_add.setEnabled(false);
                    btn_remove.setEnabled(true);
                    showItemInfo();
                }
            } else {
                currentIndexUse = -1;
            }
        }
    }//GEN-LAST:event_list_useItemValueChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ClientFrame.getClientFrame().setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    public void closeSetting() {
        ClientFrame.getClientFrame().setEnabled(true);
        this.dispose();
    }

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        if (selectedItem == null) {
            return;
        }
        if (BoomGameClient.isBomb(selectedItem.getId())) {
            for (Equipment e : listUseItem) {
                if (BoomGameClient.isBomb(e.getId())) {
                    return;
                }
            }
        }
        if (ClientFrame.getClientFrame().getMapItemUse().containsKey(selectedItem.getId()) && ClientFrame.getClientFrame().getMapItemUse().get(selectedItem.getId()) >= selectedItem.getCarryNum()) {
            return;
        }
        if (ClientFrame.getClientFrame().getMapItemUse().size() >= MAX_ITEM_USE) {
            return;
        }
        ClientFrame.getClientFrame().addItemUse(selectedItem.getId());
        if (!ClientFrame.getClientFrame().getMapItemOwn().containsKey(selectedItem.getId())) {
            selectedItem = null;
            list_useItem.clearSelection();
            list_ownItem.clearSelection();
            btn_add.setEnabled(false);
            btn_remove.setEnabled(false);
        }
        updateItemData();
        if (currentIndexOwn >= 0 && currentIndexOwn < listOwnItem.size()) {
            list_ownItem.setSelectedIndex(currentIndexOwn);
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeActionPerformed
        if (selectedItem == null) {
            return;
        }
        ClientFrame.getClientFrame().removeItemUse(selectedItem.getId());
        if (!ClientFrame.getClientFrame().getMapItemUse().containsKey(selectedItem.getId())) {
            selectedItem = null;
            list_useItem.clearSelection();
            list_ownItem.clearSelection();
            btn_add.setEnabled(false);
            btn_remove.setEnabled(false);
        }
        updateItemData();
        if (currentIndexUse >= 0 && currentIndexUse < listUseItem.size()) {
            list_useItem.setSelectedIndex(currentIndexUse);
        }
    }//GEN-LAST:event_btn_removeActionPerformed

    private void btn_removeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removeAllActionPerformed
        ClientFrame.getClientFrame().removeAllItemUse();
        selectedItem = null;
        currentIndexUse = -1;
        currentIndexOwn = -1;
        list_useItem.clearSelection();
        list_ownItem.clearSelection();
        btn_add.setEnabled(false);
        btn_remove.setEnabled(false);
        updateItemData();
    }//GEN-LAST:event_btn_removeAllActionPerformed

    private void showItemInfo() {
        if (selectedItem == null) {
            return;
        }
        descrip.setText(null);
        descrip.append("Name: " + selectedItem.getName());
        descrip.append(CommonDefine.BREAK_LINE);
        if (BoomGameClient.isBombItem(selectedItem.getId()) || BoomGameClient.isBomb(selectedItem.getId())) {
            descrip.append("Damage: " + selectedItem.getDamage());
            descrip.append(CommonDefine.BREAK_LINE);
            descrip.append("Horizontal Size: " + selectedItem.getHorizontalLen());
            descrip.append(CommonDefine.BREAK_LINE);
            descrip.append("Vertial Size: " + selectedItem.getVerticalLen());
            descrip.append(CommonDefine.BREAK_LINE);
            descrip.append("Square Size: " + selectedItem.getSquareLen());
            descrip.append(CommonDefine.BREAK_LINE);
            descrip.append("Recover Ratio: " + selectedItem.getRecoverRatio());
            descrip.append(CommonDefine.BREAK_LINE);
        }
        descrip.append("Description: " + selectedItem.getDescrip());
        descrip.append(CommonDefine.BREAK_LINE);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_ok;
    private javax.swing.JButton btn_remove;
    private javax.swing.JButton btn_removeAll;
    private javax.swing.JTextArea descrip;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel header;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jscroll_own;
    private javax.swing.JScrollPane jscroll_use;
    private javax.swing.JLabel label_gc;
    private javax.swing.JList<String> list_ownItem;
    private javax.swing.JList<String> list_useItem;
    private javax.swing.JTextField tf_gamecoin;
    // End of variables declaration//GEN-END:variables
}