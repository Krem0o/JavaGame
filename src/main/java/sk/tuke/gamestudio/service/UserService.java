package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Users;

import java.util.List;

public interface UserService {
    void addUser(Users user);
    List<Users> getUsers(String game);

}
