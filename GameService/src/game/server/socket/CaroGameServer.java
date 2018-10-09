/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.socket;

import game.server.common.CommonDefine;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author KenyDinh
 */
public class CaroGameServer {

    private static final int NUM_COLUMNS = CommonDefine.CARO_GAME_NUM_COLUMNS;
    private static final int NUM_ROWS = CommonDefine.CARO_GAME_NUM_ROWS;
    private static final int X_VALUE = CommonDefine.CARO_GAME_X_VALUE;
    private static final int O_VALUE = CommonDefine.CARO_GAME_O_VALUE;
    private static final int COUNT_TO_WIN = CommonDefine.CARO_GAME_COUNT_TO_WIN;

    private int id;
    private int[][] boardGame;
    private ClientServerSocket[] playerInGame;
    private ClientServerSocket[] viewerInGame;
    private boolean isEndGame = false;
    private int winType;//hoz, ver, cross
    private int winFlag;//x or o
    private StringBuilder winLine = new StringBuilder();
    private int countAcceptAgain;

    public CaroGameServer(int id) {
        this.id = id;
        this.countAcceptAgain = 0;
        boardGame = new int[NUM_COLUMNS][NUM_ROWS];
        playerInGame = new ClientServerSocket[CommonDefine.CARO_GAME_MAX_PLAYER];
        viewerInGame = new ClientServerSocket[CommonDefine.CARO_GAME_MAX_VIEWER];
    }

    public boolean isEndGame() {
        return isEndGame;
    }

    public int getWinFlag() {
        return winFlag;
    }

    public int getWinType() {
        return winType;
    }

    private void addCoordinate(int x, int y) {
        winLine.append(x).append(CommonDefine.SEPARATOR_KEY_VALUE).append(y);
        winLine.append(CommonDefine.COMMA);
    }

    public String getWinCoordinate() {
        String result = winLine.toString().trim();
        if (result.length() > 0) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }

    public int getId() {
        return this.id;
    }

