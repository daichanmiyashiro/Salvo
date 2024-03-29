package com.codeoftheweb.Salvo.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime fechaCreacion;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    @OneToMany(mappedBy="gPlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvos = new HashSet<>();

    public void addShip(Salvo salvo){
        salvo.setGamePlayer(this);
        salvos.add(salvo);
    }

    public GamePlayer() {
    }

    public GamePlayer( Game game, Player player) {

        this.fechaCreacion = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        this.game = game;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public Score getScoreDto() {

        Score score = this.getPlayer().getScore(this.getGame());
        if (score == null) {
            score= new Score();
            score.setPlayer(this.getPlayer());
            score.setScore(null);
        }
        return score;
    }
}
