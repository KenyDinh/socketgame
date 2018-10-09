/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.gui;

import game.server.element.BombType;
import game.server.common.CommonDefine;
import game.server.common.CommonMethod;
import game.server.socket.BauCuaGameServer;
import game.server.socket.BoomGameServer;
import game.server.socket.CardGameServer;
import game.server.socket.CaroGameServer;
import game.server.socket.ClientServerSocket;
import game.server.socket.DayXeHangGameServer;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author KenyDinh
 */
public class ServerFrame extends javax.swing.JFrame {

    /**
     * Creates new form ServerFrame
     */
    private ServerSocket serverSocket;
    private Boolean isStartServer = false;
    private int port;
    private CaroGameServer[] listCaroGame = new CaroGameServer[CommonDefine.CARO_GAME_MAX_GAME];
    private BauCuaGameServer[] listBauCuaGame = new BauCuaGameServer[CommonDefine.BAUCUA_GAME_MAX_CREATED];
    private BoomGameServer[] listBoomGame = new BoomGameServer[CommonDefine.BOOM_GAME_MAX_GAME];
    private CardGameServer[] listCardGame = new CardGameServer[CommonDefine.CARD_GAME_MAX_GAME];

    private DefaultListModel<String> listClientName = new DefaultListModel<String>();
    private String[] listAction;

    private static Map<String, Map<String, String>> mapData;
    private static String dataDxh;
    private static StringBuilder boomData = new StringBuilder();
    private static Map<Integer, String> mapBoom = new HashMap<>();
    private static String shopData;
    private static Map<String, Map<Integer, Integer>> mapItemOwn = new HashMap<>();
    private static int BASE_DXH_REWARD = 10;
    private static ServerFrame serverFrame;

