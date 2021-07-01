package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ShipRepository shipRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping("/games")
    public List<Object> getListaGames(){
        List<Game> games= gameRepository.findAll();
        return games.stream().map(game -> makeGameDto( game)).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{id}")
    public Map<String, Object> getViewGame(@PathVariable Long id){
        Map<String , Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();
        dto = makeGameDto(game);
        dto.put("ships",gamePlayer.getShips().stream().map(ship->this.makeShip(ship)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String , Object> makeGameDto(Game game){
        Map<String , Object> dto =  new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created",game.getFechaCreacion());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gameP->makeGamePlayer(gameP)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String,Object> makeGamePlayer(GamePlayer gamePlayer){
        Map<String , Object> dto =  new LinkedHashMap<>();
        dto.put("id",gamePlayer.getId());
        dto.put("player",makePlayer(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String,Object> makePlayer(Player player){
        Map<String , Object> dto = new LinkedHashMap<>();
        dto.put("id",player.getId());
        dto.put("email",player.getUserName());
        return dto;
    }

    private Map<String,Object> makeShip(Ship ship){
        Map<String , Object> dto =  new LinkedHashMap<>();
        dto.put("type",ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }

}

