package yacht.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import yacht.model.YahtzeeGame;

import java.util.List;

@Repository
@Qualifier(value = "userRepository")
public interface YahtzeeGameRepository extends CrudRepository<YahtzeeGame, Long> {
    List<YahtzeeGame> findByUserId(Long userId);
}
