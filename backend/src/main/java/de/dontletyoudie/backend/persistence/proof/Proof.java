package de.dontletyoudie.backend.persistence.proof;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.category.Category;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Transactional
@Entity(name = "Proof")
@Table(name = "proof")
public class Proof {

    @Id
    @SequenceGenerator(name = "proof_sequence", sequenceName = "proof_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proof_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    private Account account;

    @ManyToOne(optional = false)
    private Category category;

    @Column(name = "image", nullable = false, length = 200000)
    byte[] image;
    @Column(name = "comment", updatable = false)
    private String comment;

    @Column(name = "creation_time", updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "approved")
    private boolean approved;



    public Proof(Account account, Category category, byte[] image, String comment, LocalDateTime dateTime, boolean approved) {
        this.account = account;
        this.category = category;
        this.image= image;
        this.comment = comment;
        this.dateTime = dateTime;
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Proof proof = (Proof) o;
        return id != null && Objects.equals(id, proof.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
