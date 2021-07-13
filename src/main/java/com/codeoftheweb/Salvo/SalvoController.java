package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public  ResponseEntity<Object> getViewGame(@PathVariable Long id,Authentication authentication){
        Map<String , Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();

        Player playerAuthentication = playerRepository.findByUserName(authentication.getName());
        if(gamePlayer.getPlayer().getId() == playerAuthentication.getId()){
            dto = makeGameDto(game);
            dto.put("ships",gamePlayer.getShips().stream().map(ship->this.makeShip(ship)).collect(Collectors.toList()));
            dto.put("salvoes",gamePlayer.getGame().getGamePlayers().stream().flatMap(gp->gp.getSalvos().stream().map(salvo->makeSalvoDto(salvo))).collect(Collectors.toList()));
            dto.put("hits",makeHit());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
        Map<String , Object> map= new LinkedHashMap<>();
        if (email.isEmpty() || password.isEmpty()) {
            map.put("error","Missing data");
            return new ResponseEntity<>(map,HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) !=  null) {
            map.put("error","Name already in use");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
     public ResponseEntity<Map<String, Object>>  createGame(Authentication authentication){
        Player playerAuthentication = getPlayer(authentication);
        if(playerAuthentication != null){
           Game game = new Game(LocalDateTime.now());
            gameRepository.save(game);
            GamePlayer gamePlayer = new GamePlayer(game, playerAuthentication);
            gamePlayerRepo.save(gamePlayer);
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long nn,Authentication authentication){
        Player playerAuthentication = getPlayer(authentication);

        if(playerAuthentication != null){
             Game game = gameRepository.findById(nn).get();
             if(game != null){
                    if(game.getPlayers().size() == 1){
                            Player player = playerRepository.findByUserName(authentication.getName());
                            GamePlayer gamePlayer = new GamePlayer(game,player);
                            gamePlayerRepo.save(gamePlayer);
                        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
                    }else{
                        return new ResponseEntity<>(makeMap("error","El juego esta lleno"),HttpStatus.FORBIDDEN);
                    }

             }else{
                 return new ResponseEntity<>(makeMap("error","No hay tal juego"),HttpStatus.FORBIDDEN);
             }

        }else{
            return new ResponseEntity<>(makeMap("error","Imposible unirse al Juego"),HttpStatus.UNAUTHORIZED);
        }

    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Player getPlayer(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        else {
            return (playerRepository.findByUserName(authentication.getName()));
        }
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
        dto.put("gameState", "PLACESHIPS");
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gameP->makeGamePlayer(gameP)).collect(Collectors.toList()));
        dto.put("scores",game.getGamePlayers().stream().map(gp-> gp.getScoreDto()).collect(Collectors.toList()));
        return dto;
    }

    private Map<String,Object> makeGamePlayer(GamePlayer gamePlayer){
        Map<String , Object> dto = new LinkedHashMap<>();
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

    private Map<String,Object> makeHit(){
        Map<String , Object> dto =  new LinkedHashMap<>();
        dto.put("self",new ArrayList<>());
        dto.put("opponent",new ArrayList<>());
        return dto;

    }

}

