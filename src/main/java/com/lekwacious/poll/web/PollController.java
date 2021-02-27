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

    @PostMapping
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
