package de.dontletyoudie.backend.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.Role;
import de.dontletyoudie.backend.security.tokenservice.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RestController
@Validated
@RequestMapping(path = "/login/token")
public class TokenController {

    private final AccountService accountService;
    private final TokenService tokenService;

    @Autowired
    public TokenController(AccountService accountService, TokenService tokenService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
    }

    @GetMapping(path = "/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        //TODO was gscheid zurückgeben
        String username;

        Optional<DecodedJWT> decodedTokenOptional =
                tokenService.extractAndDecodeJWT(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (decodedTokenOptional.isPresent()) username = decodedTokenOptional.get().getSubject();
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RefreshToken is needed");

        try {
            Account account = accountService.getAccount(username);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),
                    tokenService.genAccessAndRefreshToken(account.getUsername(), request,
                            account.getRoleAsList().stream().map(Role::name).toList()));

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
