package de.dontletyoudie.backend.persistence.proof;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProofRepository extends JpaRepository<Proof, Long> {

    List<Proof> findProofsByUsername(String username);

}