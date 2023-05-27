package de.dontletyoudie.backend.controller;

import de.dontletyoudie.backend.persistence.account.AccountService;
import de.dontletyoudie.backend.persistence.account.exceptions.AccountNotFoundException;
import de.dontletyoudie.backend.persistence.stat.Stat;
import de.dontletyoudie.backend.persistence.stat.StatService;
import de.dontletyoudie.backend.security.filter.Filter;
import de.dontletyoudie.backend.security.filter.FilterData;
import de.dontletyoudie.backend.security.filter.PathFilter;
import de.dontletyoudie.backend.security.filter.PathFilterResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@Filter
@RestController
@RequestMapping("/api/stats")
public class StatController {

    private final StatService statService;
    private final AccountService accountService;

    public StatController(StatService statService, AccountService accountService) {
        this.statService = statService;
        this.accountService = accountService;
    }

    /**
     * @param username Username for which pending friend requests are being queried.
     * @return HashMap<String, Integer> Hashmap containing breakdown of points the user has in each stat-category
     */
    @GetMapping(path = "/getStats")
    public ResponseEntity<HashMap<String, Integer>> getFriends(@RequestParam(value="username") String username) {

        Long miniMeId;

        try {
            miniMeId = accountService.getAccount(username).getMiniMe().getId();
        }
        catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        List<Stat> stats = statService.getStatsByMiniMeId(miniMeId);
        HashMap<String, Integer> statMap = new HashMap<>();

        stats.forEach(stat -> statMap.put(stat.getCategory().getName(), stat.getPoints()));

        return new ResponseEntity<>(statMap, HttpStatus.OK);
    }

    @PathFilter(path={"/api/stats/getStats"}, tokenRequired = true)
    public static PathFilterResult filterGetFriends(FilterData data) {
        return data.getRequest().getParameter("username").equals(data.getToken().getSubject())
                ? PathFilterResult.getNotDenied()
                : PathFilterResult.getAccessDenied("username does not match Token subject");
    }
}
