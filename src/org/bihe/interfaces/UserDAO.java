package org.bihe.interfaces;

import org.bihe.models.User;

import java.util.List;

public interface UserDAO {
    List<User> getAllUsers() ;
    User getUserByID(int id);
    User getUserByUsername(String username);
    boolean saveUser (User user);
    boolean deleteUser(int id);
    boolean updateUser(int id);
    List<String> getAllUsernames();
}
