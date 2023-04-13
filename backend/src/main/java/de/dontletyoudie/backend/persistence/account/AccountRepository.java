package de.dontletyoudie.backend.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUsername(String name);

    Optional<Account> findAccountByEmail(String email);

    Optional<Account> findAccountById(Long id);
}
