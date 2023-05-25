package de.dontletyoudie.backend.persistence.stat;

import de.dontletyoudie.backend.persistence.proof.Proof;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("statService")
@RequiredArgsConstructor
public class StatService {

    private final StatRepository statRepository;

    public List<Stat> getStatsByMiniMeId(Long id) {
        return statRepository.findStatsByMiniMeId(id);
    }

    public void increaseStats(Proof proof) {
        Stat previously = statRepository.findStatByMiniMe_IdAndCategory_Id(
                proof.getAccount().getMiniMe().getId(),
                proof.getCategory().getId()
        );

        if (proof.getCategory().getEssential()) {
            previously.setPoints(100);
        } else {
            // If stat was 100 already, we just leave it, otherwise add  5
            int toSet = previously.getPoints() == 100 ? 100 : previously.getPoints() + 5;
            previously.setPoints(toSet);
        }

        statRepository.save(previously);
    }
}
