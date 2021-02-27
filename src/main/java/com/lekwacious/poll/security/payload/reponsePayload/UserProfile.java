package com.lekwacious.poll.security.payload.reponsePayload;

import com.lekwacious.poll.data.audit.DateAudit;
import com.lekwacious.poll.data.audit.UserDateAudit;
import lombok.Data;
import java.time.Instant;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String firstName;
    private Instant joinedAt;
    private Long pollCount;
    private Long voteCount;

    public UserProfile(Long id, String username, String firstName, Instant joinedAt, Long pollCount, Long voteCount) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.joinedAt = joinedAt;
        this.pollCount = pollCount;
        this.voteCount = voteCount;
    }

}
