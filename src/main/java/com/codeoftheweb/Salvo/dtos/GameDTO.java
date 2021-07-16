package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.Game;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GameDTO {

    private Long id;

    private LocalDateTime created;

    private Set<GamePlayerDTO> gamePlayers;

    private Set<ScoreDTO> scores;

    public GameDTO() {
    }

    public GameDTO(Game game){
       this.id = game.getId();
       this.created = game.getFechaCreacion();
       this.gamePlayers = game.getGamePlayers().stream().map(gp -> new GamePlayerDTO(gp)).collect(Collectors.toSet());
       this.scores = game.getGamePlayers().stream().map(gp -> new ScoreDTO(gp.getScoreDto())).collect(Collectors.toSet());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Set<GamePlayerDTO> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayerDTO> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<ScoreDTO> getScores() {
        return scores;
    }

    public void setScores(Set<ScoreDTO> scores) {
        this.scores = scores;
    }
}
