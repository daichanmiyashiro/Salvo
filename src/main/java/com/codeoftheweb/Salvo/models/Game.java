package com.codeoftheweb.Salvo.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;



@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    //TAREA 5
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();


    public Game() {
    }

    public Game(LocalDateTime date) {

        this.fechaCreacion = date;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

   /* @JsonIgnore
    public List<Player> getGamePlayer() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(Collectors.toList());
    }*/

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

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", gamePlayers=" + gamePlayers +
                '}';
    }
}
