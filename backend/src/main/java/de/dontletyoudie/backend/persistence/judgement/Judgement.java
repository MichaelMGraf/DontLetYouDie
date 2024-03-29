package de.dontletyoudie.backend.persistence.judgement;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.proof.Proof;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

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
    @ManyToOne
    private Account judge;

    @NonNull
    @ManyToOne(optional = false)
    private Proof proof;

    @NonNull
    @Column(name = "approved")
    private Boolean approved;

    @NonNull
    @Column(name = "date")
    private LocalDateTime date;
}


