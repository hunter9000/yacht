package yacht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table
@Entity(name="die_roll")
public class DieRoll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "yahtzee_game", nullable = false, updatable = false)
    @JsonIgnore
    private YahtzeeGame yahtzeeGame;

    @Column(name = "roll_value", nullable = false)
    private Integer value;

    @Column(name = "is_marked", nullable = false)
    private Boolean marked;

    @Column(name = "roll_order", nullable = false, updatable = false)
    private Integer order;


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

    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getMarked() {
        return marked;
    }
    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public Integer getOrder() {
        return order;
    }
    public void setOrder(Integer order) {
        this.order = order;
    }
}
