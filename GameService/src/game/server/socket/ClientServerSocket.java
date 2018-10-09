/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.socket;

import game.server.common.CommonDefine;
import game.server.common.CommonMethod;
import game.server.element.Card;
import game.server.gui.ServerFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author KenyDinh
 */
public class ClientServerSocket extends Thread {

    private static ClientServerSocket[] listClientServerSocket = new ClientServerSocket[CommonDefine.MAX_CLIENT_CONNECT];
    private static int countClient = 0;
    private int clientId;
    private String clientName;
    private String macAddress;
    private DataInputStream dataRead;
    private DataOutputStream dataWrite;
    private final Socket clientSocket;
    private Boolean isConnect = false;
    private boolean isAllowRename = true;
    private int gameId;
    private int gameType;
    private boolean isPlayer = false;
    private boolean isViewer = false;
    private int totalCoins;
    //-----------------------//
    private int caroGameValue;
    //-----------------------//
    private int boomGameValue;
    private int boomHP;
    private boolean boomReady;
    private boolean protect;
    private static final int BOOM_HP_MAX = 1000;
    //-----------------------//
    private boolean pass;
    private int cardGameValue;
    private boolean firstPlay;
    private List<Card> listCard = new ArrayList<>();

    public ClientServerSocket() {
        clientSocket = null;
    }

    public ClientServerSocket(Socket clientSocket) {
        countClient++;
        this.clientId = getGenarateId();
        this.clientSocket = clientSocket;
        isConnect = true;
        try {
            dataRead = new DataInputStream(clientSocket.getInputStream());
            dataWrite = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
        }
    }

    public List<Card> getListCard() {
        return listCard;
    }

    public void setListCard(List<Card> listCard) {
        this.listCard = listCard;
    }

    public void cleanListCard() {
        getListCard().clear();
    }

    public void updateListCard(List<Card> listCardFight) {
        for (Card cardRemove : listCardFight) {
            getListCard().remove(cardRemove);
        }
    }

