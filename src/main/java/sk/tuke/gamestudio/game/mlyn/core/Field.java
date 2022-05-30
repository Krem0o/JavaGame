package sk.tuke.gamestudio.game.mlyn.core;

public class Field {
    private GameState state = GameState.PLAYING;
    private int count;
    private int moveCount;
    private String comment;
    private int rating;
    private final Tile[][] tiles;

    public Field(int count) {
        this.count = count;
        tiles = new Tile[7][7];
        fill();
    }

       private void fill() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                tiles[i][j] = new Tile();
            }
        }
        fillWithBlocked();
    }

    private void fillWithBlocked() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if ((i == 0 || i == 6) && (j == 1 || j == 2 || j == 4 || j == 5)) {
                    getTile(i,j).setState(TileState.BLOCKED);
                } else if ((i == 1 || i == 5) && (j == 0 || j == 2 || j == 4 || j == 6)) {
                    getTile(i,j).setState(TileState.BLOCKED);
                } else if ((i == 2 || i == 4) && (j == 0 || j == 1 || j == 5 || j == 6)) {
                    getTile(i,j).setState(TileState.BLOCKED);
                } else if (i == 3 && j == 3){
                    getTile(i,j).setState(TileState.BLOCKED);
                }
            }
        }
    }

    public int getNeighbor(int x, int y) {
        int player1CountY = 0;
        int player2CountY = 0;
        int player1CountX = 0;
        int player2CountX = 0;
        int player = 0;

        if (x != 3) {
            //y
            for (int i = x; i < x + 1; i++) {
                for (int j = 0; j < 7; j++) {
                    if (getTile(i, j).getState() == TileState.PLAYER1) {
                        player1CountY++;
                    }
                    if (getTile(i, j).getState() == TileState.PLAYER2) {
                        player2CountY++;
                    }
                }
            }
        }
        if (y != 3) {
            //x
            for (int i = 0; i < 7; i++) {
                for (int j = y; j < y + 1; j++) {
                    if (getTile(i,j).getState() == TileState.PLAYER1) {
                        player1CountX++;
                    }
                    if (getTile(i,j).getState() == TileState.PLAYER2) {
                        player2CountX++;
                    }
                }
            }
        }

        if (player1CountX == 3 || player1CountY == 3         ||
                specialThreeUp(x, y) == 1 || specialThreeDown(x, y) == 1 ||
                specialThreeRight(x, y) == 1 || specialThreeLeft(x, y) == 1) {
        player = 1;
        } else if (player2CountY == 3 || player2CountX == 3         ||
                specialThreeUp(x, y) == 2 || specialThreeDown(x, y) == 2 ||
                specialThreeRight(x, y) == 2 || specialThreeLeft(x, y) == 2) {
            player = 2;
        }
        return player;
    }

    private int specialThreeLeft(int x, int y) {
        int player1CountY = 0;
        int player2CountY = 0;
        int result = 0;
        if (x == 3 && (y == 0 || y == 1 || y == 2)) {
            for (int i = x; i < x + 1; i++) {
                for (int j = 0; j < 3; j++) {
                    if (getTile(i, j).getState() == TileState.PLAYER1) {
                        player1CountY++;
                    }
                    if (getTile(i, j).getState() == TileState.PLAYER2) {
                        player2CountY++;
                    }
                }
            }
        } else {
            return 0;
        }
        if (player1CountY == 3) {
            result = 1;
        } else if (player2CountY == 3) {
            result = 2;
        }
        return result;
    }

    private int specialThreeRight(int x, int y) {
        int player1CountY = 0;
        int player2CountY = 0;
        int result = 0;
        if (x == 3 && (y == 4 || y == 5 || y == 6)) {
            for (int i = x; i < x + 1; i++) {
                for (int j = 4; j < 7; j++) {
                    if (getTile(i, j).getState() == TileState.PLAYER1) {
                        player1CountY++;
                    }
                    if (getTile(i, j).getState() == TileState.PLAYER2) {
                        player2CountY++;
                    }
                }
            }
        } else {
            return 0;
        }
        if (player1CountY == 3) {
            result = 1;
        } else if (player2CountY == 3) {
            result = 2;
        }
        return result;
    }

    private int specialThreeDown(int x, int y) {
        int player1CountX = 0;
        int player2CountX = 0;
        int result = 0;
        if (y == 3 && (x == 4 || x == 5 || x == 6)) {
            for (int i = 4; i < 7; i++) {
                for (int j = y; j < y + 1; j++) {
                    if (getTile(i, j).getState() == TileState.PLAYER1) {
                        player1CountX++;
                    }
                    if (getTile(i, j).getState() == TileState.PLAYER2) {
                        player2CountX++;
                    }
                }
            }
        } else {
            return 0;
        }
        if (player1CountX == 3) {
            result = 1;
        } else if (player2CountX == 3) {
            result = 2;
        }
        return result;
    }

    private int specialThreeUp(int x, int y) {
        int player1CountX = 0;
        int player2CountX = 0;
        int result = 0;
        if (y == 3 && (x == 0 || x == 1 || x == 2)) {
            for (int i = 0; i < 3; i++) {
                for (int j = y; j < y + 1; j++) {
                    if (getTile(i, j).getState() == TileState.PLAYER1) {
                        player1CountX++;
                    }
                    if (getTile(i, j).getState() == TileState.PLAYER2) {
                        player2CountX++;
                    }
                }
            }
        } else {
            return 0;
        }
        if (player1CountX == 3) {
            result = 1;
        } else if (player2CountX == 3) {
            result = 2;
        }
        return result;
    }

    public boolean setTile(int x, int y, int player) {
        if (state != GameState.PLAYING) {
            return false;
        }
        Tile tile = tiles[x][y];
        if (player == 1 && count > 0) {
            if (tile.getState() == TileState.EMPTY) {
                tile.setState(TileState.PLAYER1);
                count--;
                return true;
            }
        } else if (player == 2 && count > 0){
            if (tile.getState() == TileState.EMPTY) {
                tile.setState(TileState.PLAYER2);
                count--;
                return true;
            }
        }
        return false;
    }

    private void solved() {
        int player1 = 0;
        int player2 = 0;
        if (getCount() <=0) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if (getTile(i, j).getState() == TileState.PLAYER1) {
                        player1++;
                    } else if (getTile(i, j).getState() == TileState.PLAYER2) {
                        player2++;
                    }
                }
            }
        }
        if (player1 == 2) {
            state = GameState.PLAYER2;
        } else if (player2 == 2) {
            state = GameState.PLAYER1;
        }
    }

     public boolean moveTile(int myX, int myY ,int nextX, int nextY, int player) {
         if (state != GameState.PLAYING) {
             return false;
         }
        boolean bool = false;
        if (checkUp(myX, myY, nextX, nextY) && !checkThree(player)) {
            bool = go(myX, myY, nextX, nextY, player);
        }
        if (checkDown(myX, myY, nextX, nextY) && !checkThree(player)) {
            bool = go(myX, myY, nextX, nextY, player);
        }
        if (checkLeft(myX, myY, nextX, nextY) && !checkThree(player)) {
            bool = go(myX, myY, nextX, nextY, player);
        }
        if (checkRight(myX, myY, nextX, nextY) && !checkThree(player)) {
            bool = go(myX, myY, nextX, nextY, player);
        }
        if (!canMove(player)) {
            if (player == 1) {
                state = GameState.PLAYER2;
            } else if (player == 2) {
                state = GameState.PLAYER1;
            }
        }
        if (checkThree(player)) {
            bool = go(myX, myY, nextX, nextY, player);
        }
        solved();
        moveCount++;
        return bool;
     }

     private boolean canMove(int player) {
        boolean bool = true;
        if (checkThree(player)) {
            return true;
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (player == 1 && getTile(i, j).getState() == TileState.PLAYER1) {
                    for (int g = 0; g < 7; g++) {
                        for (int k = 0; k < 7; k++) {
                            if(!checkUp(i, j, g, k) && !checkDown(i, j, g, k)
                                    && !checkLeft(i, j, g, k) && !checkRight(i, j, g, k)) {
                                bool = false;
                            } else {
                                return true;
                            }
                        }
                    }
                } else if (player == 2 && getTile(i, j).getState() == TileState.PLAYER2) {
                    for (int g = 0; g < 7; g++) {
                        for (int k = 0; k < 7; k++) {
                            if(!checkUp(i, j, g, k) && !checkDown(i, j, g, k)
                                    && !checkLeft(i, j, g, k) && !checkRight(i, j, g, k)) {
                                bool = false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return bool;
     }

     private boolean checkThree(int player) {
        int count1 = 0;
        int count2 = 0;
         for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (player == 1 && getTile(i, j).getState() == TileState.PLAYER1) {
                    count1++;
                } else if (player == 2 && getTile(i, j).getState() == TileState.PLAYER2) {
                    count2++;
                }
            }
        }
         return count1 == 3 || count2 == 3;
     }

     private boolean go(int myX, int myY ,int nextX, int nextY, int player) {
         if (player == 1 && getTile(myX, myY).getState() == TileState.PLAYER1 &&
                 getTile(nextX, nextY).getState() != TileState.BLOCKED &&
                 getTile(nextX, nextY).getState() != TileState.PLAYER2 &&
                 getTile(nextX, nextY).getState() != TileState.PLAYER1) {
             getTile(myX, myY).setState(TileState.EMPTY);
             getTile(nextX, nextY).setState(TileState.PLAYER1);
             return true;
         } else if (player == 2 && getTile(myX, myY).getState() == TileState.PLAYER2 &&
                 getTile(nextX, nextY).getState() != TileState.BLOCKED &&
                 getTile(nextX, nextY).getState() != TileState.PLAYER1 &&
                 getTile(nextX, nextY).getState() != TileState.PLAYER2) {
             getTile(myX, myY).setState(TileState.EMPTY);
             getTile(nextX, nextY).setState(TileState.PLAYER2);
             return true;
         }
         return false;
     }

    private boolean checkRight(int myX, int myY ,int nextX, int nextY) {
        if (myY == 7 || myY > nextY || myX != nextX || (myX == 3 && myY == 2)) {
            return false;
        }
        boolean bool;
        int count = 0;
        for (int i = myY+1; i <= nextY; i++) {
            if (getTile(myX, i).getState() == TileState.BLOCKED) {
                continue;
            }
            if (getTile(myX, i).getState() == TileState.PLAYER1 || getTile(myX, i).getState() == TileState.PLAYER2) {
                return false;
            }
            if (getTile(myX, i).getState() != TileState.BLOCKED) {
                if (getTile(myX, i).getState() == TileState.EMPTY) {
                    count++;
                }
            }
        }
        bool = count == 1;
        return bool;
    }

    private boolean checkLeft(int myX, int myY ,int nextX, int nextY) {
        if (myY == 0 || myY < nextY || myX != nextX || (myX == 3 && myY == 4)) {
            return false;
        }
        boolean bool;
        int count = 0;
        for (int i = myY-1; i >= nextY; i--) {
            if (getTile(myX, i).getState() == TileState.BLOCKED) {
                continue;
            }
            if (getTile(myX, i).getState() == TileState.PLAYER1 || getTile(myX, i).getState() == TileState.PLAYER2) {
                return false;
            }
            if(getTile(myX, i).getState() != TileState.BLOCKED) {
                if (getTile(myX, i).getState() == TileState.EMPTY) {
                    count++;
                }
            }
        }
        bool = count == 1;
        return bool;
    }

     private boolean checkUp(int myX, int myY ,int nextX, int nextY) {
        if (myX == 0 || myX < nextX || myY != nextY || (myX == 4 && myY == 3)) {
            return false;
        }
        boolean bool;
        int count = 0;
        for (int i = myX-1; i >= nextX; i--) {
            if (getTile(i,myY).getState() == TileState.BLOCKED) {
                continue;
            }
            if (getTile(i,myY).getState() == TileState.PLAYER1 || getTile(i,myY).getState() == TileState.PLAYER2) {
                return false;
            }
            if (getTile(i, myY).getState() != TileState.BLOCKED) {
                if (getTile(i, myY).getState() == TileState.EMPTY) {
                    count++;
                }
            }
        }
         bool = count == 1;
        return bool;
     }

    private boolean checkDown(int myX, int myY ,int nextX, int nextY) {
        if (myX == 7 || myX > nextX || myY != nextY || (myX == 2 && myY == 3)) {
            return false;
        }
        boolean bool;
        int count = 0;
        for (int i = myX+1; i <= nextX; i++) {
            if (getTile(i,myY).getState() == TileState.BLOCKED) {
                continue;
            }
            if (getTile(i,myY).getState() == TileState.PLAYER1 || getTile(i,myY).getState() == TileState.PLAYER2) {
                return false;
            }
            if (getTile(i, myY).getState() == TileState.EMPTY) {
                count++;
            }
        }
        bool = count == 1;
        return bool;
    }

    private int searchThree() {
        int search;
        int player1 = 0;
        int player2 = 0;
        int total1 = 0;
        int total2 = 0;
        int two1 = 0;
        int two2 = 0;
        int bool = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                search = getNeighbor(i, j);
                if (search == 1) {
                    player1++;
                } else if (search == 2) {
                    player2++;
                }
                if (getTile(i, j).getState() == TileState.PLAYER1) {
                    total1++;
                    two1 += inTwo(i,j);
                } else if (getTile(i, j).getState() == TileState.PLAYER2) {
                    total2++;
                    two2 += inTwo(i, j);
                }
            }
        }
        player1 = (player1 - 1) / 2;
        player2 /= 2;
        two1 = (two1 - 1) / 2;
        two2 /= 2;
        if (two1 >= 2) {
            total1 += two1/2;
            player1 += two1;
        } else if (two2 >= 5) {
            total2 += 1;
            player2 += 2;
        }
        if ((total1 - player1) == 0) {
            bool = 1;
        } else if ((total2 - player2) == 0) {
            bool = 2;
        }
        return bool;
    }

    private int inTwo(int x, int y) {
        int count = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
               count = getNeighbor(x, y);
            }
        }
        return count;
    }

    public boolean takeTile(int x, int y, int player) {
        if (state != GameState.PLAYING) {
            return false;
        }
        Tile tile = tiles[x][y];
            if (getNeighbor(x, y) != 2 && tile.getState() == TileState.PLAYER2 && player == 1) {
                tile.setState(TileState.EMPTY);
                return true;
            } else if (getNeighbor(x, y) != 1 && tile.getState() == TileState.PLAYER1 && player == 2) {
                tile.setState(TileState.EMPTY);
                return true;
            } else if (searchThree() == 1 && tile.getState() == TileState.PLAYER1 && player == 2) {
                tile.setState(TileState.EMPTY);
                return true;
            } else if (searchThree() == 2 && tile.getState() == TileState.PLAYER2 && player == 1) {
                tile.setState(TileState.EMPTY);
                return true;
            }
        solved();
        return false;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public GameState getState() {
        return state;
    }

    public int getCount() {
        return count;
    }

    public int numbersOfMove(int moves) {
        if (moves == -1) {
            return 0;
        }
        return moves;
    }

    public int getScore() {
        return 200 - moveCount;
    }

    public void setCount(int count) {
        this.count += count;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
}