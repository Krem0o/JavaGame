package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity
@NamedQuery(name = "Comment.getComments", query = "SELECT c FROM Comment c WHERE c.game=:game")
@NamedQuery(name = "Comment.resetComments", query = "DELETE FROM Comment ")
public class Comment {
    @Id
    @GeneratedValue
    private int id;

    private String game;
    private String player;
    private String comment;
    private Date date;

    public Comment() {}

    public Comment(String game, String player, String comment, Date date) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}