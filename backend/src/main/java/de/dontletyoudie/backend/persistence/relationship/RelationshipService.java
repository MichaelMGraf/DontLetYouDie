package de.dontletyoudie.backend.persistence.relationship;

import de.dontletyoudie.backend.persistence.account.Account;
import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipShowDTO;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipShowDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("relationshipService")
@RequiredArgsConstructor
public class RelationshipService {

    private final AccountService accountService;
    private final RelationshipRepository relationshipRepository;

    public Relationship save(RelationshipAddDto relationshipAddDto) throws AccountNotFoundException {
        Account srcAccount = accountService.getAccount(relationshipAddDto.getSrcUsername());
        Account relAccount = accountService.getAccount(relationshipAddDto.getRelUsername());

        Relationship returnRelationship = new Relationship();

        if (relationshipRepository.findRelationshipBySrcAccountAndRelAccount(srcAccount, relAccount).isPresent()) {
            //TODO Behavior is a bit weird atm, but afaik we want to remodel adding friends anyways to have a proper search function, then this case will never happen
            return returnRelationship;
        } else {
            return relationshipRepository.save(new Relationship(
                    srcAccount,
                    relAccount,
                    RelationshipStatus.PENDING
            ));
        }
    }

    public List<RelationshipShowDTO> getPendingFriendRequests(String username) {

        Account account = accountService.getAccount(username);

        // Find relationships initiated by the account
        Optional<List<Relationship>> relationships = relationshipRepository.findRelationshipsByRelAccount(account);

        List<RelationshipShowDTO> relationshipShowDTOs = new ArrayList<>();

        if (relationships.isPresent()) {
            relationships.ifPresent(relationshipsToExtract -> extractPendingFriendRequests(relationshipsToExtract, relationshipShowDTOs));
        }

        return relationshipShowDTOs;
    }

    public List<RelationshipShowDTO> getFriends(String username) {

        Account account = accountService.getAccount(username);

        // Find relationships either initiated by the account or received by the account
        Optional<List<Relationship>> relationshipsSource = relationshipRepository.findRelationshipsBySrcAccount(account);
        Optional<List<Relationship>> relationshipsRelated = relationshipRepository.findRelationshipsByRelAccount(account);

        List<RelationshipShowDTO> relationshipShowDTOs = new ArrayList<>();

        if (relationshipsSource.isPresent() && relationshipsRelated.isPresent()) {
            relationshipsSource.get().addAll(relationshipsRelated.get());
            extractFriends(relationshipsSource.get(), relationshipShowDTOs, username);
        } else if (relationshipsSource.isPresent()) {
            relationshipsSource.ifPresent(relationships -> extractFriends(relationshipsSource.get(), relationshipShowDTOs, username));
        } else if (relationshipsRelated.isPresent()) {
            relationshipsRelated.ifPresent(relationships -> extractFriends(relationshipsRelated.get(), relationshipShowDTOs, username));
        }

        return relationshipShowDTOs;
    }


    private static void extractPendingFriendRequests(List<Relationship> relationships, List<RelationshipShowDTO> relationshipShowDTOs) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.PENDING) {
                relationshipShowDTOs.add(new RelationshipShowDTO(relationship));
            }
        }
    }

    private static void extractFriends(List<Relationship> relationships, List<RelationshipShowDTO> relationshipShowDTOs, String username) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.FRIEND) {
                // If the username whose friends are being queried is the originator of the relationship, just add the relationship
                if (relationship.getSrcAccount().getUsername().equals(username)) {
                    relationshipShowDTOs.add(new RelationshipShowDTO(relationship));
                } else {
                    // If he isn't, swap them for convenience in the frontend
                    relationshipShowDTOs.add(new RelationshipShowDTO(
                            relationship.getRelAccount().getUsername(),
                            relationship.getSrcAccount().getUsername(),
                            relationship.getRelationshipStatus()));
                }
            }
        }
    }

    public Optional<Relationship> getRelationship(String srcAccount, String relAccount) throws AccountNotFoundException {
        return relationshipRepository.findRelationshipBySrcAccountAndRelAccount(accountService.getAccount(srcAccount),
                accountService.getAccount(relAccount));
    }

    public RelationshipDto accept(String srcAccount, String relAccount) throws AccountNotFoundException, RelationshipNotFoundException, RelationshipStatusException {
        Optional<Relationship> relationshipO = getRelationship(srcAccount, relAccount);
        if (relationshipO.isEmpty()) throw new RelationshipNotFoundException(srcAccount, relAccount);

        Relationship relationship = relationshipO.get();
        if (relationship.getRelationshipStatus() != RelationshipStatus.PENDING)
            throw new RelationshipStatusException(srcAccount, relAccount, relationship.getRelationshipStatus(), RelationshipStatus.PENDING);

        relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
        relationshipRepository.save(relationship);

        return new RelationshipDto(relationship);
    }

    public List<RelationshipShowDTO> getPendingFriendRequests(String username) {

        Account account = accountService.getAccount(username);

        // Find relationships initiated by the account
        Optional<List<Relationship>> relationships = relationshipRepository.findRelationshipsByRelAccount(account);

        List<RelationshipShowDTO> relationshipShowDTOs = new ArrayList<>();

        if (relationships.isPresent()) {
            relationships.ifPresent(relationshipsToExtract -> extractPendingFriendRequests(relationshipsToExtract, relationshipShowDTOs));
        }

        return relationshipShowDTOs;
    }

    public List<RelationshipShowDTO> getFriends(String username) {

        Account account = accountService.getAccount(username);

        // Find relationships either initiated by the account or received by the account
        Optional<List<Relationship>> relationshipsSource = relationshipRepository.findRelationshipsBySrcAccount(account);
        Optional<List<Relationship>> relationshipsRelated = relationshipRepository.findRelationshipsByRelAccount(account);

        List<RelationshipShowDTO> relationshipShowDTOs = new ArrayList<>();

        if (relationshipsSource.isPresent() && relationshipsRelated.isPresent()) {
            relationshipsSource.get().addAll(relationshipsRelated.get());
            extractFriends(relationshipsSource.get(), relationshipShowDTOs, username);
        } else if (relationshipsSource.isPresent()) {
            relationshipsSource.ifPresent(relationships -> extractFriends(relationshipsSource.get(), relationshipShowDTOs, username));
        } else if (relationshipsRelated.isPresent()) {
            relationshipsRelated.ifPresent(relationships -> extractFriends(relationshipsRelated.get(), relationshipShowDTOs, username));
        }

        return relationshipShowDTOs;
    }


    private static void extractPendingFriendRequests(List<Relationship> relationships, List<RelationshipShowDTO> relationshipShowDTOs) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.PENDING) {
                relationshipShowDTOs.add(new RelationshipShowDTO(relationship));
            }
        }
    }

    private static void extractFriends(List<Relationship> relationships, List<RelationshipShowDTO> relationshipShowDTOs, String username) {
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipStatus() == RelationshipStatus.FRIEND) {
                // If the username whose friends are being queried is the originator of the relationship, just add the relationship
                if (relationship.getSrcAccount().getUsername().equals(username)) {
                    relationshipShowDTOs.add(new RelationshipShowDTO(relationship));
                } else {
                    // If he isn't, swap them for convenience in the frontend
                    relationshipShowDTOs.add(new RelationshipShowDTO(
                            relationship.getRelAccount().getUsername(),
                            relationship.getSrcAccount().getUsername(),
                            relationship.getRelationshipStatus()));
                }
            }
        }
    }
}
