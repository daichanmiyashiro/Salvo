package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.GamePlayer;
import com.codeoftheweb.Salvo.models.Salvo;

import java.util.List;

public class SalvoDTO {

    private int turn;

    private Long player;

    private List<String> locations;

    public SalvoDTO() {
    }

    public SalvoDTO(Salvo salvo){
        this.turn = salvo.getTurn();
        this.player = salvo.getGamePlayer().getPlayer().getId();
        this.locations = salvo.getSalvoLocations();
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
