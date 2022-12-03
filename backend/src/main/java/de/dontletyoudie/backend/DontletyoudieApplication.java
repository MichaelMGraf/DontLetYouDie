package de.dontletyoudie.backend;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountRepository;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DontletyoudieApplication {
	public static void main(String[] args) {
		SpringApplication.run(DontletyoudieApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner (AccountService accountService) {
		return args -> {
			try {
				accountService.createAccount(new AccountAddDTO("passi0305", "nichtpassis@e.mail",
						"passi007"
				));
			} catch (DataIntegrityViolationException e) {
				System.out.println("Es scheint als ob ein Eintrag mit diesem username schon existiert.");
			}
		};
	}
}
