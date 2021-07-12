package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo,ShipRepository shipRepo, SalvoRepository salvoRepo,ScoreRepository scoreRepo,PasswordEncoder passwordEncoder) {
		return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player player3 = new Player("kim_bauer@gmail.com",passwordEncoder().encode("kb"));
			Player player4 = new Player("t.almeida@ctu.gov",passwordEncoder().encode("mole"));
			Player player5 = new Player("d.palmer@whitehouse.gov",passwordEncoder().encode("palmer"));

			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);
			repository.save(player5);

			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			Game game3 = new Game(LocalDateTime.now().plusHours(2));

			gameRepo.save(game1);
			gameRepo.save(game2);
			gameRepo.save(game3);

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

	@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		PlayerRepository playerRepository;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userName -> {
				Player player = playerRepository.findByUserName(userName);
				if (player != null) {
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + userName);
				}
			});
		}

	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		// METODO CONFIGURE
		//Cómo debe obtener el explorador el nombre de usuario y la contraseña para enviar la aplicación Web
		//los patrones de direcciones URL que son y no son accesibles para diferentes tipos de usuarios
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.antMatchers( "/web/**","/api/games","/api/players","/rest/**").permitAll()
					.anyRequest().authenticated();

			//Cómo debe obtener el explorador el nombre de usuario y la contraseña para enviar la aplicación Web
			http.formLogin()
					.usernameParameter("name")
					.passwordParameter("pwd")
					 .loginPage("/api/login");

			http.logout().logoutUrl("/api/logout");

			// turn off checking for CSRF tokens
			http.csrf().disable();

			// if user is not authenticated, just send an authentication failure response
			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if login is successful, just clear the flags asking for authentication
			http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

			// if login fails, just send an authentication failure response
			http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if logout is successful, just send a success response
			http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

		private void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
		}

	}




