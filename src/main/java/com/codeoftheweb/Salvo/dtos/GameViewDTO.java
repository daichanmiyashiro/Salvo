package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.State;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class GameViewDTO {

    private Long id;

    private LocalDateTime created;

    private State gameState;

    private Set<GamePlayerDTO> gamePlayers = new HashSet<>();

    private Set<ShipDTO> ships = new HashSet<>();

    private Set<SalvoDTO> salvoes = new HashSet<>();

    private HitDTO hits;

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

    public State getGameState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public Set<GamePlayerDTO> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayerDTO> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<ShipDTO> getShips() {
        return ships;
    }

    public void setShips(Set<ShipDTO> ships) {
        this.ships = ships;
    }

    public Set<SalvoDTO> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<SalvoDTO> salvoes) {
        this.salvoes = salvoes;
    }

    public HitDTO getHits() {
        return hits;
    }

    public void setHits(HitDTO hits) {
        this.hits = hits;
    }
}
