package com.thvnhng.mockproject.Service;

public interface UserService {

    Boolean checkExistUsername(String username);
    Boolean checkExistUsernamePassword(String username, String password);
    Boolean checkExistEmail(String email);

}
