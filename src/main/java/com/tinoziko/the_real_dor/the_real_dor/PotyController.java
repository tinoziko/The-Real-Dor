package com.tinoziko.the_real_dor.the_real_dor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PotyController {

    private final ApiFootballService apiService;
    private final PotyPredictionService predictionService;

    public PotyController(ApiFootballService apiService, PotyPredictionService predictionService) {
        this.apiService = apiService;
        this.predictionService = predictionService;
    }

    @GetMapping("/")
    public String getRankings(Model model) {
        System.out.println("----> GENERATING HARDCODED RANKINGS...");


        int targetSeason = 2024;


        int[] globalContenders = {278, 184, 386828, 1100, 1485, 44, 483, 153, 19617, 81013, 128384, 263482, 22224, 22090};

        List<PlayerStats> rankedPlayers = new ArrayList<>();

        for (int id : globalContenders) {
            try {

                PlayerStats liveStats = apiService.getLivePlayerStats(id, targetSeason);

                if (liveStats != null && liveStats.getTotalMinutes() > 0) {

                    double predictedScore = predictionService.predictScore(liveStats);
                    liveStats.setAiScore(predictedScore);

                    rankedPlayers.add(liveStats);
                    System.out.println("AI Scored: " + liveStats.getName() + " -> " + predictedScore);
                } else {
                    System.out.println("Filtered out ID " + id + " (Under 500 minutes or null data)");
                }
            } catch (Exception e) {
                System.out.println("Error predicting ID " + id + ": " + e.getMessage());
            }
        }

        rankedPlayers.sort((p1, p2) -> Double.compare(p2.getAiScore(), p1.getAiScore()));

        model.addAttribute("players", rankedPlayers);

        return "index";
    }
}