package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    Optional<Relationship> findRelationshipBySrcAccountAndRelAccount(Account srcAccount, Account relAccount);
    Optional<List<Relationship>> findRelationshipsBySrcAccount(Account account);
    Optional<List<Relationship>> findRelationshipsByRelAccount(Account account);
}