    @SuppressWarnings("LeakingThisInConstructor")
    public ServerFrame() {
        initComponents();
        setResizable(false);
        CommonMethod.setLocationFrame(this);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/game/server/imageIcon/server.png")).getImage());
        initListAction();
        initData();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    //get Singleton Instance
    public static ServerFrame getServerFrame() {
        if (serverFrame == null) {
            serverFrame = new ServerFrame();
        }
        return serverFrame;
    }

    public static void initData() {
        mapData = CommonMethod.getMapClientData();
        dataDxh = CommonMethod.getMapDXHData();
        mapBoom = CommonMethod.getBoomMap();
        shopData = CommonMethod.getShopData();
        mapItemOwn = CommonMethod.getMapItemOwn();
        BombType.initBombTypeData();
    }

    public static String getShopData() {
        return shopData;
    }

    public static void updateItemOwn(String mac, int itemVal, int itemNum) {
        Map<Integer, Integer> mItem = new HashMap<>();
        if (mapItemOwn.containsKey(mac)) {
            mItem = mapItemOwn.get(mac);
        }
        if (mItem.containsKey(itemVal)) {
            itemNum += mItem.get(itemVal);
        }
        mItem.put(itemVal, itemNum);
        mapItemOwn.put(mac, mItem);
    }

    public static String getItemOwnByMac(String mac) {
        if (mapItemOwn.containsKey(mac)) {
            StringBuilder sb = new StringBuilder();
            Map<Integer, Integer> mItem = mapItemOwn.get(mac);
            for (Entry<Integer, Integer> entry : mItem.entrySet()) {
                sb.append(entry.getKey()).append("-").append(entry.getValue()).append(CommonDefine.COMMA);
            }
            return sb.toString().isEmpty() ? null : sb.toString();
        }
        return null;
    }

    private String getStringItemOwnData() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Map<Integer, Integer>> entrys : mapItemOwn.entrySet()) {
            sb.append(entrys.getKey()).append(CommonDefine.SEPARATOR_KEY_VALUE);
            for (Entry<Integer, Integer> entry : entrys.getValue().entrySet()) {
                sb.append(entry.getKey()).append("-").append(entry.getValue()).append(CommonDefine.COMMA);
            }
            sb.append(CommonDefine.BREAK_LINE);
        }
        return sb.toString();
    }

    public static String getBoomData() {
        //random
        if (mapBoom.isEmpty()) {
            return "";
        }
        int ran = (int) (Math.random() * mapBoom.size() + 1);
        if (mapBoom.containsKey(ran)) {
            return mapBoom.get(ran);
        }
        return "";
    }

    public static String getDataDxh() {
        return dataDxh;
    }

    public static String getDataByMAC(String mac) {
        if (!mapData.containsKey(mac)) {
            createNewData(mac);
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : mapData.get(mac).entrySet()) {
            sb.append(entry.getKey());
            sb.append(CommonDefine.SEPARATOR_KEY_VALUE);
            sb.append(entry.getValue());
            sb.append(CommonDefine.BREAK_LINE);
        }
        return sb.toString().trim();
    }

    private static void createNewData(String mac) {
        Map<String, String> value = new HashMap<>();
        value.put(CommonDefine.DATA_COIN_KEY, String.valueOf(CommonDefine.START_COIN_DEFAULT));
        mapData.put(mac, value);
    }

    /**
     * update map data
     *
     * @param mac MAC Address like key in map data
     * @param data data to update
     */
    public static void updateData(String mac, String data) {
        if (mapData.containsKey(mac)) {
            Map<String, String> value = new HashMap<>();
            for (String s : data.split(CommonDefine.BREAK_LINE)) {
                if (s.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                    value.put(s.split(CommonDefine.SEPARATOR_KEY_VALUE)[0], s.split(CommonDefine.SEPARATOR_KEY_VALUE)[1]);
                }
            }
            if (!value.isEmpty()) {
                mapData.put(mac, value);
            }
        }
    }

    private static boolean saveData() {
        if (mapData.isEmpty()) {
            return false;
        }
        return CommonMethod.storeClienData(mapData);
    }

    private void initListAction() {
        listAction = CommonDefine.LIST_ACTION;
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (String a : listAction) {
            model.addElement(a);
        }
        cbb_action.setModel(model);
    }

    public boolean isFullGame(int gamtype) {
        switch (gamtype) {
            case CommonDefine.GAME_TYPE_CARO_GAME:
                for (int i = 0; i < CommonDefine.CARO_GAME_MAX_GAME; i++) {
                    if (listCaroGame[i] == null) {
                        return false;
                    }
                }
                return true;
            case CommonDefine.GAME_TYPE_BTCC_GAME:
                for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_CREATED; i++) {
                    if (listBauCuaGame[i] == null) {
                        return false;
                    }
                }
                return true;
            case CommonDefine.GAME_TYPE_BOOM_GAME:
                for (int i = 0; i < CommonDefine.BOOM_GAME_MAX_GAME; i++) {
                    if (listBoomGame[i] == null) {
                        return false;
                    }
                }
                return true;
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                for (int i = 0; i < CommonDefine.CARD_GAME_MAX_GAME; i++) {
                    if (listCardGame[i] == null) {
                        return false;
                    }
                }
                return true;
            default:
                return true;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Caro Game">
    private CaroGameServer getCaroGameById(int gameId) {
        for (CaroGameServer csg : listCaroGame) {
            if (csg != null && csg.getId() == gameId) {
                return csg;
            }
        }
        return null;
    }

    private void updateCaroGameClienOutOrOff(int caroGameId, int clientId) {
        CaroGameServer caroGame = getCaroGameById(caroGameId);
        if (caroGame == null) {
            //log null
            return;
        }
        ClientServerSocket client = caroGame.getClientInGameById(clientId);
        if (client != null) {
            if (client.isIsPlayer()) {
                caroGame.removePlayer(client);
                ClientServerSocket.sendToAllClientInGame(CommonDefine.NOTICE_GAME_IS_DESTROY, caroGameId);
                removeCaroGame(caroGame);
            } else if (client.isIsViewer()) {
                caroGame.removeViewer(client);
            }
        }
    }

    private void removeCaroGame(CaroGameServer caroGame) {
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_GAME; i++) {
            if (listCaroGame[i] != null && listCaroGame[i].getId() == caroGame.getId()) {
                listCaroGame[i] = null;
            }
        }
    }

    private void joinCaroGame(int caroGameId, int clientId) {
        ClientServerSocket client = ClientServerSocket.getClientServerSocketById(clientId);
        if (client == null) {
            //log null
            return;
        }
        CaroGameServer caroGame = null;
        if (caroGameId == 0) {
            for (CaroGameServer cgs : listCaroGame) {
                if (cgs != null && (cgs.getCountPlayer() < CommonDefine.CARO_GAME_MAX_PLAYER || cgs.getCountViewer() < CommonDefine.CARO_GAME_MAX_VIEWER)) {
                    caroGame = cgs;
                    break;
                }
            }
            if (caroGame == null) {
                client.sendMessageToSelf(CommonDefine.NOTICE_NO_GAME_TO_JOIN);
                return;
            }
        } else {
            caroGame = getCaroGameById(caroGameId);
            if (caroGame == null) {
                caroGame = new CaroGameServer(caroGameId);
            }
        }
        if (caroGame.getCountPlayer() == 0) {
            boolean flag = false;
            for (int i = 0; i < CommonDefine.CARO_GAME_MAX_GAME; i++) {
                if (listCaroGame[i] == null) {
                    listCaroGame[i] = caroGame;
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                client.sendMessageToSelf(CommonDefine.NOTICE_FULL_GAME);
                return;
            }
        }
        if (caroGame.isEndGame()) {
            client.sendMessageToSelf(CommonDefine.CARO_GAME_EXIT_ROOM);
            return;
        }
        if (caroGame.getCountPlayer() < CommonDefine.CARO_GAME_MAX_PLAYER) {
            caroGame.addPlayer(client);
        } else if (caroGame.getCountViewer() < CommonDefine.CARO_GAME_MAX_VIEWER) {
            caroGame.addViewer(client);
        } else {
            client.sendMessageToSelf(CommonDefine.NOTICE_FULL_CLIENT_INGAME);
        }
    }

    public void getCoordinateCaroGame(String line, int gameId) {
        String arrValue[] = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
        if (arrValue.length < 4) {
            return;
        }
        if (CommonMethod.isNumeric(arrValue[1]) && CommonMethod.isNumeric(arrValue[2]) && CommonMethod.isNumeric(arrValue[3])) {
            int x = Integer.parseInt(arrValue[1].trim());
            int y = Integer.parseInt(arrValue[2].trim());
            int value = Integer.parseInt(arrValue[3].trim());
            for (CaroGameServer cgs : listCaroGame) {
                if (cgs != null && cgs.getId() == gameId) {
                    if (cgs.isAvailableCoordinate(x, y)) {
                        cgs.storeCoordinate(x, y, value);
                        for (ClientServerSocket css : cgs.getListClientInGame()) {
                            if (cgs.isEndGame()) {
                                css.sendMessageToSelf(line + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_END + CommonDefine.SEPARATOR_GROUP + cgs.getWinCoordinate() + CommonDefine.SEPARATOR_GROUP + cgs.getWinType() + CommonDefine.SEPARATOR_GROUP + cgs.getWinFlag());
                            } else {
                                css.sendMessageToSelf(line);
                            }
                        }
                    }
                }
            }
        }

    }

    public void playAgainCaroGame(ClientServerSocket client, int gameId) {
        for (CaroGameServer cgs : listCaroGame) {
            if (cgs != null && cgs.getId() == gameId) {
                cgs.playAgain(client);
                break;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Bau Cua Game">
    private BauCuaGameServer getBaucuaGameById(int gameId) {
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_CREATED; i++) {
            if (listBauCuaGame[i] != null && listBauCuaGame[i].getGameId() == gameId) {
                return listBauCuaGame[i];
            }
        }
        return null;
    }

    public void setTotalHostCoins(int gameId, int coins) {
        BauCuaGameServer baucua = getBaucuaGameById(gameId);
        if (baucua != null) {
            baucua.setHostCoins(coins);
        }
    }

    private void joinBauCuaGame(int gameId, int clientId) {
        ClientServerSocket client = ClientServerSocket.getClientServerSocketById(clientId);
        if (client == null) {
            //log null
            return;
        }
        if (gameId == 0) {
            for (BauCuaGameServer bc : listBauCuaGame) {
                if (bc != null && bc.getCountPlayer() < CommonDefine.BAUCUA_GAME_MAX_PLAYER) {
                    bc.addPlayer(client);
                    return;
                }
            }
            client.sendMessageToSelf(CommonDefine.NOTICE_NO_GAME_TO_JOIN);
        } else {
            BauCuaGameServer baucua = getBaucuaGameById(gameId);
            if (baucua == null) {
                baucua = new BauCuaGameServer(gameId);
            }
            if (baucua.getCountPlayer() == 0) {
                boolean flag = false;
                for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_CREATED; i++) {
                    if (listBauCuaGame[i] == null) {
                        listBauCuaGame[i] = baucua;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    client.sendMessageToSelf(CommonDefine.NOTICE_FULL_GAME);
                    return;
                }
            }
            if (baucua.getCountPlayer() >= CommonDefine.BAUCUA_GAME_MAX_PLAYER) {
                client.sendMessageToSelf(CommonDefine.NOTICE_FULL_CLIENT_INGAME);
                return;
            }
            baucua.addPlayer(client);
        }
    }

    public void resetBaucuaValue(int gameId) {
        BauCuaGameServer baucua = getBaucuaGameById(gameId);
        if (baucua != null) {
            baucua.resetAllValue();
        }
    }

    public void getResultBauCua(int gameId) {
        BauCuaGameServer baucua = getBaucuaGameById(gameId);
        if (baucua != null) {
            baucua.resultBauCua();
        }
    }

    public void updateBaucuaOpenClose(int gameId, boolean open) {
        BauCuaGameServer baucua = getBaucuaGameById(gameId);
        if (baucua != null) {
            baucua.setIsOpen(open);
        }
    }

    public void updateBauCuaCoin(String choice, String cn, int gameId) {
        if (CommonMethod.isValidNumber(choice) && CommonMethod.isValidNumber(cn)) {
            BauCuaGameServer baucua = getBaucuaGameById(gameId);
            if (baucua != null) {
                baucua.updateTienCuoc(Integer.parseInt(choice.trim()), Integer.parseInt(cn.trim()));
            }
        }
    }

    private void updateBauCuaGameClientOff(int gameId, int clientId) {
        BauCuaGameServer baucua = getBaucuaGameById(gameId);
        if (baucua == null) {
            return;
        }
        ClientServerSocket client = baucua.getPlayerById(clientId);
        if (client != null) {
            baucua.removePlayer(client);
            if (baucua.getHost().getClientId() == clientId) {
                ClientServerSocket.sendToAllClientInGame(CommonDefine.NOTICE_GAME_IS_DESTROY, gameId);
                for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_CREATED; i++) {
                    if (listBauCuaGame[i] != null && listBauCuaGame[i].getGameId() == baucua.getGameId()) {
                        listBauCuaGame[i] = null;
                    }
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Boom">
    public BoomGameServer getBoomGameById(int gameId) {
        for (BoomGameServer boom : listBoomGame) {
            if (boom != null && boom.getGameId() == gameId) {
                return boom;
            }
        }
        return null;
    }

    public void joinBoomGame(int gameId, int clientId) {
        ClientServerSocket client = ClientServerSocket.getClientServerSocketById(clientId);
        if (client == null) {
            //log null
            return;
        }
        if (gameId == 0) {
            for (BoomGameServer boom : listBoomGame) {
                if (boom != null && boom.getCountPlayer() < CommonDefine.BOOM_GAME_MAX_PLAYER && !boom.isGameStart()) {
                    boom.addPlayer(client);
                    return;
                }
            }
            client.sendMessageToSelf(CommonDefine.NOTICE_NO_GAME_TO_JOIN);
        } else {
            BoomGameServer boom = getBoomGameById(gameId);
            if (boom == null) {
                boom = new BoomGameServer(gameId);
                boom.setMap(boom.parseMap(getBoomData()));
                boolean flag = false;
                for (int i = 0; i < CommonDefine.BOOM_GAME_MAX_GAME; i++) {
                    if (listBoomGame[i] == null) {
                        listBoomGame[i] = boom;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    client.sendMessageToSelf(CommonDefine.NOTICE_FULL_GAME);
                    return;
                }
            }
            if (boom.getCountPlayer() >= CommonDefine.BOOM_GAME_MAX_PLAYER) {
                client.sendMessageToSelf(CommonDefine.NOTICE_FULL_CLIENT_INGAME);
                return;
            }
            if (boom.isGameStart()) {
                client.sendMessageToSelf(CommonDefine.NOTICE_ALERT_MESS_SERVER + "This game has started.");
                return;
            }
            if (boom.isEndGame()) {
                client.sendMessageToSelf(CommonDefine.NOTICE_ALERT_MESS_SERVER + "This game has finished.");
                return;
            }
            boom.addPlayer(client);
        }
    }

    public void startBoomGame(int gameId) {
        BoomGameServer boom = getBoomGameById(gameId);
        if (boom == null) {
            return;
        }
        if (!boom.isAllPlayerReady()) {
            boom.getHost().sendMessageToSelf(CommonDefine.NOTICE_ALERT_MESS_SERVER + "Player are not ready!");
            return;
        }
        boom.startGame();
    }

    public void updatePlayerMove(int gameId, int x, int y, int val, int status, String type) {
        BoomGameServer boom = getBoomGameById(gameId);
        if (boom == null) {
            return;
        }
        boom.movePlayer(x, y, val, status, type);
    }

    public void addBoom(int gameId, int x, int y, int val, int m_val) {
        BoomGameServer boom = getBoomGameById(gameId);
        if (boom == null) {
            return;
        }
        boom.addBoom(x, y, val, m_val);
    }

    public void updateBoomGameClientOff(int gameId, int clientId) {
        BoomGameServer boom = getBoomGameById(gameId);
        if (boom == null) {
            return;
        }
        ClientServerSocket player = boom.getPlayerById(clientId);
        if (player == null) {
            return;
        }
        boom.removePlayer(player);
        if (boom.getCountPlayer() <= 1 && boom.isGameStart()) {
            boom.setEndGame(true);
            boom.setGameStart(false);
            boom.sendMessToAllPlayer(CommonDefine.BOOM_GAME_END_GAME);
            boom.removeAllPlayer();
            for (int i = 0; i < CommonDefine.BOOM_GAME_MAX_GAME; i++) {
                if (listBoomGame[i] != null && listBoomGame[i].getGameId() == boom.getGameId()) {
                    listBoomGame[i] = null;
                }
            }
        }
    }

    public void updateBoomGameCoin(int gameId, int coin) {
        BoomGameServer boom = getBoomGameById(gameId);
        if (boom != null) {
            boom.updateGameCoin(coin);
        }
    }

    public void removeBoomGame(int gameId) {
        for (int i = 0; i < CommonDefine.BOOM_GAME_MAX_GAME; i++) {
            if (listBoomGame[i] != null && listBoomGame[i].getGameId() == gameId) {
                listBoomGame[i] = null;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Card Game">
    public CardGameServer getCardGameById(int gameId) {
        for (CardGameServer cgs : listCardGame) {
            if (cgs != null && cgs.getGameId() == gameId) {
                return cgs;
            }
        }
        return null;
    }

    public void joinCardGame(int gameId, int clientId, int gameType) {
        ClientServerSocket client = ClientServerSocket.getClientServerSocketById(clientId);
        if (client == null) {
            return;
        }
        if (gameId == 0) {
            for (CardGameServer card : listCardGame) {
                if (card != null && card.getCountPlayer() < CommonDefine.CARD_GAME_MAX_GAME) {
                    card.addPlayer(client);
                    return;
                }
            }
            client.sendMessageToSelf(CommonDefine.NOTICE_NO_GAME_TO_JOIN);
        } else {
            CardGameServer card = getCardGameById(gameId);
            if (card == null) {
                card = new CardGameServer(gameId, gameType);
                boolean flag = false;
                for (int i = 0; i < CommonDefine.CARD_GAME_MAX_GAME; i++) {
                    if (listCardGame[i] == null) {
                        listCardGame[i] = card;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    client.sendMessageToSelf(CommonDefine.NOTICE_FULL_GAME);
                    return;
                }
            }
            if (card.getCountPlayer() >= CardGameServer.MAX_PLAYER) {
                client.sendMessageToSelf(CommonDefine.NOTICE_FULL_CLIENT_INGAME);
                return;
            }
            card.addPlayer(client);
        }
    }

    public void startCardGame(int gameId) {
        CardGameServer cgs = getCardGameById(gameId);
        if (cgs != null) {//&& cgs.getCountPlayer() > 1){
            cgs.startGame();
        }
    }

    public void executeAction(int gameId, String data) {
        CardGameServer cgs = getCardGameById(gameId);
        if (cgs != null) {//&& cgs.getCountPlayer() > 1){
            cgs.endPlayerTurn(data);
        }
    }

    public void updateBetCoin(int gameId, int betCoin) {
        CardGameServer cgs = getCardGameById(gameId);
        if(cgs != null){
            cgs.setBetCoin(betCoin);
        }
    }
    
    public void updateCardGameClienOutOrOff(int gameType, int gameId, int clientId){
        CardGameServer cgs = getCardGameById(gameId);
        if(cgs == null){
            return;
        }
        ClientServerSocket player = ClientServerSocket.getClientServerSocketById(clientId);
        if (player == null) {
            return;
        }
        cgs.removePlayer(player);
        if(cgs.getCountPlayer() < 1){
            for(int i = 0; i < CommonDefine.CARD_GAME_MAX_GAME; i++){
                if(listCardGame[i] != null && listCardGame[i].getGameId() == cgs.getGameId()){
                    System.out.println("destroy game :" + cgs.getGameId());
                    listCardGame[i] = null;
                    break;
                }
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Common">
    public void findingMatch(int gameType, int gameId, int clientId) {
        switch (gameType) {
            case CommonDefine.GAME_TYPE_CARO_GAME:
                joinCaroGame(gameId, clientId);
                break;
            case CommonDefine.GAME_TYPE_BTCC_GAME:
                joinBauCuaGame(gameId, clientId);
                break;
            case CommonDefine.GAME_TYPE_BOOM_GAME:
                joinBoomGame(gameId, clientId);
                break;
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                joinCardGame(gameId, clientId, gameType);
                break;
            default:
                break;
        }
    }

    private void removeClientFormList(int clientId) {
        ClientServerSocket client = ClientServerSocket.getClientServerSocketById(clientId);
        if (client != null) {
            for (int i = 0; i < listClientName.size(); i++) {
                if (listClientName.getElementAt(i).equals(client.getClientName())) {
                    listClientName.removeElementAt(i);
                    ClientServerSocket.removeClientServerSocket(client);
                    ClientServerSocket.sendToAllClient(CommonDefine.NOTICE_CLIENT_OFF + CommonDefine.SEPARATOR_KEY_VALUE + client.getClientName());
                    break;
                }
            }

        }
        list_client.setModel(listClientName);
    }

    public void removeOldClientNameOnList(String oldName) {
        for (int i = 0; i < listClientName.size(); i++) {
            if (listClientName.getElementAt(i).equals(oldName)) {
                listClientName.removeElementAt(i);
                break;
            }
        }
        list_client.setModel(listClientName);
    }

    public void addClientToList(String name) {
        listClientName.addElement(name);
        list_client.setModel(listClientName);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listClientName.size(); i++) {
            sb.append(listClientName.get(i)).append(CommonDefine.SEPARATOR_KEY_VALUE);
        }
        String result = sb.toString().trim();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        ClientServerSocket.sendToAllClient(CommonDefine.NOTICE_CLIENT_IN + CommonDefine.SEPARATOR_GROUP + result);
    }

    public void updateClientOutOrOff(int gameType, int gameId, int clientId, boolean isOff) {
        if (isOff) {
            removeClientFormList(clientId);
        }
        switch (gameType) {
            case CommonDefine.GAME_TYPE_CARO_GAME:
                updateCaroGameClienOutOrOff(gameId, clientId);
                break;
            case CommonDefine.GAME_TYPE_BTCC_GAME:
                updateBauCuaGameClientOff(gameId, clientId);
                break;
            case CommonDefine.GAME_TYPE_BOOM_GAME:
                updateBoomGameClientOff(gameId, clientId);
                break;
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                updateCardGameClienOutOrOff(gameType, gameId, clientId);
                break;
            default:
                break;
        }
    }
    //</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_start = new javax.swing.JButton();
        tf_mess = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        btn_close = new javax.swing.JButton();
        lb_notice = new javax.swing.JLabel();
        btn_create_cfg = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_client = new javax.swing.JList<>();
        cbb_action = new javax.swing.JComboBox<>();
        lb_action_0 = new javax.swing.JLabel();
        btn_active = new javax.swing.JButton();
        tf_value1 = new javax.swing.JTextField();
        lb_action_1 = new javax.swing.JLabel();
        tf_value2 = new javax.swing.JTextField();
        lb_action_2 = new javax.swing.JLabel();
        tf_value3 = new javax.swing.JTextField();
        lb_action_3 = new javax.swing.JLabel();
        lb_action_4 = new javax.swing.JLabel();
        tf_value4 = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btn_start.setText("Start Server");
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startActionPerformed(evt);
            }
        });

        tf_mess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_messKeyPressed(evt);
            }
        });

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        btn_close.setText("Close Server");
        btn_close.setToolTipText("");
        btn_close.setEnabled(false);
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        lb_notice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btn_create_cfg.setText("Create Config");
        btn_create_cfg.setActionCommand("");
        btn_create_cfg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_create_cfgActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(list_client);

        cbb_action.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbb_actionItemStateChanged(evt);
            }
        });

        lb_action_0.setText("Action");

        btn_active.setText("Execute");
        btn_active.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_activeActionPerformed(evt);
            }
        });

        lb_action_1.setText("Option 1");

        lb_action_2.setText("Option 2");

        lb_action_3.setText("Option 3");

        lb_action_4.setText("Option 4");

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tf_mess, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btn_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_close, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb_action_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReset, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_create_cfg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbb_action, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tf_value1)
                            .addComponent(tf_value2)
                            .addComponent(tf_value3)
                            .addComponent(tf_value4)
                            .addComponent(btn_active, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(30, 30, 30))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_notice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_close, btn_create_cfg, btn_start});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_notice)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_start)
                    .addComponent(btn_close)
                    .addComponent(btn_create_cfg))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbb_action, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_0))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_value1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_value2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_value3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_action_3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_action_4)
                            .addComponent(tf_value4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_active)
                            .addComponent(btnReset))))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_mess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_send))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_close, btn_create_cfg, btn_start});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startActionPerformed
        if (!isStartServer) {
            String s_port = JOptionPane.showInputDialog(ServerFrame.getServerFrame(), "Enter the port connect!");
            if (s_port != null && (s_port.length() != 4 || !s_port.matches("[0-9]+"))) {
                s_port = null;
            }
            Thread server = new Thread(new ServerStart(s_port));
            server.start();
            isStartServer = true;
            btn_start.setEnabled(false);
            btn_close.setEnabled(true);
        }

    }//GEN-LAST:event_btn_startActionPerformed

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        if (isStartServer) {
            isStartServer = false;
            closeServer();
            btn_start.setEnabled(true);
            btn_close.setEnabled(false);
            listCaroGame = new CaroGameServer[CommonDefine.CARO_GAME_MAX_GAME];
            listBauCuaGame = new BauCuaGameServer[CommonDefine.BAUCUA_GAME_MAX_CREATED];
            listClientName = new DefaultListModel<String>();
            list_client.setModel(listClientName);
        }
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        String mess = "[" + CommonMethod.getDateFormatStringNow() + "] <span style=\"color:#EE3316;\">Server: " + tf_mess.getText() + "</span>";
        ClientServerSocket.sendToAllClient(mess);
        tf_mess.setText("");
        tf_mess.requestFocus();
    }//GEN-LAST:event_btn_sendActionPerformed

    private void tf_messKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_messKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_sendActionPerformed(null);
        }
    }//GEN-LAST:event_tf_messKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeServer();
        new Thread(() -> {
            try {
                Thread.sleep(500);
                System.exit(0);
            } catch (InterruptedException ex) {
            }
        }).start();
    }//GEN-LAST:event_formWindowClosing

    private void btn_create_cfgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_create_cfgActionPerformed
        String strIp = JOptionPane.showInputDialog(ServerFrame.getServerFrame(), "Enter your IP.");

        try {
            InetAddress host_server = InetAddress.getLocalHost();
            Map<String, String> mapConfig = new HashMap<>();
            mapConfig.put(CommonDefine.VALID_CONTENT_KEY, String.valueOf(new Integer(1)));
            mapConfig.put(CommonDefine.VALID_CONTENT_VALUE, String.valueOf(new Integer(1)));
            if (strIp == null || !strIp.matches("[0-9]+(\\.[0-9]+)+")) {
                mapConfig.put(CommonDefine.HOST_SERVER_KEY, host_server.getHostAddress());
            } else {
                mapConfig.put(CommonDefine.HOST_SERVER_KEY, strIp);
            }
            CommonMethod.log("HOST: " + mapConfig.get(CommonDefine.HOST_SERVER_KEY));
            mapConfig.put(CommonDefine.POST_SERVER_KEY, String.valueOf(getPort()));
            CommonMethod.createFileWithContent(CommonDefine.CONFIG_FILE_NAME, CommonMethod.formatMapToContentFile(mapConfig));
            showNotify("Done!!!");
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btn_create_cfgActionPerformed

    private void showNotify(String noti) {
        lb_notice.setText(noti);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                }
                lb_notice.setText("");
            }
        }.start();
    }

    private void cbb_actionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbb_actionItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            int index = cbb_action.getSelectedIndex();
            tf_value1.setText("");
            tf_value2.setText("");
            tf_value3.setText("");
            tf_value4.setText("");
            lb_action_1.setText("Option 1");
            lb_action_2.setText("Option 2");
            lb_action_3.setText("Option 3");
            lb_action_4.setText("Option 4");
            switch (index) {
                case CommonDefine.UPDATE_COIN:
                    //update Data
                    lb_action_1.setText("Coin");
                    break;
                case CommonDefine.BLOCK_CHAT:
                    lb_action_1.setText("Time(s)");
                    break;
                case CommonDefine.UPDATE_LEVEL_DXH:
                    lb_action_1.setText("Level");
                    break;
                case CommonDefine.ALLOW_RENAME:

                    break;
                case CommonDefine.UPDATE_REWARD_DXH:
                    lb_action_1.setText("Base reward");
                    break;
                case CommonDefine.SHOW_COIN_ALL:

                    break;
                case CommonDefine.GENERATE_MAP_DXH:

                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_cbb_actionItemStateChanged

    private void resetAfterAction() {
        tf_value1.setText("");
        tf_value2.setText("");
        tf_value3.setText("");
        tf_value4.setText("");
        lb_action_1.setText("Option 1");
        lb_action_2.setText("Option 2");
        lb_action_3.setText("Option 3");
        lb_action_4.setText("Option 4");
        cbb_action.setSelectedIndex(0);
    }

    private boolean isValidNumber(String str) {
        return str.matches("[0-9]{1,9}");
    }

    private void btn_activeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_activeActionPerformed
        int val_act = cbb_action.getSelectedIndex();
        if (val_act == 0) {
            showNotify("Choose Action!");
            return;
        }
        switch (val_act) {
            case CommonDefine.UPDATE_COIN:
                //update Data
                String strCoin = tf_value1.getText();
                if (strCoin != null && isValidNumber(strCoin)) {
                    String mess = CommonDefine.SETTING_UPDATE_COIN + strCoin;
                    for (String strName : list_client.getSelectedValuesList()) {
                        ClientServerSocket.sendMessageByName(strName, mess);
                    }
                }
                break;
            case CommonDefine.BLOCK_CHAT:
                //block chat
                String strTime = tf_value1.getText();
                if (strTime != null && isValidNumber(strTime)) {
                    String mess = CommonDefine.NOTICE_BLOCK_CHAT + CommonDefine.SEPARATOR_KEY_VALUE + strTime + "000" + CommonDefine.SEPARATOR_KEY_VALUE + "<i>Chat is block!</i>";
                    for (String strName : list_client.getSelectedValuesList()) {
                        ClientServerSocket.sendMessageByName(strName, mess);
                    }
                }
                break;
            case CommonDefine.UPDATE_LEVEL_DXH:
                //update level dxh
                String strLvl = tf_value1.getText();
                if (strLvl != null && isValidNumber(strLvl)) {
                    String mess = CommonDefine.SETTING_UPDATE_LEVEL_DXH + strLvl;
                    for (String strName : list_client.getSelectedValuesList()) {
                        ClientServerSocket.sendMessageByName(strName, mess);
                    }
                }
                break;
            case CommonDefine.ALLOW_RENAME:
                //rename
                String mess = CommonDefine.SETTING_ALLOW_RENAME + "<span style=\"color:#0EA9AE\">Type CLIENT_NAME_SET:<i>new name</i> to change your nick name.</span>";
                for (String strName : list_client.getSelectedValuesList()) {
                    for (ClientServerSocket css : ClientServerSocket.getListClientServerSocket()) {
                        if (css != null && css.getClientName().equals(strName)) {
                            css.allowRename();
                            css.sendMessageToSelf(mess);
                        }
                    }
                }
                break;
            case CommonDefine.UPDATE_REWARD_DXH:
                // update BASE_DXH_REWARD
                String strReward = tf_value1.getText();
                if (strReward != null && isValidNumber(strReward)) {
                    BASE_DXH_REWARD = Integer.parseInt(strReward);
                    ClientServerSocket.sendToAllClient(String.format("%s:%d", CommonDefine.SETTING_CLIENT_DXH_BASE_REWARD, BASE_DXH_REWARD));
                }
                break;
            case CommonDefine.SHOW_COIN_ALL:
//                DayXeHangGameServer.initGameData(getDataDxh());
//                SokobanTest.getSokoban().setVisible(true);
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<h3>Player info</h3>");
                int cnt = 0;
                sb.append("<p>");
                for (ClientServerSocket css : ClientServerSocket.getListClientServerSocket()) {
                    if (css != null) {
                        if (cnt > 0) {
                            sb.append("<br/>");
                        }
                        sb.append(css.getClientName());
                        sb.append(" : ");
                        sb.append(css.getTotalCoins()).append("C");
                        cnt ++;
                    }
                }
                sb.append("</p>");
                sb.append("<br/>");
                sb.append("</html>");
                JOptionPane.showMessageDialog(this, sb.toString(), "Infomations", JOptionPane.INFORMATION_MESSAGE);
                break;
            case CommonDefine.GENERATE_MAP_DXH:
                if (!CommonMethod.createFileWithContent(CommonDefine.MAP_HASH_FILE_NAME, CommonMethod.getEncryptContent(CommonMethod.getContentFile(CommonDefine.MAP_FILE_NAME)))) {
                    showNotify("Failed!!!");
                } else {
                    showNotify("Done!!!");
                }
                dataDxh = CommonMethod.getMapDXHData();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_activeActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetAfterAction();
    }//GEN-LAST:event_btnResetActionPerformed

    //<editor-fold defaultstate="collapsed" desc="Socket Server">
    private class ServerStart implements Runnable {

        private int serverPort;

        public ServerStart(String serverPort) {
            if (serverPort != null) {
                this.serverPort = Integer.parseInt(serverPort);
            } else {
                this.serverPort = CommonDefine.DEFAULT_PORT;
            }
            CommonMethod.log("Start server at port: " + this.serverPort);
            setPort(this.serverPort);
        }

        public int getServerPort() {
            return serverPort;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(getServerPort());
                while (true) {
                    for (int i = 0; i < CommonDefine.MAX_CLIENT_CONNECT; i++) {
                        if (ClientServerSocket.getClientServerSocketByIndex(i) == null) {
                            ClientServerSocket css = new ClientServerSocket(serverSocket.accept());
                            ClientServerSocket.storeClientServerSocket(css, i);
                            if (ClientServerSocket.getClientServerSocketByIndex(i) != null) {
                                ClientServerSocket.getClientServerSocketByIndex(i).start();
                            }
                        }
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    private void closeServer() {
        for (int i = 0; i < CommonDefine.MAX_CLIENT_CONNECT; i++) {
            ClientServerSocket css = ClientServerSocket.getClientServerSocketByIndex(i);
            if (css != null) {
                css.sendMessageToSelf(CommonDefine.NOTICE_REQUIRE_COIN);
            }
        }
        new Thread(() -> {
            try {
                Thread.sleep(200);
                if (!saveData()) {
                    JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "Can not save data client!");
                }
                String itemdata = getStringItemOwnData();
                if (!itemdata.isEmpty()) {
                    if (!CommonMethod.createFileWithContent(CommonDefine.ITEM_OWN_FILE_NAME, CommonMethod.getEncryptContent(itemdata))) {
                        JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "Can not save data item!");
                    }
                }
                ClientServerSocket.closeAllClientServer();
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                    }
                }
            } catch (InterruptedException ex) {
            }
            CommonMethod.log("//----------Server Closed----------//");
        }).start();
    }
    //</editor-fold>

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        getServerFrame().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btn_active;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_create_cfg;
    private javax.swing.JButton btn_send;
    private javax.swing.JButton btn_start;
    private javax.swing.JComboBox<String> cbb_action;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_action_0;
    private javax.swing.JLabel lb_action_1;
    private javax.swing.JLabel lb_action_2;
    private javax.swing.JLabel lb_action_3;
    private javax.swing.JLabel lb_action_4;
    private javax.swing.JLabel lb_notice;
    private javax.swing.JList<String> list_client;
    private javax.swing.JTextField tf_mess;
    private javax.swing.JTextField tf_value1;
    private javax.swing.JTextField tf_value2;
    private javax.swing.JTextField tf_value3;
    private javax.swing.JTextField tf_value4;
    // End of variables declaration//GEN-END:variables
}
