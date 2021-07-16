package com.codeoftheweb.Salvo.models;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();


    public Game() {
        this.fechaCreacion = LocalDateTime.of(LocalDate.now(), LocalTime.now());
    }

    public Game(LocalDateTime date) {

        this.fechaCreacion = date;

    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public LocalDateTime getFechaCreacion() {

        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {

        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public Set<Score> getScores() {
        return scores;
    }


    @JsonIgnore
    public List<Player> getPlayers(){
        return this.gamePlayers.stream().map(gp-> gp.getPlayer()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", gamePlayers=" + gamePlayers +
                '}';
    }
}
