package yacht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Map;

@Table
@Entity(name = "game_sheet")
public class GameSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "yahtzee_game", nullable = false, updatable = false)
    @JsonIgnore
    private YahtzeeGame yahtzeeGame;

    @OneToMany(mappedBy = "gameSheet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "rollType")
    private Map<RollType, RollScore> rollScores;

    @Column(name = "top_section_subtotal", nullable = false)
    private Integer topSectionSubtotal = 0;

    @Column(name = "has_top_section_bonus", nullable = false)
    private Boolean hasTopSectionBonus = false;

    @Column(name = "top_section_score", nullable = false)
    private Integer topSectionScore = 0;

    @Column(name = "bottom_section_score", nullable = false)
    private Integer bottomSectionScore = 0;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YahtzeeGame getYahtzeeGame() {
        return yahtzeeGame;
    }
    public void setYahtzeeGame(YahtzeeGame yahtzeeGame) {
        this.yahtzeeGame = yahtzeeGame;
    }

    public Map<RollType, RollScore> getRollScores() {
        return rollScores;
    }
    public void setRollScores(Map<RollType, RollScore> rollScores) {
        this.rollScores = rollScores;
    }

    public Integer getTopSectionSubtotal() {
        return topSectionSubtotal;
    }
    public void setTopSectionSubtotal(Integer topSectionSubtotal) {
        this.topSectionSubtotal = topSectionSubtotal;
    }

    public Boolean getHasTopSectionBonus() {
        return hasTopSectionBonus;
    }
    public void setHasTopSectionBonus(Boolean hasTopSectionBonus) {
        this.hasTopSectionBonus = hasTopSectionBonus;
    }

    public Integer getTopSectionScore() {
        return topSectionScore;
    }
    public void setTopSectionScore(Integer topSectionScore) {
        this.topSectionScore = topSectionScore;
    }

    public Integer getBottomSectionScore() {
        return bottomSectionScore;
    }
    public void setBottomSectionScore(Integer bottomSectionScore) {
        this.bottomSectionScore = bottomSectionScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
