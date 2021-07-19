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
    private List<String> salvoLocations;

    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, int turn, List<String> locations) {
        this.gPlayer = gamePlayer;
        this.turn = turn;
        this.salvoLocations = locations;
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

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }
}
