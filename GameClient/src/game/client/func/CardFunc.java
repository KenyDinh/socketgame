/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.func;

import game.client.common.CommonDefine;
import game.client.element.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class CardFunc {

    private static final int MIN_VALUE_STRAIGHT_TLMN = 3;
    private static final int MAX_VALUE_STRAIGHT_TLMN = 14;
    private static final int MIN_VALUE_STRAIGHT_SL = 1;
    private static final int MAX_VALUE_STRAIGHT_SL = 14;
    private static final int CARD_NUMBER_TWO_FAKE_VALUE = 15;
    private static final int STEAL_WIN_STRAIGHT = 9;
    private static final int STEAL_WIN_SIX_LINK_PAIR = 8;
    private static final int STEAL_WIN_DOUBLE_FOUR_OF_KIND = 7;
    private static final int STEAL_WIN_FOUR_OF_KIND = 6;
    private static final int STEAL_WIN_SAME_COLOR = 5;
    private static final int STEAL_WIN_FIVE_LINK_PAIR = 4;
    private static final int STEAL_WIN_THREE_OF_KIND = 3;
    private static final int STEAL_WIN_SIX_PAIR = 2;
    private static final int STEAL_WIN_FIVE_PAIR = 1;

    //----------------------------------------------//
    public static boolean checkListCardFight(List<Card> listAtk, List<Card> listDef, int gameType) {
        switch (gameType) {
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                if (listAtk == null || listAtk.isEmpty()) {
                    return checkAtkTLMN(listDef);
                }
                return checkDefTLMN(listAtk, listDef);
            case CommonDefine.GAME_TYPE_SL_GAME:
                if (listAtk == null || listAtk.isEmpty()) {
                    return checkAtkSL(listDef);
                }
                return checkDefSL(listAtk, listDef);
            default:
                return false;
        }
    }

    public static int checkStealWin(List<Card> listCard, int gameType) {
        switch (gameType) {
            case CommonDefine.GAME_TYPE_TLMN_GAME:
                return checkStealWinTLMN(listCard);
            case CommonDefine.GAME_TYPE_SL_GAME:
                return checkStealWinSL(listCard);
            default:
                return 0;
        }
    }
    //----------------------------------------------//

    //----------------------------------------------//
    private static boolean checkAtkSL(List<Card> listAtk) {
        switch (listAtk.size()) {
            case 1:
                return true;
            case 2:
                return isAPair(listAtk);
            case 3:
                return isThreeOfAKind(listAtk) || isAStraight(listAtk, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL);
            case 4:
                return isFourOfAKind(listAtk) || isAStraight(listAtk, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL);
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return isAStraight(listAtk, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL);
            default:
                return false;
        }
    }

    private static boolean checkDefSL(List<Card> listAtk, List<Card> listDef) {
        boolean ignoreType = true;
        int lastElem = listAtk.size() - 1;
        switch (listAtk.size()) {
            case 1:
                if (listDef.size() == 1 && isHighCard(listDef.get(0), listAtk.get(0), ignoreType)) {
                    return true;
                } else if (listAtk.get(0).getFakeNum() == CARD_NUMBER_TWO_FAKE_VALUE) {
                    return isFourOfAKind(listDef);
                }
                return false;
            case 2:
                if (isAPair(listAtk)) {
                    return (isAPair(listDef) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType));
                }
                return false;
            case 3:
                if (isThreeOfAKind(listAtk)) {
                    return isThreeOfAKind(listDef) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                } else if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL)) {
                    return (listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType));
                }
                return false;
            case 4:
                if (isFourOfAKind(listAtk)) {
                    return (isFourOfAKind(listDef) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType));
                } else if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL)) {
                    return (listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType));
                }
                return false;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL)) {
                    return (listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType));
                }
                return false;
            default:
                return false;
        }
    }

    private static int checkStealWinSL(List<Card> list) {
        if (isAStraight(list, MIN_VALUE_STRAIGHT_SL, MAX_VALUE_STRAIGHT_SL)) {
            return STEAL_WIN_STRAIGHT;
        }
        if (getCountCardNumTwo(list) == 4) {
            return STEAL_WIN_FOUR_OF_KIND;
        }
        if (getCountCardSameColor(list) == list.size()) {
            return STEAL_WIN_SAME_COLOR;
        }
        if (getCountThreeOfKind(list) == 3) {
            return STEAL_WIN_THREE_OF_KIND;
        }
        sortCardByNum(list, false);
        if (getCountPair(list) == 5) {
            return STEAL_WIN_FIVE_PAIR;
        }
        return 0;
    }
    //----------------------------------------------//

    //----------------------------------------------//
    private static boolean checkAtkTLMN(List<Card> listAtk) {
        switch (listAtk.size()) {
            case 1:
                return true;
            case 2:
                return isAPair(listAtk);
            case 3:
                return isThreeOfAKind(listAtk) || isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            case 4:
                return isFourOfAKind(listAtk) || isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            case 5:
                return isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            case 6:
                return isLinkNPairs(listAtk, 3) || isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            case 7:
                return isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            case 8:
                return isLinkNPairs(listAtk, 4) || isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            case 9:
            case 10:
            case 11:
                return isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN);
            default:
                return false;
        }
    }

    private static boolean checkDefTLMN(List<Card> listAtk, List<Card> listDef) {
        boolean ignoreType = false;
        int lastElem = listAtk.size() - 1;
        switch (listAtk.size()) {
            case 1:
                if (listDef.size() == 1 && isHighCard(listDef.get(0), listAtk.get(0), ignoreType)) {
                    return true;
                } else if (listAtk.get(0).getFakeNum() == CARD_NUMBER_TWO_FAKE_VALUE) {
                    return isFourOfAKind(listDef) || isLinkNPairs(listDef, 3) || isLinkNPairs(listDef, 4);
                }
                return false;
            case 2:
                sortCardByType(listAtk);
                return ( (isAPair(listDef) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType)) || (listAtk.get(lastElem).getFakeNum() == CARD_NUMBER_TWO_FAKE_VALUE && isLinkNPairs(listDef, 4)));
            case 3:
                if (isThreeOfAKind(listAtk)) {
                    return isThreeOfAKind(listDef) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                } else if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN)) {
                    return listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                }
                return false;
            case 4:
                if (isFourOfAKind(listAtk)) {
                    if (isFourOfAKind(listDef) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType)) {
                        return true;
                    } else if (isLinkNPairs(listDef, 4)) {
                        return true;
                    }
                    return false;
                } else if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN)) {
                    return listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                }
                return false;
            case 5:
            case 7:
                if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN)) {
                    return listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                }
                return false;
            case 6:
            case 8:
                if (isLinkNPairs(listAtk, listAtk.size() / 2)) {
                    if (isLinkNPairs(listDef, listAtk.size() / 2) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType)) {
                        return true;
                    } else if (listDef.size() > listAtk.size() && isLinkNPairs(listDef, 4)) {
                        return true;
                    }
                    return false;
                } else if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN)) {
                    return listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                }
                return false;
            case 9:
            case 10:
            case 11:
                if (isAStraight(listAtk, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN)) {
                    return listDef.size() == listAtk.size() && isAStraight(listDef, MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN) && isHighCard(listDef.get(lastElem), listAtk.get(lastElem), ignoreType);
                }
                return false;
            default:
                return false;
        }
    }

    private static int checkStealWinTLMN(List<Card> list) {
        sortCardByNum(list, false);
        if (isAStraight(list.subList(0, list.size() - 1), MIN_VALUE_STRAIGHT_TLMN, MAX_VALUE_STRAIGHT_TLMN)) {
            return STEAL_WIN_STRAIGHT;
        }
        if (getCountCardNumTwo(list) == 4) {
            return STEAL_WIN_FOUR_OF_KIND;
        }
        if (isContainsLinkPair(list, 6)) {
            return STEAL_WIN_SIX_LINK_PAIR;
        }
//        if (getCountCardSameColor(list) == list.size() - 1) {
//            return STEAL_WIN_SAME_COLOR;
//        }
        if (isContainsLinkPair(list, 5)) {
            return STEAL_WIN_FIVE_LINK_PAIR;
        }
//        if (getCountFourOfKind(list) == 4) {
//            return STEAL_WIN_THREE_OF_KIND;
//        }
        if (getCountPair(list) == 6) {
            return STEAL_WIN_SIX_PAIR;
        }
        return 0;
    }

    //----------------------------------------------//

    //----------------------------------------------// 
    public static int getCountFourOfKind(List<Card> list) {
        int count = 0;
        sortCardByNum(list, false);
        boolean[] flags = new boolean[Card.MAX_VALUE_NUM + 1];
        for (int i = 0; i < list.size() - 3; i++) {
            boolean flag = true;
            for (int j = i + 1; j < i + 4; j++) {
                if (list.get(i).getFakeNum() != list.get(j).getFakeNum()) {
                    flag = false;
                    break;
                }
            }
            if (flag && !flags[list.get(i).getRealNum()]) {
                count++;
                flags[list.get(i).getRealNum()] = true;
            }
        }
        return count;
    }

    private static int getCountThreeOfKind(List<Card> list) {
        int count = 0;
        sortCardByNum(list, false);
        boolean[] flags = new boolean[Card.MAX_VALUE_NUM + 1];
        for (int i = 0; i < list.size() - 2; i++) {
            boolean flag = true;
            for (int j = i + 1; j < i + 3; j++) {
                if (list.get(i).getFakeNum() != list.get(j).getFakeNum()) {
                    flag = false;
                    break;
                }
            }
            if (flag && !flags[list.get(i).getRealNum()]) {
                count++;
                flags[list.get(i).getRealNum()] = true;
            }
        }
        return count;
    }

    private static int getCountCardSameColor(List<Card> list) {
        int count_red = 0;
        int count_black = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCardTypeColor() == Card.CARD_TYPE_RED_COLOR) {
                count_red++;
            } else if (list.get(i).getCardTypeColor() == Card.CARD_TYPE_BLACK_COLOR) {
                count_black++;
            }
        }
        return Math.max(count_red, count_black);
    }

    public static int getCountCardNumTwo(List<Card> list) {
        int count = 0;
        for (Card card : list) {
            if (card.getFakeNum() == CARD_NUMBER_TWO_FAKE_VALUE) {
                count++;
            }
        }
        return count;
    }

    public static int getCountPair(List<Card> list) {
        int count = 0;
        boolean[] flags = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (flags[i]) {
                continue;
            }
            for (int j = 0; j < list.size(); j++) {
                if (i == j || flags[j]) {
                    continue;
                }
                if (list.get(i).getFakeNum() == list.get(j).getFakeNum()) {
                    flags[i] = true;
                    flags[j] = true;
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public static boolean isContainsLinkPair(List<Card> list, int len) {
        boolean[] flags = new boolean[CARD_NUMBER_TWO_FAKE_VALUE + 1];
        for (int i = 0; i < list.size() - 1; i++) {
            if (flags[list.get(i).getFakeNum()]) {
                continue;
            }
            for (int j = i + 1; j < list.size(); j++) {
                if (flags[list.get(i).getFakeNum()]) {
                    continue;
                }
                if (list.get(i).getFakeNum() == list.get(j).getFakeNum()) {
                    flags[list.get(i).getFakeNum()] = true;
                }
            }
        }
        int count = 0;
        for (int i = 0; i < flags.length - 1; i++) {
            if (flags[i] && flags[i + 1]) {
                count++;
            }
            if (flags[i] && !flags[i + 1]) {
                break;
            }
        }
        return count + 1 >= len;
    }

    //----------------------------------------------//
    //----------------------------------------------//
    private static boolean checkSelectCard(List<Card> listSelected) {
        List<Card> list = new ArrayList<>();
        for (Card card : listSelected) {
            if (card.isSelected()) {
                list.add(card);
            }
        }
        return checkAtkTLMN(list);
    }

    private static boolean isFourOfAKind(List<Card> listCard) {
        if (listCard.size() != 4) {
            return false;
        }
        if (listCard.get(0).getFakeNum() == listCard.get(1).getFakeNum() && listCard.get(1).getFakeNum() == listCard.get(2).getFakeNum() && listCard.get(2).getFakeNum() == listCard.get(3).getFakeNum()) {
            return true;
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> listCard) {
        if (listCard.size() != 3) {
            return false;
        }
        if (listCard.get(0).getFakeNum() == listCard.get(1).getFakeNum() && listCard.get(1).getFakeNum() == listCard.get(2).getFakeNum()) {
            return true;
        }
        return false;
    }

    private static boolean isAPair(List<Card> listCard) {
        if (listCard.size() != 2) {
            return false;
        }
        sortCardByType(listCard);
        return listCard.get(0).getFakeNum() == listCard.get(1).getFakeNum();
    }

    private static boolean isLinkNPairs(List<Card> listCard, int n) {
        if (listCard.size() != 2 * n) {
            return false;
        }
        sortCardByNumAndType(listCard);
        int check = 0;
        for (int i = 0; i < listCard.size(); i += 2) {
            if (check != 0 && listCard.get(i).getFakeNum() != check + 1) {
                return false;
            }
            if (listCard.get(i).getFakeNum() != listCard.get(i + 1).getFakeNum()) {
                return false;
            }
            check = listCard.get(i).getFakeNum();
        }
        return true;
    }

    private static boolean isAStraight(List<Card> listCard, int minNum, int maxNum) {
        if (listCard.size() < 3) {
            return false;
        }
        boolean b = true;
        sortCardByNum(listCard, false);
        if (listCard.get(0).getFakeNum() < minNum) {
            b = false;
        }
        if (listCard.get(listCard.size() - 1).getFakeNum() > maxNum) {
            b = false;
        }
        for (int i = 0; i < listCard.size() - 1; i++) {
            if (listCard.get(i).getFakeNum() != listCard.get(i + 1).getFakeNum() - 1) {
                b = false;
                break;
            }
        }
        if (b) {
            return true;
        } else if (minNum == MIN_VALUE_STRAIGHT_SL) {
            sortCardByNum(listCard, true);
            if (listCard.get(0).getRealNum() < minNum) {
                return false;
            }
            if (listCard.get(listCard.size() - 1).getRealNum() > maxNum) {
                return false;
            }
            for (int i = 0; i < listCard.size() - 1; i++) {
                if (listCard.get(i).getRealNum() != listCard.get(i + 1).getRealNum() - 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isAStraightFlush(List<Card> listCard) {
        if (listCard.size() != 5) {
            return false;
        }
        boolean b = true;
        sortCardByNum(listCard, false);
        for (int i = 0; i < listCard.size() - 1; i++) {
            if (listCard.get(i).getFakeNum() != listCard.get(i + 1).getFakeNum() - 1 || listCard.get(i).getType().getValue() != listCard.get(i + 1).getType().getValue()) {
                b = false;
                break;
            }
        }
        if (b) {
            return true;
        } else {
            sortCardByNum(listCard, true);
            for (int i = 0; i < listCard.size() - 1; i++) {
                if (listCard.get(i).getRealNum() != listCard.get(i + 1).getRealNum() - 1 || listCard.get(i).getType().getValue() != listCard.get(i + 1).getType().getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isFullHouse(List<Card> listCard) {
        if (listCard.size() != 5) {
            return false;
        }
        sortCardByNum(listCard, false);
        int num1 = listCard.get(0).getFakeNum();
        int num2 = listCard.get(4).getFakeNum();
        int count1 = 0, count2 = 0;
        for (Card card : listCard) {
            if (card.getFakeNum() == num1) {
                count1++;
            } else if (card.getFakeNum() == num2) {
                count2++;
            }
        }
        return (count1 + count2 == listCard.size() && Math.abs(count1 - count2) == 1);
    }

    private static boolean isHighCard(Card carddef, Card cardatk, boolean ignoreType) {
        if (ignoreType) {
            return carddef.getFakeNum() > cardatk.getFakeNum();
        }
        if (carddef.getFakeNum() > cardatk.getFakeNum()) {
            return true;
        }
        if (carddef.getFakeNum() == cardatk.getFakeNum() && carddef.getType().getValue() > cardatk.getType().getValue()) {
            return true;
        }
        return false;
    }

    public static void sortCardByNum(List<Card> listCard, boolean realNum) {
        Collections.sort(listCard, (Card o1, Card o2) -> {
            if (realNum) {
                return o1.getRealNum() - o2.getRealNum();
            }
            return o1.getFakeNum() - o2.getFakeNum();
        });
    }

    public static void sortCardByType(List<Card> listCard) {
        Collections.sort(listCard, (Card o1, Card o2) -> o1.getType().getValue() - o2.getType().getValue());
    }

    public static void sortCardByNumAndType(List<Card> listCard) {
        Collections.sort(listCard, (Card o1, Card o2) -> {
            if (o1.getFakeNum() != o2.getFakeNum()) {
                return o1.getFakeNum() - o2.getFakeNum();
            }
            return o1.getType().getValue() - o2.getType().getValue();
        });
    }
    //----------------------------------------------//

    public static void main(String[] args) {
//        List<Card> listAtk = new ArrayList<>();
//        listAtk.add(new Card(2, CardType.SPADE));
//        listAtk.add(new Card(2, CardType.SPADE));
//        listAtk.add(new Card(3, CardType.SPADE));
//        listAtk.add(new Card(3, CardType.SPADE));
//        listAtk.add(new Card(4, CardType.SPADE));
//        listAtk.add(new Card(4, CardType.SPADE));
//        listAtk.add(new Card(6, CardType.SPADE));
//        listAtk.add(new Card(4, CardType.SPADE));
//        listAtk.add(new Card(5, CardType.SPADE));
//        listAtk.add(new Card(5, CardType.SPADE));
//        listAtk.add(new Card(5, CardType.SPADE));
//        listAtk.add(new Card(8, CardType.SPADE));
//        System.out.println(getCountPair(listAtk));
    }
}
