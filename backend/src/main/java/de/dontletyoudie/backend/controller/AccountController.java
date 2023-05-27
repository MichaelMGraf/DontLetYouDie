package de.dontletyoudie.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.account.dtos.AccountShowDto;
import de.dontletyoudie.backend.persistence.account.dtos.AccountUpdateDTO;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountAlreadyExistsException;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.account.exceptions.IdNotFoundException;
import de.dontletyoudie.backend.security.filter.Filter;
import de.dontletyoudie.backend.security.filter.FilterData;
import de.dontletyoudie.backend.security.filter.PathFilter;
import de.dontletyoudie.backend.security.filter.PathFilterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@RestController
@Validated
@Filter
@RequestMapping(path = "/api/account")
public class AccountController {

    private final AccountService accountService;
    private static AccountService accountServiceStatic;

    @Autowired
    public AccountController(@Lazy AccountService accountService) {
        this.accountService = accountService;
        accountServiceStatic = accountService;
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

    @PathFilter(path={"/api/account/get", "/api/account/delete"}, tokenRequired = true)
    public static PathFilterResult filterGetAccount(FilterData data) {
        if (data.getRequest().getParameter("username").equals(data.getToken().getSubject()))
            return PathFilterResult.getNotDenied();
        return PathFilterResult.getAccessDenied("username does not match Token subject");
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

    @PathFilter(path={"/api/account/alter"}, tokenRequired = true)
    public static PathFilterResult filterAlterAccount(FilterData data) {
        ObjectMapper mapper = new ObjectMapper();
        AccountUpdateDTO dto;
        Account account;
        try {
            String body = data.getBody();
            dto = mapper.readValue(body, AccountUpdateDTO.class);
            account = accountServiceStatic.getAccount(data.getToken().getSubject());
        } catch (IOException | AccountNotFoundException e) {
            e.printStackTrace();
            return PathFilterResult.getAccessDenied("authorization credentials could not be parsed");
        }

        if (dto.getId() == account.getId())
            return PathFilterResult.getNotDenied();
        return PathFilterResult.getAccessDenied("id does not match Token subjects id");
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "username") String username) {

        try {
            accountService.delete(username);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PathFilter(path = {"/api/account/add"})
    public static PathFilterResult filterAddAccount(FilterData data) {
        return PathFilterResult.getInstantGrant();
    }
}
