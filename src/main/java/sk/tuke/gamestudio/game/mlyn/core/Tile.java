package sk.tuke.gamestudio.game.mlyn.core;

public class Tile {
    private TileState state = TileState.EMPTY;

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }
}