package de.dontletyoudie.backend.persistence.proof;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface ProofRepository extends JpaRepository<Proof, Long> {

    Optional<ArrayList<Proof>> findProofsByUsername(String username);

    Optional<Proof> findProofByUsername(String username);

}