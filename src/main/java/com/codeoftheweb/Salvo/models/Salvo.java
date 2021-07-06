package com.codeoftheweb.Salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gPlayer;

    private int turn;

    @ElementCollection
    @Column(name="salvoLocation")
    private List<String> locations;

    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, int turn, List<String> locations) {
        this.gPlayer = gamePlayer;
        this.turn = turn;
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gPlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gPlayer = gamePlayer;
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
