package de.dontletyoudie.backend.persistence.account;

import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.account.dtos.AccountUpdateDTO;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.account.exceptions.IdNotFoundException;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountAlreadyExistsException;
import de.dontletyoudie.backend.persistence.category.Category;
import de.dontletyoudie.backend.persistence.category.CategoryRepository;
import de.dontletyoudie.backend.persistence.minime.MiniMe;
import de.dontletyoudie.backend.persistence.minime.MiniMeRepository;
import de.dontletyoudie.backend.persistence.minime.Skin;
import de.dontletyoudie.backend.persistence.stat.Stat;
import de.dontletyoudie.backend.persistence.stat.StatRepository;
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
import java.util.List;
import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MiniMeRepository miniMeRepository;
    private final CategoryRepository categoryRepository;
    private final StatRepository statRepository;

    public Account createAccount(AccountAddDTO accountAdd) throws AccountAlreadyExistsException {
        if (accountRepository.findAccountByUsername(accountAdd.getUsername()).isPresent())
            throw new AccountAlreadyExistsException(accountAdd.getUsername());

        if (accountRepository.findAccountByEmail(accountAdd.getEmail()).isPresent())
            throw new AccountAlreadyExistsException(accountAdd.getEmail());

        MiniMe miniMe = miniMeRepository.save(new MiniMe(Skin.DEFAULT));

        List<Category> categories = categoryRepository.findAll();

        categories.forEach(category -> statRepository.save(new Stat(0, category, miniMe)));

        return accountRepository.save(
                new Account(accountAdd.getUsername(), passwordEncoder.encode(accountAdd.getPassword()),
                        accountAdd.getEmail(), Role.USER,
                        miniMe)
        );
    }

    public Account updateAccount(AccountUpdateDTO accountUpdate) throws IdNotFoundException {
        Optional<Account> accountById = accountRepository.findById(accountUpdate.getId());
        if (accountById.isEmpty()) throw new IdNotFoundException(accountUpdate.getId());

        Account account = accountById.get();

        if (!accountUpdate.getUsername().equals("")) account.setUsername(accountUpdate.getUsername());

        if (!accountUpdate.getEmail().equals("")) account.setEmail(accountUpdate.getEmail());


        if (!accountUpdate.getPassword().equals(""))
            account.setPassword(passwordEncoder.encode(accountUpdate.getPassword()));

        return accountRepository.save(account);
    }

    public Account getAccount(String username) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findAccountByUsername(username);

        if (account.isEmpty()) throw new AccountNotFoundException(username);
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
        Account account;
        try {
            account = getAccount(username);
        } catch (AccountNotFoundException e) {
            throw new UsernameNotFoundException(username);
        }

        Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole().toString()));

        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}
