package yacht;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import yacht.manager.RollScoringManager;
import yacht.model.DieRoll;
import yacht.model.RollType;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@ActiveProfiles("local")
public class RollScoringManagerTest {

    private final Logger logger = LoggerFactory.getLogger(RollScoringManagerTest.class);

    @Test
    public void testCardImageStitcher() {
        List<DieRoll> smallStraightDieRolls = new ArrayList<>();
        smallStraightDieRolls.add(makeDieRollWithValue(1));
        smallStraightDieRolls.add(makeDieRollWithValue(2));
        smallStraightDieRolls.add(makeDieRollWithValue(3));
        smallStraightDieRolls.add(makeDieRollWithValue(4));
        smallStraightDieRolls.add(makeDieRollWithValue(6));

        RollScoringManager rollScoringManager = new RollScoringManager(smallStraightDieRolls);

        Integer score = rollScoringManager.calculateScore(RollType.SMALL_STRAIGHT);

        Assert.assertEquals("Score not correct for small straight", 30, score.intValue());
    }

    private DieRoll makeDieRollWithValue(Integer value) {
        DieRoll dieRoll = new DieRoll();
        dieRoll.setValue(value);
        dieRoll.setMarked(true);
        return dieRoll;
    }
}
