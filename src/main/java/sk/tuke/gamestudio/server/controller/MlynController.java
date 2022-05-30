package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.mlyn.core.*;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/mlyn")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MlynController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    private Field field = new Field(18);
    private int player = 1;
    private String status = "Set Only";
    int x ,y, myX, myY, nextX, nextY = 0;
    private final String message = "message";
    private Model model;
    private boolean take = false;
    private boolean myTile = false;
    private boolean notMyTile = false;
    private boolean move = false;
    private boolean first = true;
    @Autowired
    private UserController userController;

    @RequestMapping
    public String mlyn(@RequestParam(required = false) String i, @RequestParam(required = false) String j,
                       Model model) {
        //player = Player.switchPlayer(player);
        try {
            if (!status.equals("SOLVED")) {
                this.model = model;
                this.model.addAttribute("score", scoreService.getTopScores("Mlyn"));

                if (i != null && j != null) {
                    x = Integer.parseInt(i);
                    y = Integer.parseInt(j);
                }
                if (!take && !move) {
                    status = "Set Only";
                }

                //SET
                if (field.getCount() > 0 && !take && !move) {
                    if (!field.setTile(x, y, player)) {
                        this.model.addAttribute(message, "Wrong tile X: " + x + " Y: " + y);
                        return "mlyn";
                    } else {
                        player = Player.switchPlayer(player);
                    }
                }
                //TAKE
                if (!move) {
                    if (!take) {
                        myX = x;
                        myY = y;
                    }
                    if (field.getNeighbor(myX, myY) == 1 || take) {
                        return Take(x, y);
                    } else if (field.getNeighbor(myX, myY) == 2 || take) {
                        return Take(x, y);
                    }
                }
                //MOVE
                if (field.getCount() <= 0) {
                    if (!take) {
                        status = "Move Only";
                    }
                    move = true;
                    if (first) {
                        first = false;
                        return "mlyn";
                    }
                    if (((field.getTile(x, y).getState() == TileState.PLAYER1 || field.getTile(x, y).getState() == TileState.PLAYER2) &&
                            !myTile) && !take) {
                        myX = x;
                        myY = y;
                        myTile = true;
                        return "mlyn";
                    }
                    if ((field.getTile(x, y).getState() == TileState.EMPTY || field.getTile(x, y).getState() == TileState.BLOCKED) ||
                            (player == 1 && field.getTile(x, y).getState() == TileState.PLAYER1)
                            || (player == 2 && field.getTile(x, y).getState() == TileState.PLAYER2)
                            && (!notMyTile && !take)) {
                        nextX = x;
                        nextY = y;
                        notMyTile = true;
                    }
                    move();

                }
                win();
            }
        }catch (Exception e) {
            e.initCause(e);
        }
        //this.model.addAttribute("htmlField", getHtmlField());
        return "mlyn";
    }

    public boolean isSolved() {
        return status.equals("SOLVED");
    }

    private void win() {
        if (field.getState() == GameState.PLAYER1) {
            player = Player.switchPlayer(player);
            if (userController.isLogged()) {
                scoreService.addScore(new Score("Mlyn", userController.getLoggedUser().getLogin(), field.getScore(), new Date()));
                this.model.addAttribute(message, "Winner: " + userController.getLoggedUser().getLogin());
            }
            status = "SOLVED";
        } else if (field.getState() == GameState.PLAYER2){
            this.model.addAttribute(message, "Winner: Enemy");
            status = "SOLVED";
        }
    }

    private String move() {
        if (myTile && notMyTile) {
            if (!field.moveTile(myX, myY, nextX, nextY, player) && !take) {
                            myTile = false;
                            notMyTile = false;
                this.model.addAttribute(message, "Wrong tile X: " + myX + " Y: " + myY +
                        " and X: " + nextX + " Y: " + nextY);
                return "mlyn";
            } else {
                if (field.getNeighbor(nextX, nextY) == 1) {
                    return Take(x, y);
                } else if (field.getNeighbor(nextX, nextY) == 2) {
                    return Take(x, y);
                }
                player = Player.switchPlayer(player);
            }
        }
        if (!take) {
            myTile = false;
            notMyTile = false;
        }
        return "mlyn";
    }

    public boolean getMove() {
        return move;
    }

    public boolean getMyTile() {
        return myTile;
    }

    public boolean getNotMyTile() {
        return notMyTile;
    }

    public String getPlayer() {
        if (player == 1) {
            if (userController.getLoggedUser() != null) {
                return userController.getLoggedUser().getLogin();
            }
        }
        return "Enemy";
    }

    private String Take(int xx, int yy) {
        if (!take) {
            take = true;
            status = "Take Only";
            player = Player.switchPlayer(player);
            if (move) {
                player = Player.switchPlayer(player);
            }
            return "mlyn";
        }
        if (!field.takeTile(xx, yy, player)) {
            this.model.addAttribute(message, "Wrong tile X: " + xx + " Y: " + yy);
            return "mlyn";
        } else {
            clear();
        }
        return "mlyn";
    }

    private void clear() {
        myX = 0;
        myY = 0;
        take = false;
        nextX = 0;
        nextY = 0;
        if (move) {
            status = "Move Only";
        } else {
            status = "Set Only";
        }
        myTile = false;
        notMyTile = false;
        player = Player.switchPlayer(player);
    }

    public boolean getTake() {
        return take;
    }

    public int getMyX() {
        return myX;
    }

    public int getMyY() {
        return myY;
    }

    public int getNextX() {
        return nextX;
    }

    public int getNextY() {
        return nextY;
    }

    @RequestMapping("/new")
    public String newGame() {
        field = new Field(6);
        status = "Set Only";
        player = 1;
        myY = 0;
        myX = 0;
        nextX = 0;
        nextY = 0;
        take = false;
        move = false;
        myTile = false;
        notMyTile = false;
        first = true;
        return "mlyn";
    }

    public int getCount() {
        return field.getCount();
    }

    public String getStatus() {
        return status;
    }

    @RequestMapping(value = "field", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getHtmlField() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table>\n");
        for (int i = 0; i < 7; i++) {
            stringBuilder.append("<tr>\n");
            for (int j = 0; j < 7; j++) {
                Tile tile = field.getTile(i, j);
                stringBuilder.append("<td>");
                stringBuilder.append(String.format("<a href='/mlyn?i=%d&j=%d'>\n", i, j));
                stringBuilder.append("<img src='/images/" + getImage(tile) + ".png'>");
                stringBuilder.append("</a>\n");
                stringBuilder.append("</td>\n");
            }
            stringBuilder.append("</tr>\n");
        }
        stringBuilder.append("</table>\n");

        return stringBuilder.toString();
    }

    public double getAvgRating() {
        int rate = 0;
        int count = 0;
        double avg = 0.0;
        List<Rating> ratings = ratingService.getRatings("Mlyn");
        for(Rating rating : ratings) {
            rate += rating.getRating();
            count++;
        }
        if (count != 0) {
            avg = (double)rate/count;
        }
        BigDecimal bd = new BigDecimal(avg).setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    private String getImage(Tile tile) {
        return switch (tile.getState()) {
            case EMPTY -> "closed";
            case PLAYER1 -> "player1";
            case PLAYER2 -> "player2";
            case BLOCKED -> "blocked";
            //default -> throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
        };
    }
}
