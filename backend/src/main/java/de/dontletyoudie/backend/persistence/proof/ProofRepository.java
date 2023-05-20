package de.dontletyoudie.backend.persistence.proof;

import de.dontletyoudie.backend.persistence.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProofRepository extends JpaRepository<Proof, Long> {

    List<Proof> findProofsByAccount(Account username);

    Optional<Proof> findById(long id);


}