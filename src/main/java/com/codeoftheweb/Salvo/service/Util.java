package com.codeoftheweb.Salvo.service;
import com.codeoftheweb.Salvo.models.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public static Map<String , Object> getHits(GamePlayer gp){
        Optional<GamePlayer> gpOpponent = getOpponent(gp.getGame(),gp.getPlayer());
        Map<String , Object> dto = new LinkedHashMap<>();
        dto.put("self",getHitsDamages(gp));
        dto.put("opponent",getHitsDamages(gpOpponent.get()));
        System.out.println("ENTRO OK : " + dto);
        return dto;

    }

    public static List<Map<String,Object>> getHitsDamages(GamePlayer gp){
        List<Map<String,Object>> map = new ArrayList<>();
        int[] acumuladorCantXposicion = new int[5];
        Arrays.fill(acumuladorCantXposicion,0,4,0);
        for (Salvo salvo: gp.getSalvos()) {
            Map<String , Object> dto = new LinkedHashMap<>();
            dto.put("turn",salvo.getTurn());
            List<String> hitsLocations =getHitLocations(gp,salvo);
            dto.put("hitLocations",hitsLocations);
            dto.put("damages",getDamages(gp,hitsLocations,acumuladorCantXposicion));
            dto.put("missed",getMissed(hitsLocations,salvo));
            map.add(dto);
        }
        return map;
    }

    public static List<String> getHitLocations(GamePlayer gp, Salvo salvo){
        List<String> hitLocations = new ArrayList<>();
        GamePlayer gpOpponent = getOpponent(gp.getGame(),gp.getPlayer()).get();
        List<Ship> shipsOpponent = gpOpponent.getShips().stream().collect(Collectors.toList());
        for(String salvoLocation : salvo.getSalvoLocations()){
            int i = 0;
            boolean encontro = false;
            while(i < shipsOpponent.size() && !encontro){
                int j = 0;
                List<String> shipLocations = shipsOpponent.get(i).getShipLocations();
                while(j < shipLocations.size() && !encontro){
                    if(salvoLocation.equals(shipLocations.get(j))){
                        hitLocations.add(salvoLocation);
                        encontro=true;
                    }else{
                        j++;
                    }
                }
                i++;
            }
        }
        return hitLocations;
    }

    public static Map<String , Object> getDamages(GamePlayer gp,List<String> hitsLocations,int[] acumuladorCantXposicion){
        GamePlayer gpOpponent = getOpponent(gp.getGame(),gp.getPlayer()).get();
        List<Ship> ships = gpOpponent.getShips().stream().collect(Collectors.toList());
        int[] cantXtipo = new int[5];
        Arrays.fill(cantXtipo,0,4,0);
        for(String location : hitsLocations){
            int i = 0;
            boolean encontro = false;
            while(i < ships.size() && !encontro){
                int j = 0;
                List<String> shipLocation = ships.get(i).getShipLocations();
                while(j < shipLocation.size() &&  !encontro){
                    if(location.equals(shipLocation.get(j))){
                        int posicion = ships.get(i).getType().ordinal();
                        cantXtipo[posicion]++;
                        encontro = true;
                    }else{
                        j++;
                    }
                }
                i++;
            }
        }
        for(int i = 0; i < cantXtipo.length ; i++){
            acumuladorCantXposicion[i] = acumuladorCantXposicion[i] + cantXtipo[i];
        }

        return getDamagesMap(cantXtipo,acumuladorCantXposicion);
    }

    public static Map<String , Object> getDamagesMap(int[] cantXtipo,int[] acumuladorCantXposicion){
        Map<String , Object> dto = new LinkedHashMap<>();
        dto.put("carrierHits", cantXtipo[0]);
        dto.put("battleshipHits", cantXtipo[1]);
        dto.put("submarineHits", cantXtipo[2]);
        dto.put("destroyerHits", cantXtipo[3]);
        dto.put("patrolboatHits", cantXtipo[4]);
        dto.put("carrier", acumuladorCantXposicion[0]);
        dto.put("battleship", acumuladorCantXposicion[1]);
        dto.put("submarine", acumuladorCantXposicion[2]);
        dto.put("destroyer", acumuladorCantXposicion[3]);
        dto.put("patrolboat", acumuladorCantXposicion[4]);

        return dto;
    }

    public static int getMissed(List<String> hitsLocations,Salvo salvo){
        return salvo.getSalvoLocations().size() - hitsLocations.size();
    }

}