    public String getRestListCard() {
        StringBuilder sb = new StringBuilder();
        if (listCard != null && !listCard.isEmpty()) {
            for (int i = 0; i < listCard.size(); i++) {
                sb.append(listCard.get(i).toString());
                if (i != listCard.size() - 1) {
                    sb.append(CommonDefine.COMMA);
                }
            }
        }
        return sb.toString();
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    public boolean isFirstPlay() {
        return firstPlay;
    }

    public void setFirstPlay(boolean firstPlay) {
        this.firstPlay = firstPlay;
    }

    public int getCardNums() {
        return listCard.size();
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public int getCardGameValue() {
        return cardGameValue;
    }

    public void setCardGameValue(int cardGameValue) {
        this.cardGameValue = cardGameValue;
    }

    public boolean isProtect() {
        return protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    private void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public static int getCountClient() {
        return countClient;
    }

    public static void setCountClient(int countClient) {
        ClientServerSocket.countClient = countClient;
    }

    public boolean isBoomReady() {
        return boomReady;
    }

    public void setBoomReady(boolean rd) {
        this.boomReady = rd;
    }

    public void toggleBoomReady() {
        this.boomReady = !this.boomReady;
    }

    public int getBoomGameValue() {
        return boomGameValue;
    }

    public void setBoomGameValue(int boomGameValue) {
        this.boomGameValue = boomGameValue;
    }

    public int getCaroGameValue() {
        return caroGameValue;
    }

    public void setCaroGameValue(int caroGameValue) {
        this.caroGameValue = caroGameValue;
    }

    public int getClientId() {
        return clientId;
    }

    public int getBoomHP() {
        return boomHP;
    }

    public void setBoomHP(int boomHP) {
        if (boomHP <= 0) {
            this.boomHP = 0;
        } else if (boomHP >= BOOM_HP_MAX) {
            this.boomHP = BOOM_HP_MAX;
        } else {
            this.boomHP = boomHP;
        }

    }

    public void allowRename() {
        this.isAllowRename = true;
    }

    public void setClientName(String name) {
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
        if (gameType == CommonDefine.GAME_TYPE_TLMN_GAME) {
            listCard = new ArrayList<>();
        }
    }

    public boolean isIsPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public boolean isIsViewer() {
        return isViewer;
    }

    public void setIsViewer(boolean isViewer) {
        this.isViewer = isViewer;
    }

    public void sendMessageToSelf(String mess) {
        if (mess == null || mess.length() == 0) {
            return;
        }
        try {
            dataWrite.writeUTF(mess);
            dataWrite.flush();
        } catch (IOException ex) {
        }
    }

    public static void sendMessageByName(String name, String mess) {
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null && css.getClientName().equals(name)) {
                css.sendMessageToSelf(mess);
                break;
            }
        }
    }

    public void closeClientServerSocket() {
        if (clientSocket != null) {
            try {
                dataRead.close();
                dataWrite.close();
                clientSocket.close();
                isConnect = false;
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!isConnect) {
                break;
            }
            try {
                String line = dataRead.readUTF();
                if (line != null && line.length() > 0) {
                    if (line.contains(CommonDefine.SETTING_CLIENT_RENAME)) {
                        if (isAllowRename) {
                            if (line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 1) {
                                String newName = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                                newName = getGenerateName(newName);
                                if (getClientName() != null) {
                                    sendToAllClient(CommonMethod.formatClientRename(getClientName(), newName));
                                    ServerFrame.getServerFrame().removeOldClientNameOnList(getClientName());
                                }
                                setClientName(newName);
                                sendMessageToSelf(CommonDefine.SETTING_CLIENT_RENAME + CommonDefine.SEPARATOR_KEY_VALUE + newName);
                                ServerFrame.getServerFrame().addClientToList(newName);
                                isAllowRename = false;
                            }
                        } else {
                            sendMessageToSelf(CommonMethod.formatMessageServer(CommonDefine.INFO_RENAME_INVALID));
                        }
                    } else if (line.contains(CommonDefine.MAC_ADDRESS)) {
                        if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                            if (arr.length == 2) {
                                if (isExistMacAddress(arr[1].trim())) {
                                    sendMessageToSelf(CommonDefine.NOTICE_MAC_ADDRESS_EXIST);
                                } else {
                                    setMacAddress(arr[1].trim());
                                    sendMessageToSelf(CommonDefine.SETTING_CLIENT_DATA + ServerFrame.getDataByMAC(arr[1].trim()));
                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(200);
                                            sendMessageToSelf(CommonDefine.SETTING_CLIENT_DXH_DATA + ServerFrame.getDataDxh());
                                        } catch (InterruptedException ex) {
                                        }
                                    }).start();
                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(300);
                                            String shopData = ServerFrame.getShopData();
                                            if (shopData != null) {
                                                sendMessageToSelf(CommonDefine.BOOM_GAME_EQUIP_DATA + shopData);
                                            }
                                        } catch (InterruptedException ex) {
                                        }
                                    }).start();
                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(500);
                                            String s = ServerFrame.getItemOwnByMac(arr[1].trim());
                                            if (s != null) {
                                                sendMessageToSelf(CommonDefine.BOOM_GAME_ITEM_OWN_DATA + s);
                                            }
                                        } catch (InterruptedException ex) {
                                        }
                                    }).start();
                                }
                            }
                        }
                    } else if (line.contains(CommonDefine.SETTING_CLIENT_DATA)) {
                        line = line.replace(CommonDefine.SETTING_CLIENT_DATA, "");
                        ServerFrame.updateData(getMacAddress(), line);
                    } else if (line.contains(CommonDefine.NOTICE_CHAT_PRIVATE)) {
                        if (line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 2) {
                            String to = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                            String mess = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[2];
                            if (to != null && mess != null) {
                                sendPrivateMessage(getClientName(), mess.trim(), to.trim());
                            }
                        }
                    } else if (line.contains(CommonDefine.NOTICE_CHAT_IN_GAME)) {
                        if (line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 1) {
                            String mess = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1];
                            if (mess != null) {
                                if (getGameId() != 0) {
                                    sendMessageInGame(getClientName(), mess.trim(), getGameId());
                                } else {
                                    sendMessageToSelf(CommonMethod.formatMessageServer("You are not in game!"));
                                }
                            }
                        }
                    } else if (line.contains(CommonDefine.NOTICE_CLIENT_OFF)) {
                        ServerFrame.getServerFrame().updateClientOutOrOff(getGameType(), getGameId(), getClientId(), true);
                    } else if (line.contains(CommonDefine.NOTICE_CLIENT_OUT_GAME)) {
                        if (getGameId() != 0 && getGameType() != 0) {
                            ServerFrame.getServerFrame().updateClientOutOrOff(getGameType(), getGameId(), getClientId(), false);
                        }
                    } else if (line.contains(CommonDefine.NOTICE_FINDING_MATCH)) {
                        if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE) && line.split(CommonDefine.SEPARATOR_KEY_VALUE).length > 2) {
                            String str_room = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1].trim();
                            String str_game_type = line.split(CommonDefine.SEPARATOR_KEY_VALUE)[2].trim();
                            if (CommonMethod.isValidNumber(str_room) && CommonMethod.isValidNumber(str_game_type)) {
                                int game_Id = Integer.parseInt(str_room);
                                int game_Type = Integer.parseInt(str_game_type);
                                if (ServerFrame.getServerFrame().isFullGame(game_Type)) {
                                    sendMessageToSelf(CommonDefine.NOTICE_FULL_GAME);
                                } else {
                                    ServerFrame.getServerFrame().findingMatch(game_Type, game_Id, getClientId());
                                }
                            } else {
                                sendMessageToSelf(CommonDefine.NOTICE_FINDING_MATCH_FAIL);
                            }
                        }
                    } else if (line.contains(CommonDefine.CARO_GAME_POSITION)) {
                        ServerFrame.getServerFrame().getCoordinateCaroGame(line, getGameId());
                    } else if (line.contains(CommonDefine.CARO_GAME_PLAY_AGAIN)) {
                        ServerFrame.getServerFrame().playAgainCaroGame(getClientServerSocketById(getClientId()), getGameId());
                    } else if (line.contains(CommonDefine.BAUCUA_GAME_START_SHAKE)) {
                        ServerFrame.getServerFrame().updateBaucuaOpenClose(getGameId(), false);
                        sendToAllClientInGame(line, getGameId());
                    } else if (line.contains(CommonDefine.BAUCUA_GAME_FINISH_SHAKE)) {
                        if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE) && line.split(CommonDefine.SEPARATOR_KEY_VALUE).length == 2) {
                            int coins = Integer.parseInt(line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1].trim());
                            ServerFrame.getServerFrame().setTotalHostCoins(getGameId(), coins);
                        }
                        ServerFrame.getServerFrame().resetBaucuaValue(getGameId());
                        sendToAllClientInGame(line, getGameId());
                    } else if (line.contains(CommonDefine.BAUCUA_GAME_START_OPEN)) {
                        ServerFrame.getServerFrame().getResultBauCua(getGameId());
                    } else if (line.contains(CommonDefine.BAUCUA_GAME_FINISH_OPEN)) {
                        ServerFrame.getServerFrame().updateBaucuaOpenClose(getGameId(), true);
                        sendToAllClientInGame(line, getGameId());
                    } else if (line.contains(CommonDefine.BAUCUA_GAME_VALUE_CHOOSE)) {
                        if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                            if (arr.length == 3) {
                                if (Integer.parseInt(arr[2].trim()) > 0) {
                                    sendMessageToSelf(line);
                                    ServerFrame.getServerFrame().updateBauCuaCoin(arr[1].trim(), arr[2].trim(), getGameId());
                                } else {
                                    sendMessageToSelf(CommonMethod.formatMessageServer("Không đặt cược được đâu em ạ!!!"));
                                }
                            }
                        }
                    } else if (line.contains(CommonDefine.BOOM_GAME_START_BATTLE)) {
                        ServerFrame.getServerFrame().startBoomGame(getGameId());
                    } else if (line.contains(CommonDefine.BOOM_GAME_READY)) {
                        toggleBoomReady();
                    } else if (line.contains(CommonDefine.BOOM_GAME_PLAYER_MOVE)) {
                        String[] arrs = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                        int x = Integer.parseInt(arrs[1].trim());
                        int y = Integer.parseInt(arrs[2].trim());
                        int val = Integer.parseInt(arrs[3].trim());
                        int status = Integer.parseInt(arrs[4].trim());
                        String type = arrs[5].trim();
                        ServerFrame.getServerFrame().updatePlayerMove(getGameId(), x, y, val, status, type);
                    } else if (line.contains(CommonDefine.BOOM_GAME_ADD_BOOM)) {
                        String[] arrs = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                        int x = Integer.parseInt(arrs[1].trim());
                        int y = Integer.parseInt(arrs[2].trim());
                        int val = Integer.parseInt(arrs[3].trim());
                        int m_val = Integer.parseInt(arrs[4].trim());
                        ServerFrame.getServerFrame().addBoom(getGameId(), x, y, val, m_val);
                    } else if (line.contains(CommonDefine.BOOM_GAME_UPDATE_HP)) {
                        int hp = Integer.parseInt(line.split(CommonDefine.SEPARATOR_KEY_VALUE)[1].trim());
                        setBoomHP(hp);
                    } else if (line.contains(CommonDefine.BOOM_GAME_REMOVE_PR)) {
                        setProtect(false);
                    } else if (line.contains(CommonDefine.BOOM_GAME_GCOIN)) {
                        if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                            String[] arr = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                            if (arr.length == 2) {
                                int c = Integer.parseInt(arr[1].trim());
                                ServerFrame.getServerFrame().updateBoomGameCoin(getGameId(), c);
                            }
                        }
                    } else if (line.contains(CommonDefine.BOOM_GAME_ADD_ITEM)) {
                        if (line.contains(CommonDefine.SEPARATOR_KEY_VALUE)) {
                            String arr[] = line.split(CommonDefine.SEPARATOR_KEY_VALUE);
                            if (arr.length == 3) {
                                if (CommonMethod.isValidNumber(arr[1].trim()) && CommonMethod.isValidNumber(arr[2].trim())) {
                                    int itemVal = Integer.parseInt(arr[1].trim());
                                    int itemNum = Integer.parseInt(arr[2].trim());
                                    ServerFrame.updateItemOwn(getMacAddress(), itemVal, itemNum);
                                }
                            }
                        }
                    } else if (line.contains(CommonDefine.SETTING_UPDATE_COIN)) {
                        String strcoin = line.replace(CommonDefine.SETTING_UPDATE_COIN, "");
                        if (CommonMethod.isValidNumber(strcoin.trim())) {
                            totalCoins = Integer.parseInt(strcoin.trim());
                        }
                    } else if (line.contains(CommonDefine.CARD_GAME_START_GAME)) {
                        if (getGameId() != 0) {
                            ServerFrame.getServerFrame().startCardGame(getGameId());
                        }
                    } else if (line.contains(CommonDefine.CARD_GAME_ACTION_FIGHT)) {
                        if (getGameId() != 0) {
                            ServerFrame.getServerFrame().executeAction(getGameId(), line.replace(CommonDefine.CARD_GAME_ACTION_FIGHT, ""));
                        }
                    } else if (line.contains(CommonDefine.CARD_GAME_ACTION_PASS)) {
                        if (getGameId() != 0) {
                            ServerFrame.getServerFrame().executeAction(getGameId(), line.replace(CommonDefine.CARD_GAME_ACTION_PASS, ""));
                        }
                    } else if (line.contains(CommonDefine.CARD_GAME_BETTING_COIN)) {
                        if (getGameId() != 0) {
                            String strCoin = line.replace(CommonDefine.CARD_GAME_BETTING_COIN, "");
                            if (CommonMethod.isValidNumber(strCoin)) {
                                ServerFrame.getServerFrame().updateBetCoin(getGameId(), Integer.parseInt(strCoin.trim()));
                            }
                        }
                    } else if (line.contains(CommonDefine.CARD_GAME_SEND_MESS)) {
                        if (getGameId() != 0) {
                            sendToAllClientInGame(CommonMethod.formatMessageClient(getClientName(), line), getGameId());
                        }
                    } else {
                        if (getClientName() != null) {
                            StringBuilder sb = new StringBuilder();
                            for(String word : line.split(CommonDefine.SPACE)){
                                while(word.length() > 24){
                                    sb.append(word.substring(0, 24)).append("<br>");
                                    word = word.substring(24, word.length());
                                }
                                sb.append(word).append(CommonDefine.SPACE);
                            }
                            sendToAllClient(CommonMethod.formatMessageClient(getClientName(), sb.toString()));
                        }
                    }
                }
            } catch (Exception ex) {
            }

        }

    }

    //static method
    public static void sendToAllClient(String mess) {
        if (mess == null || mess.length() == 0) {
            return;
        }
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null) {
                css.sendMessageToSelf(mess);
            }
        }
    }

    public static void sendPrivateMessage(String from, String mess, String to) {
        if (mess == null || mess.length() == 0) {
            return;
        }
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null) {
                if (css.getClientName().equals(to)) {
                    css.sendMessageToSelf(CommonMethod.formatPrivateMessageTo(from, mess));
                } else if (css.getClientName().equals(from)) {
                    css.sendMessageToSelf(CommonMethod.formatPrivateMessageFrom(to, mess));
                }

            }
        }
    }

    public static void sendMessageInGame(String name, String mess, int gameId) {
        if (mess == null || mess.length() == 0) {
            return;
        }
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null) {
                if (css.getGameId() == gameId) {
                    css.sendMessageToSelf(CommonMethod.formatMessageInGame(name, mess));
                }
            }
        }
    }

    public static void closeAllClientServer() {
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null) {
                css.sendMessageToSelf(CommonDefine.NOTICE_SERVER_OFF);
                css.closeClientServerSocket();
            }
        }
        listClientServerSocket = new ClientServerSocket[CommonDefine.MAX_CLIENT_CONNECT];
    }

    public static void storeClientServerSocket(ClientServerSocket css, int index) {
        for (ClientServerSocket c : listClientServerSocket) {
            if (c != null && c.getMacAddress().equals(css.getMacAddress())) {
                return;
            }
        }
        if (0 <= index && index < CommonDefine.MAX_CLIENT_CONNECT) {
            if (listClientServerSocket[index] == null) {
                listClientServerSocket[index] = css;
            }
        }
    }

    public static ClientServerSocket[] getListClientServerSocket() {
        return listClientServerSocket;
    }

    public static ClientServerSocket getClientServerSocketByIndex(int index) {
        return listClientServerSocket[index];
    }

    public static ClientServerSocket getClientServerSocketById(int clientId) {
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null && css.getClientId() == clientId) {
                return css;
            }
        }
        return null;
    }

    public static void removeClientServerSocket(ClientServerSocket client) {
        for (int i = 0; i < CommonDefine.MAX_CLIENT_CONNECT; i++) {
            if (listClientServerSocket[i] != null && listClientServerSocket[i].getClientId() == client.getClientId()) {
                listClientServerSocket[i] = null;
                client.closeClientServerSocket();
            }
        }
    }

    public static void sendToAllClientInGame(String mess, int gameId) {
        if (mess == null || mess.length() == 0) {
            return;
        }
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null && css.getGameId() == gameId) {
                css.sendMessageToSelf(mess);
            }
        }
    }

    private static int getGenarateId() {
        int id = 11111;
        do {
            id = (int) (Math.random() * CommonDefine.MAX_NUMBER_RANDOM + CommonDefine.MIN_NUMBER_RANDOM);
        } while (isExistId(id));
        return id;
    }

    private static boolean isExistId(int id) {
        for (ClientServerSocket client : listClientServerSocket) {
            if (client != null && client.getClientId() == id) {
                return true;
            }
        }
        return false;
    }

    private static String getGenerateName(String name) {
        int i = 1;
        String new_name = name;
        while (isExistName(new_name)) {
            new_name = name + "_" + i;
            i++;
        }
        return new_name;
    }

    private static boolean isExistName(String name) {
        for (ClientServerSocket client : listClientServerSocket) {
            if (client != null && client.getClientName() != null && client.getClientName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isExistMacAddress(String mac) {
        for (ClientServerSocket css : listClientServerSocket) {
            if (css != null && css.getMacAddress() != null && css.getMacAddress().equals(mac)) {
                return true;
            }
        }
        return false;
    }
}
