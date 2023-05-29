package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    Optional<Relationship> findRelationshipBySrcAccountAndRelAccount(Account srcAccount, Account relAccount);
    Optional<List<Relationship>> findRelationshipsBySrcAccount(Account account);
    Optional<List<Relationship>> findRelationshipsByRelAccount(Account account);
    Optional<List<Relationship>> findRelationshipsByRelAccountOrSrcAccount(Account account1, Account account2);
    void deleteAllBySrcAccountOrRelAccount(Account account1, Account account2);

    /**
     * Do not use this as long not all systems us mysql
     */
    @Modifying(flushAutomatically = true)
    @Transactional()
    @Query(value = "ALTER TABLE relationship" +
            "    ADD COLUMN least_account_id BIGINT AS (LEAST(src_account_id, rel_account_id)) VIRTUAL," +
            "    ADD COLUMN greatest_account_id BIGINT AS (GREATEST(src_account_id, rel_account_id)) VIRTUAL," +
            "    ADD CONSTRAINT unique_pair_constraint UNIQUE (least_account_id, greatest_account_id);", nativeQuery = true)
    void addUniqueConstrain();

}
