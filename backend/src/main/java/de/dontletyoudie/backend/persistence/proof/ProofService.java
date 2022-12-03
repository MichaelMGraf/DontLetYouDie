package de.dontletyoudie.backend.persistence.proof;

import de.dontletyoudie.backend.persistence.proof.dtos.ProofAddDto;
import de.dontletyoudie.backend.persistence.proof.dtos.ProofReturnDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("proofService")
@RequiredArgsConstructor
public class ProofService {

    private final ProofRepository proofRepository;

    public Optional<ArrayList<ProofReturnDto>> getPendingProofs(String username) {

        List<Proof> proofs = proofRepository.findProofsByUsername(username);
        ArrayList<ProofReturnDto> proofReturnDto = new ArrayList<>();

        if (proofs.isEmpty()) {
            return Optional.empty();
        } else {
            for (Proof proof : proofs) {
                proofReturnDto.add(new ProofReturnDto(
                        proof.getUsername(),
                        proof.getImage(),
                        proof.getDateTime(),
                        proof.getCategory(),
                        proof.getComment(),
                        proof.getAvgScore(),
                        proof.getJudgements()
                ));
            }
            return Optional.of(proofReturnDto);
        }
    }

    public Proof saveProof(ProofAddDto proofAddDto) {
        return proofRepository.save(new Proof(
                proofAddDto.getUsername(),
                proofAddDto.getCategory(),
                proofAddDto.getImage(),
                proofAddDto.getComment(),
                proofAddDto.getCreationDate(),
                0,
                0
        ));
    }
}
