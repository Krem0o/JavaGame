package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Users;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.UserService;

import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private Users loggedUser;
    private boolean registered = false;
    @Autowired
    private UserService userService;
    private String message;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private boolean comment = false;
    private boolean rating = false;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/register")
    public String register(String login, String password) {
        String nameExist;
        boolean bool = false;
        List<Users> users = userService.getUsers("Mlyn");
        for(Users x : users) {
            nameExist = x.getLogin();
            if (login.equals(nameExist)) {
                bool = true;
                message = "Name already exists";
            }
        }
        if (!bool) {
            userService.addUser(new Users("Mlyn", login, password));
            message = "Successfully registered";
            registered = true;
        }
        return "redirect:/";
    }


    @RequestMapping("/login")
    public String login(String login, String password) {
        String nameExist;
        String passExist;
        boolean bool = false;
        List<Users> users = userService.getUsers("Mlyn");
        for(Users x : users) {
            nameExist = x.getLogin();
            passExist = x.getPassword();
            if (login.equals(nameExist) && password.equals(passExist)) {
                bool = true;
            } else {
                message = "Incorrect name or password";
            }
        }
        if (bool) {
            loggedUser = new Users("Mlyn", login, password);
            return "redirect:/mlyn";
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logOut() {
        loggedUser = null;
        message = "You have been logged out";
        return "redirect:/";
    }

    @RequestMapping("/again")
    public String again() {
        registered = false;
        return "redirect:/";
    }

    @RequestMapping("/rating")
    public String rating(String ratings) {
        if (!this.rating) {
            message = "Successfully rating sended " + Integer.parseInt(ratings);
            ratingService.addRating(new Rating("Mlyn", getLoggedUser().getLogin(), Integer.parseInt(ratings), new Date()));
            this.rating = true;
        } else {
            message = "You already send rating!";
        }
        return "redirect:/mlyn";
    }

    @RequestMapping("/comment")
    public String comment(String comment) {
        if (!this.comment) {
            commentService.addComment(new Comment("Mlyn", getLoggedUser().getLogin(), comment, new Date()));
            message = "Comment successfully sended! Comment: " + comment;
            this.comment = true;
        } else {
            message = "You already send comment!";
        }
        return "redirect:/mlyn";
    }

    public Users getLoggedUser() {
        return loggedUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRegistered() {
        return registered != false;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}
