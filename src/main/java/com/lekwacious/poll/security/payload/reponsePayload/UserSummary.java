package com.lekwacious.poll.security.payload.reponsePayload;

import lombok.Data;

@Data
public class UserSummary {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    public UserSummary(Long id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
