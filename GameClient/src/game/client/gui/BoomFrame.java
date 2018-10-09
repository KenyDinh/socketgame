/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.gui;

import game.client.common.CommonDefine;
import game.client.common.CommonMethod;
import game.client.socket.BoomGameClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class BoomFrame extends JFrame {

    //<editor-fold defaultstate="collapsed" desc="Swing variable">
    private JPanel topPanel;
    private JPanel main_paint;

    private JPanel bombPanel;
    private JPanel coinPanel;
    private JPanel l3Panel;
    private JPanel l4Panel;
    private JPanel l5Panel;
    private JPanel l6Panel;
    private JPanel l7Panel;
    private JPanel timePanel;
    private JPanel directPanel;
    private JPanel speedPanel;
    private JPanel hpPanel;
    private JPanel ebPanel;
    private JPanel njPanel;
    private JPanel kbPanel;
    private JPanel prPanel;

    private JLabel lb_boom;
    private JPanel pn_boom;
    private JLabel lb_coin;
    private JPanel pn_coin;
    private JLabel lb_time_1;
    private JLabel lb_to;
    private JLabel lb_speed;
    private JPanel pn_speed;
    private JLabel lb_direct;
    private JPanel pn_direct;
    private JLabel lb_hp;
    private JPanel pn_hp;
    private JLabel lb_eb;
    private JPanel pn_eb;
    private JLabel lb_nj;
    private JPanel pn_nj;
    private JLabel lb_kb;
    private JPanel pn_kb;
    private JLabel lb_pr;
    private JPanel pn_pr;
    //</editor-fold>

    private boolean startGame;

    private boolean enableAction = true;
    private boolean isDeath;

    private final Color FILL_COLOR = new Color(240, 240, 240);
    private BoomGameClient boomClient;

    private static int UNIT_SIZE = 40;
    private static int FRAME_WIDTH = 800;

    public BoomFrame() {
        initFrameSize();
        initComponent();
        setTitle("Boom Boom!!!");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/super-man.png")).getImage());
    }

    //<editor-fold defaultstate="collapsed" desc="Init Swing Component">
    private void initFrameSize() {
        int sh = CommonMethod.getHeightScreen();
        int temp = (sh - UNIT_SIZE - 30 - 80) / 20;
        while (temp < UNIT_SIZE) {
            UNIT_SIZE -= 5;
        }
        FRAME_WIDTH = 20 * UNIT_SIZE;
        BoomGameClient.setSIZE(UNIT_SIZE);
        BoomGameClient.setMAP_SIZE(FRAME_WIDTH);
    }

    private void initComponent() {
        this.setSize(FRAME_WIDTH + 10, (FRAME_WIDTH + UNIT_SIZE + 30 + 30));

        topPanel = new JPanel();
        main_paint = new JPanel();

        bombPanel = new JPanel();
        coinPanel = new JPanel();
        l3Panel = new JPanel();
        l4Panel = new JPanel();
        l5Panel = new JPanel();
        l6Panel = new JPanel();
        l7Panel = new JPanel();
        timePanel = new JPanel();
        directPanel = new JPanel();
        speedPanel = new JPanel();
        hpPanel = new JPanel();
        ebPanel = new JPanel();
        njPanel = new JPanel();
        kbPanel = new JPanel();
        prPanel = new JPanel();

        lb_boom = new JLabel("00:00");
        pn_boom = new JPanel();
        lb_coin = new JLabel("0");
        pn_coin = new JPanel();
        lb_time_1 = new JLabel("Time");
        lb_to = new JLabel("00:00");
        lb_speed = new JLabel("00:00");
        pn_speed = new JPanel();
        lb_direct = new JLabel("");
        pn_direct = new JPanel();

        lb_eb = new JLabel("0");
        pn_eb = new JPanel();
        lb_hp = new JLabel("1000");
        pn_hp = new JPanel();
        lb_kb = new JLabel("00:00");
        pn_kb = new JPanel();
        lb_nj = new JLabel("00:00");
        pn_nj = new JPanel();
        lb_pr = new JLabel("00:00");
        pn_pr = new JPanel();

        initTopGridPanel(bombPanel);
        initTopGridPanel(coinPanel);
        initTopGridPanel(l3Panel);
        initTopGridPanel(l4Panel);
        initTopGridPanel(l5Panel);
        initTopGridPanel(l6Panel);
        initTopGridPanel(l7Panel);
        initTopGridPanel(timePanel);
        initTopGridPanel(speedPanel);
        initTopGridPanel(directPanel);
        initTopGridPanel(hpPanel);
        initTopGridPanel(ebPanel);
        initTopGridPanel(njPanel);
        initTopGridPanel(kbPanel);
        initTopGridPanel(prPanel);

        initTopLabel(lb_boom, new Color(0, 153, 153));
        pn_boom.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        bombPanel.add(lb_boom, BorderLayout.NORTH);
        bombPanel.add(pn_boom, BorderLayout.CENTER);

        initTopLabel(lb_coin, new Color(255, 193, 0));
        pn_coin.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        coinPanel.add(lb_coin, BorderLayout.NORTH);
        coinPanel.add(pn_coin, BorderLayout.CENTER);

        initTopLabel(lb_speed, new Color(15, 160, 172));
        pn_speed.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        speedPanel.add(lb_speed, BorderLayout.NORTH);
        speedPanel.add(pn_speed, BorderLayout.CENTER);

        initTopLabel(lb_direct, new Color(32, 206, 1));
        pn_direct.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        directPanel.add(lb_direct, BorderLayout.NORTH);
        directPanel.add(pn_direct, BorderLayout.CENTER);

        initTopLabel(lb_hp, new Color(220, 43, 0));
        pn_hp.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        hpPanel.add(lb_hp, BorderLayout.NORTH);
        hpPanel.add(pn_hp, BorderLayout.CENTER);

        initTopLabel(lb_eb, new Color(0, 77, 220));
        pn_eb.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        ebPanel.add(lb_eb, BorderLayout.NORTH);
        ebPanel.add(pn_eb, BorderLayout.CENTER);

        initTopLabel(lb_nj, new Color(94, 96, 98));
        pn_nj.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        njPanel.add(lb_nj, BorderLayout.NORTH);
        njPanel.add(pn_nj, BorderLayout.CENTER);

        initTopLabel(lb_kb, new Color(0, 77, 220));
        pn_kb.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        kbPanel.add(lb_kb, BorderLayout.NORTH);
        kbPanel.add(pn_kb, BorderLayout.CENTER);

        initTopLabel(lb_pr, new Color(255, 155, 0));
        pn_pr.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        prPanel.add(lb_pr, BorderLayout.NORTH);
        prPanel.add(pn_pr, BorderLayout.CENTER);

        initTopLabel(lb_time_1, new Color(64, 153, 50));
        lb_time_1.setFont(new java.awt.Font("Tahoma", 1, 11));
        lb_to.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE));
        lb_to.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_to.setFont(new java.awt.Font("Tahoma", 1, 11));
        lb_to.setForeground(new Color(64, 153, 50));
        timePanel.add(lb_time_1, BorderLayout.NORTH);
        timePanel.add(lb_to, BorderLayout.CENTER);

        topPanel.setPreferredSize(new Dimension(FRAME_WIDTH, UNIT_SIZE + 20 + 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.setLayout(new GridLayout(1, 15, 5, 10));
        topPanel.add(bombPanel);
        topPanel.add(coinPanel);
        topPanel.add(l3Panel);
        topPanel.add(l4Panel);
        topPanel.add(l5Panel);
        topPanel.add(l6Panel);
        topPanel.add(l7Panel);
        topPanel.add(timePanel);
        topPanel.add(speedPanel);
        topPanel.add(directPanel);
        topPanel.add(hpPanel);
        topPanel.add(ebPanel);
        topPanel.add(njPanel);
        topPanel.add(kbPanel);
        topPanel.add(prPanel);

        main_paint.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_WIDTH));
        main_paint.setBackground(new Color(130, 130, 130));

        this.add(topPanel, BorderLayout.NORTH);
        this.add(main_paint, BorderLayout.CENTER);
        this.setResizable(false);
        CommonMethod.setLocationFrame(this);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closingFrame(evt);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressAction(e);
            }

        });
        main_paint.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressAction(e);
            }
        });
    }

    private void initTopGridPanel(JPanel panel) {
        panel.setPreferredSize(new Dimension(UNIT_SIZE, UNIT_SIZE + 20));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setLayout(new BorderLayout());
    }

    private void initTopLabel(JLabel label, Color c) {
        label.setPreferredSize(new Dimension(UNIT_SIZE, 20));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setForeground(c);
    }
    //</editor-fold>

    public void setBoomClient(BoomGameClient boomClient) {
        this.boomClient = boomClient;
    }

    public Graphics getBoomGraphic() {
        return main_paint.getGraphics();
    }

    private boolean checkDialog = false;

    public void setTimeOut(int timeOut) {
        if (!isStartGame()) {
            return;
        }
        String s = String.format("%02d:%02d", timeOut / 60, timeOut % 60);
        lb_to.setText(s);
        if (timeOut <= 0) {
            setStartGame(false);
            ClientFrame.getClientFrame().resetBoomGame();
            StringBuilder mess = new StringBuilder();
            mess.append("This game is end: Time out").append(CommonDefine.BREAK_LINE).append("Coin: ").append(String.valueOf(boomClient.getM_coin()));
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    if (!checkDialog) {
                        this.dispose();
                        ClientFrame.getClientFrame().setVisible(true);
                        ClientFrame.getClientFrame().addCoin(boomClient.getM_coin());
                    }
                } catch (InterruptedException ex) {
                }
            }).start();
            JOptionPane.showMessageDialog(this, mess);
            checkDialog = true;
            this.dispose();
            ClientFrame.getClientFrame().setVisible(true);
            ClientFrame.getClientFrame().addCoin(boomClient.getM_coin());

        }
    }

    public void endGame() {
        int coin;
        StringBuilder mess = new StringBuilder();
        if (isDeath) {
            coin = 0;
            mess.append("This game is end: You Lose");
        } else {
            coin = boomClient.getM_coin() + (int) (boomClient.getG_coin());
            mess.append("This game is end: You Win").append(CommonDefine.BREAK_LINE).append("Coin: ").append(String.valueOf(coin));
        }
        setStartGame(false);
        ClientFrame.getClientFrame().resetBoomGame();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                if (!checkDialog) {
                    this.dispose();
                    ClientFrame.getClientFrame().setVisible(true);
                    ClientFrame.getClientFrame().addCoin(boomClient.getM_coin());
                }
            } catch (InterruptedException ex) {
            }
        }).start();
        JOptionPane.showMessageDialog(this, mess);
        checkDialog = true;
        this.dispose();
        ClientFrame.getClientFrame().setVisible(true);
        ClientFrame.getClientFrame().addCoin(coin);
    }

    public void enableAction() {
        this.enableAction = true;
    }

    public void disableAction() {
        this.enableAction = false;
    }

    public void updatePlayerDie() {
        this.isDeath = true;
    }

    public void resetPlayerStatus() {
        this.isDeath = false;
    }

    public void startBoom() {
        boomClient.drawLoadingBackground(main_paint.getGraphics(), null);
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
        if (this.startGame) {
            boomClient.reDrawPositionTimeLoad(main_paint.getGraphics());
        }
    }

    public void drawLoading(String time) {
        boomClient.drawLoadingBackground(main_paint.getGraphics(), time);
    }

    public void playerMove(int x, int y, int val, int status, int it, String type, int nx, int ny) {
        boomClient.drawPlayerMove(main_paint.getGraphics(), x, y, val, status, it, type, nx, ny);
    }

    public void addBoom(int x, int y, int val, int m_val) {
        boomClient.drawBoomCreate(main_paint.getGraphics(), val, x, y, m_val);
    }

    public void exlodeBoom(String line) {
        boomClient.drawBoomExplode(main_paint.getGraphics(), line);
    }

    public void addItemRandom(int x, int y, int val) {
        boomClient.createRandomItem(main_paint.getGraphics(), x, y, val);
    }

    private static final int SCALE_X = 4;

    //<editor-fold defaultstate="collapsed" desc="Update Icon">
    public void updateHpPlayer(int hp, int victim_val) {
        boomClient.updateHpPlayer(main_paint.getGraphics(), hp, victim_val);
    }

    public void updateBombCharge(int mytotal, int time) {
        if (mytotal + time == 0) {
            lb_boom.setText("00:00");
        }
        if (time != 0) {
            lb_boom.setText(mytotal + " + " + time);
        } else {
            lb_boom.setText(String.valueOf(mytotal));
        }
    }

    public void updateBombImage(Image img) {
        Graphics g = pn_boom.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updateNJTime(int time) {
        String s = String.format("%02d:%02d", time / 60, time % 60);
        lb_nj.setText(s);
    }

    public void updateNJImage(Image img) {
        Graphics g = pn_nj.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updateKBTime(int time) {
        String s = String.format("%02d:%02d", time / 60, time % 60);
        lb_kb.setText(s);
    }

    public void updateKBImage(Image img) {
        Graphics g = pn_kb.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updateEBTime(int count) {
        lb_eb.setText(String.valueOf(count));
    }

    public void updateEBImage(Image img) {
        Graphics g = pn_eb.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updateSpeedTime(int time) {
        String s = String.format("%02d:%02d", time / 60, time % 60);
        lb_speed.setText(s);
    }

    public void updateSpeedImage(Image img) {
        Graphics g = pn_speed.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updatePRTime(int time) {
        String s = String.format("%02d:%02d", time / 60, time % 60);
        lb_pr.setText(s);
    }

    public void updatePRImage(Image img) {
        Graphics g = pn_pr.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updateHPTime(int hp) {
        lb_hp.setText(String.valueOf(hp));
    }

    public void updateHPImage(Image img) {
        Graphics g = pn_hp.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updateCNTime(int coin) {
        String txt = String.valueOf(coin);
        if (coin > 999999) {
            txt = String.valueOf((int) (coin / 10000)) + "0K";
        } else if (coin > 99999) {
            txt = String.valueOf((int) (coin / 1000)) + "K";
        }
        lb_coin.setText(String.valueOf(txt));
    }

    public void updateCNImage(Image img) {
        Graphics g = pn_coin.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (img != null) {
            g.drawImage(img, SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }

    public void updatePlayerOut(int val) {
        boomClient.drawPlayerExit(main_paint.getGraphics(), val);
    }

    public void updateDirectImage() {
        Graphics g = pn_direct.getGraphics();
        g.setColor(FILL_COLOR);
        g.fillRect(SCALE_X, 0, UNIT_SIZE, UNIT_SIZE);
        if (boomClient.isBackBombDirection()) {
            g.drawImage(ClientFrame.getClientFrame().getListImageBoom().get("back_side"), SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        } else {
            g.drawImage(ClientFrame.getClientFrame().getListImageBoom().get("next_side"), SCALE_X, 0, UNIT_SIZE, UNIT_SIZE, null);
        }
    }
    //</editor-fold>

    ////////
    private void closingFrame(WindowEvent evt) {
        int choose = JOptionPane.showConfirmDialog(this, "Are you sure to quit!", "Notify", JOptionPane.YES_NO_CANCEL_OPTION);
        if (choose == JOptionPane.YES_OPTION) {
            setStartGame(false);
            this.dispose();
            ClientFrame.getClientFrame().resetBoomGame();
            ClientFrame.getClientFrame().setVisible(true);
            ClientFrame.getClientFrame().sendMessage(CommonDefine.NOTICE_CLIENT_OUT_GAME);
        }
    }

    private void keyPressAction(KeyEvent evt) {
        if (boomClient == null) {
            return;
        }
        if (isStartGame()) {
            int keycode = evt.getKeyCode();
            playerAction(keycode);
        }
    }

    private void playerAction(int keycode) {
        switch (keycode) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (enableAction && !isDeath) {
                    boomClient.movePlayer(BoomGameClient.STATUS_LEFT);
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (enableAction && !isDeath) {
                    boomClient.movePlayer(BoomGameClient.STATUS_RIGHT);
                }
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (enableAction && !isDeath) {
                    boomClient.movePlayer(BoomGameClient.STATUS_UP);
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (enableAction && !isDeath) {
                    boomClient.movePlayer(BoomGameClient.STATUS_DOWN);
                }
                break;
            case KeyEvent.VK_SPACE:
                if (!isDeath) {
                    boomClient.createBoom();
                }
                break;
            case KeyEvent.VK_NUMPAD0:
            case KeyEvent.VK_Q:
                if (!isDeath && boomClient.isHasBombDirection()) {
                    boomClient.toggleBackBombDirection();
                    updateDirectImage();
                }
                break;
            case KeyEvent.VK_F5:
                boomClient.drawMainGame(main_paint.getGraphics());
                break;
            default:
                break;
        }
    }

}
