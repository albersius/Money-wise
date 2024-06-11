package com.moneywise.repository;

import com.moneywise.model.BankModel;

import java.util.List;

public interface IBankRepository {
    BankModel getById(int id);
    List<BankModel> getAll(int limit);
    BankModel getByName(String name);
    boolean create(String name);
    boolean update(int bankId, String name);
    boolean delete(int bankId);
}
