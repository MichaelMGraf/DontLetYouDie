package de.dontletyoudie.repository;

import de.dontletyoudie.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
