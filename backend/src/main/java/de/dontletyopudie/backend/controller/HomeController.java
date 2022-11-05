package de.dontletyopudie.backend.controller;

import de.dontletyopudie.backend.persistence.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    AccountRepository accountRepository;

    /**
     * Ist nur um zu zeigen wie man mit Spring Boot einen Request annehmen kann
     * und hauptsächlich um auszugeben was in der account Tabelle in der DB steht
     * @return
     */
    @GetMapping("/")
    public String getAllAccounts() {
        return accountRepository.findAll().toString();
    }
}
