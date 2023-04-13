package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;




@RestController
@RequestMapping("/api/relationship")
public class RelationshipController {

    private final RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    /**
     * @param relationshipAddDto Json in form of RelationshipAddDto containing the needed information to create a new Relationship.
     */
    @PostMapping(path = "/add")
    public ResponseEntity<String> add(@RequestBody RelationshipAddDto relationshipAddDto) {
        try {
            relationshipService.save(relationshipAddDto);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>("Friend request sent successfully", HttpStatus.CREATED);
    }

    /**
     * @param username Username for which pending friend requests are being queried.
     * @return List<RelationshipDto> List of incoming friend requests pending if any exist, else 204 no content
     */
    @GetMapping(path = "/getPendingFriendRequests")
    public ResponseEntity<List<RelationshipDto>> getPendingFriendRequests(@RequestParam (value="username") String username) {

        List<RelationshipDto> relationships;
        try {
            relationships = relationshipService.getPendingFriendRequests(username);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (relationships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(relationships, HttpStatus.OK);
        }
    }

    /**
     * @param username Username for which pending friend requests are being queried.
     * @return List<RelationshipDto> List of friends if any exist, else 204 no content
     */
    @GetMapping(path = "/getFriends")
    public ResponseEntity<List<RelationshipDto>> getFriends(@RequestParam (value="username") String username) {

        List<RelationshipDto> relationships;

        try {
            relationships = relationshipService.getFriends(username);
        }
        catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (relationships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(relationships, HttpStatus.OK);
        }
    }

    @PutMapping(path = "/accept")
    public ResponseEntity<RelationshipDto> accept(@RequestParam(value = "srcAccount") String srcAccount,
                                                  @RequestParam(value = "relAccount") String relAccount) {

        try {
            return new ResponseEntity<>(relationshipService.accept(srcAccount, relAccount), HttpStatus.OK);
        } catch (AccountNotFoundException | RelationshipNotFoundException | RelationshipStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "srcAccount") String srcAccount,
                                                  @RequestParam(value = "relAccount") String relAccount) {

        try {
            return new ResponseEntity<>(relationshipService.delete(srcAccount, relAccount), HttpStatus.OK);
        } catch (AccountNotFoundException | RelationshipNotFoundException | RelationshipStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

