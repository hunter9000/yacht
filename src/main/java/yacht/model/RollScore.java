package yacht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table
@Entity(name = "roll_score")
public class RollScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_sheet", nullable = false, updatable = false)
    @JsonIgnore
    private GameSheet gameSheet;

    @Column(name = "roll_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private RollType rollType;

    @Column(name = "chosen", nullable = false)
    private Boolean chosen = false;

    @Column(name = "score")
    private Integer score;

    /** The score potential from the current roll */
    @Transient
    private Integer potentialScore;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public GameSheet getGameSheet() {
        return gameSheet;
    }
    public void setGameSheet(GameSheet gameSheet) {
        this.gameSheet = gameSheet;
    }

    public RollType getRollType() {
        return rollType;
    }
    public void setRollType(RollType rollType) {
        this.rollType = rollType;
    }

    public Boolean getChosen() {
        return chosen;
    }
    public void setChosen(Boolean chosen) {
        this.chosen = chosen;
    }

    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getPotentialScore() {
        return potentialScore;
    }
    public void setPotentialScore(Integer potentialScore) {
        this.potentialScore = potentialScore;
    }
}
