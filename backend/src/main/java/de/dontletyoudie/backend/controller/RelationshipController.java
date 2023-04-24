package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.dtos.FriendReturnDTO;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import de.dontletyoudie.backend.security.filter.Filter;
import de.dontletyoudie.backend.security.filter.FilterData;
import de.dontletyoudie.backend.security.filter.PathFilter;
import de.dontletyoudie.backend.security.filter.PathFilterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;



@Filter
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
     * @return List<FriendReturnDTO> List of incoming friend requests pending if any exist, else 204 no content
     */
    @GetMapping(path = "/getPendingFriendRequests")
    public ResponseEntity<FriendReturnDTO> getPendingFriendRequests(@RequestParam (value="username") String username) {

        List<String> relationships;
        try {
            relationships = relationshipService.getPendingFriendRequests(username);
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (relationships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>((new FriendReturnDTO(relationships)), HttpStatus.OK);
        }
    }

    /**
     * @param username Username for which pending friend requests are being queried.
     * @return List<FriendReturnDTO> List of friends if any exist, else 204 no content
     */
    @GetMapping(path = "/getFriends")
    public ResponseEntity<FriendReturnDTO> getFriends(@RequestParam (value="username") String username) {

        List<String> friends;

        try {
            friends = relationshipService.getFriends(username);
        }
        catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if (friends.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>((new FriendReturnDTO(friends)), HttpStatus.OK);
        }
    }

    @PathFilter(path={"/api/relationship/getFriends", "/api/relationship/getPendingFriendRequests"}, tokenRequired = true)
    public static PathFilterResult filterGetFriends(FilterData data) {
        if (data.getRequest().getParameter("username").equals(data.getToken().getSubject()))
            return PathFilterResult.getNotDenied();
        return PathFilterResult.getAccessDenied("username does not match Token subject");
    }

    @PutMapping(path = "/accept")
    public ResponseEntity<Void> accept(@RequestParam(value = "srcAccount") String srcAccount,
                                         @RequestParam(value = "relAccount") String relAccount) {
        try {
            relationshipService.accept(srcAccount, relAccount);
        } catch (AccountNotFoundException | RelationshipNotFoundException | RelationshipStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "srcAccount") String srcAccount,
                                         @RequestParam(value = "relAccount") String relAccount) {

        try {
            relationshipService.delete(srcAccount, relAccount);
        } catch (AccountNotFoundException | RelationshipNotFoundException | RelationshipStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PathFilter(path={"/api/relationship/accept", "/api/relationship/delete"}, tokenRequired = true)
    public static PathFilterResult filterAccept(FilterData data) {
        if (data.getRequest().getParameter("srcAccount").equals(data.getToken().getSubject()))
            return PathFilterResult.getNotDenied();
        return PathFilterResult.getAccessDenied("srcAccount does not match Token subject");

    }
}

