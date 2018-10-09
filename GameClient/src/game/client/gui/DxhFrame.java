/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.gui;

import game.client.socket.DayXeHangGameClient;
import game.client.common.CommonDefine;
import game.client.common.CommonMethod;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tkv-vule
 */
public class DxhFrame extends javax.swing.JFrame {

    private static DxhFrame sDxh;

    private Map<String, Image> mapImg = new HashMap<String, Image>();
    private Map<Integer, String> mapData = new HashMap<>();
    private int level = 0;
    private int BASE_REWARD = 0;
    private int countMove;
    private Map<Integer, int[][]> mapDataMove = new HashMap<>();
    private Map<Integer, int[]> mapDataXYMove = new HashMap<>();

    private boolean enableAddCoin = true;
    private boolean isEndgame = false;

    /**
     * Creates new form MainGame
     */
    public DxhFrame() {
        initComponents();
        setResizable(false);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/sokoban.png")).getImage());
    }
    
    public void updateBaseReward(int br) {
        if (br < 0) {
            br = 0;
        }
        BASE_REWARD = br;
        int reward = getRewardByLevel(this.level);
        lb_level.setText("Level: " + level + " Reward: " + reward + " Coin");
    }

    public static DxhFrame getDxhFrame() {
        if (sDxh == null) {
            sDxh = new DxhFrame();
            CommonMethod.setLocationFrame(sDxh);
        }
        return sDxh;
    }

    public void updateCoin(int level) {
        int total = 0;
        for (int i = 1; i <= level; i++) {
            total += getRewardByLevel(i);
        }
        lb_coins.setText("Total Reward: " + total + " Coin");
    }

    public void setCountMove(int countMove) {
        this.countMove = countMove;
    }

    public void setIsEndgame(boolean isEndgame) {
        this.isEndgame = isEndgame;
    }

    public void updateCoin() {
        if (enableAddCoin) {
            updateCoin(level);
            ClientFrame.getClientFrame().addCoin(getRewardByLevel(level));
        }
    }

    public void setEnableAddCoin(boolean flag) {
        enableAddCoin = flag;
    }

    public void setEndGame() {
        isEndgame = true;
    }

    public boolean isEndGame() {
        return isEndgame;
    }

    public void setLevel(int level) {
        this.level = level;
        setMap(level, null);
    }

    public void setLevel(int level, String data) {
        this.level = level;
        setMap(level, data);
    }

    public int getLevel() {
        return this.level;
    }

