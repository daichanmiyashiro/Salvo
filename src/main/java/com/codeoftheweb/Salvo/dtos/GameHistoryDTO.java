package com.codeoftheweb.Salvo.dtos;

import java.util.List;
import java.util.Map;

public class GameHistoryDTO {

    private int turn;

    private List<String> hitLocations;

    private DamageDTO damages;

    private int missed;

    public GameHistoryDTO() {
    }

    public GameHistoryDTO(Map<String , Object> map) {
        this.turn = (int)map.get("turn");
        this.hitLocations =(List<String>) map.get("hitLocations") ;
        this.damages = new DamageDTO((Map<String , Object>)map.get("damages"));
        this.missed = (int) map.get("missed");
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getHitLocations() {
        return hitLocations;
    }

    public void setHitLocations(List<String> hitLocations) {
        this.hitLocations = hitLocations;
    }

    public DamageDTO getDamages() {
        return damages;
    }

    public void setDamages(DamageDTO damages) {
        this.damages = damages;
    }

    public int getMissed() {
        return missed;
    }

    public void setMissed(int missed) {
        this.missed = missed;
    }
}
