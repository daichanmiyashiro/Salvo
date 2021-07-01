package com.codeoftheweb.Salvo.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
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

    //TAREA 3
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
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
}