    public String getCurrentMapDxh() {
        int[][] curMap = DayXeHangGameClient.getCurrentMap();
        if (curMap != null) {
            StringBuilder sb = new StringBuilder();
            int len = curMap.length;
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    sb.append(curMap[i][j]);
                    if (j < len - 1) {
                        sb.append(",");
                    }
                }
                if (i < len - 1) {
                    sb.append(";");
                }
            }
            String flag = enableAddCoin ? "1" : "0";
            sb.append(" ").append(DayXeHangGameClient.getPx()).append(" ").append(DayXeHangGameClient.getPy()).append(" ").append(DayXeHangGameClient.getStatusPlayer()).append(" ").append(flag);
            return (sb.toString().trim());
        }
        return null;
    }

    public void initMapData(String data) {
        if (data.contains(CommonDefine.BREAK_LINE)) {
            String[] arr = data.split(CommonDefine.BREAK_LINE);
            for (String s : arr) {
                if (s.contains(":")) {
                    mapData.put(Integer.parseInt(s.split(":")[0].trim()), s.split(":")[1].trim());
                }
            }
        }
    }

    public void setMap(int level, String str_data) {
        if (mapData.isEmpty()) {
            return;
        }
        int mapValue[][];
        if (!mapData.containsKey(level)) {
            return;
        }
        if (str_data == null) {
            str_data = mapData.get(level);
        }
        String[] arrLine = str_data.split(";");
        mapValue = new int[arrLine.length][arrLine.length];
        int i = 0;
        for (String line : arrLine) {
            String[] d = line.split(",");
            int j = 0;
            for (String e : d) {
                mapValue[i][j] = Integer.parseInt(e.trim());
                j++;
            }
            i++;
        }
        int reward = getRewardByLevel(level);
        lb_level.setText("Level: " + level + " Reward: " + reward + " Coin");
        DayXeHangGameClient.initGameData(mapValue);
        DayXeHangGameClient.drawMainGame(display.getGraphics());
        if (DayXeHangGameClient.isGameEnd()) {
            DxhFrame.getDxhFrame().enableContinue();
            updateCoin(level);
            setEndGame();
        } else {
            updateCoin(level - 1);
        }
        if (!enableAddCoin) {
            lb_level.setText("Level: " + level + " Reward: Achieved!");
            updateCoin(level);
        }
    }

    private int getRewardByLevel(int level) {
        double x = (double) level / 2;
        return (int) ((2 * level - 1) * x * BASE_REWARD);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        display = new javax.swing.JPanel();
        btn_playagain = new javax.swing.JButton();
        lb_level = new javax.swing.JLabel();
        btn_continue = new javax.swing.JButton();
        lb_coins = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        btn_next = new javax.swing.JButton();
        lb_countMove = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Sokoban");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        display.setBackground(new java.awt.Color(0, 0, 0));
        display.setAutoscrolls(true);
        display.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        display.setPreferredSize(new java.awt.Dimension(800, 400));

        javax.swing.GroupLayout displayLayout = new javax.swing.GroupLayout(display);
        display.setLayout(displayLayout);
        displayLayout.setHorizontalGroup(
            displayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        displayLayout.setVerticalGroup(
            displayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btn_playagain.setText("Play again");
        btn_playagain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_playagainActionPerformed(evt);
            }
        });
        btn_playagain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_playagainKeyPressed(evt);
            }
        });

        lb_level.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_level.setForeground(new java.awt.Color(0, 51, 255));
        lb_level.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_level.setText("000");

        btn_continue.setText("Continue");
        btn_continue.setEnabled(false);
        btn_continue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_continueActionPerformed(evt);
            }
        });

        lb_coins.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_coins.setForeground(new java.awt.Color(0, 51, 255));
        lb_coins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_coins.setText("000");

        btn_back.setText("<");
        btn_back.setToolTipText("Back to Previous Move");
        btn_back.setEnabled(false);
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        btn_back.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_backKeyPressed(evt);
            }
        });

        btn_next.setText(">");
        btn_next.setToolTipText("Go to Next Move");
        btn_next.setEnabled(false);
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        btn_next.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_nextKeyPressed(evt);
            }
        });

        lb_countMove.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_countMove.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_countMove.setText("0");
        lb_countMove.setToolTipText("Total Move");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_level, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_coins, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_playagain, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_continue)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_back)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_countMove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_next)))
                .addGap(18, 18, 18)
                .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_continue, btn_playagain});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_back, btn_next});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(lb_level, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(181, 181, 181)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back)
                    .addComponent(btn_next)
                    .addComponent(lb_countMove, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lb_coins, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_playagain, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_continue))
                .addContainerGap(395, Short.MAX_VALUE))
            .addComponent(display, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_continue, btn_playagain});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_back, btn_next, lb_countMove});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int keyCode = evt.getKeyCode();
        runPlayer(keyCode);
    }//GEN-LAST:event_formKeyPressed

    public void resetMoveWhenEndGame() {
        mapDataMove.clear();
        mapDataXYMove.clear();
        btn_back.setEnabled(false);
        btn_next.setEnabled(false);
    }

    public void changeCountMove(int[][] mapdata, int px, int py, int status_player) {
        countMove++;
        lb_countMove.setText(String.valueOf(Integer.parseInt(lb_countMove.getText()) + 1));
        int curMove = Integer.parseInt(lb_countMove.getText());
        if (curMove < countMove) {
            countMove = curMove;
        }
        if (countMove > 1) {
            btn_back.setEnabled(true);
        }
        btn_next.setEnabled(false);
        //save map data
        int[][] hMapdata = new int[mapdata.length][mapdata.length];
        //hMapdata[0][0] = 1;
        for (int i = 0; i < hMapdata.length; i++) {
            System.arraycopy(mapdata[i], 0, hMapdata[i], 0, hMapdata.length);
        }
        mapDataMove.put(countMove, hMapdata);
        int[] flag_player = new int[3];
        flag_player[0] = px;
        flag_player[1] = py;
        flag_player[2] = status_player;
        mapDataXYMove.put(countMove, flag_player);
    }

    private void runPlayer(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                DayXeHangGameClient.moveUnit(display.getGraphics(), DayXeHangGameClient.IMG_MOVE_LEFT);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                DayXeHangGameClient.moveUnit(display.getGraphics(), DayXeHangGameClient.IMG_MOVE_RIGHT);
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                DayXeHangGameClient.moveUnit(display.getGraphics(), DayXeHangGameClient.IMG_MOVE_UP);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                DayXeHangGameClient.moveUnit(display.getGraphics(), DayXeHangGameClient.IMG_MOVE_DOWN);
                break;
            default:
                break;
        }
    }

    public void enableContinue() {
        btn_continue.setEnabled(true);
    }

    public void resetComponent() {
        enableAddCoin = true;
        isEndgame = false;
        countMove = 0;
        lb_countMove.setText(String.valueOf(countMove));
        mapDataMove.clear();
        mapDataXYMove.clear();
        btn_back.setEnabled(false);
        btn_next.setEnabled(false);
        btn_continue.setEnabled(false);
    }

    private void btn_playagainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_playagainKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_btn_playagainKeyPressed

    private void btn_playagainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_playagainActionPerformed
        if (isEndGame()) {
            enableAddCoin = false;
        }
        isEndgame = false;
        countMove = 0;
        lb_countMove.setText(String.valueOf(countMove));
        mapDataMove.clear();
        mapDataXYMove.clear();
        btn_back.setEnabled(false);
        btn_next.setEnabled(false);
        setMap(level, null);
    }//GEN-LAST:event_btn_playagainActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        DayXeHangGameClient.drawMainGame(display.getGraphics());
    }//GEN-LAST:event_formWindowActivated

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        DayXeHangGameClient.drawMainGame(display.getGraphics());
    }//GEN-LAST:event_formMouseEntered

    private void btn_continueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_continueActionPerformed
        enableAddCoin = true;
        isEndgame = false;
        countMove = 0;
        lb_countMove.setText(String.valueOf(countMove));
        mapDataMove.clear();
        mapDataXYMove.clear();
        btn_back.setEnabled(false);
        btn_next.setEnabled(false);
        setLevel(getLevel() + 1);
        ClientFrame.getClientFrame().setLevelDxh(getLevel());
        btn_continue.setEnabled(false);

    }//GEN-LAST:event_btn_continueActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DxhFrame.getDxhFrame().setVisible(false);
        ClientFrame.getClientFrame().setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        int curMove = Integer.parseInt(lb_countMove.getText());
        --curMove;
        lb_countMove.setText(String.valueOf(curMove));
        if (curMove == 1) {
            btn_back.setEnabled(false);
        }
        if (curMove < countMove) {
            btn_next.setEnabled(true);
        }
        DayXeHangGameClient.initGameData(mapDataMove.get(curMove));
        DayXeHangGameClient.setPlayerX(mapDataXYMove.get(curMove)[0]);
        DayXeHangGameClient.setPlayerY(mapDataXYMove.get(curMove)[1]);
        DayXeHangGameClient.setStatusPlayer(mapDataXYMove.get(curMove)[2]);
        DayXeHangGameClient.drawMainGame(display.getGraphics());
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        int curMove = Integer.parseInt(lb_countMove.getText());
        ++curMove;
        lb_countMove.setText(String.valueOf(curMove));
        if (curMove == countMove) {
            btn_next.setEnabled(false);
        }
        if (curMove > 1) {
            btn_back.setEnabled(true);
        }
        DayXeHangGameClient.initGameData(mapDataMove.get(curMove));
        DayXeHangGameClient.setPlayerX(mapDataXYMove.get(curMove)[0]);
        DayXeHangGameClient.setPlayerY(mapDataXYMove.get(curMove)[1]);
        DayXeHangGameClient.setStatusPlayer(mapDataXYMove.get(curMove)[2]);
        DayXeHangGameClient.drawMainGame(display.getGraphics());
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_backKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_backKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_btn_backKeyPressed

    private void btn_nextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_nextKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_btn_nextKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_continue;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_playagain;
    private javax.swing.JPanel display;
    private javax.swing.JLabel lb_coins;
    private javax.swing.JLabel lb_countMove;
    private javax.swing.JLabel lb_level;
    // End of variables declaration//GEN-END:variables

}
