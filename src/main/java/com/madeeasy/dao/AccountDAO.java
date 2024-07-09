package com.madeeasy.dao;

import com.madeeasy.entity.Account;
import com.madeeasy.entity.User;

public interface AccountDAO {
    User findByUserId(String userId);

    Account findByAccountId(String accountId);

    void save(Account account);
}
