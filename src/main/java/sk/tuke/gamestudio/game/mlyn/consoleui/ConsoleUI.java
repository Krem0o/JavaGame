package sk.tuke.gamestudio.game.mlyn.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.Main;
import sk.tuke.gamestudio.game.mlyn.core.Field;
import sk.tuke.gamestudio.game.mlyn.core.GameState;
import sk.tuke.gamestudio.game.mlyn.core.Player;
import sk.tuke.gamestudio.game.mlyn.core.Tile;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([S])([A-G])([1-7])");
    private static final Pattern PATTERN_MOVE = Pattern.compile("([M])([A-G])([1-7])([A-G])([1-7])");
    private static final Pattern PATTERN_INT = Pattern.compile("([A-G])([1-7])");
    private final Field field;
    private final Scanner scanner = new Scanner(System.in);
    private int player = 1;
    private int count = 0;
    private String line;
    private int x;
    private int y;
    private int nextX;
    private int nextY;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private Player playerClass;

    /*public ConsoleUI(Field field, ScoreService scoreService, CommentService commentService, RatingService ratingService) {
        this.field = field;
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }*/

    public ConsoleUI(Field field) {
        this.field = field;
    }

    private void servicesPlayer1() {
        String c;
        scoreService.addScore(new Score("Mlyn", playerClass.getName(1), field.getScore(), new Date()));
        System.out.println("Please rate us: ");
        c = scanner.nextLine();
        if (!Pattern.matches("[1-5]",c)) {
            do {
                System.out.println("Rating is only number 1 - 5: ");
                System.out.println("1 - the best: ");
                System.out.println("5 - not good: ");
                System.out.println("Please rate us: ");
                c = scanner.nextLine();
            }while (!Pattern.matches("[1-5]",c));
        }
        field.setRating(Integer.parseInt(c));
        ratingService.addRating(new Rating("Mlyn", playerClass.getName(1), field.getRating(), new Date()));

        System.out.println("Leave us comment please: ");
        c = scanner.nextLine();
        if (!Pattern.matches("^[a-zA-Z_ ]+$",c)) {
            do {
                System.out.println("Comment must contain only letters");
                System.out.println("Leave us comment please: ");
                c = scanner.nextLine();
            } while (!Pattern.matches("^[a-zA-Z_ ]+$", c));
        }
        field.setComment(c);
        commentService.addComment(new Comment("Mlyn", playerClass.getName(1), field.getComment(), new Date()));
    }

    private void servicesPlayer2() {
        String c;
        scoreService.addScore(new Score("Mlyn", playerClass.getName(2), field.getScore(), new Date()));
        System.out.println("Please rate us: ");
        c = scanner.nextLine();
        if (!Pattern.matches("[1-5]",c)) {
            do {
                System.out.println("Rating is only number 1 - 5: ");
                System.out.println("1 - the best: ");
                System.out.println("5 - not good: ");
                System.out.println("Please rate us: ");
                c = scanner.nextLine();
            }while (!Pattern.matches("[1-5]",c));
        }
        field.setRating(Integer.parseInt(c));
        ratingService.addRating(new Rating("Mlyn", playerClass.getName(2), field.getRating(), new Date()));

        System.out.println("Leave us comment please: ");
        c = scanner.nextLine();
        if (!Pattern.matches("^[a-zA-Z_ ]+$",c)) {
            do {
                System.out.println("Comment must contain only letters");
                System.out.println("Leave us comment please: ");
                c = scanner.nextLine();
            } while (!Pattern.matches("^[a-zA-Z_ ]+$", c));
        }
        field.setComment(c);
        commentService.addComment(new Comment("Mlyn", playerClass.getName(2), field.getComment(), new Date()));
    }

    public  void play() {
        printTopScores();
        printRating();
        printComments();
        do {
            if (count == 0) {
                printRules();
            }
            printField();
            if (field.getCount() < 0) {
                System.out.println("Now " + playerClass.getName(player) + " can only move own tiles");
            }
            processInput();
        } while (field.getState() == GameState.PLAYING);
        printField();
        if (field.getState() == GameState.PLAYER1) {
            System.out.println("Winner: "+playerClass.getName(1));
            servicesPlayer1();
            playAgain();
        } else if (field.getState() == GameState.PLAYER2){
            System.out.println("Winner: "+playerClass.getName(2));
            servicesPlayer2();
            playAgain();
        }
    }

    private void playAgain() {
        System.out.println("Do you wanna play again? (Y/N)");
        String c = scanner.nextLine().toUpperCase();
        if ("Y".equals(c)) {
            Main.main(null);
        } else if ("N".equals(c)) {
            printTopScores();
            printRating();
            printComments();
            System.exit(0);
        } else {
            System.out.println("Wrong command " + c);
        }
    }

    private void names() {
        System.out.println("Enter a name of player1: ");
        String player1 = scanner.nextLine();
        if (!Pattern.matches("[a-zA-Z]+",player1) || !checkNames(player1)) {
            do {
                System.out.println("Name must contain only letters or name already exists");
                System.out.println("Enter a name of player1: ");
                player1 = scanner.nextLine();
            } while (!Pattern.matches("[a-zA-Z]+", player1) || !checkNames(player1));
        }

        System.out.println("Enter a name of player2: ");
        String player2 = scanner.nextLine();
        if (!Pattern.matches("[a-zA-Z]+",player2) || player1.equals(player2) || !checkNames(player2)) {
            do {
                System.out.println("Name must contain only letters or name already exists");
                System.out.println("Enter a name of player2: ");
                player2 = scanner.nextLine();
            } while (!Pattern.matches("[a-zA-Z]+", player2) || player1.equals(player2) || !checkNames(player2));
        }
        playerClass = new Player();
        playerClass.setName(player1, player2);
    }

    private void printRules() {
        names();
        System.out.println("--------------------------------RULES------------------------------------------------");
        System.out.println("Player must set all of stones, then he can move own stones.");
        System.out.println("If player have 3 stones in a row or in the column he can take enemy tile.");
        System.out.println("Player can move by one position.");
        System.out.println("If player have only 3 stones left, he can move by the more positions. ");
        System.out.println("If player have 3 stones in a row or in the column so the enemy cannot take them from him.");
        System.out.println("He can take his stones only if he have If player have 3 stones in a row or in the column.");
        System.out.println("Or if player have an abandoned stone.");
        System.out.println("Player lose the game if he have only 2 stones left or player cannot move his stones anymore.");
        System.out.println("Map is only 7x7.");
        System.out.println("Each player have 9 stones so in total its 19 stones.");
        System.out.println("Score algorithm is 200 - each move.");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Press any key to continue...");
        scanner.nextLine();
        count++;
    }

    private void printField() {
        System.out.println();
        for (int i = 0; i < 7; i++) {
            System.out.print(" ");
            System.out.print(i + 1);
        }
        System.out.println();

        for (int i = 0; i < 7; i++) {
           System.out.print((char) (i + 'A'));
           for (int j = 0; j < 7; j++) {
               Tile tile = field.getTile(i, j);
               System.out.print(" ");
               switch (tile.getState()) {
                   case EMPTY -> System.out.print("O");
                   case PLAYER1 -> System.out.print("1");
                   case PLAYER2 -> System.out.print("2");
                   case BLOCKED -> System.out.print(" ");
                   default -> throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
               }
           }
           System.out.println();
        }
    }

    private void wrongTile(int x, int y) {
        if (field.getCount() != 0) {
            char c = (char) (x + 'A');
            System.out.println("Wrong tile X: " + c + " Y: " + (y + 1));
        } else {
            System.out.println("Out of stones");
        }
    }

    private void repeatSet() {
        do {
            printField();
            wrongTile(x, y);
            System.out.println("On the turn is " + playerClass.getName(player));
            System.out.println("Number of stones " + field.numbersOfMove(field.getCount()));
            System.out.print("Set X and Y of tile: ");
            line = scanner.nextLine().toUpperCase();
            Matcher matcherInt = PATTERN_INT.matcher(line);
            if (matcherInt.matches()) {
                x = line.charAt(0) - 'A';
                y = Integer.parseInt(matcherInt.group(2)) - 1;
            } else {
                System.err.println("Wrong input " + line);
            }
        } while (!field.setTile(x, y, player));
    }

    private boolean repeatTake() {
        do {
        printField();
        wrongTile(x, y);
        System.out.println("On the turn is " + playerClass.getName(player));
        System.out.print("Take X and Y of enemy tile: ");
        line = scanner.nextLine().toUpperCase();
        Matcher matcherInt = PATTERN_INT.matcher(line);
        if (matcherInt.matches()) {
            x = line.charAt(0) - 'A';
            y = Integer.parseInt(matcherInt.group(2)) - 1;
            wrongTile(x, y);

        } else {
            System.err.println("Wrong input " + line);
        }
        } while (!field.takeTile(x, y, player));
        return true;
    }

    private boolean take(int xx, int yy) {
        x = xx;
        y = yy;
        boolean bool = true;
        player = Player.switchPlayer(player);
            printField();
            System.out.println("Now " + playerClass.getName(player) + " can take enemy tile");
            System.out.print("Take X and Y of enemy tile: ");
            line = scanner.nextLine().toUpperCase();
            Matcher matcherInt = PATTERN_INT.matcher(line);
            if (matcherInt.matches()) {
                x = line.charAt(0) - 'A';
                y = Integer.parseInt(matcherInt.group(2)) - 1;
                if (!field.takeTile(x, y, player)) {
                       bool = repeatTake();
                }
            } else {
                System.err.println("Wrong input " + line);
                bool = false;
            }
        player = Player.switchPlayer(player);
        return bool;
    }

    private void repeatMove() {
        do {
            printField();
            char cX = (char) (x + 'A');
            char cNextX = (char) (nextX + 'A');
            System.out.println("Cant move tile X:" + cX + " Y:" + (y + 1) + " to X:" + cNextX + " Y:" + (nextY + 1));
            System.out.println("On the turn is " + playerClass.getName(player));
            System.out.print("Set X, Y of tile and X, Y of tile where u want to move: ");
            line = scanner.nextLine().toUpperCase();
            Matcher matcherMove = PATTERN_MOVE.matcher(line);
            if (matcherMove.matches()) {
                x = line.charAt(1) - 'A';
                y = Integer.parseInt(matcherMove.group(3)) - 1;
                nextX = line.charAt(3) - 'A';
                nextY = Integer.parseInt(matcherMove.group(5)) - 1;
            } else {
                System.err.println("Wrong input " + line);
            }
        }while (!field.moveTile(x, y, nextX, nextY, player));
    }

    private void modifyCount() {
        if (field.getCount() > 0) {
            player = Player.switchPlayer(player);
        } else if (field.getCount() == 0) {
            player = Player.switchPlayer(player);
            field.setCount(-1);
        }
    }

    private void processInput() {
        System.out.println("On the turn is " + playerClass.getName(player));
        System.out.println("Number of stones " + field.numbersOfMove(field.getCount()));
        System.out.print("Enter command (X - exit, SA1 - set, MB2 - move): ");
        line = scanner.nextLine().toUpperCase();
        if ("X".equals(line))
            System.exit(0);

        Matcher matcher = COMMAND_PATTERN.matcher(line);
        Matcher matcherMove = PATTERN_MOVE.matcher(line);
        if (matcher.matches()) {
            if (matcher.group(1).equals("S")) {
                x = line.charAt(1) - 'A';
                y = Integer.parseInt(matcher.group(3)) - 1;
                if (field.getCount() > 0) {
                    if (!field.setTile(x, y, player)) {
                        repeatSet();
                    }
                }
                modifyCount();
                if (field.getNeighbor(x, y) == 1) {
                    while(!take(x, y)) {
                        System.out.println("You can't take this tile");
                    }
                } else if (field.getNeighbor(x, y) == 2) {
                    while(!take(x, y)) {
                        System.out.println("You can't take this tile");
                    }
                }
            }
        } else if (matcherMove.matches()) {
            if (matcherMove.group(1).equals("M")) {
                if (field.getCount() < 0) {
                    x = line.charAt(1) - 'A';
                    y = Integer.parseInt(matcherMove.group(3)) - 1;
                    nextX = line.charAt(3) - 'A';
                    nextY = Integer.parseInt(matcherMove.group(5)) - 1;
                    if(!field.moveTile(x, y, nextX, nextY, player)) {
                        repeatMove();
                    }
                    player = Player.switchPlayer(player);
                    if (field.getNeighbor(nextX, nextY) == 1) {
                        while(!take(nextX, nextY)) {
                            System.out.println("You can't take this tile");
                        }
                    } else if (field.getNeighbor(nextX, nextY) == 2) {
                        while(!take(nextX, nextY)) {
                            System.out.println("You can't take this tile");
                        }
                    }
                } else {
                    System.out.println("Number of stones must be 0");
                }
            }
        } else {
            System.err.println("Wrong input " + line);
        }
    }

    private boolean checkNames(String name) {
        String nameExist;
        List<Score> scores = scoreService.getTopScores("Mlyn");
        for(Score score : scores) {
            nameExist = score.getPlayer();
            if (name.equals(nameExist)) {
                return false;
            }
        }
        return true;
    }

    private void printTopScores() {
        System.out.println("------TOP 10 SCORES-------");
        List<Score> scores = scoreService.getTopScores("Mlyn");
        for(Score score : scores) {
            System.out.printf("%s %d\n", score.getPlayer(), score.getPoints());
        }
    }

    private void printComments() {
        System.out.println("---------COMMENTS--------");
        List<Comment> comments = commentService.getComment("Mlyn");
        for(Comment comment : comments) {
            System.out.printf("%s %s\n", comment.getPlayer(), comment.getComment());
        }
    }

    private void printRating() {
        int rate = 0;
        int count = 0;
        double avg = 0.0;
        System.out.println("----------RATING---------");
        List<Rating> ratings = ratingService.getRatings("Mlyn");
        for(Rating rating : ratings) {
            rate += rating.getRating();
            count++;
        }
        if (count != 0) {
            avg = (double)rate/count;
        }
        System.out.printf("%.2f\n", avg);
    }
}