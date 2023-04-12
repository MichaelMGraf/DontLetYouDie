package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.RelationshipStatus;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


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
        relationshipService.save(relationshipAddDto);
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
}
