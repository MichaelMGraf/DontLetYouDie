package de.dontletyoudie.backend.persistence.account;

import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.account.dtos.AccountUpdateDTO;
import de.dontletyoudie.backend.persistence.account.exceptions.IdNotFoundException;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountAlreadyExistsException;
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

    public Account createAccount(AccountAddDTO accountAdd) {
        if (accountRepository.findAccountByUsername(accountAdd.getUsername()).isPresent())
            throw new AccountAlreadyExistsException(accountAdd.getUsername());

        if (accountRepository.findAccountByEmail(accountAdd.getEmail()).isPresent())
            throw new AccountAlreadyExistsException(accountAdd.getEmail());

        return accountRepository.save(
                new Account(accountAdd.getUsername(), passwordEncoder.encode(accountAdd.getPassword()),
                        accountAdd.getEmail(), Role.USER));
    }

    public Account updateAccount(AccountUpdateDTO accountUpdate) {
        Optional<Account> accountByUsername = accountRepository.findById(accountUpdate.getId());
        if (accountByUsername.isEmpty()) throw new IdNotFoundException(accountUpdate.getId());

        Account account = accountByUsername.get();

        if (!accountUpdate.getUsername().equals("")) account.setUsername(accountUpdate.getUsername());

        if (!accountUpdate.getEmail().equals("")) account.setEmail(accountUpdate.getEmail());


        if (!accountUpdate.getPassword().equals(""))
            account.setPassword(passwordEncoder.encode(accountUpdate.getPassword()));

        return accountRepository.save(account);
    }

    public Account getAccount(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findAccountByUsername(username);

        if (account.isEmpty()) throw new UsernameNotFoundException(username + " not found");

        return account.get();
    }

    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findAccountById(id);

        if (account.isEmpty()) throw new IllegalArgumentException("User with id " + id + " not found");

        return account.get();
    }

    public Collection<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccount(username);

        Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole().toString()));

        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}
