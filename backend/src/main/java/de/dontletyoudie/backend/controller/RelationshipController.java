package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipShowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

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
     * @return List<Proof> Instance of the pending friend requests if any exist, else just 204 no content
     */
    @GetMapping(path = "/getPending")
    public ResponseEntity<HashMap<HashMap<String, String>, RelationshipStatus>> getPending(@RequestParam (value="username") String username) {
        //TODO: Only return pending outgoing friend requests
        HashMap<HashMap<String, String>, RelationshipStatus> relationships = relationshipService.getPending(username);

        if (relationships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(relationships, HttpStatus.OK);
        }
    }

    //TODO: Implement a route which returns the homies

    @PutMapping(path = "/accept")
    public ResponseEntity<RelationshipDto> accept(@RequestParam(value = "srcAccount") String srcAccount,
                                                  @RequestParam(value = "relAccount") String relAccount) {

        try {
            return new ResponseEntity<>(relationshipService.accept(srcAccount, relAccount), HttpStatus.OK);
        } catch (AccountNotFoundException | RelationshipNotFoundException | RelationshipStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * @param username Username for which pending friend requests are being queried.
     * @return List<RelationshipShowDTO> List of incoming friend requests pending if any exist, else 204 no content
     */
    @GetMapping(path = "/getPendingFriendRequests")
    public ResponseEntity<List<RelationshipShowDTO>> getPendingFriendRequests(@RequestParam (value="username") String username) {
        List<RelationshipShowDTO> relationships = relationshipService.getPendingFriendRequests(username);

        if (relationships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(relationships, HttpStatus.OK);
        }
    }

    /**
     * @param username Username for which pending friend requests are being queried.
     * @return List<RelationshipShowDTO> List of friends if any exist, else 204 no content
     */
    @GetMapping(path = "/getFriends")
    public ResponseEntity<List<RelationshipShowDTO>> getFriends(@RequestParam (value="username") String username) {
        List<RelationshipShowDTO> relationships = relationshipService.getFriends(username);

        if (relationships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(relationships, HttpStatus.OK);
        }
    }

}
