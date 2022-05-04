package Capston.camo.service;

import Capston.camo.domain.Account;
import Capston.camo.domain.Role;

import java.util.List;

public interface UserService {

    Account saveAccount(Account account);
    Role saveRole(Role role);

    void addRoleToAccount(String nickname,String roleName);

    Account getAccount(String email);

    List<Account> getAccounts();
}
