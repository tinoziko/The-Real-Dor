package com.tinoziko.the_real_dor.the_real_dor;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiFootballService {

    @Value("${api.football.key}")
    private String apiKey;

    @Value("${api.football.host}")
    private String apiHost;

    @Value("${api.football.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlayerStats getLivePlayerStats(int playerId, int season) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", apiKey);
            headers.set("x-rapidapi-host", apiHost);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = apiUrl + "?id=" + playerId + "&season=" + season;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);



            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode responseArray = root.path("response");

            if (responseArray.isEmpty() || responseArray.isNull()) {
                System.out.println("No data found for player ID: " + playerId);
                return null;
            }

            JsonNode firstResult = responseArray.get(0);
            JsonNode playerNode = firstResult.path("player");
            JsonNode statisticsArray = firstResult.path("statistics");

            String name = playerNode.path("name").asText();
            String pos = "FW";

            float minutes = 0;
            float goals = 0;
            float assists = 0;
            float keyPasses = 0;
            float interceptions = 0;
            float tackles = 0;
            float dribbles = 0;

            for (JsonNode stat : statisticsArray) {


                if (minutes == 0 && !stat.path("games").path("position").isMissingNode() && !stat.path("games").path("position").isNull()) {
                    String rawPos = stat.path("games").path("position").asText();
                    if (rawPos.equals("Attacker")) pos = "FW";
                    else if (rawPos.equals("Midfielder")) pos = "MF";
                    else pos = "DF";
                }


                minutes += (float) stat.path("games").path("minutes").asDouble(0);
                goals += (float) stat.path("goals").path("total").asDouble(0);
                assists += (float) stat.path("goals").path("assists").asDouble(0);
                keyPasses += (float) stat.path("passes").path("key").asDouble(0);
                interceptions += (float) stat.path("tackles").path("interceptions").asDouble(0);
                tackles += (float) stat.path("tackles").path("total").asDouble(0);
                dribbles += (float) stat.path("dribbles").path("success").asDouble(0);
            }

            if (minutes == 0) {
                return null;
            }

            float goalsP90 = (goals / minutes) * 90;
            float assistsP90 = (assists / minutes) * 90;
            float keyPassesP90 = (keyPasses / minutes) * 90;
            float interceptionsP90 = (interceptions / minutes) * 90;
            float tacklesP90 = (tackles / minutes) * 90;

            float expectedGoals = goals * 0.95f;
            float scaP90 = keyPassesP90 * 2.5f;
            float progPassesP90 = keyPassesP90 * 3.0f;
            float progCarriesP90 = (dribbles / minutes) * 90 * 2.0f;

            return new PlayerStats(name, pos, minutes, goals, assists, goalsP90, assistsP90,
                    expectedGoals, scaP90, keyPassesP90, interceptionsP90,
                    tacklesP90, progPassesP90, progCarriesP90);

        } catch (Exception e) {
            System.out.println("Error fetching live data for player " + playerId + ": " + e.getMessage());
            return null;
        }
    }
}