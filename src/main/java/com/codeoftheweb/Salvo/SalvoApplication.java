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
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo,ShipRepository shipRepo) {
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

			Ship ship1 = shipRepo.save(new Ship(gamePlayer1,"Destroyer", List.of("B5","B6","B7","B8","B9")));
			Ship ship2 = shipRepo.save(new Ship(gamePlayer1,"Submarine", List.of("F3","G3","H3")));
			Ship ship3 = shipRepo.save(new Ship(gamePlayer2,"Patrol Boat", List.of("A1","A2","A3")));
			Ship ship4 = shipRepo.save(new Ship(gamePlayer2,"buque", List.of("B1","B2","B3","B4","B5")));


		};
	}

}
