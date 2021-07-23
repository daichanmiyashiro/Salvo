package com.codeoftheweb.Salvo.dtos;
import java.util.Map;

public class DamageDTO {

    private int carrierHits;
    private int battleshipHits;
    private int submarineHits;
    private int destroyerHits;
    private int patrolboatHits;
    private int carrier;
    private int battleship;
    private int submarine;
    private int destroyer;
    private int patrolboat;

    public DamageDTO() {
    }

    public DamageDTO(Map<String , Object> map) {

        this.carrierHits = (int) map.get("carrierHits");
        this.battleshipHits = (int) map.get("battleshipHits");
        this.submarineHits = (int) map.get("submarineHits");
        this.destroyerHits = (int) map.get("destroyerHits");
        this.patrolboatHits = (int) map.get("patrolboatHits");
        this.carrier = (int) map.get("carrier");
        this.battleship = (int) map.get("battleship");
        this.submarine = (int) map.get("submarine");
        this.destroyer =(int) map.get("destroyer");
        this.patrolboat = (int) map.get("patrolboat");
    }

    public int getCarrierHits() {
        return carrierHits;
    }

    public void setCarrierHits(int carrierHits) {
        this.carrierHits = carrierHits;
    }

    public int getBattleshipHits() {
        return battleshipHits;
    }

    public void setBattleshipHits(int battleshipHits) {
        this.battleshipHits = battleshipHits;
    }

    public int getSubmarineHits() {
        return submarineHits;
    }

    public void setSubmarineHits(int submarineHits) {
        this.submarineHits = submarineHits;
    }

    public int getDestroyerHits() {
        return destroyerHits;
    }

    public void setDestroyerHits(int destroyerHits) {
        this.destroyerHits = destroyerHits;
    }

    public int getPatrolboatHits() {
        return patrolboatHits;
    }

    public void setPatrolboatHits(int patrolboatHits) {
        this.patrolboatHits = patrolboatHits;
    }

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getBattleship() {
        return battleship;
    }

    public void setBattleship(int battleship) {
        this.battleship = battleship;
    }

    public int getSubmarine() {
        return submarine;
    }

    public void setSubmarine(int submarine) {
        this.submarine = submarine;
    }

    public int getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(int destroyer) {
        this.destroyer = destroyer;
    }

    public int getPatrolboat() {
        return patrolboat;
    }

    public void setPatrolboat(int patrolboat) {
        this.patrolboat = patrolboat;
    }
}
