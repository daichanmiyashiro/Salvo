package com.codeoftheweb.Salvo.controller;
import com.codeoftheweb.Salvo.dtos.*;
import com.codeoftheweb.Salvo.models.*;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.GameRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import com.codeoftheweb.Salvo.repository.ShipRepository;
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

    @Autowired
    private ShipRepository shipRepository;

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
        dto.put("games",games.stream().map(game->new GameDTO(game)).collect(Collectors.toList()));
        return dto;
    }

    @GetMapping("/game_view/{id}")
    public  ResponseEntity<?> getViewGame(@PathVariable Long id,Authentication authentication){
        Map<String , Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();
        Player playerAuthentication = playerRepository.findByUserName(authentication.getName());
        if(gamePlayer.getPlayer().getId() == playerAuthentication.getId()){
            return new ResponseEntity<>(new GameViewDTO(game,gamePlayer), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(makeMap("error","Id no autorizado"),HttpStatus.UNAUTHORIZED);
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
            Game game = new Game();
            GamePlayer gamePlayer = new GamePlayer(game, playerAuthentication);
            gameRepository.save(game);
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


    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(@PathVariable Long gamePlayerId,Authentication authentication, @RequestBody List<Ship> ships){
        Player playerAuthentication = getPlayer(authentication);
        GamePlayer gp = gamePlayerRepo.findById(gamePlayerId).get();
            if(playerAuthentication != null){
                if(idValid(gamePlayerId)){
                    if(isIdReference(gamePlayerId,authentication)){
                        if(ships.size() == 5){
                            if(gp.getShips().size() == 0){
                                for (Ship ship: ships) {
                                    System.out.println("posiciones : " + ship.getShipLocations());
                                    shipRepository.save(new Ship(gp,ship.getType(),ship.getShipLocations()));
                                }
                                return new ResponseEntity<>(makeMap("OK", "Las naves se han guardado correctamente"), HttpStatus.CREATED);
                            }else{
                                return new ResponseEntity<>(makeMap("error","No se puede agregar las naves"),HttpStatus.FORBIDDEN);
                            }
                        }else{
                            return new ResponseEntity<>(makeMap("error","Se requiere  5 naves"),HttpStatus.FORBIDDEN);
                        }

                    }else{
                        return new ResponseEntity<>(makeMap("error","Id no corresponde a Game player"),HttpStatus.UNAUTHORIZED);
                    }
                }else{
                    return new ResponseEntity<>(makeMap("error","Id invalido"),HttpStatus.UNAUTHORIZED);
                }

            }else{
                return new ResponseEntity<>(makeMap("error","Imposible unirse al Juego"),HttpStatus.UNAUTHORIZED);
            }
    }


    private boolean idValid(Long id){
        boolean idValid = false;
        GamePlayer gp = gamePlayerRepo.findById(id).get();
        if(gp != null){
            idValid = true;
        }
        return idValid;
    }

    private boolean isIdReference(Long id, Authentication authentication){
        boolean idReference = false;
        GamePlayer gp = gamePlayerRepo.findById(id).get();
        Player player = getPlayer(authentication);
        if(gp.getPlayer().getId() == player.getId()){
            idReference = true;
        }
        return idReference;
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

}

