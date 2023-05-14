package de.dontletyoudie.backend.persistence.stat;

import de.dontletyoudie.backend.persistence.category.Category;
import de.dontletyoudie.backend.persistence.minime.MiniMe;
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
@Entity(name = "Stat")
@Table(name = "stat")
public class Stat {

    @Id
    @SequenceGenerator(name = "proof_sequence", sequenceName = "proof_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proof_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    private MiniMe miniMe;

    @ManyToOne(optional = false)
    private Category category;

    @Column(name = "score")
    private int points;
}
