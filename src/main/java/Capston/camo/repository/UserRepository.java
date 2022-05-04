package Capston.camo.repository;

import Capston.camo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account,Long> {

    Account findByNickName(String nickName);
    Account findByEmail(String email);
}
