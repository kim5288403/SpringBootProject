package com.example.Board.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.example.Board.restfull.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String servletPath = ((HttpServletRequest) request).getServletPath();
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		String refreshToken = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);

		if (servletPath.equals("/api/token/refresh")) {
			chain.doFilter(request, response);
		}
		else {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} 
			else {
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
}