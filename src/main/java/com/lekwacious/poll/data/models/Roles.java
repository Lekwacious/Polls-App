package com.lekwacious.poll.data.models;

import com.lekwacious.poll.data.models.RoleName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;


@Data
@NoArgsConstructor
@Table(name = "roles")
@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private RoleName name;

    public Roles(RoleName name) {
        this.name = name;
    }


}
