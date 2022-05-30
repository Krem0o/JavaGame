package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity
@NamedQuery(name = "Rating.getRating", query = "SELECT r FROM Rating r WHERE r.game=:game")
@NamedQuery(name = "Rating.resetRating", query = "DELETE FROM Rating")
public class Rating {
    @Id
    @GeneratedValue
    private int id;

    private String game;
    private String player;
    private int rating;
    private Date date;

    public Rating() {}

    public Rating(String game, String player, int rating, Date date) {
        this.game = game;
        this.player = player;
        this.rating = rating;
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

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
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