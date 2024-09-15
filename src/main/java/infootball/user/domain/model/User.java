package infootball.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;



    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ElementCollection
    @CollectionTable(
            name = "user_competitions",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "competition_id")
    private List<Long> competitions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "user_teams",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "team_id")
    private List<Long> teams = new ArrayList<>();


}
