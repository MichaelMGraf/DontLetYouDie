package de.dontletyoudie.backend.persistence.minime;

import de.dontletyoudie.backend.persistence.stat.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("miniMeService")
@RequiredArgsConstructor
public class MiniMeService {

    private final MiniMeRepository miniMeRepository;
    private final StatService statService;

    public MiniMe createMiniMe () {
        MiniMe miniMe = miniMeRepository.save(new MiniMe(Skin.DEFAULT));
        statService.initializeStats(miniMe);
        return miniMe;
    }

    public void deleteMiniMe(MiniMe minime) {
        miniMeRepository.delete(minime);
    }
}
