/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.socket;

import game.server.common.CommonDefine;
import game.server.common.CommonMethod;
import game.server.element.Card;
import game.server.element.CardType;
import game.server.func.CardFunc;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class CardGameServer {

    public static final int MAX_PLAYER = 4;
    private static final int MAX_CARD_NUM = 13;
    private static final int MAX_CARD_TYPE = 4;
    private static final int MAX_TIME_COUNT = 25;

    private int LIMIT_CARD = 13;
    private static final int LIMIT_CARD_NUM_TLMN = 13;
    private static final int LIMIT_CARD_NUM_SL = 10;

    private int gameId;
    private int gameType;
    private ClientServerSocket[] listPlayer;
    private String[] cardDecks;
    private boolean start;
    private boolean end;
    private ClientServerSocket host;
    private boolean timeCountDown;
    private int currentVal;
    private int previousVal;
    private int betCoin;
    private List<Card> listPreviousCard;

    public CardGameServer(int gameId, int gameType) {
        this.gameId = gameId;
        this.gameType = gameType;
        listPlayer = new ClientServerSocket[MAX_PLAYER];
        initCardDeck();
        switch (gameType) {
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                LIMIT_CARD = LIMIT_CARD_NUM_TLMN;
                break;
            case CommonDefine.GAME_TYPE_SL_GAME:
                LIMIT_CARD = LIMIT_CARD_NUM_SL;
                break;
            default:
                break;
        }
    }

    public int getGameType() {
        return gameType;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private void initCardDeck() {
        cardDecks = new String[MAX_CARD_NUM * MAX_CARD_TYPE];
        int index = 0;
        for (int type = 1; type <= MAX_CARD_TYPE; type++) {
            for (int num = 1; num <= MAX_CARD_NUM; num++) {
                cardDecks[index] = String.valueOf(num + "_" + type);
                ++index;
            }
        }
    }

    private void shuffleCardDecks() {
        for (int i = 0; i < cardDecks.length; i++) {
            int j = (int) (Math.random() * 52);
            //swap
            String temp = cardDecks[i];
            cardDecks[i] = cardDecks[j];
            cardDecks[j] = temp;
        }
    }

    private void divideCardDecks() {
        for (int i = 0; i < listPlayer.length; i++) {
            if (listPlayer[i] != null) {//
                StringBuilder sb = new StringBuilder();
                List<Card> listCard = new ArrayList<>();
                for (int j = 0; j < MAX_CARD_NUM; j++) {
                    String strCard = cardDecks[i + MAX_PLAYER * j];
                    sb.append(strCard);
                    Card card = CardFunc.getCardFromCardString(strCard);
                    if (card != null) {
                        listCard.add(card);
                    }
                    if (j != MAX_CARD_NUM - 1) {
                        sb.append(CommonDefine.COMMA);
                    }
                }
                listPlayer[i].setListCard(listCard);
                listPlayer[i].sendMessageToSelf(CommonDefine.CARD_GAME_PLAYER_CARD + sb.toString());
            }
        }
    }

    public void addPlayer(ClientServerSocket player) {
        boolean flagAdd = false;
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (listPlayer[i] == null) {
                player.setGameId(getGameId());
                player.setGameType(getGameType());
                player.setCardGameValue(i + 1);
                player.sendMessageToSelf(CommonDefine.CARD_GAME_SET_VALUE + (i + 1));
                if (start) {
                    player.setIsViewer(true);
                    player.setIsPlayer(false);
                } else {
                    player.setIsPlayer(true);
                    player.setIsViewer(false);
                }
                sendMessToAllPlayer(CommonDefine.CARD_GAME_ADD_PLAYER + (i + 1) + CommonDefine.COMMA + player.getClientName() + CommonDefine.COMMA + player.getCardNums() + CommonDefine.COMMA + player.getTotalCoins());
                listPlayer[i] = player;
                player.sendMessageToSelf(CommonDefine.CARD_GAME_PLAYER_JOIN + getListPlayerInGame());
                flagAdd = true;
                break;
            }
        }
        if (getCountPlayer() == 1) {
            currentVal = player.getCardGameValue();
            host = player;
            host.sendMessageToSelf(CommonDefine.CARD_GAME_IS_HOST);
        } else if (getCountPlayer() > 1) {
            if (host != null) {
                host.sendMessageToSelf(CommonDefine.CARD_GAME_ENABLE_PLAY);
                if (flagAdd) {
                    player.sendMessageToSelf(CommonDefine.CARD_GAME_SET_HOST + host.getCardGameValue());
                    player.sendMessageToSelf(CommonDefine.CARD_GAME_BETTING_COIN + getBetCoin());
                }
            }
        }
    }

    public void removePlayer(ClientServerSocket player) {
        int val = player.getCardGameValue();
        for (int i = 0; i < MAX_PLAYER; i++) {
            if (listPlayer[i].getClientId() == player.getClientId()) {
                listPlayer[i] = null;
                sendMessToAllPlayer(CommonDefine.CARD_GAME_PLAYER_OUT + player.getCardGameValue());
                clearPlayer(player);
                break;
            }
        }

        if (host.getClientId() == player.getClientId()) {
            if (getCountPlayer() != 0) {
                for (ClientServerSocket css : listPlayer) {
                    if (css != null && css.isIsPlayer()) {
                        host = css;
                        host.sendMessageToSelf(CommonDefine.CARD_GAME_IS_HOST);
                        break;
                    }
                }
            } else if (getCountClient() != 0) {
                for (ClientServerSocket css : listPlayer) {
                    if (css != null) {
                        host = css;
                        host.sendMessageToSelf(CommonDefine.CARD_GAME_IS_HOST);
                        break;
                    }
                }
            } else {
                return;
            }
            sendMessToAllPlayer(CommonDefine.CARD_GAME_SET_HOST + host.getCardGameValue());
        }
        if (isStart() && getCountPlayer() > 1 && currentVal == val) {
            activeNextPlayerTurn();
        } else if (isStart() && getCountPlayer() == 1 && getCountClient() > 1) {
            host.sendMessageToSelf(CommonDefine.CARD_GAME_ENABLE_PLAY);
        } else if (getCountClient() == 1) {
            timeCountDown = false;
            setStart(false);
        } else if (!isStart() && getCountClient() > 1) {
            host.sendMessageToSelf(CommonDefine.CARD_GAME_ENABLE_PLAY);
        }
    }

    private void clearPlayer(ClientServerSocket player) {
        player.setPass(false);
        player.setCardGameValue(0);
        player.setFirstPlay(false);
        player.setIsPlayer(false);
        player.setIsViewer(false);
        player.setGameId(0);
        player.setGameType(0);
        player.cleanListCard();
    }

    private void clearAttributeAllPlayer() {
        for (ClientServerSocket player : listPlayer) {
            if (player != null) {
                player.setPass(false);
                player.cleanListCard();
            }
        }
    }

    private String getListPlayerInGame() {
        StringBuilder sb = new StringBuilder();
        for (ClientServerSocket css : listPlayer) {
            if (css != null) {
                sb.append(css.getCardGameValue()).append(CommonDefine.COMMA);
                sb.append(css.getClientName()).append(CommonDefine.COMMA);
                sb.append(css.getCardNums()).append(CommonDefine.COMMA);
                sb.append(css.getTotalCoins()).append(CommonDefine.SEPARATOR_GROUP);
            }
        }
        return sb.toString();
    }

    public int getBetCoin() {
        return betCoin;
    }

    public void setBetCoin(int betCoin) {
        this.betCoin = betCoin;
        sendMessToAllPlayer(CommonDefine.CARD_GAME_BETTING_COIN + this.betCoin);
    }

    private boolean checkStealWin() {
        int max = 0;
        ClientServerSocket stealWinner = null;
        for (ClientServerSocket player : listPlayer) {
            if (player != null && player.isIsPlayer()) {
                int stVal = CardFunc.checkStealWin(player.getListCard(), getGameType());
                if ((stVal > max) || (stVal == max && stealWinner != null && player.getCardGameValue() < stealWinner.getCardGameValue())) {
                    max = stVal;
                    stealWinner = player;
                }
            }
        }
        if (max != 0 && stealWinner != null) {
            setStart(false);
            currentVal = stealWinner.getCardGameValue();
            listPreviousCard = null;
            previousVal = 0;
            sendMessToAllPlayer(CommonDefine.CARD_GAME_END_GAME + max);
            calcEndGame(max);
            return true;
        }
        return false;
    }

    public void startGame() {
        start = true;
        clearAttributeAllPlayer();
        shuffleCardDecks();
//        shuffleCardTest();
        divideCardDecks();
        activeAllPlayerState();
        if (checkStealWin()) {
            return;
        }
        new Thread(() -> {
            try {
                if (start) {
                    Thread.sleep(3000);
                    activeFirstPlay();
                }
            } catch (InterruptedException ex) {
            }
        }).start();
    }

    private void activeAllPlayerState() {
        for (ClientServerSocket css : listPlayer) {
            if (css != null && css.isIsViewer()) {
                css.setIsPlayer(true);
                css.setIsViewer(false);
            }
        }
    }

    public void activeFirstPlay() {
        for (ClientServerSocket css : listPlayer) {
            if (css != null && css.isIsPlayer() && css.getCardGameValue() == currentVal) {
                css.sendMessageToSelf(CommonDefine.CARD_GAME_ENABLE_TURN);
                timeCountDown = true;
                timeCountDown(css);
                break;
            }
        }
    }

    public void timeCountDown(ClientServerSocket css) {
        new Thread(() -> {
            int count = 0;
            int tempVal = currentVal;
            while (tempVal == currentVal && timeCountDown && count < MAX_TIME_COUNT) {
                try {
                    css.sendMessageToSelf(CommonDefine.CARD_GAME_TIME_COUNT_DOWN + CommonDefine.SEPARATOR_KEY_VALUE + (MAX_TIME_COUNT - count) + CommonDefine.SEPARATOR_KEY_VALUE + MAX_TIME_COUNT);
                    count++;
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            if (count >= MAX_TIME_COUNT && tempVal == currentVal && timeCountDown) {
                css.sendMessageToSelf(CommonDefine.CARD_GAME_TIME_COUNT_DOWN + CommonDefine.SEPARATOR_KEY_VALUE + "0" + CommonDefine.SEPARATOR_KEY_VALUE + MAX_TIME_COUNT);
                css.setPass(true);
                activeNextPlayerTurn();
            }
        }).start();
    }

    public void endPlayerTurn(String data) {
        timeCountDown = false;
        ClientServerSocket p = getPlayerByVal(currentVal);
        if (p == null) {
            return;
        }
        if (data != null && data.length() != 0) {
            List<Card> listFight = getListCardFight(data);
            calcEndTurn(listFight);
            previousVal = currentVal;
            listPreviousCard = listFight;
            p.updateListCard(listFight);
        } else {
            p.setPass(true);
            data = "?";
        }
        sendMessToAllPlayer(CommonDefine.CARD_GAME_ACTION_FIGHT + data + CommonDefine.SEPARATOR_KEY_VALUE + currentVal);
        new Thread(() -> {
            try {
                Thread.sleep(200);
                if (p.getCardNums() == 0) {
                    currentVal = p.getCardGameValue();
                    listPreviousCard = null;
                    previousVal = 0;
                    start = false;
                    sendMessToAllPlayer(CommonDefine.CARD_GAME_END_GAME);
                    calcEndGame(0);
                    return;
                }
                activeNextPlayerTurn();
            } catch (InterruptedException ex) {
                Logger.getLogger(CardGameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    private void calcEndTurn(List<Card> listFight) {
        if (previousVal != 0 && previousVal != currentVal && listPreviousCard != null && !listPreviousCard.isEmpty()) {
            int coin = CardFunc.getCalcCoinEndTurn(listPreviousCard, listFight, getBetCoin());
            if (coin != 0) {
                sendMessToAllPlayer(CommonDefine.CARD_GAME_UPDATE_COIN + currentVal + CommonDefine.SEPARATOR_KEY_VALUE + coin + CommonDefine.COMMA + previousVal + CommonDefine.SEPARATOR_KEY_VALUE + "-" + coin);
            }
        }
    }

    public void calcEndGame(int stVal) {
        new Thread(() -> {
            try {
                sendMessToAllPlayer(CommonDefine.CARD_GAME_PLAYER_REST_CARD + getRestListOfAllPlayer());
                StringBuilder sb = new StringBuilder();
                sb.append(CommonDefine.CARD_GAME_UPDATE_COIN);
                int wcoin = 0;
                for (ClientServerSocket css : listPlayer) {
                    if (css != null && css.isIsPlayer()) {
                        if (css.getCardNums() > 0 && css.getCardGameValue() != currentVal) {
                            int coinupdate = CardFunc.getCoinUpdate(css.getListCard(), getBetCoin(), stVal, LIMIT_CARD);
                            wcoin += coinupdate;
                            sb.append(css.getCardGameValue()).append(CommonDefine.SEPARATOR_KEY_VALUE).append("-").append(coinupdate).append(CommonDefine.COMMA);
                        }
                    }
                }
                sb.append(currentVal).append(CommonDefine.SEPARATOR_KEY_VALUE).append(wcoin);
                sendMessToAllPlayer(sb.toString());
                Thread.sleep(2000);
                host.sendMessageToSelf(CommonDefine.CARD_GAME_RESTART_GAME_ENABLE);
            } catch (InterruptedException ex) {
                Logger.getLogger(CardGameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    public String getRestListOfAllPlayer() {
        StringBuilder sb = new StringBuilder();
        for (ClientServerSocket css : listPlayer) {
            if (css != null && css.isIsPlayer()) {
                if (!css.getRestListCard().isEmpty()) {
                    sb.append(css.getCardGameValue()).append(CommonDefine.SEPARATOR_KEY_VALUE).append(css.getRestListCard().trim()).append(CommonDefine.SEPARATOR_GROUP);
                }
            }
        }
        return sb.toString();
    }

    public void activeNextPlayerTurn() {
        if (isEndRound()) {
            sendMessToAllPlayer(CommonDefine.CARD_GAME_NEXT_ROUND);
            listPreviousCard = null;
            previousVal = 0;
            for (ClientServerSocket css : listPlayer) {
                if (css != null && css.isIsPlayer() && !css.isPass()) {
                    //activeAllPlayerState();
                    currentVal = css.getCardGameValue();
                    css.sendMessageToSelf(CommonDefine.CARD_GAME_ENABLE_TURN);
                    timeCountDown = true;
                    timeCountDown(css);
                    break;
                }
            }
            for (ClientServerSocket css : listPlayer) {
                if (css != null && css.isIsPlayer()) {
                    css.setPass(false);
                }
            }
            return;
        }
        ClientServerSocket css = getNextPlayer();
        if (css != null) {
            currentVal = css.getCardGameValue();
            css.sendMessageToSelf(CommonDefine.CARD_GAME_ENABLE_TURN);
            timeCountDown = true;
            timeCountDown(css);
        } else {
            host.sendMessageToSelf(CommonDefine.CARD_GAME_RESTART_GAME_ENABLE);
        }
    }

    public boolean isEndRound() {
        int countPlayer = getCountPlayer();
        int countPass = 0;
        for (ClientServerSocket css : listPlayer) {
            if (css != null && css.isIsPlayer() && css.isPass()) {
                countPass++;
            }
        }
        return (countPass == countPlayer - 1);
    }

    public ClientServerSocket getNextPlayer() {
        int nextVal = currentVal;
        ClientServerSocket css = null;
        int count = 0;
        do {
            count++;
            nextVal = getNextPlayerValue(nextVal);
            css = getPlayerByVal(nextVal);
        } while (count < 4 && (css == null || !css.isIsPlayer() || css.isPass()));
        if (count >= 4) {
            if (css == null || !css.isIsPlayer() || css.isPass()) {
                return null;
            }
        }
        return css;
    }

    private ClientServerSocket getPlayerByVal(int val) {
        for (ClientServerSocket css : listPlayer) {
            if (css != null && css.isIsPlayer() && css.getCardGameValue() == val) {
                return css;
            }
        }
        return null;
    }

    public int getNextPlayerValue(int curVal) {
        switch (curVal) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 1;
            default:
                return 0;
        }
    }

    public int getCountPlayer() {
        int count = 0;
        for (ClientServerSocket css : listPlayer) {
            if (css != null && css.isIsPlayer()) {
                count++;
            }
        }
        return count;
    }

    public int getCountClient() {
        int count = 0;
        for (ClientServerSocket css : listPlayer) {
            if (css != null) {
                count++;
            }
        }
        return count;
    }

    public void sendMessToAllPlayer(String mess) {
        for (ClientServerSocket css : listPlayer) {
            if (css != null) {
                css.sendMessageToSelf(mess);
            }
        }
    }

    public List<Card> getListCardFight(String data) {
        List<Card> listCard = new ArrayList<>();
        if (data.contains(CommonDefine.COMMA)) {
            for (String s : data.split(CommonDefine.COMMA)) {
                Card card = CardFunc.getCardFromCardString(s.trim());
                if (card != null) {
                    listCard.add(card);
                }
            }
        }
        return listCard;
    }

    private void shuffleCardTest() {
        cardDecks[0] = "9_1";
        cardDecks[4] = "9_2";
        cardDecks[8] = "9_3";
        cardDecks[12] = "9_4";
        cardDecks[1] = "2_1";
        cardDecks[5] = "2_2";
//        cardDecks[9] = "9_3";
//        cardDecks[13] = "9_4";

    }
}
