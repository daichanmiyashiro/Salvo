package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo) {
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

			gamePlayerRepo.save(new GamePlayer(game1,player1));
			gamePlayerRepo.save(new GamePlayer(game1,player2));

			gamePlayerRepo.save(new GamePlayer(game2,player1));
			gamePlayerRepo.save(new GamePlayer(game2,player2));

			gamePlayerRepo.save(new GamePlayer(game3,player2));
			gamePlayerRepo.save(new GamePlayer(game3,player2));

			gamePlayerRepo.save(new GamePlayer(game4,player1));
			gamePlayerRepo.save(new GamePlayer(game4,player4));

			gamePlayerRepo.save(new GamePlayer(game5,player4));
			gamePlayerRepo.save(new GamePlayer(game5,player1));

			gamePlayerRepo.save(new GamePlayer(game6,player5));

		};
	}

}
