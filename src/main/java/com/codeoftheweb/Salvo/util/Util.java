package com.codeoftheweb.Salvo.util;
import com.codeoftheweb.Salvo.models.*;
import java.util.*;

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



