package Capston.camo.service;

import Capston.camo.domain.Account;
import Capston.camo.domain.Role;
import Capston.camo.repository.RoleRepository;
import Capston.camo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Account account = userRepository.findByNickName(nickname);
        if(account == null){
            log.error("{}을 가진 유저가 존재하지 않습니다",nickname);
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        } else{
            log.info("{}을 가진 유저가 존재",nickname);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        account.getRoles().forEach(role ->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        log.info("새로운 아이디 {} 저장",account.getNickName());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return userRepository.save(account);
    }

    @Override
    @Transactional
    public Role saveRole(Role role) {
        log.info("새로운 역할 : {} 저장",role.getName());
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void addRoleToAccount(String nickname, String roleName) {
        Account account = userRepository.findByNickName(nickname);
        Role role = roleRepository.findByName(roleName);

        account.getRoles().add(role);

    }

    @Override
    public Account getAccount(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Account> getAccounts() {
        return userRepository.findAll();
    }


}
