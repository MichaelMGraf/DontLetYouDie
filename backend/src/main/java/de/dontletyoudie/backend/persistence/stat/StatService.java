package de.dontletyoudie.backend.persistence.stat;

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
}
