package com.example.Board.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.example.Board.restfull.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

//해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달합니다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 헤더에서 JWT 를 받아옵니다.
		String servletPath = ((HttpServletRequest) request).getServletPath();
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		String refreshToken = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);
		
		
		if (servletPath.equals("/api/token/refresh")) {
			chain.doFilter(request, response);
        }
		else if (token != null && jwtTokenProvider.validateToken(token)) {
			// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			// SecurityContext 에 Authentication 객체를 저장합니다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} 
		else {
			((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			
			if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
				RestResponse<Object> RestreSponse = new RestResponse<Object>(400, "access token 유효기간이 지났습니다.");
				new ObjectMapper().writeValue(response.getWriter(), RestreSponse);
			} 
			else if (refreshToken != null && !jwtTokenProvider.validateToken(refreshToken)) {
				RestResponse<Object> RestreSponse = new RestResponse<Object>(400, "다시 로그인을  해주세요.");
				new ObjectMapper().writeValue(response.getWriter(), RestreSponse);
			}
			
		}

		chain.doFilter(request, response);
	}
}