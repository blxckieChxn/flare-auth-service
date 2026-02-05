package org.mario.authservice.authservice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @UUID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true, nullable = false, length = 30)
    private String username;

    @Column (nullable = false)
    private String password;

    private boolean enabled = true;

    @Column (nullable = false, length = 30)
    private String firstName;

    @Column (nullable = false, length = 30)
    private String lastName;

    @Column (nullable = false, length = 50)
    private String email;

    //Puede ser buena idea añadir un campo prefix +34 - españa (rollo un Map o Set o algo vinculado a Country, otro posible campo)
    @Column (nullable = false, length = 9)
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
