/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.gui;

import game.client.common.CommonDefine;
import game.client.common.CommonMethod;
import game.client.element.Card;
import game.client.element.CardType;
import game.client.element.Equipment;
import game.client.element.ItemType;
import game.client.element.Player;
import game.client.image.ImageInit;
import game.client.socket.BauCuaGameClient;
import game.client.socket.BoomGameClient;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import game.client.socket.CaroGameClient;
import game.client.socket.DayXeHangGameClient;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JColorChooser;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author KenyDinh
 */
public class ClientFrame extends javax.swing.JFrame {

    /**
     * Creates new form ClientFrame
     */
    private DataInputStream dataRead;
    private DataOutputStream dataWrite;
    private boolean isConnect = false;
    private Socket clientSocket;
    private Thread receiveMessage;
    private Thread conn;
    private static ClientFrame clientFrame;
    private UnlockChat unlock;
    private boolean isShowNotify = false;
    private int gameType;
    private String hostAddress;
    private String username;
    private Integer portNumber;
    private boolean isBlockChat = false;
    private String textBlock = "---";
    private String MACAddress;
    private int totalCoins;
    private DefaultCaret caret;
    private Map<String, String> mapData = new HashMap<>();
    private StringBuilder chatHistory = new StringBuilder();
    private Map<String, String> mapConfig;
    //---------------------------------//
    private MapDesign mapDesign;
    //---------------------------------//
    //---------------dxh---------------//
    private boolean isFirst = true;
    private String curMap = null;
    private int px;
    private int py;
    private int status_player;
    private boolean enableAddCoin;

    private Map<String, Image> listImageDxh = new HashMap<>();

    //---------------caro game variable---------------//
    private int caroGameValue;//x or o
    private boolean isCaroGameEnd = false;
    private boolean isAllow = false;
    private boolean isHoverPlayerAgain = false;
    private int[][] caroBoardGame;
    private String wineLine;
    private boolean isAllowRepaint = false;
    private DefaultListModel<String> listClientName;
    private int currentXHover = -1;
    private int currentYHover = -1;
    private int currentX = -1;
    private int currentY = -1;
    //---------------caro game variable---------------//
    //---------------bau cua game variable---------------//
    private Map<String, Image> listImageBaucua = new HashMap<>();
    private String[] listUserInBauCua;
    private Image[] resultImgBauCua;
    private String[] tiencuocBauCua;
    private String[] resultStrBauCua;
    private boolean isOpenBauCua = true;
    private boolean isHostBauCua;
    private int levelDh = 1;
    private Map<Integer, Integer> baucuaChoose;
    //---------------bau cua game variable---------------//
    //---------------Boom game variable---------------//
    private Map<String, Image> listImageBoom = new HashMap<>();
    private List<String> listUserInBoom;
    private boolean isHostBoom;
    private boolean isEnableFighting;
    private int boomValue;
    private int boomMax;
    private int playerVal;
    private int timeCharge;
    private int luckyRatio;
    private int gCoin = 0;
    private int tCoin;
    public static int BOOM_MAX_HP = 1000;
    public static int BOOM_DEFAULT_VALUE = 40;
    public static int BOOM_DEFAULT_MAX_CREATE = 3;
    public static int BOOM_DEFAULT_TIMECHARGE = 3;
    public static int BOOM_DEFAULT_LUCKY_RATIO = 1;
    private BoomSettingFrame bsf;
    private BoomFrame boomFrame;
    private BoomGameClient boomClient;
    private ShopItem shopItem;
    private Inventory inventory;
    private Map<Integer, Integer> mapItemOwn = new HashMap<>();
    private Map<Integer, Integer> mapItemUse = new HashMap<>();
    private List<Equipment> listEquipment = new ArrayList<>();
    //------------------------------------------------//
    //---------------Card game variable---------------//
    private Map<String, Image> listImageCard = new HashMap<>();
    private FrameCard cardFrame;
    //------------------------------------------------//

    @SuppressWarnings("LeakingThisInConstructor")
    public ClientFrame() {
        initComponents();
        setResizable(false);
        CommonMethod.setLocationFrame(this);
        setTitle("MiniGame Client");
        initListGame();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/client.png")).getImage());
        //
        listImageDxh = ImageInit.initDxhImage();
        if (listImageDxh != null) {
            DayXeHangGameClient.initImage(listImageDxh);
        }
        //
        listImageBaucua = ImageInit.initBauCuaImage();
        //
        listImageBoom = ImageInit.initBoomImage();
        if (listImageBoom != null) {
            BoomGameClient.initImageGame(listImageBoom);
        }
        //
        listImageCard = ImageInit.initCardImage();
    }

    public void addItemUse(int itemVal) {
        if (mapItemOwn.containsKey(itemVal)) {
            int num_own = mapItemOwn.get(itemVal) - 1;
            if (num_own == 0) {
                mapItemOwn.remove(itemVal);
            } else {
                mapItemOwn.put(itemVal, num_own);
            }

            int num_use = 1;
            if (mapItemUse.containsKey(itemVal)) {
                num_use += mapItemUse.get(itemVal);
            }
            mapItemUse.put(itemVal, num_use);
        }
    }

    public void removeItemUse(int itemVal) {
        if (mapItemUse.containsKey(itemVal)) {
            int num_own = 1;
            if (mapItemOwn.containsKey(itemVal)) {
                num_own += mapItemOwn.get(itemVal);
            }
            mapItemOwn.put(itemVal, num_own);

            int num_use = mapItemUse.get(itemVal) - 1;
            if (num_use == 0) {
                mapItemUse.remove(itemVal);
            } else {
                mapItemUse.put(itemVal, num_use);
            }
        }
    }

    public void removeAllItemUse() {
        for (Entry<Integer, Integer> entry : mapItemUse.entrySet()) {
            if (mapItemOwn.containsKey(entry.getKey())) {
                mapItemOwn.put(entry.getKey(), mapItemOwn.get(entry.getKey()) + entry.getValue());
            }
        }
        mapItemUse = new HashMap<>();
    }

