package com.moneywise.repository;

import com.moneywise.model.UserModel;

public interface IUserRepository {
    UserModel getByEmailPassword(String email, String password) throws Exception;
    boolean create(String email, String password);
    boolean update(int id, UserModel model);
}