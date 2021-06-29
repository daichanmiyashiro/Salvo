package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GameRepository;
import com.codeoftheweb.Salvo.models.Player;
import com.codeoftheweb.Salvo.models.PlayerRepository;
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
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo) {
		return (args) -> {

			repository.save(new Player("j.bauer@ctu.gov"));
			repository.save(new Player("c.obrian@ctu.gov"));
			repository.save(new Player("kim_bauer@gmail.com"));
			repository.save(new Player("t.almeida@ctu.gov"));

			Date date1 = new Date();
			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date1.toInstant().plusSeconds(7200));
			gameRepo.save(new Game(LocalDateTime.now()));
			gameRepo.save(new Game(LocalDateTime.now().plusHours(1)));
			gameRepo.save(new Game(LocalDateTime.now().plusHours(2)));


		};
	}

}
