/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.socket;

import game.server.common.CommonDefine;

/**
 *
 * @author tkv-nhukhanhdinh
 */
public class ThreadBoom extends Thread {

    private int x;
    private int y;
    private int bomb_val;
    private int creator_val;
    private BoomGameServer boomGame;
    private boolean isAlive;

    public ThreadBoom(int x, int y, int bomb_val, int creator_val, BoomGameServer boomGame) {
        this.x = x;
        this.y = y;
        this.bomb_val = bomb_val;
        this.creator_val = creator_val;
        this.boomGame = boomGame;
        isAlive = true;
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBomb_val() {
        return bomb_val;
    }

    public void setBomb_val(int bomb_val) {
        this.bomb_val = bomb_val;
    }

    public int getCreator_val() {
        return creator_val;
    }

    public void setCreator_val(int creator_val) {
        this.creator_val = creator_val;
    }

    public BoomGameServer getBoomGame() {
        return boomGame;
    }

    public void setBoomGame(BoomGameServer boomGame) {
        this.boomGame = boomGame;
    }

    @Override
    public void run() {
        try {
            sleep(3000);
            if (isIsAlive()) {
                if (!boomGame.isEndGame()) {
                    boomGame.sendMessToAllPlayer(CommonDefine.BOOM_GAME_EXPLODE_BOOM + boomGame.explodeBoom(this.x, this.y, this.bomb_val, this.creator_val));
                    boomGame.checkGameEndByHp();
                }
            }
        } catch (InterruptedException ex) {
        }
    }

    public String explodeBoom() {
        setIsAlive(false);
        return boomGame.explodeBoom(this.x, this.y, this.bomb_val, this.creator_val);
    }

    public void removeBoom() {
        setIsAlive(false);
    }

}
