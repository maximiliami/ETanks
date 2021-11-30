package thl.gruppef.etankrest.etankrestapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thl.gruppef.etankrest.etankrestapi.entities.GameStatistic;
import thl.gruppef.etankrest.etankrestapi.repository.GameStatisticRepository;

import java.util.List;

@RestController
@RequestMapping("/user_game_statistic")
public class GameStatisticController {
    private GameStatisticRepository userGameStatisticRepository;

    public GameStatisticController(GameStatisticRepository userGameStatisticRepository) {
        this.userGameStatisticRepository = userGameStatisticRepository;
    }

    @GetMapping("")
    public List<GameStatistic> index() {
        return userGameStatisticRepository.findAll();
    }

}