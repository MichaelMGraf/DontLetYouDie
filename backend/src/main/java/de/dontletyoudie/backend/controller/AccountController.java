package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.account.dtos.AccountShowDto;
import de.dontletyoudie.backend.persistence.account.dtos.AccountUpdateDTO;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountAlreadyExistsException;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.account.exceptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Validated
@RequestMapping(path = "/api/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    /**
     * @param username Username of the account that is being queried for
     * @return Account Instance of the account if it exists, else null
     */
    @GetMapping(path = "/get")
    public ResponseEntity<AccountShowDto> getByUsername(@RequestParam(value = "username") String username) {
        Account account;
        try {
            account = accountService.getAccount(username);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(new AccountShowDto(account), HttpStatus.OK);
    }


    /**
     * @param accountAddDTO Json in form of AccountAddDTO containing the needed information to create a new Account.
     */
    @PostMapping(path = "/add")
    public ResponseEntity<AccountShowDto> add(@RequestBody AccountAddDTO accountAddDTO) {
        Account savedAccount;
        try {
            savedAccount = accountService.createAccount(accountAddDTO);
        } catch (AccountAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return new ResponseEntity<>(new AccountShowDto(savedAccount), HttpStatus.CREATED);
    }

    @PutMapping(path = "/alter")
    public ResponseEntity<AccountShowDto> update(@RequestBody AccountUpdateDTO accountUpdateDTO) {

        Account savedAccount;

        try {
            savedAccount = accountService.updateAccount(accountUpdateDTO);
        } catch (IdNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        AccountShowDto accountShowDto = new AccountShowDto(
                savedAccount.getUsername(),
                savedAccount.getEmail());

        return new ResponseEntity<>(accountShowDto, HttpStatus.OK);
    }
}
