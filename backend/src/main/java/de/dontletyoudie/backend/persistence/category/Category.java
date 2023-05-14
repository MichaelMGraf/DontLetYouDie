package de.dontletyoudie.backend.persistence.category;


import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Transactional
@Entity(name = "Category")
@Table(name = "category")
public class Category {

    @Id
    @SequenceGenerator(name = "proof_sequence", sequenceName = "proof_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proof_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "essential")
    private Boolean essential;

    public Category(String name, Boolean essential) {
        this.name = name;
        this.essential = essential;
    }
}
