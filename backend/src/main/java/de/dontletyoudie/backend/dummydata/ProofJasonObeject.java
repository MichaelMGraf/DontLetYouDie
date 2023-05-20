package de.dontletyoudie.backend.dummydata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProofJasonObeject implements Comparable<ProofJasonObeject> {
    String accountname;
    String category;
    LocalDateTime dateTime;


    @Override
    public int compareTo(ProofJasonObeject o) {
        return dateTime.compareTo(o.dateTime);
    }
}
