package com.codeoftheweb.Salvo.controller;
import com.codeoftheweb.Salvo.dtos.*;
import com.codeoftheweb.Salvo.models.*;
import com.codeoftheweb.Salvo.repository.*;
import com.codeoftheweb.Salvo.service.GameViewService;
import com.codeoftheweb.Salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private GameViewService gameViewService;

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
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();
        Player playerAuthentication = playerRepository.findByUserName(authentication.getName());

        if(gamePlayer.getPlayer().getId() == playerAuthentication.getId()){

            return new ResponseEntity<>(gameViewService.makeGameViewDTO(game,gamePlayer), HttpStatus.OK);
        }else{

            return new ResponseEntity<>(Util.makeMap("error","User ID not authorized"),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/players")
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

    @PostMapping(path = "/games")
     public ResponseEntity<Map<String, Object>>  createGame(Authentication authentication){
        Player playerAuthentication = getPlayer(authentication);
        if(playerAuthentication != null){
            Game game = new Game();
            GamePlayer gamePlayer = new GamePlayer(game, playerAuthentication);
            gameRepository.save(game);
            gamePlayerRepo.save(gamePlayer);
            return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/game/{nn}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long nn,Authentication authentication){
        Player playerAuthentication = getPlayer(authentication);

        if(playerAuthentication != null){
             Game game = gameRepository.findById(nn).get();
             if(game != null){
                    if(game.getPlayers().size() == 1){
                            Player player = playerRepository.findByUserName(authentication.getName());
                            GamePlayer gamePlayer = new GamePlayer(game,player);
                            gamePlayerRepo.save(gamePlayer);
                        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
                    }else{
                        return new ResponseEntity<>(Util.makeMap("error","Player limit reached"),HttpStatus.FORBIDDEN);
                    }

             }else{
                 return new ResponseEntity<>(Util.makeMap("error","Game not found"),HttpStatus.FORBIDDEN);
             }

        }else{
            return new ResponseEntity<>(Util.makeMap("error","User ID not authorized"),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> addShips(@PathVariable Long gamePlayerId,Authentication authentication, @RequestBody List<Ship> ships){
        Player playerAuthentication = getPlayer(authentication);
        if(playerAuthentication == null){
            return new ResponseEntity<>(Util.makeMap("error","User ID not authorized"),HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gp = gamePlayerRepo.findById(gamePlayerId).get();
        if(!idValid(gamePlayerId)){
            return new ResponseEntity<>(Util.makeMap("error","ID is not valid"),HttpStatus.UNAUTHORIZED);
        }
        if(!isIdReference(gamePlayerId,authentication)){
            return new ResponseEntity<>(Util.makeMap("error","User ID not authorized"),HttpStatus.UNAUTHORIZED);
        }
        if(ships.size() != 5){
            return new ResponseEntity<>(Util.makeMap("error","Five ships needed"),HttpStatus.FORBIDDEN);
        }
        if(gp.getShips().size() != 0){
            return new ResponseEntity<>(Util.makeMap("error","Ships limit reached"),HttpStatus.FORBIDDEN);
        }
        for (Ship ship: ships) {
            shipRepository.save(new Ship(gp,ship.getType(),ship.getShipLocations()));
        }
        return new ResponseEntity<>(Util.makeMap("OK", "Ships saved correctly"), HttpStatus.CREATED);

    }

    @PostMapping(path = "/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> addSalvos(@PathVariable Long gamePlayerId,Authentication authentication, @RequestBody Salvo salvo){
        Player playerAuthentication = getPlayer(authentication);

        if(playerAuthentication == null){
            return new ResponseEntity<>(Util.makeMap("error","User ID not authorized"),HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gp1 = gamePlayerRepo.findById(gamePlayerId).orElse(null);

        if(gp1 == null){
            return new ResponseEntity<>(Util.makeMap("error","ID is not valid"),HttpStatus.UNAUTHORIZED);
        }
        Game game = gp1.getGame();

        if(gp1.getPlayer().getId() != playerAuthentication.getId() ){
            return new ResponseEntity<>(Util.makeMap("error","ID does not correspond to the player"),HttpStatus.UNAUTHORIZED);
        }

        Optional<GamePlayer> gp2 = Util.getOpponent(game,playerAuthentication);
        if(gp2 == null){
            return new ResponseEntity<>(Util.makeMap("error","Two players are required"),HttpStatus.FORBIDDEN);
        }

        if(salvo.getSalvoLocations().size() < 1 || salvo.getSalvoLocations().size() > 5){
            return new ResponseEntity<>(Util.makeMap("error","this amount is not allowed"),HttpStatus.FORBIDDEN);
        }

        if(gp1.getSalvos().size() == gp2.get().getSalvos().size()){
            salvoRepository.save(new Salvo(gp1,gp1.getSalvos().size()+1,salvo.getSalvoLocations()));
            return new ResponseEntity<>(Util.makeMap("OK", "Salvo saved correctly"), HttpStatus.OK);
        }else{
            if(gp1.getSalvos().size() > gp2.get().getSalvos().size()){
                 return new ResponseEntity<>(Util.makeMap("error","Non-corresponded turn"),HttpStatus.FORBIDDEN);
            }else{
                salvoRepository.save(new Salvo(gp1,gp1.getSalvos().size()+1,salvo.getSalvoLocations()));
                 return new ResponseEntity<>(Util.makeMap("OK", "Salvo saved correctly"), HttpStatus.OK);
            }
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

    private Player getPlayer(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        else {
            return (playerRepository.findByUserName(authentication.getName()));
        }
    }

}































