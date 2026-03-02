package org.mario.authservice.authservice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

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

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
