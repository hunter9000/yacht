package yacht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import yacht.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table
@Entity(name = "yahtzee_game")
public class YahtzeeGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false, updatable = false)
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "yahtzeeGame", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private GameSheet gameSheet;

    @OneToMany(mappedBy = "yahtzeeGame", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DieRoll> dieRolls;

    @Column(name = "num_rolls", nullable = false)
    private Integer numRolls = 1;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "start_date", nullable = false, updatable = false)
    private Date startDate = new Date();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public GameSheet getGameSheet() {
        return gameSheet;
    }
    public void setGameSheet(GameSheet gameSheet) {
        this.gameSheet = gameSheet;
    }

    public Set<DieRoll> getDieRolls() {
        return dieRolls;
    }
    public void setDieRolls(Set<DieRoll> dieRolls) {
        this.dieRolls = dieRolls;
    }

    public Integer getNumRolls() {
        return numRolls;
    }
    public void setNumRolls(Integer numRolls) {
        this.numRolls = numRolls;
    }

    public Boolean getCompleted() {
        return completed;
    }
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
