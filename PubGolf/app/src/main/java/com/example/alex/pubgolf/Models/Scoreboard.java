package com.example.alex.pubgolf.Models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Lasse Priebe on 30/05/2017.
 */

public class Scoreboard {

    public static ArrayList<Score> calculateScoresForGame(Game game) {

        HashMap<String, Score> hashMap = new HashMap<>();

        // Initialize the hash map with zero scores for each player.
        for (Player player : game.Players.values()) {

            Score score = new Score(player, (long) 0);
            hashMap.put(player.UUID, score);
        }

        // Aggregate scores for each hole, put maxScore if score is missing.
        for (Hole hole : game.Holes) {

            // Do not calculate score for holes in the future.
            if (game.State != Game.GameState.Completed && hole.Index >= game.HoleIndex) {
                continue;
            }

            Set<String> remainingKeys = game.Players.keySet();

            if (hole.Scores != null) {
                for (Score score : hole.Scores.values()) {

                    String playerId = score.Player.UUID;
                    remainingKeys.remove(playerId);

                    Score playerScore = hashMap.get(playerId);
                    playerScore.Value += score.Value;
                }
            }

            // Add max score for all unregistered scores.
            for (String key : remainingKeys) {

                Score playerScore = hashMap.get(key);
                long maxScore = 10;
                playerScore.Value += maxScore;
            }
        }

        return new ArrayList<>(hashMap.values());
    }
}
