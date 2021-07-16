package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.*;

public class GamePlayerDTO {

    private Long id;

    private PlayerDTO player;

    public GamePlayerDTO(GamePlayer gamePlayer){
        this.id = gamePlayer.getId();
        this.player = new PlayerDTO(gamePlayer.getPlayer());
    }

    public Long getId() {
        return id;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}
