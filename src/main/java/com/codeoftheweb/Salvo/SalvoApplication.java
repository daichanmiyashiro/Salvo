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
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo,ShipRepository shipRepo, SalvoRepository salvoRepo) {
		return (args) -> {

			Player player1 = repository.save(new Player("j.bauer@ctu.gov"));
			Player player2 = repository.save(new Player("c.obrian@ctu.gov"));
			Player player3 = repository.save(new Player("kim_bauer@gmail.com"));
			Player player4 = repository.save(new Player("t.almeida@ctu.gov"));
			Player player5 = repository.save(new Player("d.palmer@whitehouse.gov"));

			Game game1 = gameRepo.save(new Game(LocalDateTime.now()));
			Game game2 =gameRepo.save(new Game(LocalDateTime.now().plusHours(1)));
			Game game3 =gameRepo.save(new Game(LocalDateTime.now().plusHours(2)));
			Game game4 =gameRepo.save(new Game(LocalDateTime.now().plusHours(3)));
			Game game5 =gameRepo.save(new Game(LocalDateTime.now().plusHours(4)));
			Game game6 =gameRepo.save(new Game(LocalDateTime.now().plusHours(5)));

			GamePlayer gamePlayer1 = gamePlayerRepo.save(new GamePlayer(game1,player1));
			GamePlayer gamePlayer2 = gamePlayerRepo.save(new GamePlayer(game1,player2));
			GamePlayer gamePlayer3 = gamePlayerRepo.save(new GamePlayer(game2,player1));
			gamePlayerRepo.save(new GamePlayer(game2,player2));
			gamePlayerRepo.save(new GamePlayer(game3,player2));
			gamePlayerRepo.save(new GamePlayer(game3,player2));
			gamePlayerRepo.save(new GamePlayer(game4,player1));
			gamePlayerRepo.save(new GamePlayer(game4,player4));
			gamePlayerRepo.save(new GamePlayer(game5,player4));
			gamePlayerRepo.save(new GamePlayer(game5,player1));
			gamePlayerRepo.save(new GamePlayer(game6,player5));

			Ship ship1 = shipRepo.save(new Ship(gamePlayer1,"Destroyer", List.of("H2","H3","H4")));
			Ship ship2 = shipRepo.save(new Ship(gamePlayer1,"Submarine", List.of("E5","F5","G5")));
			Ship ship3 = shipRepo.save(new Ship(gamePlayer2,"Battleship", List.of("A1","A2","A3","A4")));
			Ship ship4 = shipRepo.save(new Ship(gamePlayer2,"Destroyer ", List.of("F1","G1","H1")));

			Salvo salvo1 = salvoRepo.save(new Salvo(gamePlayer1, 1 ,List.of("H1","A2","C5","E1","G6")));
			Salvo salvo2 = salvoRepo.save(new Salvo(gamePlayer2, 1 ,List.of("C5","F6","A1","I2","J6")));
			Salvo salvo3 = salvoRepo.save(new Salvo(gamePlayer1, 2 ,List.of("B4","D8","C3","E4","G1")));
			Salvo salvo4 = salvoRepo.save(new Salvo(gamePlayer2, 2 ,List.of("A7","F1","H5","J9","E9")));
			Salvo salvo5 = salvoRepo.save(new Salvo(gamePlayer1, 3 ,List.of("B4","D8","C3","E4","G1")));
			Salvo salvo6 = salvoRepo.save(new Salvo(gamePlayer2, 3 ,List.of("A7","F1","H5","J9","E9")));


			
		};
	}

}
