package com.lekwacious.poll.web;

import com.lekwacious.poll.data.models.User;
import com.lekwacious.poll.data.repository.PollRepository;
import com.lekwacious.poll.data.repository.UserRepository;
import com.lekwacious.poll.data.repository.VoteRepository;
import com.lekwacious.poll.exception.ResourceNotFoundException;
import com.lekwacious.poll.security.CurrentUser;
import com.lekwacious.poll.security.UserPrincipal;
import com.lekwacious.poll.security.payload.reponsePayload.*;
import com.lekwacious.poll.service.PollService;
import com.lekwacious.poll.utils.AppConstants;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
@ApiResponses(value ={
        @io.swagger.annotations.ApiResponse(code=400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code=401, message = "Due to security constraints, your access request cannot be authorized"),
        @ApiResponse(code=500, message = "The server is down. Please bear with us."),


}
)
public class UserController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser){
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName());
        return userSummary;
    }
    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username){
        Boolean isAvailable =! userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public  UserIdentityAvailability checkEmailAvailability(@RequestParam("email") String email){
        Boolean isAvailable =! userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User","username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile =new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getCreateAt(), pollCount, voteCount);

        return userProfile;
    }
    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse>getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                        @CurrentUser UserPrincipal currentUser,
                                                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return pollService.getPollsCreatedBy(username, currentUser, page, size);

    }

    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }
}
