package de.dontletyoudie.backend;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DontletyoudieApplication {

	/*
	@Autowired
	private AccountRepository repo;
	*/

	public static void main(String[] args) {
		SpringApplication.run(DontletyoudieApplication.class, args);
	}

	/**
	 * Mit dieser Methode kann man neue Accounts beim starten der Application hinzufügen lassen.
	 * (Ist nur zum testen und zeigen)
	 * @param studentRepository
	 * @return
	 */

	/*
	@Bean
	CommandLineRunner commandLineRunner (AccountRepository studentRepository) {
		return args -> {
			try {
				studentRepository.save(new Account("passi0305",
						"passi's PWD (btw aktuell alles noch unverschlüsselt. Aber es kann sich eh keiner damit anmelden)",
						"nichtpassis@e.mail"));
			} catch (DataIntegrityViolationException e) {
				System.out.println("Es scheint als ob ein Eintrag mit diesem username schon existiert.");
			}
		};
	}
	 */
}