    public int getCountPlayer() {
        int count = 0;
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_PLAYER; i++) {
            if (playerInGame != null && playerInGame[i] != null) {
                count++;
            }
        }
        return count;
    }

    public int getCountViewer() {
        int count = 0;
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_VIEWER; i++) {
            if (viewerInGame != null && viewerInGame[i] != null) {
                count++;
            }
        }
        return count;
    }

    public void addPlayer(ClientServerSocket player) {
        if (getCountPlayer() >= CommonDefine.CARO_GAME_MAX_PLAYER) {
            return;
        }
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_PLAYER; i++) {
            if (playerInGame[i] == null) {
                if (i == 0) {
                    player.setCaroGameValue(getRandomXO());
                } else if (i == 1) {
                    player.setCaroGameValue(playerInGame[0].getCaroGameValue() * -1);
                }
                player.setGameType(CommonDefine.GAME_TYPE_CARO_GAME);
                player.setIsPlayer(true);
                player.setIsViewer(false);
                player.setGameId(this.getId());
                playerInGame[i] = player;
                if (getCountPlayer() == CommonDefine.CARO_GAME_MAX_PLAYER) {
                    int ran = (int) (Math.random() * 100);
                    if (ran % 2 == 0) {
                        playerInGame[0].sendMessageToSelf(CommonDefine.CARO_GAME_VALUE + CommonDefine.SEPARATOR_KEY_VALUE + playerInGame[0].getCaroGameValue() + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_READY + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_FIRST_PLAY);
                        playerInGame[1].sendMessageToSelf(CommonDefine.CARO_GAME_VALUE + CommonDefine.SEPARATOR_KEY_VALUE + playerInGame[1].getCaroGameValue() + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_READY);
                    } else {
                        playerInGame[0].sendMessageToSelf(CommonDefine.CARO_GAME_VALUE + CommonDefine.SEPARATOR_KEY_VALUE + playerInGame[0].getCaroGameValue() + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_READY);
                        playerInGame[1].sendMessageToSelf(CommonDefine.CARO_GAME_VALUE + CommonDefine.SEPARATOR_KEY_VALUE + playerInGame[1].getCaroGameValue() + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_READY + CommonDefine.SEPARATOR_KEY_VALUE + CommonDefine.CARO_GAME_FIRST_PLAY);
                    }
                }
                break;
            }
        }
    }

    private int getRandomXO() {
        if (Math.random() < 0.5) {
            return CommonDefine.CARO_GAME_O_VALUE;
        }
        return CommonDefine.CARO_GAME_X_VALUE;
    }

    public List<ClientServerSocket> getListClientInGame() {
        List<ClientServerSocket> listClientInGame = new ArrayList<>();
        for (ClientServerSocket css : playerInGame) {
            if (css != null) {
                listClientInGame.add(css);
            }
        }
        for (ClientServerSocket css : viewerInGame) {
            if (css != null) {
                listClientInGame.add(css);
            }
        }
        return listClientInGame;
    }

    public ClientServerSocket getClientInGameById(int clientId) {
        for (ClientServerSocket viewer : viewerInGame) {
            if (viewer != null && viewer.getClientId() == clientId) {
                return viewer;
            }
        }
        for (ClientServerSocket player : playerInGame) {
            if (player != null && player.getClientId() == clientId) {
                return player;
            }
        }
        return null;
    }

    public void addViewer(ClientServerSocket viewer) {
        if (getCountViewer() >= CommonDefine.CARO_GAME_MAX_VIEWER) {
            return;
        }
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_VIEWER; i++) {
            if (viewerInGame[i] == null) {
                viewer.setGameType(CommonDefine.GAME_TYPE_CARO_GAME);
                viewer.setIsPlayer(false);
                viewer.setIsViewer(true);
                viewer.setGameId(this.getId());
                String str = getCurrentStateGame();
                if (str != null) {
                    if (isEndGame) {
                        viewer.sendMessageToSelf(CommonDefine.CARO_GAME_VIEWER + CommonDefine.SEPARATOR_GROUP + str + CommonDefine.SEPARATOR_GROUP + getWinCoordinate() + CommonDefine.SEPARATOR_GROUP + getWinType() + CommonDefine.SEPARATOR_GROUP + getWinFlag() + CommonDefine.SEPARATOR_GROUP + CommonDefine.CARO_GAME_END);
                    } else {
                        viewer.sendMessageToSelf(CommonDefine.CARO_GAME_VIEWER + CommonDefine.SEPARATOR_GROUP + str);
                    }
                } else {
                    viewer.sendMessageToSelf(CommonDefine.CARO_GAME_VIEWER);
                }
                viewerInGame[i] = viewer;
                break;
            }
        }
    }

    private void reInitViewer() {
        if (getCountPlayer() == CommonDefine.CARO_GAME_MAX_PLAYER) {
            for (ClientServerSocket viewer : viewerInGame) {
                if (viewer != null) {
                    String str = getCurrentStateGame();
                    if (str != null) {
                        viewer.sendMessageToSelf(CommonDefine.CARO_GAME_VIEWER + CommonDefine.SEPARATOR_GROUP + str + CommonDefine.SEPARATOR_GROUP + CommonDefine.CARO_GAME_PLAY_AGAIN);
                    } else {
                        viewer.sendMessageToSelf(CommonDefine.CARO_GAME_VIEWER + CommonDefine.SPACE + CommonDefine.CARO_GAME_PLAY_AGAIN);
                    }
                }
            }
        }
    }

    public void removePlayer(ClientServerSocket player) {
        if (getCountPlayer() == 0) {
            return;
        }
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_PLAYER; i++) {
            if (playerInGame[i] != null && playerInGame[i].getClientId() == player.getClientId()) {
                player.setCaroGameValue(0);
                player.setGameType(0);
                player.setIsPlayer(false);
                player.setIsViewer(false);
                player.setGameId(0);
                playerInGame[i] = null;
            }
        }
    }

    public void emptyPlayerInGame() {
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_PLAYER; i++) {
            if (playerInGame[i] != null) {
                playerInGame[i] = null;
            }
        }
    }

    public void playAgain(ClientServerSocket client) {
        countAcceptAgain++;
        if (countAcceptAgain % 2 != 0) {
            boardGame = new int[NUM_COLUMNS][NUM_ROWS];
            winType = 0;
            winFlag = 0;
            winLine = new StringBuilder();
            emptyPlayerInGame();
        } else {
            isEndGame = false;
            countAcceptAgain = 0;
        }
        addPlayer(client);
        reInitViewer();
    }

    public void removeViewer(ClientServerSocket viewer) {
        if (getCountViewer() == 0) {
            return;
        }
        for (int i = 0; i < CommonDefine.CARO_GAME_MAX_VIEWER; i++) {
            if (viewerInGame[i] != null && viewerInGame[i].getClientId() == viewer.getClientId()) {
                viewer.setGameType(0);
                viewer.setIsPlayer(false);
                viewer.setIsViewer(false);
                viewer.setGameId(0);
                viewerInGame[i] = null;
            }
        }
    }

    public boolean isAvailableCoordinate(int x, int y) {
        return boardGame[x][y] == 0;
    }

    public void storeCoordinate(int x_column, int y_row, int value) {
        int temp_hoz = value;
        int temp_ver = value;
        int temp_cross1 = value;
        int temp_cross2 = value;
        // <editor-fold defaultstate="collapsed" desc="Calculate End Game">   
        for (int i = x_column + 1; i < NUM_COLUMNS; i++) {
            if (value == X_VALUE) {
                if (boardGame[i][y_row] > 0) {
                    temp_hoz++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[i][y_row] < 0) {
                    temp_hoz--;
                } else {
                    break;
                }
            }
        }
        for (int i = x_column - 1; i >= 0; i--) {
            if (value == X_VALUE) {
                if (boardGame[i][y_row] > 0) {
                    temp_hoz++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[i][y_row] < 0) {
                    temp_hoz--;
                } else {
                    break;
                }
            }
        }
        for (int j = y_row + 1; j < NUM_ROWS; j++) {
            if (value == X_VALUE) {
                if (boardGame[x_column][j] > 0) {
                    temp_ver++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[x_column][j] < 0) {
                    temp_ver--;
                } else {
                    break;
                }
            }
        }
        for (int j = y_row - 1; j >= 0; j--) {
            if (value == X_VALUE) {
                if (boardGame[x_column][j] > 0) {
                    temp_ver++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[x_column][j] < 0) {
                    temp_ver--;
                } else {
                    break;
                }
            }
        }
        for (int i = x_column + 1, j = y_row + 1; i < NUM_COLUMNS && j < NUM_ROWS; i++, j++) {
            if (value == X_VALUE) {
                if (boardGame[i][j] > 0) {
                    temp_cross1++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[i][j] < 0) {
                    temp_cross1--;
                } else {
                    break;
                }
            }
        }
        for (int i = x_column - 1, j = y_row - 1; i >= 0 && j >= 0; i--, j--) {
            if (value == X_VALUE) {
                if (boardGame[i][j] > 0) {
                    temp_cross1++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[i][j] < 0) {
                    temp_cross1--;
                } else {
                    break;
                }
            }
        }
        for (int i = x_column + 1, j = y_row - 1; i < NUM_COLUMNS && j >= 0; i++, j--) {
            if (value == X_VALUE) {
                if (boardGame[i][j] > 0) {
                    temp_cross2++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[i][j] < 0) {
                    temp_cross2--;
                } else {
                    break;
                }
            }
        }
        for (int i = x_column - 1, j = y_row + 1; i >= 0 && j < NUM_ROWS; i--, j++) {
            if (value == X_VALUE) {
                if (boardGame[i][j] > 0) {
                    temp_cross2++;
                } else {
                    break;
                }
            } else if (value == O_VALUE) {
                if (boardGame[i][j] < 0) {
                    temp_cross2--;
                } else {
                    break;
                }
            }
        }
        //</editor-fold>
        boardGame[x_column][y_row] = value;
        // <editor-fold defaultstate="collapsed" desc="Get Win Line">       
        if (Math.abs(temp_cross1) >= COUNT_TO_WIN || Math.abs(temp_cross2) >= COUNT_TO_WIN || Math.abs(temp_hoz) >= COUNT_TO_WIN || Math.abs(temp_ver) >= COUNT_TO_WIN) {
            if (Math.abs(temp_cross1) >= COUNT_TO_WIN) {
                winType = CommonDefine.CARO_GAME_END_WITH_CROSS_1;
                winFlag = value;
                for (int i = x_column, j = y_row; i < NUM_COLUMNS && j < NUM_ROWS; i++, j++) {
                    if (value == X_VALUE) {
                        if (boardGame[i][j] > 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[i][j] < 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    }
                }
                for (int i = x_column - 1, j = y_row - 1; i >= 0 && j >= 0; i--, j--) {
                    if (value == X_VALUE) {
                        if (boardGame[i][j] > 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[i][j] < 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    }
                }
            } else if (Math.abs(temp_cross2) >= COUNT_TO_WIN) {
                winType = CommonDefine.CARO_GAME_END_WITH_CROSS_2;
                winFlag = value;
                for (int i = x_column, j = y_row; i < NUM_COLUMNS && j >= 0; i++, j--) {
                    if (value == X_VALUE) {
                        if (boardGame[i][j] > 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[i][j] < 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    }
                }
                for (int i = x_column - 1, j = y_row + 1; i >= 0 && j < NUM_ROWS; i--, j++) {
                    if (value == X_VALUE) {
                        if (boardGame[i][j] > 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[i][j] < 0) {
                            addCoordinate(i, j);
                        } else {
                            break;
                        }
                    }
                }
            } else if (Math.abs(temp_hoz) >= COUNT_TO_WIN) {
                winType = CommonDefine.CARO_GAME_END_WITH_HOZ;
                winFlag = value;
                for (int i = x_column; i < NUM_COLUMNS; i++) {
                    if (value == X_VALUE) {
                        if (boardGame[i][y_row] > 0) {
                            addCoordinate(i, y_row);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[i][y_row] < 0) {
                            addCoordinate(i, y_row);
                        } else {
                            break;
                        }
                    }
                }
                for (int i = x_column - 1; i >= 0; i--) {
                    if (value == X_VALUE) {
                        if (boardGame[i][y_row] > 0) {
                            addCoordinate(i, y_row);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[i][y_row] < 0) {
                            addCoordinate(i, y_row);
                        } else {
                            break;
                        }
                    }
                }
            } else if (Math.abs(temp_ver) >= COUNT_TO_WIN) {
                winType = CommonDefine.CARO_GAME_END_WITH_VER;
                winFlag = value;
                for (int j = y_row; j < NUM_ROWS; j++) {
                    if (value == X_VALUE) {
                        if (boardGame[x_column][j] > 0) {
                            addCoordinate(x_column, j);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[x_column][j] < 0) {
                            addCoordinate(x_column, j);
                        } else {
                            break;
                        }
                    }
                }
                for (int j = y_row - 1; j >= 0; j--) {
                    if (value == X_VALUE) {
                        if (boardGame[x_column][j] > 0) {
                            addCoordinate(x_column, j);
                        } else {
                            break;
                        }
                    } else if (value == O_VALUE) {
                        if (boardGame[x_column][j] < 0) {
                            addCoordinate(x_column, j);
                        } else {
                            break;
                        }
                    }
                }
            }
            isEndGame = true;
        }
        // </editor-fold>    
    }

    public String getCurrentStateGame() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CommonDefine.CARO_GAME_NUM_COLUMNS; i++) {
            for (int j = 0; j < CommonDefine.CARO_GAME_NUM_ROWS; j++) {
                if (boardGame[i][j] != 0) {
                    sb.append(i).append(CommonDefine.SEPARATOR_KEY_VALUE).append(j).append(CommonDefine.SEPARATOR_KEY_VALUE).append(boardGame[i][j]);
                    sb.append(CommonDefine.COMMA);
                }
            }
        }
        String result = sb.toString().trim();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
            return result;
        }
        return null;
    }
}
