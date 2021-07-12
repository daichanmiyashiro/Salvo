package com.codeoftheweb.Salvo.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    private String password;

    public Player() {
    }

    public Player(String userName,String password) {
        this.userName = userName;
        this.password = password;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public List<Game> getGamePlayer() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Score getScore(Game game){
        return this.scores.stream().filter(score -> score.getGame().getId()== game.getId()).findFirst().orElse(null);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Player {" +
                " id = " + id +
                ", email = '" + userName + '\'' +
                '}';
    }
}
