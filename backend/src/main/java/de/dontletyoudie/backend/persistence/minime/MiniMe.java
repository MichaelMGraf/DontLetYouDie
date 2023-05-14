package de.dontletyoudie.backend.persistence.minime;

import lombok.*;
import javax.persistence.*;
import javax.transaction.Transactional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Transactional
@Entity(name = "MiniMe")
@Table(name = "minime")
public class MiniMe  {

    @Id
    @SequenceGenerator(name = "proof_sequence", sequenceName = "proof_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proof_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "skin", updatable = false)
    private Skin skin;
}
