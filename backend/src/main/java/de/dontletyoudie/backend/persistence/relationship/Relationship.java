package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Transactional
@Entity(name = "Relationship")
@Table(name = "relationship", uniqueConstraints = {
        @UniqueConstraint(name = "relation_unique", columnNames = {"src_account_id", "rel_account_id"})}
)
public class Relationship {

    @Id
    @SequenceGenerator(name = "relationship_sequence", sequenceName = "relationship_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "relationship_sequence")
    @Column(name = "relationship_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Account srcAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account relAccount;

    @Column(name = "status")
    private RelationshipStatus relationshipStatus;

    public Relationship(Account srcAccount, Account relAccount, RelationshipStatus relationshipStatus) {
        this.srcAccount = srcAccount;
        this.relAccount = relAccount;
        this.relationshipStatus = relationshipStatus;
    }
}
