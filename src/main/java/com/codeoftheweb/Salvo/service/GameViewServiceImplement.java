package com.codeoftheweb.Salvo.service;
import com.codeoftheweb.Salvo.dtos.*;
import com.codeoftheweb.Salvo.models.*;
import com.codeoftheweb.Salvo.repository.ScoreRepository;
import com.codeoftheweb.Salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameViewServiceImplement implements GameViewService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public GameViewDTO makeGameViewDTO(Game game, GamePlayer gamePlayer) {
        State state = getState(gamePlayer);
        Optional<GamePlayer> gpOpponent = Util.getOpponent(gamePlayer.getGame(),gamePlayer.getPlayer());

        if((state == State.WON ||state == State.TIE ||state == State.LOST) && gamePlayer.getScoreDto().getScore() == null){
            if(state == State.WON){
                scoreRepository.save(new Score(gamePlayer.getPlayer(),gamePlayer.getGame(),1.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
                scoreRepository.save(new Score(gpOpponent.get().getPlayer(),gpOpponent.get().getGame(),0.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
            }
            if(state == State.TIE){
                scoreRepository.save(new Score(gamePlayer.getPlayer(),gamePlayer.getGame(),0.5, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
                scoreRepository.save(new Score(gpOpponent.get().getPlayer(),gpOpponent.get().getGame(),0.5, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
            }
            if(state == State.LOST){
                scoreRepository.save(new Score(gamePlayer.getPlayer(),gamePlayer.getGame(),0.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
                scoreRepository.save(new Score(gpOpponent.get().getPlayer(),gpOpponent.get().getGame(),1.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
            }
        }

        Map<String , Object> hits = getHits(gamePlayer);
        HitDTO hitDTO = new HitDTO(hits);

        GameViewDTO gameViewDTO = new GameViewDTO();
        gameViewDTO.setId(game.getId());
        gameViewDTO.setCreated(game.getFechaCreacion());
        gameViewDTO.setGameState(state);
        gameViewDTO.setGamePlayers(game.getGamePlayers().stream().map(gp -> new GamePlayerDTO(gp)).collect(Collectors.toSet()));
        gameViewDTO.setShips(gamePlayer.getShips().stream().map(ship->new ShipDTO(ship)).collect(Collectors.toSet()));
        gameViewDTO.setSalvoes(game.getGamePlayers().stream().map(gp -> gp.getSalvos()).flatMap(salvos -> salvos.stream()).map(salvo -> new SalvoDTO(salvo)).collect(Collectors.toSet()));
        gameViewDTO.setHits(hitDTO);

        return gameViewDTO;
    }

    public Map<String , Object> getHits(GamePlayer gp){
        Optional<GamePlayer> gpOpponent = Util.getOpponent(gp.getGame(),gp.getPlayer());
        Map<String , Object> dto = new LinkedHashMap<>();
        if(gpOpponent.isPresent()){
            dto.put("self",getHitsDamages(gpOpponent.get()));
            dto.put("opponent",getHitsDamages(gp));
        }else{
            dto.put("self",new ArrayList<>());
            dto.put("opponent",new ArrayList<>());
        }
        return dto;
    }

    private List<Map<String,Object>> getHitsDamages(GamePlayer gp){
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

    private List<String> getHitLocations(GamePlayer gp, Salvo salvo){

        GamePlayer gpOpponent = Util.getOpponent(gp.getGame(),gp.getPlayer()).get();
        List<String> shipLocationOpponent = gpOpponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
        List<String> hitLocations = salvo.getSalvoLocations().stream().filter(salvoLocation -> shipLocationOpponent.contains(salvoLocation)).collect(Collectors.toList());
        return hitLocations;
    }

    private Map<String , Object> getDamages(GamePlayer gp,List<String> hitsLocations,int[] acumuladorCantXposicion){
        GamePlayer gpOpponent = Util.getOpponent(gp.getGame(),gp.getPlayer()).get();
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

    private Map<String , Object> getDamagesMap(int[] cantXtipo,int[] acumuladorCantXposicion){
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

    private int getMissed(List<String> hitsLocations,Salvo salvo){
        return salvo.getSalvoLocations().size() - hitsLocations.size();
    }


    private State getState(GamePlayer gp){
        if(gp.getShips().size() != 0){
            Optional<GamePlayer> gpOpponent = Util.getOpponent(gp.getGame(),gp.getPlayer());

            if(!gpOpponent.isPresent()){
                return State.WAITINGFOROPP;
            }
            if(gpOpponent.get().getShips().size() == 0){
                return State.WAIT;
            }

            List<String> listHitLocations = gp.getSalvos().stream().flatMap(salvo -> getHitLocations(gp,salvo).stream()).collect(Collectors.toList());
            List<String> shipsLocationsOpponent = gpOpponent.get().getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            boolean isShipsSunk = listHitLocations.size() == shipsLocationsOpponent.size();

            List<String> listHitLocationsOpponent = gpOpponent.get().getSalvos().stream().flatMap(salvo -> getHitLocations(gpOpponent.get(),salvo).stream()).collect(Collectors.toList());
            List<String> shipsLocationsSelf = gp.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            boolean isShipsSunkOpponent = listHitLocationsOpponent.size() == shipsLocationsSelf.size();

            if(gp.getSalvos().size() == gpOpponent.get().getSalvos().size()){
                if(gpOpponent.get().getShips().size() > 0){
                    if( isShipsSunk && !isShipsSunkOpponent ){
                        return State.WON;
                    }
                    if(isShipsSunk && isShipsSunkOpponent){
                        return State.TIE;
                    }
                    if( !isShipsSunk && isShipsSunkOpponent ){
                        return State.LOST;
                    }
                }

            }
            if(gp.getSalvos().size() > gpOpponent.get().getSalvos().size()){
                return State.WAIT;
            }else{

                return State.PLAY;
            }
        }
        return State.PLACESHIPS;
    }
}
