/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.socket;

import game.server.common.CommonDefine;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class BauCuaGameServer {

    private int id;
    private ClientServerSocket host;
    private int totalHostCoins;
    private ClientServerSocket[] playerInGame;
    private int[] resultValue;
    private boolean isOpen;
    private Map<Integer, Integer> totalMoney = new HashMap<>();

    public BauCuaGameServer(int id) {
        this.id = id;
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_BAU, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_CUA, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_CA, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_TOM, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_GA, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_HUOU, 0);
        isOpen = true;
        playerInGame = new ClientServerSocket[CommonDefine.BAUCUA_GAME_MAX_PLAYER];
        resultValue = new int[3];
        resultValue[0] = CommonDefine.BAUCUA_VALUE_OF_BAU;
        resultValue[1] = CommonDefine.BAUCUA_VALUE_OF_BAU;
        resultValue[2] = CommonDefine.BAUCUA_VALUE_OF_BAU;
    }

    public void setHostCoins(int coins) {
        totalHostCoins = coins;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
        this.isOpen = open;
    }

    public int getGameId() {
        return id;
    }

    public void setHost(ClientServerSocket host) {
        this.host = host;
    }

    public ClientServerSocket getHost() {
        return host;
    }

    public int getCountPlayer() {
        int count = 0;
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
            if (playerInGame != null && playerInGame[i] != null) {
                count++;
            }
        }
        return count;
    }

    private String getTienCuocAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CommonDefine.BAUCUA_STR_OF_HUOU).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(totalMoney.get(CommonDefine.BAUCUA_VALUE_OF_HUOU));
        sb.append(CommonDefine.COMMA);
        sb.append(CommonDefine.BAUCUA_STR_OF_BAU).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(totalMoney.get(CommonDefine.BAUCUA_VALUE_OF_BAU));
        sb.append(CommonDefine.COMMA);
        sb.append(CommonDefine.BAUCUA_STR_OF_GA).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(totalMoney.get(CommonDefine.BAUCUA_VALUE_OF_GA));
        sb.append(CommonDefine.COMMA);
        sb.append(CommonDefine.BAUCUA_STR_OF_CA).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(totalMoney.get(CommonDefine.BAUCUA_VALUE_OF_CA));
        sb.append(CommonDefine.COMMA);
        sb.append(CommonDefine.BAUCUA_STR_OF_CUA).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(totalMoney.get(CommonDefine.BAUCUA_VALUE_OF_CUA));
        sb.append(CommonDefine.COMMA);
        sb.append(CommonDefine.BAUCUA_STR_OF_TOM).append(CommonDefine.SEPARATOR_KEY_VALUE).append(CommonDefine.SPACE).append(totalMoney.get(CommonDefine.BAUCUA_VALUE_OF_TOM));

        return sb.toString().trim();
    }

    private String getResultAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (resultValue[i] != 0) {
                sb.append(resultValue[i]).append(CommonDefine.SEPARATOR_KEY_VALUE);
            }
        }
        String result = sb.toString().trim();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
            return result;
        }
        return null;
    }

    private String getListPlayerAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
            if (playerInGame[i] != null) {
                sb.append(playerInGame[i].getClientName()).append(CommonDefine.SEPARATOR_KEY_VALUE);
            }
        }
        String result = sb.toString().trim();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
            return result;
        }
        return null;
    }

    public void addPlayer(ClientServerSocket player) {
        if (getCountPlayer() >= CommonDefine.BAUCUA_GAME_MAX_PLAYER) {
            return;
        }
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
            if (playerInGame != null && playerInGame[i] == null) {
                player.setGameId(getGameId());
                player.setGameType(CommonDefine.GAME_TYPE_BTCC_GAME);
                player.setIsPlayer(true);
                player.setIsViewer(false);
                if (getCountPlayer() == 0) {
                    host = player;
                    player.sendMessageToSelf(CommonDefine.BAUCUA_GAME_IS_HOST);
                }
                playerInGame[i] = player;
                String mess = getCurrentStateGame();
                if (getCountPlayer() == 2) {
                    ClientServerSocket.sendToAllClientInGame(mess, getGameId());
                } else if (getCountPlayer() > 2) {
                    for (ClientServerSocket css : playerInGame) {
                        if (css != null && css.getClientId() != player.getClientId()) {
                            css.sendMessageToSelf(CommonDefine.BAUCUA_GAME_JOIN_GAME + CommonDefine.SEPARATOR_KEY_VALUE + player.getClientName());
                        }
                    }
                    player.sendMessageToSelf(mess);
                }
                break;
            }
        }
    }

    private String getCurrentStateGame() {
        String mess = CommonDefine.BAUCUA_GAME_READY_PLAY + CommonDefine.SEPARATOR_GROUP + getListPlayerAsString();
        if (isOpen) {
            mess += CommonDefine.SEPARATOR_GROUP + 1;
        } else {
            mess += CommonDefine.SEPARATOR_GROUP + 0;
        }
        mess += CommonDefine.SEPARATOR_GROUP + getResultAsString();
        mess += CommonDefine.SEPARATOR_GROUP + getTienCuocAsString();
        return mess;
    }

    public void removePlayer(ClientServerSocket player) {
        if (getCountPlayer() == 0) {
            return;
        }
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
            if (playerInGame[i] != null && playerInGame[i].getClientId() == player.getClientId()) {
                player.setGameId(0);
                player.setGameType(0);
                player.setIsPlayer(false);
                player.setIsViewer(false);
                playerInGame[i] = null;
                break;
            }
        }
        if (host.getClientId() != player.getClientId()) {
            if (getCountPlayer() > 1) {
                ClientServerSocket.sendToAllClientInGame(CommonDefine.BAUCUA_GAME_PLAYER_OUT + CommonDefine.SEPARATOR_KEY_VALUE + player.getClientName(), getGameId());
            } else if (getCountPlayer() == 1) {
                resetAllValue();
                host.sendMessageToSelf(CommonDefine.BAUCUA_GAME_RESET);
            }
        }
    }

    public ClientServerSocket getPlayerById(int clientId) {
        for (int i = 0; i < CommonDefine.BAUCUA_GAME_MAX_PLAYER; i++) {
            if (playerInGame[i] != null && playerInGame[i].getClientId() == clientId) {
                return playerInGame[i];
            }
        }
        return null;
    }

    public void resultBauCua() {
        resultValue[0] = (int) (Math.random() * 6000) / 1000 + 1;
        resultValue[1] = (int) (Math.random() * 6000) / 1000 + 1;
        resultValue[2] = (int) (Math.random() * 6000) / 1000 + 1;
        int coinsMinus = totalMoney.get(resultValue[0]) + totalMoney.get(resultValue[1]) + totalMoney.get(resultValue[2]);
        totalHostCoins = totalHostCoins + getTotalCoins() - coinsMinus;
        if (totalHostCoins <= coinsMinus) {
            String mess = CommonDefine.BAUCUA_GAME_RESULT_VALUE + CommonDefine.SEPARATOR_KEY_VALUE + resultValue[0] + CommonDefine.SEPARATOR_KEY_VALUE + resultValue[1] + CommonDefine.SEPARATOR_KEY_VALUE + resultValue[2] + CommonDefine.SEPARATOR_KEY_VALUE + totalHostCoins + CommonDefine.SEPARATOR_KEY_VALUE + coinsMinus;
            ClientServerSocket.sendToAllClientInGame(mess, getGameId());
        } else {
            String mess = CommonDefine.BAUCUA_GAME_RESULT_VALUE + CommonDefine.SEPARATOR_KEY_VALUE + resultValue[0] + CommonDefine.SEPARATOR_KEY_VALUE + resultValue[1] + CommonDefine.SEPARATOR_KEY_VALUE + resultValue[2];
            ClientServerSocket.sendToAllClientInGame(mess, getGameId());
        }

    }

    private int getTotalCoins() {
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : totalMoney.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public void updateTienCuoc(int choice, int cn) {
        if (!totalMoney.containsKey(choice)) {
            return;
        }
        int bf = totalMoney.get(choice);
        totalMoney.put(choice, cn + bf);
        ClientServerSocket.sendToAllClientInGame(CommonDefine.BAUCUA_GAME_UPDATE_TC + CommonDefine.SEPARATOR_GROUP + getTienCuocAsString(), getGameId());
    }

    public void resetAllValue() {
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_BAU, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_CUA, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_CA, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_TOM, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_GA, 0);
        totalMoney.put(CommonDefine.BAUCUA_VALUE_OF_HUOU, 0);
        resultValue = new int[3];
    }
}
