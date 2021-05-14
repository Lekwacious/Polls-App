package com.lekwacious.poll.web;

import com.lekwacious.poll.data.models.Poll;
import com.lekwacious.poll.data.repository.PollRepository;
import com.lekwacious.poll.data.repository.UserRepository;
import com.lekwacious.poll.data.repository.VoteRepository;
import com.lekwacious.poll.security.CurrentUser;
import com.lekwacious.poll.security.UserPrincipal;
import com.lekwacious.poll.security.payload.reponsePayload.ApiResponse;
import com.lekwacious.poll.security.payload.reponsePayload.PagedResponse;
import com.lekwacious.poll.security.payload.reponsePayload.PollResponse;
import com.lekwacious.poll.security.payload.requestPayload.PollRequest;
import com.lekwacious.poll.security.payload.requestPayload.VoteRequest;
import com.lekwacious.poll.service.PollService;
import com.lekwacious.poll.utils.AppConstants;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/polls")
@ApiResponses(value ={
        @io.swagger.annotations.ApiResponse(code=400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code=401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code=500, message = "The server is down. Please bear with us."),


}
)
public class PollController {
    @Autowired
    PollService pollService;

    @Autowired
    PollRepository pollRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
                                                ){
        return pollService.getAllPolls(currentUser,  page,  size);

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest){
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();
        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll created Successfully"));
    }

    @GetMapping("/{pollId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long pollId){
        return pollService.getPollById(pollId, currentUser);
    }
    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long pollId,
                                 @Valid @RequestBody VoteRequest voteRequest){
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
    }
}
