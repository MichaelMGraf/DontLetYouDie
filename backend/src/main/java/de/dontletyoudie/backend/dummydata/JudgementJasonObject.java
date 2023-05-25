package de.dontletyoudie.backend.dummydata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JudgementJasonObject implements Comparable<JudgementJasonObject> {

    private String judge;
    private Long proofId;
    private Boolean approved;
    private LocalDateTime date;

    @Override
    public int compareTo(JudgementJasonObject o) {
        return date.compareTo(o.date);
    }
}
