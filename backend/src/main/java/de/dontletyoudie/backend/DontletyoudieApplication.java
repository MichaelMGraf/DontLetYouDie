package de.dontletyoudie.backend;


import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.dtos.AccountAddDTO;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootApplication
public class DontletyoudieApplication {
	public static void main(String[] args) {
		SpringApplication.run(DontletyoudieApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (AccountService accountService, RelationshipService relationshipService) {
		return args -> {
			try {
				accountService.createAccount(new AccountAddDTO("passi0305", "nichtpassis@e.mail",
						"passi007"
				));
				accountService.createAccount(new AccountAddDTO("michael0305", "nichtmichael@e.mail",
						"michael007"
				));
				accountService.createAccount(new AccountAddDTO("gloria0305", "nichtgloria@e.mail",
						"gloria007"
				));
				accountService.createAccount(new AccountAddDTO("michael0604", "vielleictdochmichael@e.mail",
						"michael007"
				));
				accountService.createAccount(new AccountAddDTO("gloria0306", "vielleichtdochgloria@e.mail",
						"gloria007"
				));

				// Passi and Gloria are friends by default
				relationshipService.save(new RelationshipAddDto("passi0305", "gloria0305"));
				relationshipService.accept("passi0305", "gloria0305");

				// Passi and michael0604 are friends by default
				relationshipService.save(new RelationshipAddDto("passi0305", "michael0604"));
				relationshipService.accept("passi0305", "michael0604");

				// Gloria has sent Michael a friend request which is pending
				relationshipService.save(new RelationshipAddDto("gloria0305", "michael0305"));

				// Michael has sent Passi a friend request which is pending
				relationshipService.save(new RelationshipAddDto("michael0305", "passi0305"));

			} catch (DataIntegrityViolationException e) {
				System.out.println("Es scheint als ob ein Eintrag mit diesem username schon existiert.");
			}
		};
	}
}
