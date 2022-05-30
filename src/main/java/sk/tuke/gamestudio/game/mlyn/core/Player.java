package sk.tuke.gamestudio.game.mlyn.core;

public class Player {
    private String player1;
    private String player2;

    public static int switchPlayer(int p) {
        int player = 0;
        if (p == 1) {
            player = 2;
        } else if (p == 2) {
            player = 1;
        }
        return player;
    }

    public void setName(String namePlayer1, String namePlayer2) {
        player1 = namePlayer1;
        player2 = namePlayer2;
    }

    public String getName(int player) {
        if (player == 1) {
            return player1;
        }
        return player2;
    }
}