    public Equipment getEquipmentById(int id) {
        for (Equipment e : listEquipment) {
            if (e != null && e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public List<Equipment> getListEquipment() {
        return listEquipment;
    }

    public Map<Integer, Integer> getMapItemUse() {
        return mapItemUse;
    }

    public void updateMapItemUse(int itemVal, int itemNum) {
        if (mapItemUse.containsKey(itemVal)) {
            itemNum += mapItemUse.get(itemVal);
        }
        mapItemUse.put(itemVal, itemNum);
    }

    public Map<Integer, Integer> getMapItemOwn() {
        return mapItemOwn;
    }

    public void updateMapItemOwn(int itemVal, int itemNum) {
        if (mapItemOwn.containsKey(itemVal)) {
            itemNum += mapItemOwn.get(itemVal);
        }
        mapItemOwn.put(itemVal, itemNum);
    }

    private void initMapItemOwn(String data) {
        if (data.contains(CommonDefine.COMMA)) {
            for (String s : data.split(CommonDefine.COMMA)) {
                String arr[] = s.split("-");
                if (arr.length == 2) {
                    mapItemOwn.put(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()));
                }
            }
        }
    }

    public Map<String, Image> getListImageBoom() {
        return listImageBoom;
    }

    public Map<String, String> getMapConfig() {
        return mapConfig;
    }

    public void setMapConfig(Map<String, String> mapConfig) {
        this.mapConfig = mapConfig;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getUsername() {
        return username;
    }

    public void addCoin(int coin) {
        totalCoins += coin;
        if (totalCoins <= 0) {
            totalCoins = 0;
        }
        sendMessage(CommonDefine.SETTING_UPDATE_COIN + totalCoins);
        updateLbCoin();

    }

    public int getTotalCoint() {
        return totalCoins;
    }

    private void updateLbCoin() {
        lb_coin.setText("[" + getUsername() + "] Coin: " + totalCoins);
    }

    public void setLevelDxh(int level) {
        this.levelDh = level;
    }

    public int getLevelDxh() {
        return levelDh;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    private void initListGame() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(CommonDefine.LIST_GAME);
        cbb_choose_game.setModel(model);
    }

    public static ClientFrame getClientFrame() {
        if (clientFrame == null) {
            clientFrame = new ClientFrame();
        }
        return clientFrame;
    }

    public void connectToServer(String host, Integer port, String name, String mac) {
        this.hostAddress = host;
        this.username = name;
        this.portNumber = port;
        this.MACAddress = mac;
        if (!isConnect) {
            conn = new Thread(new ConnectServer(host, port, name, mac));
            conn.start();
            this.setVisible(true);
        }
    }

    private void appentTextToChatBox(String mess) {
        chatHistory.append("<div style=\"width:240px;\">").append(mess).append("</div>");//.append("<br>");
        main_chat.setText(chatHistory.toString());
        main_chat.setCaretPosition(main_chat.getDocument().getLength());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tf_mess = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        game_notice = new javax.swing.JPanel();
        btn_reconnect = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        cbb_choose_game = new javax.swing.JComboBox<>();
        btn_find_match = new javax.swing.JButton();
        notify_state = new javax.swing.JLabel();
        btnShop = new javax.swing.JButton();
        btnInventory = new javax.swing.JButton();
        panel_action = new javax.swing.JPanel();
        lb_coin = new javax.swing.JLabel();
        btn_dxh = new javax.swing.JButton();
        scroll_user = new javax.swing.JScrollPane();
        list_user = new javax.swing.JList();
        main_game = new javax.swing.JPanel();
        cbb_chat_type = new javax.swing.JComboBox<>();
        scroll_chat = new javax.swing.JScrollPane();
        main_chat = new javax.swing.JEditorPane();
        game_menu = new javax.swing.JMenuBar();
        menu_file = new javax.swing.JMenu();
        menu_edit = new javax.swing.JMenu();
        menu_edit_caro = new javax.swing.JMenu();
        change_color_X = new javax.swing.JMenuItem();
        change_color_O = new javax.swing.JMenuItem();
        menu_tool = new javax.swing.JMenu();
        menu_design = new javax.swing.JMenuItem();
        menu_help = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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

        tf_mess.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        tf_mess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_messKeyPressed(evt);
            }
        });

        btn_send.setText("Send");
        btn_send.setEnabled(false);
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        btn_reconnect.setText("Reconnect");
        btn_reconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reconnectActionPerformed(evt);
            }
        });

        btn_back.setText("Back");
        btn_back.setToolTipText("Back To Join Game");
        btn_back.setEnabled(false);
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        cbb_choose_game.setMaximumRowCount(10);
        cbb_choose_game.setEnabled(false);
        cbb_choose_game.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbb_choose_gameItemStateChanged(evt);
            }
        });

        btn_find_match.setText("Finding Match");
        btn_find_match.setToolTipText("");
        btn_find_match.setEnabled(false);
        btn_find_match.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_find_matchActionPerformed(evt);
            }
        });

        notify_state.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        notify_state.setForeground(new java.awt.Color(0, 51, 255));
        notify_state.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnShop.setText("Shop");
        btnShop.setEnabled(false);
        btnShop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShopActionPerformed(evt);
            }
        });

        btnInventory.setText("Inventory");
        btnInventory.setEnabled(false);
        btnInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout game_noticeLayout = new javax.swing.GroupLayout(game_notice);
        game_notice.setLayout(game_noticeLayout);
        game_noticeLayout.setHorizontalGroup(
            game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game_noticeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(game_noticeLayout.createSequentialGroup()
                        .addComponent(btn_reconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbb_choose_game, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(game_noticeLayout.createSequentialGroup()
                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_find_match, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 362, Short.MAX_VALUE)
                .addComponent(notify_state, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addGroup(game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnInventory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnShop, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addContainerGap())
        );
        game_noticeLayout.setVerticalGroup(
            game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game_noticeLayout.createSequentialGroup()
                .addGroup(game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_reconnect)
                    .addComponent(cbb_choose_game, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notify_state, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(game_noticeLayout.createSequentialGroup()
                        .addGroup(game_noticeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_back)
                            .addComponent(btn_find_match))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(game_noticeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnInventory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShop))
        );

        panel_action.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Info", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(153, 153, 153))); // NOI18N

        lb_coin.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        lb_coin.setForeground(new java.awt.Color(0, 0, 255));
        lb_coin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_coin.setText(" ");

        btn_dxh.setText("Đẩy xe hàng");
        btn_dxh.setEnabled(false);
        btn_dxh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dxhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_actionLayout = new javax.swing.GroupLayout(panel_action);
        panel_action.setLayout(panel_actionLayout);
        panel_actionLayout.setHorizontalGroup(
            panel_actionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_actionLayout.createSequentialGroup()
                .addComponent(lb_coin, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_dxh)
                .addContainerGap())
        );
        panel_actionLayout.setVerticalGroup(
            panel_actionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_actionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_actionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_actionLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lb_coin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_actionLayout.createSequentialGroup()
                        .addComponent(btn_dxh)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        list_user.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        list_user.setForeground(new java.awt.Color(102, 102, 102));
        list_user.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        list_user.setSelectionBackground(CommonDefine.JLIST_SELECTED_BACKGROUND);
        list_user.setSelectionForeground(CommonDefine.JLIST_SELECTED_FOREGROUND);
        scroll_user.setViewportView(list_user);

        main_game.setBackground(new java.awt.Color(255, 255, 255));
        main_game.setAutoscrolls(true);
        main_game.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                main_gameMouseMoved(evt);
            }
        });
        main_game.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                main_gameMouseClicked(evt);
            }
        });

        cbb_chat_type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "In Game", "Private" }));

        main_chat.setEditable(false);
        main_chat.setContentType("text/html"); // NOI18N
        main_chat.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        main_chat.setText("");
        scroll_chat.setViewportView(main_chat);

        menu_file.setMnemonic('F');
        menu_file.setText("File");
        game_menu.add(menu_file);

        menu_edit.setMnemonic('E');
        menu_edit.setText("Edit");

        menu_edit_caro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/color_xo.png"))); // NOI18N
        menu_edit_caro.setText("Caro Color");

        change_color_X.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        change_color_X.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/caro_X.png"))); // NOI18N
        change_color_X.setText("Color X");
        change_color_X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_color_XActionPerformed(evt);
            }
        });
        menu_edit_caro.add(change_color_X);

        change_color_O.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        change_color_O.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/caro_O.png"))); // NOI18N
        change_color_O.setText("Color O");
        change_color_O.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_color_OActionPerformed(evt);
            }
        });
        menu_edit_caro.add(change_color_O);

        menu_edit.add(menu_edit_caro);

        game_menu.add(menu_edit);

        menu_tool.setMnemonic('T');
        menu_tool.setText("Tools");

        menu_design.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        menu_design.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/client/iconImage/design.png"))); // NOI18N
        menu_design.setText("Design Map");
        menu_design.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_designActionPerformed(evt);
            }
        });
        menu_tool.add(menu_design);

        game_menu.add(menu_tool);

        menu_help.setMnemonic('H');
        menu_help.setText("Help");
        game_menu.add(menu_help);

        setJMenuBar(game_menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game_notice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panel_action, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(scroll_user, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                    .addComponent(cbb_chat_type, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tf_mess, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(scroll_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(5, 5, 5)
                        .addComponent(main_game, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                        .addGap(5, 5, 5))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(game_notice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(main_game, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(panel_action, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scroll_user, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                            .addComponent(scroll_chat))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_mess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_send)
                            .addComponent(cbb_chat_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        if (isBlockChat) {
            appentTextToChatBox(CommonMethod.formatMessageServer(textBlock));
            return;
        }
        String mess = tf_mess.getText();
        if (mess == null || mess.length() == 0) {
            return;
        }
        if (mess.length() > 255) {
            mess = mess.substring(0, 255);
        }
        if (mess.contains(CommonDefine.SETTING_KEY)) {
            mess = mess.replace(CommonDefine.SETTING_KEY, "***");//|| mess.contains(CommonDefine.NOTICE_MESSAGE_KEY) || mess.contains(CommonDefine.CARO_GAME_KEY) || mess.contains(CommonDefine.BAUCUA_GAME_KEY)){
        }
        if (mess.contains(CommonDefine.NOTICE_MESSAGE_KEY)) {
            mess = mess.replace(CommonDefine.NOTICE_MESSAGE_KEY, "***");
        }
        if (mess.contains(CommonDefine.CARO_GAME_KEY)) {
            mess = mess.replace(CommonDefine.CARO_GAME_KEY, "***");
        }
        if (mess.contains(CommonDefine.BAUCUA_GAME_KEY)) {
            mess = mess.replace(CommonDefine.BAUCUA_GAME_KEY, "***");
        }
        if (mess.contains("<")) {
            mess = mess.replace("<", "&lt;");
        }
        if (mess.contains(">")) {
            mess = mess.replace(">", "&gt;");
        }
        int index_c = cbb_chat_type.getSelectedIndex();
        switch (index_c) {
            case 0:
                sendMessage(mess.trim());
                break;
            case 1:
                sendMessage(CommonDefine.NOTICE_CHAT_IN_GAME + CommonDefine.SEPARATOR_KEY_VALUE + mess.trim());
                break;
            case 2:
                if (!list_user.getSelectedValuesList().isEmpty()) {
                    for (Object object : list_user.getSelectedValuesList()) {
                        String to = object.toString();
                        if (!getUsername().equals(to)) {
                            sendMessage(CommonDefine.NOTICE_CHAT_PRIVATE + CommonDefine.SEPARATOR_KEY_VALUE + to + CommonDefine.SEPARATOR_KEY_VALUE + mess.trim());
                        }
                    }
                }
                break;
            default:
                break;
        }
        tf_mess.setText("");
        tf_mess.requestFocus();
    }//GEN-LAST:event_btn_sendActionPerformed

    private void tf_messKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_messKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_sendActionPerformed(null);
        }
    }//GEN-LAST:event_tf_messKeyPressed

    private void btn_reconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reconnectActionPerformed
        if (!isConnect) {
            btn_reconnect.setEnabled(false);
            conn = new Thread(new ConnectServer(hostAddress, portNumber, getUsername(), MACAddress));
            conn.start();

        }
    }//GEN-LAST:event_btn_reconnectActionPerformed

    private void btn_find_matchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_find_matchActionPerformed
        String str_room = null;
        if (gameType != 0) {
            str_room = JOptionPane.showInputDialog(ClientFrame.getClientFrame(), CommonDefine.INFO_ROOM_NUMBER_INPUT_MESS, CommonDefine.INFO_ROOM_NUMBER_INPUT_TITLE, JOptionPane.PLAIN_MESSAGE);
        }
        if (str_room == null) {
            return;
        }
        if (str_room.trim().length() > 10 || str_room.trim().length() == 0) {
            str_room = "0";
        }
        switch (gameType) {
            case CommonDefine.GAME_TYPE_CARO_GAME:
                isShowNotify = true;
                showNotifyState();
                caroBoardGame = new int[CommonDefine.CARO_GAME_NUM_COLUMNS][CommonDefine.CARO_GAME_NUM_ROWS];
                sendMessage(CommonDefine.NOTICE_FINDING_MATCH + CommonDefine.SEPARATOR_KEY_VALUE + str_room + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.GAME_TYPE_CARO_GAME);
                btn_find_match.setEnabled(false);
                cbb_choose_game.setEnabled(false);
                break;
            case CommonDefine.GAME_TYPE_BTCC_GAME:
                if (listImageBaucua == null) {
                    JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Image is missing! Can not start game.");
                    backToMainGame();
                    gameType = 0;
                    cbb_choose_game.setEnabled(true);
                    cbb_choose_game.setSelectedIndex(0);
                    btn_find_match.setEnabled(false);
                    break;
                }
                isShowNotify = true;
                showNotifyState();
                listUserInBauCua = new String[CommonDefine.BAUCUA_GAME_MAX_PLAYER];
                resultImgBauCua = new Image[3];
                resultStrBauCua = new String[3];
                tiencuocBauCua = new String[6];
                baucuaChoose = new HashMap<>();
                isOpenBauCua = true;
                sendMessage(CommonDefine.NOTICE_FINDING_MATCH + CommonDefine.SEPARATOR_KEY_VALUE + str_room + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.GAME_TYPE_BTCC_GAME);
                btn_find_match.setEnabled(false);
                cbb_choose_game.setEnabled(false);
                break;
            case CommonDefine.GAME_TYPE_BOOM_GAME:
                if (listImageBoom == null) {
                    JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Image is missing! Can not start game.");
                    backToMainGame();
                    gameType = 0;
                    cbb_choose_game.setEnabled(true);
                    cbb_choose_game.setSelectedIndex(0);
                    btn_find_match.setEnabled(false);
                    break;
                }
                boomFrame = new BoomFrame();
                boomClient = new BoomGameClient(boomFrame);
                boomFrame.setBoomClient(boomClient);
                isShowNotify = true;
                showNotifyState();
                sendMessage(CommonDefine.NOTICE_FINDING_MATCH + CommonDefine.SEPARATOR_KEY_VALUE + str_room + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.GAME_TYPE_BOOM_GAME);
                btn_find_match.setEnabled(false);
                BoomGameClient.rePaintMainGame(main_game.getGraphics());
                cbb_choose_game.setEnabled(false);
                break;
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                if (listImageCard == null) {
                    JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Image is missing! Can not start game.");
                    backToMainGame();
                    gameType = 0;
                    cbb_choose_game.setEnabled(true);
                    cbb_choose_game.setSelectedIndex(0);
                    btn_find_match.setEnabled(false);
                    break;
                }
                cardFrame = new FrameCard(listImageCard);
                cardFrame.initLimitCardNum(gameType);
                sendMessage(CommonDefine.NOTICE_FINDING_MATCH + CommonDefine.SEPARATOR_KEY_VALUE + str_room + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.GAME_TYPE_TLMN_GAME);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_find_matchActionPerformed

    private void main_gameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_main_gameMouseClicked
        //caro game
        if (isAllow) {
            int _x = evt.getX();
            int _y = evt.getY();
            switch (gameType) {
                case CommonDefine.GAME_TYPE_CARO_GAME:
                    if (caroGameValue != 0) {
                        if (_x <= CommonDefine.CARO_GAME_X_START || _x >= (CommonDefine.MAIN_GAME_WIDTH - CommonDefine.CARO_GAME_X_START) || _y <= CommonDefine.CARO_GAME_Y_START || _y >= (CommonDefine.MAIN_GAME_HEIGHT - CommonDefine.CARO_GAME_Y_START)) {
                            return;
                        }
                        int x_coordinate = _x - CommonDefine.CARO_GAME_X_START;
                        int y_coordinate = _y - CommonDefine.CARO_GAME_Y_START;
                        if (isCaroGameEnd) {
                            if ((CommonDefine.CARO_GAME_BUTTON_PA_MIN_X <= x_coordinate || x_coordinate >= CommonDefine.CARO_GAME_BUTTON_PA_MAX_X) && (CommonDefine.CARO_GAME_BUTTON_PA_MIN_Y <= y_coordinate || y_coordinate >= CommonDefine.CARO_GAME_BUTTON_PA_MAX_Y)) {
                                CaroGameClient.rePaintMainGame(main_game.getGraphics());
                                caroGameValue = 0;
                                isAllowRepaint = false;
                                caroBoardGame = new int[CommonDefine.CARO_GAME_NUM_COLUMNS][CommonDefine.CARO_GAME_NUM_ROWS];
                                isCaroGameEnd = false;
                                sendMessage(CommonDefine.CARO_GAME_PLAY_AGAIN);
                                notify_state.setText("");
                                isAllow = false;
                                currentX = -1;
                                currentY = -1;
                                currentXHover = -1;
                                currentYHover = -1;
                            }
                            return;
                        }
                        //
                        if (x_coordinate % CommonDefine.CARO_GAME_CELL_SIZE != 0 && y_coordinate % CommonDefine.CARO_GAME_CELL_SIZE != 0) {
                            int x_column = x_coordinate / CommonDefine.CARO_GAME_CELL_SIZE;
                            int y_row = y_coordinate / CommonDefine.CARO_GAME_CELL_SIZE;
                            if (0 <= x_column && x_column < CommonDefine.CARO_GAME_NUM_COLUMNS && 0 <= y_row && y_row < CommonDefine.CARO_GAME_NUM_ROWS) {
                                if (caroBoardGame[x_column][y_row] == 0) {
                                    String mess = CommonDefine.CARO_GAME_POSITION + CommonDefine.SEPARATOR_KEY_VALUE + String.valueOf(x_column) + CommonDefine.SEPARATOR_KEY_VALUE + String.valueOf(y_row) + CommonDefine.SEPARATOR_KEY_VALUE + caroGameValue;
                                    sendMessage(mess);
                                    isAllow = false;
                                }
                            }
                        }
                    }
                    break;
                case CommonDefine.GAME_TYPE_BTCC_GAME:
                    if (!isHostBauCua) {
                        if (totalCoins >= 10) {//min coin to play
                            int choice = BauCuaGameClient.getChoice(_x, _y);
                            if (choice != 0) {
                                String type = CommonMethod.getChooseTextString(choice);
                                String str = JOptionPane.showInputDialog(ClientFrame.getClientFrame(), "Nhập số tiền cược", "Bạn có chắc đặt cược cửa " + type + " ?", JOptionPane.PLAIN_MESSAGE);
                                if (CommonMethod.isNumberic(str)) {
                                    if (Integer.parseInt(str.trim()) <= totalCoins && Integer.parseInt(str.trim()) > 0) {
                                        if (isAllow) {
                                            sendMessage(CommonDefine.BAUCUA_GAME_VALUE_CHOOSE + CommonDefine.SEPARATOR_KEY_VALUE + choice + CommonDefine.SEPARATOR_KEY_VALUE + str);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (310 < _x && _x < 360 && 400 < _y && _y <= 425) {
                            isAllow = false;
                            if (isOpenBauCua) {
                                sendMessage(CommonDefine.BAUCUA_GAME_START_SHAKE);
                            } else {
                                sendMessage(CommonDefine.BAUCUA_GAME_START_OPEN);
                            }
                        }
                    }
                    break;
                case CommonDefine.GAME_TYPE_BOOM_GAME:
                    if (BoomGameClient.READY_X < _x && _x < (BoomGameClient.READY_X + BoomGameClient.READY_SIZE) && BoomGameClient.READY_Y < _y && _y < (BoomGameClient.READY_Y + BoomGameClient.READY_SIZE)) {
                        if (totalCoins >= gCoin) {
                            boomClient.toggleReady();
                            sendMessage(CommonDefine.BOOM_GAME_READY);
                            boomClient.drawReadyIcon(main_game.getGraphics());
                        } else {
                            JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "You not enough coin!");
                        }
                    } else if (BoomGameClient.SETTING_X < _x && _x < (BoomGameClient.SETTING_X + BoomGameClient.SETTING_SIZE) && BoomGameClient.SETTING_Y < _y && _y < (BoomGameClient.SETTING_Y + BoomGameClient.SETTING_SIZE)) {
                        //JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "This function is not available.");
                        if (!boomClient.IsReady()) {
                            bsf = new BoomSettingFrame(gCoin);
                            if (!isHostBoom) {
                                bsf.disableCoinSetting();
                            }
                            bsf.setVisible(true);
                            ClientFrame.getClientFrame().setEnabled(false);
                        }
                    } else if (BoomGameClient.FIGHT_X < _x && _x < (BoomGameClient.FIGHT_X + BoomGameClient.FIGHT_WIDHT) && BoomGameClient.FIGHT_Y < _y && _y < (BoomGameClient.FIGHT_Y + BoomGameClient.FIGHT_HEIGHT)) {
                        if (isEnableFighting) {
                            sendMessage(CommonDefine.BOOM_GAME_START_BATTLE);//////test
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_main_gameMouseClicked

    private void main_gameMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_main_gameMouseMoved
        if (isAllow) {
            int _x = evt.getX();
            int _y = evt.getY();
            switch (gameType) {
                case CommonDefine.GAME_TYPE_CARO_GAME:
                    main_game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    int x = _x - CommonDefine.CARO_GAME_X_START;
                    int y = _y - CommonDefine.CARO_GAME_Y_START;
                    if (isCaroGameEnd && caroGameValue != 0) {
                        if ((CommonDefine.CARO_GAME_BUTTON_PA_MIN_X <= x || x >= CommonDefine.CARO_GAME_BUTTON_PA_MAX_X) && (CommonDefine.CARO_GAME_BUTTON_PA_MIN_Y <= y || y >= CommonDefine.CARO_GAME_BUTTON_PA_MAX_Y)) {
                            if (!isHoverPlayerAgain) {
                                CaroGameClient.drawButtonPlayAgain(main_game.getGraphics(), CommonDefine.CARO_COLOR_PA_BACK_HOVER, CommonDefine.CARO_COLOR_PA_TEXT_HOVER);
                                isHoverPlayerAgain = true;
                            }
                        } else {
                            if (isHoverPlayerAgain) {
                                CaroGameClient.drawButtonPlayAgain(main_game.getGraphics(), CommonDefine.CARO_COLOR_PA_BACKGROUND, CommonDefine.CARO_COLOR_PA_TEXT);
                                isHoverPlayerAgain = false;
                            }
                        }
                    } //show X or O when the cell is hover
//                    else if (caroGameValue != 0) {
//                        int x_column = x / CommonDefine.CARO_GAME_CELL_SIZE;
//                        int y_row = y / CommonDefine.CARO_GAME_CELL_SIZE;
//                        if (0 <= x_column && x_column < CommonDefine.CARO_GAME_NUM_COLUMNS && 0 <= y_row && y_row < CommonDefine.CARO_GAME_NUM_ROWS) {
//                            if (currentXHover != x_column || currentYHover != y_row) {
//                                rePaintCaroGame();
//                                currentXHover = x_column;
//                                currentYHover = y_row;
//                                if (caroBoardGame[x_column][y_row] == 0) {
//                                    CaroGameClient.drawPositionHover(main_game.getGraphics(), caroGameValue, currentXHover, currentYHover);
//                                }
//                            }
//
//                        }
//                    }
                    break;
                case CommonDefine.GAME_TYPE_BTCC_GAME:
                    if (310 < _x && _x < 360 && 400 < _y && _y <= 425) {
                        main_game.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        main_game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    break;
                case CommonDefine.GAME_TYPE_BOOM_GAME:
                    if (BoomGameClient.READY_X < _x && _x < (BoomGameClient.READY_X + BoomGameClient.READY_SIZE) && BoomGameClient.READY_Y < _y && _y < (BoomGameClient.READY_Y + BoomGameClient.READY_SIZE)) {
                        main_game.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else if (BoomGameClient.SETTING_X < _x && _x < (BoomGameClient.SETTING_X + BoomGameClient.SETTING_SIZE) && BoomGameClient.SETTING_Y < _y && _y < (BoomGameClient.SETTING_Y + BoomGameClient.SETTING_SIZE)) {
                        if (!boomClient.IsReady()) {
                            main_game.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        } else {
                            main_game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    } else if (BoomGameClient.FIGHT_X < _x && _x < (BoomGameClient.FIGHT_X + BoomGameClient.FIGHT_WIDHT) && BoomGameClient.FIGHT_Y < _y && _y < (BoomGameClient.FIGHT_Y + BoomGameClient.FIGHT_HEIGHT)) {
                        if (isEnableFighting) {
                            main_game.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        } else {
                            main_game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    } else {
                        main_game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_main_gameMouseMoved

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        if (gameType != 0) {
            gameType = 0;
            sendMessage(CommonDefine.NOTICE_CLIENT_OUT_GAME);
        }
        backToMainGame();
        cbb_choose_game.setEnabled(true);
        cbb_choose_game.setSelectedIndex(0);
        btn_find_match.setEnabled(false);
    }//GEN-LAST:event_btn_backActionPerformed

    private void cbb_choose_gameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbb_choose_gameItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            int game_type = getGameType(evt.getItem().toString());
            gameType = game_type;
            if (gameType != 0) {
                btn_find_match.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cbb_choose_gameItemStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeConnection(false, true);
        if (!CommonMethod.createFileWithContent(CommonDefine.CONFIG_FILE_NAME, CommonMethod.formatMapToContentFile(getMapConfig()))) {
            JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Can't save the data file!");
        }
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void btn_dxhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dxhActionPerformed
        if (listImageDxh == null) {
            JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Image is missing! Can not start game.");
            return;
        }
        DxhFrame.getDxhFrame().setVisible(true);
        ClientFrame.getClientFrame().setVisible(false);
        if (curMap == null) {
            if (isFirst) {
                DxhFrame.getDxhFrame().setLevel(levelDh);
                isFirst = false;
            }
        } else {
            DayXeHangGameClient.setPlayerX(px);
            DayXeHangGameClient.setPlayerY(py);
            DayXeHangGameClient.setStatusPlayer(status_player);
            DxhFrame.getDxhFrame().setEnableAddCoin(enableAddCoin);
            DxhFrame.getDxhFrame().setLevel(levelDh, curMap);
            curMap = null;
            isFirst = false;
        }

    }//GEN-LAST:event_btn_dxhActionPerformed

    private void btnShopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShopActionPerformed
        if (gameType == CommonDefine.GAME_TYPE_BOOM_GAME) {
            if (boomClient != null && boomClient.IsReady()) {
                JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Cannot modify anything when you are ready!");
                return;
            }
        }
        if (shopItem != null && shopItem.isVisible()) {
            shopItem.dispose();
            ClientFrame.getClientFrame().setEnabled(true);
        }
        shopItem = new ShopItem();
        CommonMethod.setLocationFrame(shopItem);
        shopItem.setVisible(true);
        ClientFrame.getClientFrame().setEnabled(false);
    }//GEN-LAST:event_btnShopActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        if (gameType == CommonDefine.GAME_TYPE_BOOM_GAME) {
            if (boomClient != null && boomClient.IsReady()) {
                JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Cannot modify anything when you are ready!");
                return;
            }
        }
        if (inventory != null && inventory.isVisible()) {
            inventory.dispose();
            ClientFrame.getClientFrame().setEnabled(true);
        }
        inventory = new Inventory();
        CommonMethod.setLocationFrame(inventory);
        inventory.setVisible(true);
        ClientFrame.getClientFrame().setEnabled(false);
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void menu_designActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_designActionPerformed
        if (mapDesign != null && mapDesign.isVisible()) {
            return;
        }
        mapDesign = new MapDesign(listImageBoom);
        mapDesign.setVisible(true);
    }//GEN-LAST:event_menu_designActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        rePaintMainGame();
    }//GEN-LAST:event_formWindowActivated

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        rePaintMainGame();
    }//GEN-LAST:event_formMouseEntered

    private void change_color_XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_color_XActionPerformed
        if(gameType == CommonDefine.GAME_TYPE_CARO_GAME){
            Color colorX = JColorChooser.showDialog(this, "Choose Caro X's color!", CommonDefine.CARO_COLOR_X);
            if(colorX != null){
                CommonDefine.CARO_COLOR_X = colorX;
            }
        }
    }//GEN-LAST:event_change_color_XActionPerformed

    private void change_color_OActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_color_OActionPerformed
        if(gameType == CommonDefine.GAME_TYPE_CARO_GAME){
            Color colorO = JColorChooser.showDialog(this, "Choose Caro O's color!", CommonDefine.CARO_COLOR_O);
            if(colorO != null){
                CommonDefine.CARO_COLOR_O = colorO;
            }
        }
    }//GEN-LAST:event_change_color_OActionPerformed

    private int getGameType(String item) {
        String[] listGame = CommonDefine.LIST_GAME;
        for (int i = 1; i < listGame.length; i++) {
            if (listGame[i].equals(item)) {
                return i;
            }
        }
        return 0;
    }

    //<editor-fold defaultstate="collapsed" desc="Socket Client">
    private class ConnectServer implements Runnable {

        private String host;
        private Integer port;
        private String name;
        private String macAddress;

        public ConnectServer(String host, Integer port, String name, String macAddress) {
            this.host = host;
            this.port = port;
            this.name = name;
            this.macAddress = macAddress;
        }

        @Override
        public void run() {
            try {
                clientSocket = new Socket(host, port);
                appentTextToChatBox("Connect to Server successfully!");
                dataRead = new DataInputStream(clientSocket.getInputStream());
                dataWrite = new DataOutputStream(clientSocket.getOutputStream());
                isConnect = true;
                btn_reconnect.setEnabled(false);
                btn_back.setEnabled(true);
                btn_send.setEnabled(true);
                btn_dxh.setEnabled(true);
                btnInventory.setEnabled(true);
                btnShop.setEnabled(true);
                cbb_choose_game.setEnabled(true);
                sendMessage(CommonDefine.SETTING_CLIENT_RENAME + CommonDefine.SEPARATOR_KEY_VALUE + name);
                sendMessage(CommonDefine.MAC_ADDRESS + CommonDefine.SEPARATOR_KEY_VALUE + macAddress);
            } catch (IOException ex) {
                btn_reconnect.setEnabled(true);
                appentTextToChatBox("Can't connect to Server!");
                return;
            }

            receiveMessage = new Thread() {

                @Override
                public void run() {
                    String line;
                    while (true) {
                        if (!isConnect) {
                            break;
                        }
                        try {
                            line = dataRead.readUTF();
                            if (line != null && line.length() > 0) {
                                if (line.contains(CommonDefine.CARO_GAME_KEY)) {
                                    caroGameUpdate(line);
                                } else if (line.contains(CommonDefine.BAUCUA_GAME_KEY)) {
                                    bccGameUpdate(line);
                                } else if (line.contains(CommonDefine.BOOM_GAME_KEY)) {
                                    boomGameUpdate(line);
                                } else if (line.contains(CommonDefine.CARD_GAME_KEY)) {
                                    cardGameUpdate(line);
                                } else if (line.contains(CommonDefine.NOTICE_MESSAGE_KEY)) {
                                    clientGameUpdate(line);
                                } else if (line.contains(CommonDefine.SETTING_KEY)) {
                                    clientSettingUpdate(line);
                                } else if (line.contains(CommonDefine.SETTING_CLIENT_RENAME)) {
                                    if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE) && line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 1) {
                                        String name = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                                        if (name.trim().length() != 0) {
                                            setUsername(name.trim());
                                            mapConfig.put(CommonDefine.USERNAME_KEY, getUsername());
                                        }
                                    }
                                } else {
                                    appentTextToChatBox(line);

                                }
                            }
                        } catch (Exception ex) {
                            CommonMethod.log(ex.getLocalizedMessage());
                        }
                    }
                }
            };
            receiveMessage.start();
        }
    }

    public void sendMessage(String mess) {
        if (!isConnect || null == mess || mess.length() == 0) {
            return;
        }
        try {
            dataWrite.writeUTF(mess);
            dataWrite.flush();
        } catch (IOException ex) {
        }
    }

    private void closeConnection(boolean isServerOff, boolean isUpdateData) {
        if (clientSocket != null) {
            try {
                if (isUpdateData) {
                    sendMessage(CommonDefine.SETTING_CLIENT_DATA + getClientData());
                }
                if (!isServerOff) {
                    sendMessage(CommonDefine.NOTICE_CLIENT_OFF);
                }
                dataRead.close();
                dataWrite.close();
                isConnect = false;
                clientSocket.close();
            } catch (IOException ex) {
            }
        }
    }
    //</editor-fold>

    private String getClientData() {
        mapData.put(CommonDefine.DATA_COIN_KEY, String.valueOf(totalCoins));
        String d = DxhFrame.getDxhFrame().getCurrentMapDxh();
        if (d != null) {
            mapData.put(CommonDefine.DATA_LEVEL_DXH_KEY, String.valueOf(levelDh) + " " + d);
        } else {
            mapData.put(CommonDefine.DATA_LEVEL_DXH_KEY, String.valueOf(levelDh));
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mapData.entrySet()) {
            sb.append(entry.getKey());
            sb.append(CommonDefine.SEPARATOR_KEY_VALUE);
            sb.append(entry.getValue());
            sb.append(CommonDefine.BREAK_LINE);
        }
        return sb.toString().trim();
    }

    private void showNotifyState() {
        new Thread() {
            @Override
            public void run() {
                String status = "   --- Waitting Opponent!!! ---   ";
                int index = 1;
                while (isShowNotify) {
                    try {
                        String text = status.substring(0, index);
                        notify_state.setText(text);
                        index++;
                        if (index > status.length()) {
                            index = 1;
                        }
                        sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    private class UnlockChat extends Thread {

        private long time = CommonDefine.TIME_BLOCK_CHAT;

        public void setTime(long time) {
            if (time <= 0) {
                this.time = CommonDefine.TIME_BLOCK_CHAT;
            } else {
                this.time = time;
            }
        }

        @Override
        public void run() {
            try {
                sleep(time);
            } catch (InterruptedException ex) {
            }
            isBlockChat = false;
        }

    }

    public void backToMainGame() {
        CaroGameClient.rePaintMainGame(main_game.getGraphics());
        caroGameValue = 0;
        caroBoardGame = null;
        isCaroGameEnd = false;
        isHoverPlayerAgain = false;
        wineLine = null;
        /////
        listUserInBauCua = null;
        resultImgBauCua = null;
        tiencuocBauCua = null;
        resultStrBauCua = null;
        isOpenBauCua = true;
        isHostBauCua = false;
        baucuaChoose = null;
        /////
        boomValue = 0;
        boomMax = 0;
        playerVal = 0;
        bsf = null;
        boomClient = null;
        boomFrame = null;
        listUserInBoom = null;
        isHostBoom = false;
        isEnableFighting = false;
        gCoin = 0;
        tCoin = 0;
        ////
        isAllow = false;
        isAllowRepaint = false;
        btn_find_match.setEnabled(true);
        isShowNotify = false;
        notify_state.setText("");
    }

    private void clientSettingUpdate(String line) {
        if (line.contains(CommonDefine.SETTING_CLIENT_DATA)) {
            line = line.replace(CommonDefine.SETTING_CLIENT_DATA, "");
            for (String s : line.split(CommonDefine.BREAK_LINE)) {
                if (s.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                    mapData.put(s.split(CommonDefine.SEPARATOR_KEY_VALUE)[0], s.split(CommonDefine.SEPARATOR_KEY_VALUE)[1]);
                }
            }
            if (mapData.containsKey(CommonDefine.DATA_COIN_KEY)) {
                int coin = Integer.parseInt(mapData.get(CommonDefine.DATA_COIN_KEY).trim());
                addCoin(coin);
            }
            if (mapData.containsKey(CommonDefine.DATA_LEVEL_DXH_KEY)) {
                if (line.contains(CommonDefine.SPACE)) {
                    String arrs[] = mapData.get(CommonDefine.DATA_LEVEL_DXH_KEY).split(CommonDefine.SPACE);
                    if (arrs.length > 0) {
                        levelDh = Integer.parseInt(arrs[0]);
                    }
                    if (arrs.length > 1) {
                        curMap = arrs[1];
                        if (arrs.length > 2) {
                            px = Integer.parseInt(arrs[2]);
                            if (arrs.length > 3) {
                                py = Integer.parseInt(arrs[3]);
                                if (arrs.length > 5) {
                                    status_player = Integer.parseInt(arrs[4]);
                                    enableAddCoin = (Integer.parseInt(arrs[5]) == 1);
                                }
                            }
                        }
                    }
                }
            }
            if (levelDh == 0) {
                levelDh = 1;
            }

        } else if (line.contains(CommonDefine.SETTING_CLIENT_DXH_DATA)) {
            DxhFrame.getDxhFrame().initMapData(line.replace(CommonDefine.SETTING_CLIENT_DXH_DATA, ""));
        } else if (line.contains(CommonDefine.SETTING_ALLOW_RENAME)) {
            appentTextToChatBox(line.replace(CommonDefine.SETTING_ALLOW_RENAME, ""));
        } else if (line.contains(CommonDefine.SETTING_UPDATE_COIN)) {
            String strCoin = line.replace(CommonDefine.SETTING_UPDATE_COIN, "");
            if (strCoin.matches("[0-9]+")) {
                totalCoins = 0;
                addCoin(Integer.parseInt(strCoin));
            }
        } else if (line.contains(CommonDefine.SETTING_UPDATE_LEVEL_DXH)) {
            String strLvl = line.replace(CommonDefine.SETTING_UPDATE_LEVEL_DXH, "");
            if (strLvl.matches("[0-9]+")) {
                levelDh = Integer.parseInt(strLvl);
                if (levelDh == 0) {
                    levelDh = 1;
                }
                DxhFrame.getDxhFrame().resetComponent();
                if (DxhFrame.getDxhFrame().isVisible()) {
                    DxhFrame.getDxhFrame().setLevel(levelDh);
                } else {
                    isFirst = true;
                }
            }
        } else if (line.contains(CommonDefine.SETTING_CLIENT_DXH_BASE_REWARD)) {
            if (line.contains(":")) {
                String[] data = line.split(":");
                if (data.length == 2 && data[1].trim().matches("[0-9]+")) {
                    DxhFrame.getDxhFrame().updateBaseReward(Integer.parseInt(data[1].trim()));
                }
            }
        }
    }

    public int getgCoin() {
        return gCoin;
    }

    private boolean isValidNumber(String s) {
        return s.matches("[0-9]+");
    }

    //<editor-fold defaultstate="collapsed" desc="Notice">
    private void clientGameUpdate(String line) {
        if (line.contains(CommonDefine.NOTICE_BLOCK_CHAT) && line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 1) {
            String str = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
            if (line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 2) {
                textBlock = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[2];
            }
            if (CommonMethod.isNumberic(str)) {
                unlock = new UnlockChat();
                isBlockChat = true;
                unlock.setTime(Long.parseLong(str.trim()));
                unlock.start();
            }
        } else if (line.contains(CommonDefine.NOTICE_CLIENT_IN)) {
            if (line.contains(CommonDefine.SEPARATOR_GROUP) && line.split(CommonDefine.SEPARATOR_GROUP).length > 1) {
                String list = line.split(CommonDefine.SEPARATOR_GROUP)[1];
                list = list.trim();
                listClientName = new DefaultListModel<>();
                String n = "";
                for (String name : list.split(CommonDefine.SEPARATOR_KEY_VALUE)) {
                    if (name != null && name.length() != 0) {
                        n = name;
                        listClientName.addElement(name);
                    }
                }
                list_user.setModel(listClientName);
                if (!n.equals(username)) {
                    appentTextToChatBox(CommonMethod.formatMessageServer("[" + n + "] is online!"));
                }
            }

        } else if (line.contains(CommonDefine.NOTICE_CLIENT_OFF)) {
            if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE) && line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 1) {
                String name = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                for (int i = 0; i < listClientName.size(); i++) {
                    if (listClientName.get(i).equals(name.trim())) {
                        listClientName.removeElementAt(i);
                        break;
                    }
                }
                list_user.setModel(listClientName);
                appentTextToChatBox(CommonMethod.formatMessageServer("[" + name + "] is offline!"));
            }
        } else if (line.contains(CommonDefine.NOTICE_SERVER_OFF)) {
            appentTextToChatBox(CommonMethod.formatMessageServer(CommonDefine.INFO_SERVER_OFFLINE));
            btn_reconnect.setEnabled(true);
            btn_back.setEnabled(false);
            btn_find_match.setEnabled(false);
            btnInventory.setEnabled(false);
            btnShop.setEnabled(false);
            btn_send.setEnabled(false);
            cbb_choose_game.setEnabled(false);
            list_user.setModel(new DefaultListModel<>());
            DxhFrame.getDxhFrame().setVisible(false);
            if (inventory != null && inventory.isVisible()) {
                inventory.closeInventory();
            }
            if (bsf != null && bsf.isVisible()) {
                bsf.closeSetting();
            }
            if (shopItem != null & shopItem.isVisible()) {
                shopItem.closeShop();
            }
            btn_dxh.setEnabled(false);
            backToMainGame();
            closeConnection(true, false);
        } else if (line.contains(CommonDefine.NOTICE_FINDING_MATCH_FAIL)) {
            appentTextToChatBox(CommonMethod.formatMessageServer(CommonDefine.INFO_GAME_FINDING_FAIL));
            btn_find_match.setEnabled(true);
            isShowNotify = false;
            notify_state.setText("");
        } else if (line.contains(CommonDefine.NOTICE_NO_GAME_TO_JOIN)) {
            appentTextToChatBox(CommonMethod.formatMessageServer(CommonDefine.INFO_NO_GAME_AVAILABLE));
            btn_find_match.setEnabled(true);
            isShowNotify = false;
            notify_state.setText("");
        } else if (line.contains(CommonDefine.NOTICE_FULL_CLIENT_INGAME)) {
            appentTextToChatBox(CommonMethod.formatMessageServer(CommonDefine.INFO_GAME_IS_FULL));
            btn_find_match.setEnabled(true);
            isShowNotify = false;
            notify_state.setText("");
        } else if (line.contains(CommonDefine.NOTICE_GAME_IS_DESTROY)) {
            appentTextToChatBox(CommonMethod.formatMessageServer(CommonDefine.INFO_CARO_GAME_DESTROY));
            backToMainGame();
        } else if (line.contains(CommonDefine.NOTICE_FULL_GAME)) {
            btn_find_match.setEnabled(true);
            isShowNotify = false;
            notify_state.setText("");
            appentTextToChatBox(CommonMethod.formatMessageServer(CommonDefine.INFO_CAN_NOT_CREATE_GAME));
        } else if (line.contains(CommonDefine.NOTICE_MAC_ADDRESS_EXIST)) {
            JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "Your IP is exist on Server!");
            closeConnection(false, false);
            System.exit(0);
        } else if (line.contains(CommonDefine.NOTICE_REQUIRE_COIN)) {
            sendMessage(CommonDefine.SETTING_CLIENT_DATA + getClientData());
        } else if (line.contains(CommonDefine.NOTICE_ALERT_MESS_SERVER)) {
            JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), line.replace(CommonDefine.NOTICE_ALERT_MESS_SERVER, ""), "Server Notice!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Boom Game">
    private void boomGameUpdate(String line) throws Exception {
        if (line.contains(CommonDefine.BOOM_GAME_IS_HOST)) {
            isHostBoom = true;
        } else if (line.contains(CommonDefine.BOOM_GAME_PLAYER_JOIN)) {
            if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                String[] arrs = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                if (arrs.length != 2) {
                    return;
                }
                listUserInBoom = new ArrayList<>();
                for (String s : arrs[1].split(CommonDefine.COMMA)) {
                    listUserInBoom.add(s);
                }
                if (listUserInBoom.size() > 1) {
                    isShowNotify = false;
                    notify_state.setText("");
                    if (isHostBoom) {
                        isEnableFighting = true;
                    }
                }
                gCoin = 0;
                tCoin = 0;
                Equipment money_bag = ClientFrame.getClientFrame().getEquipmentById(BoomGameClient.ITEM_MONEY_BAG);
                if (money_bag != null) {
                    tCoin = 2 * money_bag.getParam_1();
                }
                isAllow = true;
                isAllowRepaint = true;
                boomClient.drawPlayerJoin(main_game.getGraphics(), listUserInBoom);
                boomClient.drawReadyIcon(main_game.getGraphics());
                boomClient.drawSettingIcon(main_game.getGraphics());
                ///
                boomClient.drawGameCoin(main_game.getGraphics(), String.valueOf(gCoin));
                boomClient.drawRewardCoin(main_game.getGraphics(), String.valueOf((int) (tCoin / 2)));
            }
        } else if (line.contains(CommonDefine.BOOM_GAME_ENABLE_FIGHTING)) {
            boomClient.drawFightingIcon(main_game.getGraphics());
        } else if (line.contains(CommonDefine.BOOM_GAME_SET_PLAYER_VAL)) {
            String val = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
            playerVal = Integer.parseInt(val);
            ////
            boomValue = BOOM_DEFAULT_VALUE;
            boomMax = BOOM_DEFAULT_MAX_CREATE;
            timeCharge = BOOM_DEFAULT_TIMECHARGE;
            luckyRatio = BOOM_DEFAULT_LUCKY_RATIO;
        } else if (line.contains(CommonDefine.BOOM_GAME_GCOIN)) {
            if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                if (arr.length == 3) {
                    gCoin = Integer.parseInt(arr[1].trim());
                    tCoin = Integer.parseInt(arr[2].trim());
                    if (tCoin == 0) {
                        Equipment money_bag = ClientFrame.getClientFrame().getEquipmentById(BoomGameClient.ITEM_MONEY_BAG);
                        if (money_bag != null) {
                            tCoin = 2 * money_bag.getParam_1();
                        }
                    }
                    ///
                    boomClient.drawGameCoin(main_game.getGraphics(), String.valueOf(gCoin));
                    boomClient.drawRewardCoin(main_game.getGraphics(), String.valueOf((int) (tCoin / 2)));
                    if (totalCoins < gCoin) {
                        if (boomClient.IsReady()) {
                            boomClient.toggleReady();
                            sendMessage(CommonDefine.BOOM_GAME_READY);
                            boomClient.drawReadyIcon(main_game.getGraphics());
                            JOptionPane.showMessageDialog(ClientFrame.getClientFrame(), "You not enough coin!");
                        }
                    }
                }
            }
        } else if (line.contains(CommonDefine.BOOM_GAME_INIT_DATA)) {
            String data = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
            int[][] m = new int[20][20];
            int i = 0;
            int j = 0;
            for (String s : data.split(";")) {
                j = 0;
                for (String v : s.split(",")) {
                    m[i][j] = Integer.parseInt(v.trim());
                    j++;
                }
                i++;
            }
            ///
            for (Entry<Integer, Integer> entry : mapItemUse.entrySet()) {
                sendMessage(CommonDefine.BOOM_GAME_ADD_ITEM + CommonDefine.SEPARATOR_KEY_VALUE + entry.getKey() + CommonDefine.SEPARATOR_KEY_VALUE + (entry.getKey() * -1));
                Equipment e = getEquipmentById(entry.getKey());
                if (e != null) {
                    if (BoomGameClient.isBomb(e.getId())) {
                        boomValue = e.getId();
                    } else if (e.getType() == ItemType.BAG_INCREASE.getId()) {
                        boomMax += e.getParam_1();
                        if (e.getId() == 1) {
                            boomClient.setHasBagPlus(true);
                        } else if (e.getId() == 2) {
                            boomClient.setHasBigBagPlus(true);
                        }
                    } else if (e.getType() == ItemType.BOMB_CHARGE_QUICKLY.getId()) {
                        timeCharge = e.getParam_1();
                    } else if (e.getType() == ItemType.LUCKY_CHEST.getId()) {
                        luckyRatio = e.getParam_1();
                    } else if (e.getType() == ItemType.CHANGE_DIRECTION.getId()) {
                        boomClient.setHasBombDirection(true);
                    }
                }
            }
            //
            boomClient.initGameData(m, playerVal, boomValue, boomMax, timeCharge, luckyRatio);
            boomClient.initCoinGame(gCoin, gCoin, tCoin);
            ClientFrame.getClientFrame().addCoin(gCoin * -1);
            boomFrame.setVisible(true);
            ClientFrame.getClientFrame().setVisible(false);
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        boomFrame.startBoom();
                        boomFrame.updateBombCharge(boomMax, 0);
                        boomFrame.updateBombImage(listImageBoom.get("bomb_" + boomValue));
                        boomFrame.updateHPImage(listImageBoom.get("hp"));
                        boomFrame.updateHPTime(BOOM_MAX_HP);
                        boomFrame.updateCNImage(listImageBoom.get("coin"));
                        boomFrame.updateCNTime(gCoin);
                        boomFrame.updateDirectImage();
                    } catch (InterruptedException ex) {
                    }
                }
            }.start();
        } else if (line.contains(CommonDefine.BOOM_GAME_LOAD_BATTLE)) {
            String time = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
            boomFrame.drawLoading(time);
        } else if (line.contains(CommonDefine.BOOM_GAME_START_BATTLE)) {

            boomFrame.setStartGame(true);
        } else if (line.contains(CommonDefine.BOOM_GAME_PLAYER_MOVE)) {
            if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                String[] arrs = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                int x = Integer.parseInt(arrs[1].trim());
                int y = Integer.parseInt(arrs[2].trim());
                int val = Integer.parseInt(arrs[3].trim());
                int status = Integer.parseInt(arrs[4].trim());
                int it = Integer.parseInt(arrs[5].trim());
                String type = arrs[6].trim();
                int nx = 0;
                int ny = 0;
                if (arrs.length > 7) {
                    nx = Integer.parseInt(arrs[7].trim());
                    ny = Integer.parseInt(arrs[8].trim());
                }
                boomFrame.playerMove(x, y, val, status, it, type, nx, ny);
            } else {
                int val = Integer.parseInt(line.replace(CommonDefine.BOOM_GAME_PLAYER_MOVE, "").trim());
                if (playerVal == val && !boomClient.isAnimate) {
                    boomFrame.enableAction();
                }
            }
        } else if (line.contains(CommonDefine.BOOM_GAME_ADD_BOOM)) {
            String[] arrs = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
            int x = Integer.parseInt(arrs[1].trim());
            int y = Integer.parseInt(arrs[2].trim());
            int val = Integer.parseInt(arrs[3].trim());
            int m_val = Integer.parseInt(arrs[4].trim());
            boomFrame.addBoom(x, y, val, m_val);
        } else if (line.contains(CommonDefine.BOOM_GAME_EXPLODE_BOOM)) {
            boomFrame.exlodeBoom(line.replace(CommonDefine.BOOM_GAME_EXPLODE_BOOM, ""));
        } else if (line.contains(CommonDefine.BOOM_GAME_UPDATE_HP)) {
            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
            int hp = Integer.parseInt(arr[1].trim());
            int victim_val = Integer.parseInt(arr[2].trim());
            boomFrame.updateHpPlayer(hp, victim_val);
        } else if (line.contains(CommonDefine.BOOM_GAME_REMOVE_PLAYER)) {
            int val = Integer.parseInt(line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1].trim());
            boomFrame.updatePlayerOut(val);
        } else if (line.contains(CommonDefine.BOOM_GAME_SET_TIME_OUT)) {
            int timeout = Integer.parseInt(line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1].trim());
            boomFrame.setTimeOut(timeout);
        } else if (line.contains(CommonDefine.BOOM_GAME_ITEM_RANDOM)) {
            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
            int x = Integer.parseInt(arr[1]);
            int y = Integer.parseInt(arr[2]);
            int val = Integer.parseInt(arr[3]);
            boomFrame.addItemRandom(x, y, val);
        } else if (line.contains(CommonDefine.BOOM_GAME_END_GAME)) {
            boomFrame.endGame();
        } else if (line.contains(CommonDefine.BOOM_GAME_EQUIP_DATA)) {
            String data = line.replace(CommonDefine.BOOM_GAME_EQUIP_DATA, "");
            if (data == null || data.length() == 0) {
                return;
            }
            if (data.contains(CommonDefine.BREAK_LINE)) {
                for (String str : data.split(CommonDefine.BREAK_LINE)) {
                    if (line.contains(CommonDefine.COMMA)) {
                        String arr[] = str.split(CommonDefine.COMMA);
                        if (arr.length == Equipment.class.getDeclaredFields().length) {
                            if (!isValidNumber(arr[0]) || !isValidNumber(arr[1]) || !isValidNumber(arr[4]) || !isValidNumber(arr[5]) || !isValidNumber(arr[6]) || !isValidNumber(arr[7]) || !isValidNumber(arr[8]) || !isValidNumber(arr[9]) || !isValidNumber(arr[10]) || !isValidNumber(arr[11]) || !isValidNumber(arr[12]) || !isValidNumber(arr[13]) || !isValidNumber(arr[14]) || !isValidNumber(arr[15]) || !isValidNumber(arr[16])) {
                                continue;
                            }
                            int id = Integer.parseInt(arr[0].trim());//
                            int price = Integer.parseInt(arr[1].trim());//
                            String name = arr[2].trim();
                            String des = arr[3].trim();
                            int damage = Integer.parseInt(arr[4].trim());//
                            int horLen = Integer.parseInt(arr[5].trim());//
                            int verLen = Integer.parseInt(arr[6].trim());//
                            int squLen = Integer.parseInt(arr[7].trim());//
                            int recover = Integer.parseInt(arr[8].trim());//
                            int carry = Integer.parseInt(arr[9].trim());//
                            int category = Integer.parseInt(arr[10].trim());
                            int type = Integer.parseInt(arr[11].trim());
                            int param_1 = Integer.parseInt(arr[12].trim());
                            int param_2 = Integer.parseInt(arr[13].trim());
                            int param_3 = Integer.parseInt(arr[14].trim());
                            int param_4 = Integer.parseInt(arr[15].trim());
                            int param_5 = Integer.parseInt(arr[16].trim());
                            listEquipment.add(new Equipment(id, price, name, des, damage, horLen, verLen, squLen, recover, carry, category, type, param_1, param_2, param_3, param_4, param_5));
                        }
                    }
                }
            }
        } else if (line.contains(CommonDefine.BOOM_GAME_ITEM_OWN_DATA)) {
            initMapItemOwn(line.replace(CommonDefine.BOOM_GAME_ITEM_OWN_DATA, ""));
        }
    }

    public void resetBoomGame() {
        boomValue = 0;
        boomMax = 0;
        playerVal = 0;
        bsf = null;
        boomClient = null;
        boomFrame = null;
        listUserInBoom = null;
        isHostBoom = false;
        isAllow = false;
        isAllowRepaint = false;
        isEnableFighting = false;
        gCoin = 0;
        tCoin = 0;
        btn_find_match.setEnabled(true);
        BoomGameClient.rePaintMainGame(main_game.getGraphics());
        mapItemUse.clear();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Bau Cua Game">
    private void bccGameUpdate(String line) {
        if (line.contains(CommonDefine.BAUCUA_GAME_IS_HOST)) {
            isHostBauCua = true;
        } else if (line.contains(CommonDefine.BAUCUA_GAME_READY_PLAY)) {
            isShowNotify = false;
            isAllowRepaint = true;
            if (isHostBauCua) {
                isAllow = true;
                notify_state.setText("?");
            } else {
                notify_state.setText("???");
            }
            String[] arr = line.split(CommonDefine.SEPARATOR_GROUP);
            if (arr.length >= 2) {
                String str_players = arr[1].trim();
                String[] players = str_players.split(CommonDefine.SEPARATOR_KEY_VALUE);
                for (String player : players) {
                    for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
                        if (listUserInBauCua[i] == null) {
                            listUserInBauCua[i] = player;
                            break;
                        }
                    }
                }
            }
            if (arr.length >= 3) {
                if (CommonMethod.isNumberic(arr[2])) {
                    isOpenBauCua = (Integer.parseInt(arr[2].trim()) == 1);
                }
            }
            if (arr.length >= 4) {
                String str_results = arr[3].trim();
                if (str_results.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                    String[] results = str_results.split(CommonDefine.SEPARATOR_KEY_VALUE);
                    if (results.length == 3) {
                        for (int i = 0; i < results.length; i++) {
                            if (CommonMethod.isNumberic(results[i])) {
                                int v = Integer.parseInt(results[i].trim());
                                String str_img = CommonMethod.getChooseImgString(v);
                                String str_t = CommonMethod.getChooseTextString(v);
                                if (str_img.length() > 0 && str_t.length() > 0) {
                                    resultImgBauCua[i] = listImageBaucua.get(str_img);
                                    resultStrBauCua[i] = str_t;
                                }
                            }
                        }
                    }
                }
            }
            if (arr.length >= 5) {
                String str_tcs = arr[4].trim();
                tiencuocBauCua = str_tcs.split(CommonDefine.COMMA);
            }
            BauCuaGameClient.repaintBaucua(main_game.getGraphics(), listUserInBauCua, resultImgBauCua, isOpenBauCua, tiencuocBauCua, resultStrBauCua, listImageBaucua.get("desk"), listImageBaucua.get("plate"), listImageBaucua.get("bowl"), listImageBaucua.get("user"), isHostBauCua);
        } else if (line.contains(CommonDefine.BAUCUA_GAME_JOIN_GAME)) {
            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
            if (arr.length == 2) {
                for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
                    if (listUserInBauCua[i] == null) {
                        listUserInBauCua[i] = arr[1].trim();
                        BauCuaGameClient.drawUserIcon(main_game.getGraphics(), listImageBaucua.get("user"), i, listUserInBauCua[i]);
                        break;
                    }
                }
            }
        } else if (line.contains(CommonDefine.BAUCUA_GAME_PLAYER_OUT)) {
            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
            if (arr.length == 2) {
                for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
                    if (listUserInBauCua[i] != null && listUserInBauCua[i].trim().equals(arr[1].trim())) {
                        listUserInBauCua[i] = null;
                        BauCuaGameClient.removeUser(main_game.getGraphics(), i);
                        break;
                    }
                }
            }
        } else if (line.contains(CommonDefine.BAUCUA_GAME_RESET)) {
            if (isHostBauCua) {
                isShowNotify = true;
                showNotifyState();
                listUserInBauCua = new String[CommonDefine.BAUCUA_GAME_MAX_PLAYER];
                resultImgBauCua = new Image[3];
                resultStrBauCua = new String[3];
                tiencuocBauCua = new String[6];
                baucuaChoose = new HashMap<>();
                isOpenBauCua = true;
                btn_find_match.setEnabled(false);
                cbb_choose_game.setEnabled(false);
                isAllow = false;
                isAllowRepaint = false;
                BauCuaGameClient.rePaintjPanel(main_game.getGraphics());
            }
        } else if (line.contains(CommonDefine.BAUCUA_GAME_VALUE_CHOOSE)) {
            if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                if (arr.length == 3 && CommonMethod.isNumberic(arr[1]) && CommonMethod.isNumberic(arr[2])) {
                    int choice = Integer.parseInt(arr[1].trim());
                    int cn = Integer.parseInt(arr[2].trim());
                    int bf = 0;
                    if (baucuaChoose.containsKey(choice)) {
                        bf = baucuaChoose.get(choice);
                    }
                    baucuaChoose.put(choice, bf + cn);
                    addCoin(-cn);
                }
            }
        } else if (line.contains(CommonDefine.BAUCUA_GAME_UPDATE_TC)) {
            if (line.contains(CommonDefine.SEPARATOR_GROUP)) {
                String[] arr = line.split(CommonDefine.SEPARATOR_GROUP);
                if (arr.length == 2) {
                    String str_tc = arr[1].trim();
                    tiencuocBauCua = str_tc.split(CommonDefine.COMMA);
                    BauCuaGameClient.drawTienCuoc(main_game.getGraphics(), tiencuocBauCua);
                    updateLbCoin();
                }
            }
        } else if (line.contains(CommonDefine.BAUCUA_GAME_START_SHAKE)) {
            resultImgBauCua = new Image[3];
            resultStrBauCua = new String[3];
            tiencuocBauCua = new String[6];
            baucuaChoose = new HashMap<>();
            BauCuaGameClient.drawTienCuoc(main_game.getGraphics(), tiencuocBauCua);
            BauCuaGameClient.drawResultStr(main_game.getGraphics(), resultStrBauCua);
            BauCuaGameClient.lacBaucua(main_game.getGraphics(), listImageBaucua.get("plate"), listImageBaucua.get("bowl"), isHostBauCua, totalCoins);
        } else if (line.contains(CommonDefine.BAUCUA_GAME_FINISH_SHAKE)) {
            isAllow = true;
            isOpenBauCua = false;
        } else if (line.contains(CommonDefine.BAUCUA_GAME_RESULT_VALUE)) {
            isAllow = false;
            if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                if (arr.length >= 4) {
                    if (CommonMethod.isNumberic(arr[1]) && CommonMethod.isNumberic(arr[2]) && CommonMethod.isNumberic(arr[3])) {
                        int r1 = Integer.parseInt(arr[1].trim());
                        int r2 = Integer.parseInt(arr[2].trim());
                        int r3 = Integer.parseInt(arr[3].trim());
                        String str_img_1 = CommonMethod.getChooseImgString(r1);
                        String str_img_2 = CommonMethod.getChooseImgString(r2);
                        String str_img_3 = CommonMethod.getChooseImgString(r3);
                        resultImgBauCua[0] = listImageBaucua.get(str_img_1);
                        resultImgBauCua[1] = listImageBaucua.get(str_img_2);
                        resultImgBauCua[2] = listImageBaucua.get(str_img_3);
                        resultStrBauCua[0] = CommonMethod.getChooseTextString(r1);
                        resultStrBauCua[1] = CommonMethod.getChooseTextString(r2);
                        resultStrBauCua[2] = CommonMethod.getChooseTextString(r3);
                        if (!isHostBauCua) {
                            int addCoins = 0;
                            if (baucuaChoose.containsKey(r1)) {
                                addCoins += baucuaChoose.get(r1) * 2;
                            }
                            if (baucuaChoose.containsKey(r2)) {
                                addCoins += baucuaChoose.get(r2);
                                if (r2 != r1) {
                                    addCoins += baucuaChoose.get(r2);
                                }
                            }
                            if (baucuaChoose.containsKey(r3)) {
                                addCoins += baucuaChoose.get(r3);
                                if (r3 != r1 && r3 != r2) {
                                    addCoins += baucuaChoose.get(r3);
                                }
                            }
                            if (arr.length == 6) {
                                int totalHostcoins = Integer.parseInt(arr[4].trim());
                                int totalMunusCoins = Integer.parseInt(arr[5].trim());
                                addCoins = (int) ((addCoins * totalHostcoins) / totalMunusCoins);
                            }
                            addCoin(addCoins);
                        } else {
                            int val = 1;
                            for (String tc : tiencuocBauCua) {
                                if (tc != null && tc.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                                    String[] arr_tc = tc.split(CommonDefine.SEPARATOR_KEY_VALUE);
                                    if (arr_tc.length == 2) {
                                        int t = Integer.parseInt(arr_tc[1].trim());
                                        int addcoins = 0;
                                        if (val == r1 || val == r2 || val == r3) {
                                            if (val == r1) {
                                                addcoins -= t;
                                            }
                                            if (val == r2) {
                                                addcoins -= t;
                                            }
                                            if (val == r3) {
                                                addcoins -= t;
                                            }
                                        } else {
                                            addcoins += t;
                                        }
                                        addCoin(addcoins);
                                    }
                                }
                                val++;
                            }
                            if (totalCoins <= 0) {
                                totalCoins = 0;
                                addCoin(0);
                            }
                        }
                        if (resultImgBauCua[0] != null && resultImgBauCua[1] != null && resultImgBauCua[2] != null) {
                            BauCuaGameClient.openBaucua(main_game.getGraphics(), listImageBaucua.get("plate"), listImageBaucua.get("bowl"), resultImgBauCua[0], resultImgBauCua[1], resultImgBauCua[2], isHostBauCua);
                        }
                    }
                }
            }
        } else if (line.contains(CommonDefine.BAUCUA_GAME_FINISH_OPEN)) {
            BauCuaGameClient.drawResultStr(main_game.getGraphics(), resultStrBauCua);
            updateLbCoin();
            isOpenBauCua = true;
            if (isHostBauCua) {
                isAllow = totalCoins != 0;

            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Caro Game">
    private void caroGameUpdate(String line) {
        if (line.contains(CommonDefine.CARO_GAME_VALUE)) {
            isAllowRepaint = true;
            CaroGameClient.rePaintMainGame(main_game.getGraphics());
            if (line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 1) {
                String str = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                if (CommonMethod.isNumberic(str)) {
                    caroGameValue = Integer.parseInt(str);
                }
            }
            if (line.contains(CommonDefine.CARO_GAME_READY)) {
                CaroGameClient.drawBoard(main_game.getGraphics());
                isShowNotify = false;
                notify_state.setText(CommonDefine.INFO_CARO_WAITING_OPPONENT);
            }
            if (line.contains(CommonDefine.CARO_GAME_FIRST_PLAY)) {
                isAllow = true;
                notify_state.setText(CommonDefine.INFO_CARO_PLAY_TURN);
            }
        } else if (line.contains(CommonDefine.CARO_GAME_VIEWER)) {
            caroGameValue = 0;
            isAllowRepaint = true;
            isShowNotify = false;
            notify_state.setText(CommonDefine.INFO_CARO_WATCHING_GAME);
            CaroGameClient.rePaintMainGame(main_game.getGraphics());
            CaroGameClient.drawBoard(main_game.getGraphics());
            if (line.contains(CommonDefine.SEPARATOR_GROUP) && line.split(CommonDefine.SEPARATOR_GROUP).length > 1) {
                String currentStateCaroGame = line.split(CommonDefine.SEPARATOR_GROUP)[1];
                for (String xyv : currentStateCaroGame.split(CommonDefine.COMMA)) {
                    if (xyv.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 2) {
                        String str_x = xyv.split(CommonDefine.SEPARATOR_KEY_VALUE)[0];
                        String str_y = xyv.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                        String str_v = xyv.split(CommonDefine.SEPARATOR_KEY_VALUE)[2];
                        if (CommonMethod.isNumberic(str_x) && CommonMethod.isNumberic(str_y) && CommonMethod.isNumberic(str_v)) {
                            int x = Integer.parseInt(str_x.trim());
                            int y = Integer.parseInt(str_y.trim());
                            int v = Integer.parseInt(str_v.trim());
                            caroBoardGame[x][y] = v;
                            if (v == CommonDefine.CARO_GAME_X_VALUE) {
                                CaroGameClient.drawX(main_game.getGraphics(), CommonDefine.CARO_COLOR_X, x, y);
                            } else if (Integer.parseInt(str_v) == CommonDefine.CARO_GAME_O_VALUE) {
                                CaroGameClient.drawO(main_game.getGraphics(), CommonDefine.CARO_COLOR_O, x, y);
                            }
                        }
                    }
                }
                if (line.contains(CommonDefine.CARO_GAME_END) && line.split(CommonDefine.SEPARATOR_GROUP).length > 4) {
                    String win_line = line.split(CommonDefine.SEPARATOR_GROUP)[2];
                    String win_type = line.split(CommonDefine.SEPARATOR_GROUP)[3];
                    String win_flag = line.split(CommonDefine.SEPARATOR_GROUP)[4];
                    String s = CommonDefine.SEPARATOR_GROUP + win_line + CommonDefine.SEPARATOR_GROUP + win_type + CommonDefine.SEPARATOR_GROUP + win_flag;
                    isCaroGameEnd = true;
                    wineLine = s;
                    drawWinLine(s);
                }
            }
            if (line.contains(CommonDefine.CARO_GAME_PLAY_AGAIN)) {
                caroBoardGame = new int[CommonDefine.CARO_GAME_NUM_COLUMNS][CommonDefine.CARO_GAME_NUM_ROWS];
                isCaroGameEnd = false;
                isAllow = false;
            }
        } else if (line.contains(CommonDefine.CARO_GAME_POSITION)) {
            String arrValue[] = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
            if (arrValue.length > 3) {
                if (CommonMethod.isNumberic(arrValue[1]) && CommonMethod.isNumberic(arrValue[2]) && CommonMethod.isNumberic(arrValue[3])) {
                    int x = Integer.parseInt(arrValue[1].trim());
                    int y = Integer.parseInt(arrValue[2].trim());
                    int value = Integer.parseInt(arrValue[3].trim());
                    caroBoardGame[x][y] = value;
                    CaroGameClient.removeCurrentPosition(main_game.getGraphics(), currentX, currentY);
                    currentX = x;
                    currentY = y;
                    if (value == CommonDefine.CARO_GAME_O_VALUE) {
                        CaroGameClient.drawO(main_game.getGraphics(), CommonDefine.CARO_COLOR_O, x, y);
                    } else if (value == CommonDefine.CARO_GAME_X_VALUE) {
                        CaroGameClient.drawX(main_game.getGraphics(), CommonDefine.CARO_COLOR_X, x, y);
                    }
                    CaroGameClient.drawCurrentPosition(main_game.getGraphics(), currentX, currentY);
                    if (caroGameValue != 0) {
                        if (value != caroGameValue) {
                            isAllow = true;
                            notify_state.setText(CommonDefine.INFO_CARO_PLAY_TURN);
                        } else {
                            notify_state.setText(CommonDefine.INFO_CARO_WAITING_OPPONENT);
                        }
                    }
                    if (line.contains(CommonDefine.CARO_GAME_END)) {
                        if (caroGameValue != 0) {
                            if (caroGameValue == value) {
                                notify_state.setText("You Win!");
                            } else {
                                notify_state.setText("You Lose!");
                            }
                        } else {
                            if (value == CommonDefine.CARO_GAME_X_VALUE) {
                                notify_state.setText("X Win!");
                            } else if (value == CommonDefine.CARO_GAME_O_VALUE) {
                                notify_state.setText("O Win!");
                            }
                        }
                        isAllow = true;
                        isCaroGameEnd = true;
                        wineLine = line;
                        drawWinLine(line);
                    }
                }
            }
        } else if (line.contains(CommonDefine.CARO_GAME_EXIT_ROOM)) {
            backToMainGame();
            notify_state.setText(CommonDefine.INFO_GAME_IS_END);
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException ex) {
                    }
                    notify_state.setText("");
                }
            }.start();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Card Game">
    private void cardGameUpdate(String line) {
        if (line.contains(CommonDefine.CARD_GAME_SET_VALUE)) {
            ClientFrame.getClientFrame().setVisible(false);
            cardFrame.setVisible(true);
            new Thread(() -> {
                String strVal = line.replace(CommonDefine.CARD_GAME_SET_VALUE, "");
                if (isValidNumber(strVal)) {
                    cardFrame.initMyValue(Integer.parseInt(strVal));
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_PLAYER_JOIN)) {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                    String str = line.replace(CommonDefine.CARD_GAME_PLAYER_JOIN, "");
                    String[] params = str.split(CommonDefine.SEPARATOR_GROUP);
                    List<Player> list = new ArrayList<>();
                    for (String s : params) {
                        String[] attrs = s.split(CommonDefine.COMMA);
                        if (!isValidNumber(attrs[0].trim()) || !isValidNumber(attrs[2].trim()) || !isValidNumber(attrs[3].trim())) {
                            continue;
                        }
                        Player p = new Player(Integer.parseInt(attrs[0].trim()), attrs[1].trim(), Integer.parseInt(attrs[2].trim()), Integer.parseInt(attrs[3].trim()));
                        list.add(p);
                    }
                    cardFrame.initPlayer(list);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_ADD_PLAYER)) {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                    String str = line.replace(CommonDefine.CARD_GAME_ADD_PLAYER, "");
                    String[] params = str.split(CommonDefine.COMMA);
                    if (isValidNumber(params[0].trim()) && isValidNumber(params[2].trim()) && isValidNumber(params[3].trim())) {
                        Player p = new Player(Integer.parseInt(params[0].trim()), params[1].trim(), Integer.parseInt(params[2].trim()), Integer.parseInt(params[3].trim()));
                        cardFrame.addPlayer(p);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_IS_HOST)) {
            cardFrame.enableHost();
        } else if (line.contains(CommonDefine.CARD_GAME_ENABLE_PLAY)) {
            cardFrame.enablePlayGame();
        } else if (line.contains(CommonDefine.CARD_GAME_PLAYER_CARD)) {
            new Thread(() -> {
                cardFrame.setStart(true);
                cardFrame.initListOwnCardDeck(line.replace(CommonDefine.CARD_GAME_PLAYER_CARD, ""));
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_ENABLE_TURN)) {
            cardFrame.setEnableTurn(true);
        } else if (line.contains(CommonDefine.CARD_GAME_TIME_COUNT_DOWN)) {
            new Thread(() -> {
                String[] elems = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                if (elems.length == 3) {
                    if (CommonMethod.isValidNumber(elems[1]) && CommonMethod.isValidNumber(elems[2])) {
                        cardFrame.countdown(Integer.parseInt(elems[1].trim()), Integer.parseInt(elems[2].trim()));
                    }
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_ACTION_FIGHT)) {
            new Thread(() -> {
                cardFrame.executeAction(line.replace(CommonDefine.CARD_GAME_ACTION_FIGHT, ""));
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_NEXT_ROUND)) {
            new Thread(() -> {
                cardFrame.clearRound();
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_END_GAME)) {
            new Thread(() -> {
                cardFrame.endGame(line.replace(CommonDefine.CARD_GAME_END_GAME, ""));
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_UPDATE_COIN)) {
            String[] params = line.replace(CommonDefine.CARD_GAME_UPDATE_COIN, "").split(CommonDefine.COMMA);
            for (String elem : params) {
                new Thread(() -> {
                    String[] arrayElem = elem.split(CommonDefine.SEPARATOR_KEY_VALUE);
                    if (arrayElem.length == 2) {///
                        if (CommonMethod.isNumberic(arrayElem[0]) && CommonMethod.isNumberic(arrayElem[1])) {
                            int val = Integer.parseInt(arrayElem[0].trim());
                            int coin = Integer.parseInt(arrayElem[1].trim());
                            cardFrame.updatePlayer(val, null, coin, 0);
                        }
                    }
                }).start();
            }
        } else if (line.contains(CommonDefine.CARD_GAME_PLAYER_REST_CARD)) {
            new Thread(() -> {
                if (line.contains(CommonDefine.SEPARATOR_GROUP)) {
                    String[] params = line.replace(CommonDefine.CARD_GAME_PLAYER_REST_CARD, "").split(CommonDefine.SEPARATOR_GROUP);
                    Map<Integer, List<Card>> mapListCard = new HashMap<>();
                    for (String elems : params) {
                        if (elems.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                            String[] array = elems.split(CommonDefine.SEPARATOR_KEY_VALUE);
                            if (array.length == 2 && CommonMethod.isValidNumber(array[0])) {
                                Integer val = Integer.parseInt(array[0].trim());
                                String strlcard = array[1].trim();
                                List<Card> listCard = new ArrayList<>();
                                if (strlcard.contains(CommonDefine.COMMA)) {
                                    String[] arrCards = strlcard.split(CommonDefine.COMMA);
                                    for (String strCard : arrCards) {
                                        String strNum = strCard.split("_")[0];
                                        String strType = strCard.split("_")[1];
                                        if (CommonMethod.isValidNumber(strNum) && CommonMethod.isValidNumber(strType)) {
                                            Card card = new Card(Integer.parseInt(strNum.trim()), CardType.valueOf(Integer.parseInt(strType.trim())));
                                            if (card.getType() != CardType.INVALID) {
                                                listCard.add(card);
                                            }
                                        }
                                    }
                                }
                                mapListCard.put(val, listCard);
                            }
                        }
                    }
                    if (!mapListCard.isEmpty()) {
                        cardFrame.drawEnemyListCardEndGame(mapListCard);
                    }
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_RESTART_GAME_ENABLE)) {
            cardFrame.setStartEnable(true);
        } else if (line.contains(CommonDefine.CARD_GAME_BETTING_COIN)) {
            new Thread(() -> {
                String strBetCoin = line.replace(CommonDefine.CARD_GAME_BETTING_COIN, "");
                if (CommonMethod.isValidNumber(strBetCoin)) {
                    cardFrame.setBetCoin(Integer.parseInt(strBetCoin.trim()));
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_SEND_MESS)) {
            cardFrame.appentTextToChatBox(line.replace(CommonDefine.CARD_GAME_SEND_MESS, ""));
        } else if (line.contains(CommonDefine.CARD_GAME_SET_HOST)) {
            new Thread(() -> {
                if (cardFrame.isHost()) {
                    return;
                }
                String strVal = line.replace(CommonDefine.CARD_GAME_SET_HOST, "");
                if (CommonMethod.isValidNumber(strVal)) {
                    cardFrame.setHostVal(Integer.parseInt(strVal.trim()));
                }
            }).start();
        } else if (line.contains(CommonDefine.CARD_GAME_PLAYER_OUT)) {
            String strVal = line.replace(CommonDefine.CARD_GAME_PLAYER_OUT, "");
            if (CommonMethod.isValidNumber(strVal)) {
                cardFrame.removePlayer(Integer.parseInt(strVal.trim()));
            }
        }
    }
    //</editor-fold>

    private void rePaintMainGame() {
        switch (gameType) {
            case CommonDefine.GAME_TYPE_CARO_GAME:
                if (isAllowRepaint) {
                    CaroGameClient.rePaintMainGame(main_game.getGraphics());
                    CaroGameClient.drawBoard(main_game.getGraphics());
                    for (int i = 0; i < CommonDefine.CARO_GAME_NUM_COLUMNS; i++) {
                        for (int j = 0; j < CommonDefine.CARO_GAME_NUM_ROWS; j++) {
                            if (caroBoardGame[i][j] == CommonDefine.CARO_GAME_X_VALUE) {
                                CaroGameClient.drawX(main_game.getGraphics(), CommonDefine.CARO_COLOR_X, i, j);
                            } else if (caroBoardGame[i][j] == CommonDefine.CARO_GAME_O_VALUE) {
                                CaroGameClient.drawO(main_game.getGraphics(), CommonDefine.CARO_COLOR_O, i, j);
                            }
                        }
                    }
                    CaroGameClient.drawCurrentPosition(main_game.getGraphics(), currentX, currentY);
                    if (isCaroGameEnd) {
                        drawWinLine(wineLine);
                    }
                }
                break;
            case CommonDefine.GAME_TYPE_BTCC_GAME:
                if (isAllowRepaint) {
                    BauCuaGameClient.repaintBaucua(main_game.getGraphics(), listUserInBauCua, resultImgBauCua, isOpenBauCua, tiencuocBauCua, resultStrBauCua, listImageBaucua.get("desk"), listImageBaucua.get("plate"), listImageBaucua.get("bowl"), listImageBaucua.get("user"), isHostBauCua);
                }
                break;
            case CommonDefine.GAME_TYPE_BOOM_GAME:
                if (isAllowRepaint && boomClient != null) {
                    boomClient.drawPlayerJoin(main_game.getGraphics(), listUserInBoom);
                    boomClient.drawReadyIcon(main_game.getGraphics());
                    boomClient.drawSettingIcon(main_game.getGraphics());
                    boomClient.drawGameCoin(main_game.getGraphics(), String.valueOf(gCoin));
                    boomClient.drawRewardCoin(main_game.getGraphics(), String.valueOf((int) (tCoin / 2)));
                    if (listUserInBoom.size() > 1 && isHostBoom) {
                        boomClient.drawFightingIcon(main_game.getGraphics());
                    }
                }
                break;
            default:
                break;
        }
    }

    //none - position - type - flag
    private void drawWinLine(String line) {
        if (line.split(CommonDefine.SEPARATOR_GROUP).length > 3) {
            String position_win = line.split(CommonDefine.SEPARATOR_GROUP)[1].trim();
            String win_type = line.split(CommonDefine.SEPARATOR_GROUP)[2].trim();
            String win_flag = line.split(CommonDefine.SEPARATOR_GROUP)[3].trim();
            String[] arr_position = position_win.split(CommonDefine.COMMA);
            if (CommonMethod.isNumberic(win_flag) && CommonMethod.isNumberic(win_type)) {
                if (arr_position.length > 1) {
                    for (String xy : arr_position) {
                        String str_xx = xy.split(CommonDefine.SEPARATOR_KEY_VALUE)[0];
                        String str_yy = xy.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                        if (CommonMethod.isNumberic(str_xx) && CommonMethod.isNumberic(str_yy)) {
                            int xx = Integer.parseInt(str_xx.trim());
                            int yy = Integer.parseInt(str_yy.trim());
                            switch (Integer.parseInt(win_type)) {
                                case CommonDefine.CARO_GAME_END_WITH_CROSS_1:
                                    CaroGameClient.drawCross1(main_game.getGraphics(), CommonDefine.CARO_COLOR_WIN_LINE, xx, yy);
                                    break;
                                case CommonDefine.CARO_GAME_END_WITH_CROSS_2:
                                    CaroGameClient.drawCross2(main_game.getGraphics(), CommonDefine.CARO_COLOR_WIN_LINE, xx, yy);
                                    break;
                                case CommonDefine.CARO_GAME_END_WITH_HOZ:
                                    CaroGameClient.drawHoz(main_game.getGraphics(), CommonDefine.CARO_COLOR_WIN_LINE, xx, yy);
                                    break;
                                case CommonDefine.CARO_GAME_END_WITH_VER:
                                    CaroGameClient.drawVer(main_game.getGraphics(), CommonDefine.CARO_COLOR_WIN_LINE, xx, yy);
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                }
                if (caroGameValue != 0) {
                    CaroGameClient.drawButtonPlayAgain(main_game.getGraphics(), CommonDefine.CARO_COLOR_PA_BACKGROUND, CommonDefine.CARO_COLOR_PA_TEXT);
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInventory;
    private javax.swing.JButton btnShop;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_dxh;
    private javax.swing.JButton btn_find_match;
    private javax.swing.JButton btn_reconnect;
    private javax.swing.JButton btn_send;
    private javax.swing.JComboBox<String> cbb_chat_type;
    private javax.swing.JComboBox<String> cbb_choose_game;
    private javax.swing.JMenuItem change_color_O;
    private javax.swing.JMenuItem change_color_X;
    private javax.swing.JMenuBar game_menu;
    private javax.swing.JPanel game_notice;
    private javax.swing.JLabel lb_coin;
    private javax.swing.JList list_user;
    private javax.swing.JEditorPane main_chat;
    private javax.swing.JPanel main_game;
    private javax.swing.JMenuItem menu_design;
    private javax.swing.JMenu menu_edit;
    private javax.swing.JMenu menu_edit_caro;
    private javax.swing.JMenu menu_file;
    private javax.swing.JMenu menu_help;
    private javax.swing.JMenu menu_tool;
    private javax.swing.JLabel notify_state;
    private javax.swing.JPanel panel_action;
    private javax.swing.JScrollPane scroll_chat;
    private javax.swing.JScrollPane scroll_user;
    private javax.swing.JTextField tf_mess;
    // End of variables declaration//GEN-END:variables
}
