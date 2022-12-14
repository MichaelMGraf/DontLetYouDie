package de.dontletyoudie.backend.persistence.judgement;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.transaction.Transactional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Transactional
@Entity(name = "Judgement")
@Table(name = "judgement")
public class Judgement {

    @Id
    @SequenceGenerator(name = "judgement_sequence", sequenceName = "judgement_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "judgement_sequence")
    @Column(name = "judgement_id", updatable = false)
    private Long id;

    @NonNull
    @Column(name = "judge_name")
    private String judgeName;

    @NonNull
    @Column(name = "proof_id")
    private Long proofId;


    @NonNull
    @Column(name = "approved")
    private Boolean approved;
}
