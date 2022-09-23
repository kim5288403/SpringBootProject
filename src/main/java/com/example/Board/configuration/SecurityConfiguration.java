package com.example.Board.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.Board.auth.CustomAuthenticationEntryPoint;
import com.example.Board.jwt.JwtTokenProvider;
import com.example.Board.model.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	private final JwtTokenProvider jwtTokenProvider;

	@Autowired
	MemberService memberService;

	// authenticationManager를 Bean 등록합니다.
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		.cors().and()
		.csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS, "api/member/**").permitAll()
		.antMatchers("/api/member/**","/member/**").permitAll()
		.and()
		.formLogin().disable();

		//		http
		//		.formLogin()
		//		.loginPage("/member/login")
		//		.defaultSuccessUrl("/")
		//		.usernameParameter("email")
		//		.failureUrl("/member/login/error")
		//		.and()
		//		.logout()
		//		.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
		//		.logoutSuccessUrl("/");

		//		http
		//		.authorizeRequests()
		//		.mvcMatchers("/", "/member/**", "/assets/**", "/h2-console/**").permitAll()
		//		.mvcMatchers("/admin/**").hasRole("ADMIN")
		//		.anyRequest().authenticated()
		//		;
		//
		//		http
		//		.exceptionHandling()
		//		.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
		//		;

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
