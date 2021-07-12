package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
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

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/games")
    public Map<String , Object> getListaGames(Authentication authentication){
        Map<String , Object> dto = new LinkedHashMap<>();
        List<Game> games= gameRepository.findAll();
            if(authentication != null){
                Map<String , Object> dtoPlayer = new LinkedHashMap<>();
                Player player = playerRepository.findByUserName(authentication.getName());
                dtoPlayer.put("id", player.getId());
                dtoPlayer.put("email",player.getUserName());
                dto.put("player",dtoPlayer);
            }else{
                dto.put("player","Guest");
            }
        dto.put("games",games.stream().map(game -> makeGameDto( game)).collect(Collectors.toList()));
        return dto;
    }

    @GetMapping("/game_view/{id}")
    public Map<String, Object> getViewGame(@PathVariable Long id){
        Map<String , Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();
        dto = makeGameDto(game);
        dto.put("ships",gamePlayer.getShips().stream().map(ship->this.makeShip(ship)).collect(Collectors.toList()));
        dto.put("salvoes",gamePlayer.getGame().getGamePlayers().stream().flatMap(gp->gp.getSalvos().stream().map(salvo->makeSalvoDto(salvo))).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    private Map<String , Object> makeSalvoDto(Salvo salvo){
        Map<String , Object> dto =  new LinkedHashMap<>();
        dto.put("turn",salvo.getTurn());
        dto.put("player",salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations",salvo.getLocations());
        return dto;
    }

    private Map<String , Object> makeGameDto(Game game){
        Map<String , Object> dto =  new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created",game.getFechaCreacion());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gameP->makeGamePlayer(gameP)).collect(Collectors.toList()));
        dto.put("scores",game.getGamePlayers().stream().map(gp-> gp.getScoreDto()).collect(Collectors.toList()));
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

