package de.dontletyoudie.backend.persistence.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) { return accountRepository.save(account); }

    public Account getAccount(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findAccountByUsername(username);

        if (account.isEmpty()) throw new UsernameNotFoundException(username + " not found");

        return account.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccount(username);

        Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole().toString()));

        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}
