package de.dontletyoudie.backend.persistence.proof.exceptions;

public class ProofNotFoundException extends Exception {
    public ProofNotFoundException(long proofId) {
        super("Proof with ID " + proofId + " not found");
    }
}
