package de.dontletyoudie.backend.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.RelationshipService;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipAddDto;
import de.dontletyoudie.backend.persistence.relationship.dtos.RelationshipDto;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipNotFoundException;
import de.dontletyoudie.backend.persistence.relationship.exceptions.RelationshipStatusException;
import de.dontletyoudie.backend.security.filter.Filter;
import de.dontletyoudie.backend.security.filter.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
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

    @PathFilter(path={"/api/relationship/getFriends", "/api/relationship/getPendingFriendRequests"}, tokenRequired = true)
    public static boolean filterGetFriends(HttpServletRequest request, DecodedJWT token) {
        String queryString = request.getQueryString();

        return !queryString.substring("username=".length()).equals(token.getSubject());
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
            try {
                return new ResponseEntity<>(relationshipService.delete(relAccount, srcAccount), HttpStatus.OK);
            } catch (AccountNotFoundException | RelationshipNotFoundException | RelationshipStatusException e2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }

    @PathFilter(path="/api/relationship/accept", tokenRequired = true)
    public static boolean filterAccept(HttpServletRequest request, DecodedJWT token) {
        String queryString = request.getQueryString();

        int i = queryString.indexOf("&relAccount=") + "&relAccount=".length();
        return !queryString.substring(i).equals(token.getSubject());
    }
}

