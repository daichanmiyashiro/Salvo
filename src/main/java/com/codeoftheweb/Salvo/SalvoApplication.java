package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo,ShipRepository shipRepo, SalvoRepository salvoRepo,ScoreRepository scoreRepo) {
		return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov");
			Player player2 = new Player("c.obrian@ctu.gov");
			Player player3 = new Player("kim_bauer@gmail.com");
			Player player4 = new Player("t.almeida@ctu.gov");
			Player player5 = new Player("d.palmer@whitehouse.gov");

			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);
			repository.save(player5);

			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			Game game3 = new Game(LocalDateTime.now().plusHours(2));
			Game game4 = new Game(LocalDateTime.now().plusHours(3));
			Game game5 = new Game(LocalDateTime.now().plusHours(4));
			Game game6 = new Game(LocalDateTime.now().plusHours(5));

			gameRepo.save(game1);
			gameRepo.save(game2);
			gameRepo.save(game3);
			gameRepo.save(game4);
			gameRepo.save(game5);

			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2);
			GamePlayer gamePlayer3 = new GamePlayer(game2,player1);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player2);

			gamePlayerRepo.save(gamePlayer1);
			gamePlayerRepo.save(gamePlayer2);
			gamePlayerRepo.save(gamePlayer3);
			gamePlayerRepo.save(gamePlayer4);


			Ship ship1 = new Ship(gamePlayer1,"Destroyer", List.of("H2","H3","H4"));
			Ship ship2 = new Ship(gamePlayer1,"Submarine", List.of("E5","F5","G5"));
			Ship ship3 = new Ship(gamePlayer2,"Battleship", List.of("A1","A2","A3","A4"));
			Ship ship4 = new Ship(gamePlayer2,"Destroyer ", List.of("F1","G1","H1"));

			shipRepo.save(ship1);
			shipRepo.save(ship2);
			shipRepo.save(ship3);
			shipRepo.save(ship4);

			Salvo salvo1 = new Salvo(gamePlayer1, 1 ,List.of("H1","A2","C5","E1","G6"));
			Salvo salvo2 = new Salvo(gamePlayer2, 1 ,List.of("C5","F6","A1","I2","J6"));
			Salvo salvo3 = new Salvo(gamePlayer1, 2 ,List.of("B4","D8","C3","E4","G1"));
			Salvo salvo4 = new Salvo(gamePlayer2, 2 ,List.of("A7","F1","H5","J9","E9"));
			Salvo salvo5 = new Salvo(gamePlayer1, 3 ,List.of("B4","D8","C3","E4","G1"));
			Salvo salvo6 = new Salvo(gamePlayer2, 3 ,List.of("A7","F1","H5","J9","E9"));

			salvoRepo.save(salvo1);
			salvoRepo.save(salvo2);
			salvoRepo.save(salvo3);
			salvoRepo.save(salvo4);
			salvoRepo.save(salvo5);
			salvoRepo.save(salvo6);


			Score score1 = new Score(player1,game1,0.5,LocalDateTime.now());
			Score score2 = new Score(player2,game1,0.5,LocalDateTime.now());
			Score score3 = new Score(player1,game2,1,LocalDateTime.now());
			Score score4 = new Score(player2,game2,0,LocalDateTime.now());

			scoreRepo.save(score1);
			scoreRepo.save(score2);
			scoreRepo.save(score3);
			scoreRepo.save(score4);


			
		};
	}

}
