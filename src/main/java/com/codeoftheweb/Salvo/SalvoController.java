package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.GameRepository;
import com.codeoftheweb.Salvo.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/games")
    public List<Object> getListaGames(){
        List<Game> games= gameRepository.findAll();
        return games.stream().map(game -> makeGameDto( game)).collect(Collectors.toList());
    }

    private Map<String , Object> makeGameDto(Game game){
        Map<String , Object> dto = new HashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getFechaCreacion());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gameP->makeGamePlayer(gameP)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String,Object> makeGamePlayer(GamePlayer gamePlayer){
        Map<String , Object> dto = new HashMap<>();
        dto.put("id",gamePlayer.getId());
        dto.put("player",makePlayer(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String,Object> makePlayer(Player player){
        Map<String , Object> dto = new HashMap<>();
        dto.put("id",player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }

}

