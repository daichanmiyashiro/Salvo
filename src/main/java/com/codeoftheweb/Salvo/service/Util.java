package com.codeoftheweb.Salvo.service;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Util {

    public static Optional<GamePlayer> getOpponent(Game game , Player playerAuthentication){
        Optional<GamePlayer> gamePlayerOpponent = game.getGamePlayers().stream().filter(gp -> gp.getPlayer().getId() != playerAuthentication.getId()).findFirst();
        return gamePlayerOpponent;
    }

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
