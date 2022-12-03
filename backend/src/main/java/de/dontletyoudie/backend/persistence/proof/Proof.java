package de.dontletyoudie.backend.persistence.proof;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
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
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username", updatable = false)
    private String username;
    @Column(name = "category", updatable = false)
    private String category;

    @Column(name = "image", unique = false, nullable = false, length = 100000)
    byte[] image;
    @Column(name = "comment", updatable = false)
    private String comment;

    @Column(name = "creation_time", updatable = false)
    private ZonedDateTime dateTime;

    @Column(name = "avg_score")
    private float avgScore;

    @Column(name = "judgements")
    private int judgements;


    public Proof(String username, String category, byte[] image, String comment, ZonedDateTime dateTime, float avgScore, int judgements) {
        this.username = username;
        this.category = category;
        this.image= image;
        this.comment = comment;
        this.dateTime = dateTime;
        this.avgScore = avgScore;
        this.judgements = judgements;
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
