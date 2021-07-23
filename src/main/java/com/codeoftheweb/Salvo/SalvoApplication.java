package com.codeoftheweb.Salvo;
import com.codeoftheweb.Salvo.models.*;
import com.codeoftheweb.Salvo.repository.*;
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
import java.util.Date;
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
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo, PasswordEncoder passwordEncoder) {
		return (args) -> {

			Date date = new Date();

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

			gameRepo.save(game1);
			gameRepo.save(game2);

			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2);
			GamePlayer gamePlayer3 = new GamePlayer(game2,player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player4);

			gamePlayerRepo.save(gamePlayer1);
			gamePlayerRepo.save(gamePlayer2);
			gamePlayerRepo.save(gamePlayer3);
			gamePlayerRepo.save(gamePlayer4);

			Ship ship1 = new Ship(gamePlayer1,ShipType.DESTROYER, List.of("H2","H3","H4"));
			Ship ship2 = new Ship(gamePlayer1,ShipType.SUBMARINE, List.of("B5","B6","B7"));
			Ship ship3 = new Ship(gamePlayer1,ShipType.CARRIER, List.of("E1","E2","E3","E4","E5"));
			Ship ship4 = new Ship(gamePlayer1,ShipType.BATTLESHIP, List.of("F5","G5","H5","I5"));
			Ship ship5 = new Ship(gamePlayer1,ShipType.PATROLBOAT, List.of("C5","D5"));

			Ship ship6 = new Ship(gamePlayer2,ShipType.DESTROYER, List.of("A1","A2","A3"));
			Ship ship7 = new Ship(gamePlayer2,ShipType.SUBMARINE, List.of("F1","G1","H1"));
			Ship ship8 = new Ship(gamePlayer2,ShipType.CARRIER, List.of("E1","E2","E3","E4","E5"));
			Ship ship9 = new Ship(gamePlayer2,ShipType.BATTLESHIP, List.of("F5","G5","H5","I5"));
			Ship ship10 = new Ship(gamePlayer2,ShipType.PATROLBOAT, List.of("C5","D5"));

			shipRepo.save(ship1);
			shipRepo.save(ship2);
			shipRepo.save(ship3);
			shipRepo.save(ship4);
			shipRepo.save(ship5);
			shipRepo.save(ship6);
			shipRepo.save(ship7);
			shipRepo.save(ship8);
			shipRepo.save(ship9);
			shipRepo.save(ship10);

			Salvo salvo1 = new Salvo(gamePlayer1, 1 ,List.of("H1","A2","C5","E1","G6"));
			Salvo salvo2 = new Salvo(gamePlayer2, 1 ,List.of("C5","F6","A1","I2","J6"));
			Salvo salvo3 = new Salvo(gamePlayer1, 2 ,List.of("B4","D8","C3","E4","G1"));
			Salvo salvo4 = new Salvo(gamePlayer2, 2 ,List.of("A7","F1","H5","J9","E9"));
			Salvo salvo5 = new Salvo(gamePlayer1, 3 ,List.of("B4","D8","C3","E4","G1"));
			Salvo salvo6 = new Salvo(gamePlayer2, 3 ,List.of("A7","F1","H5","J9","E9"));

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

		//Cómo debe obtener el explorador el nombre de usuario y la contraseña para enviar la aplicación Web
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.antMatchers( "/web/**","/api/games","/api/players","/rest/**").permitAll()
					.anyRequest().authenticated();


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




