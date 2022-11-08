package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/api/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    /**
     * @return ArrayList containing all Accounts
     */
    @GetMapping(path = "/get")
    public List<Account> getAll() { return accountRepository.findAll(); }

    /**
     *
     * @param username Username of the account that is being queried for
     * @return Account Instance of the account if it exists, else null
     */
    @GetMapping(path = "/get/{username}")
    public Account getByUsername(@PathVariable(value="username") String username) {
        return accountRepository.findAccountByUsername(username).orElse(null);
    }


    @PostMapping(path = "/add")
    /**
     * @param accountAddDTO Json in form of AccountAddDTO containing the needed information to create a new Account.
     */
    public ResponseEntity<AccountUpdateDTO> add(@RequestBody AccountAddDTO accountAddDTO) {
        Account account = new Account(accountAddDTO.getUsername(), accountAddDTO.getPassword(), accountAddDTO.getEmail());
        Account savedAccount = accountService.saveAccount(account);

        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO(
                savedAccount.getUsername(),
                savedAccount.getPassword(),
                savedAccount.getEmail(),
                savedAccount.getId());

        return new ResponseEntity<>(accountUpdateDTO, HttpStatus.CREATED);
    }
}
