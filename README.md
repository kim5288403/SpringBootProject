# SpringBootProject
spring boot, security, jwt, redis, cache, kakao login api 활용한 rest full 프로젝트

## Description  
웹 프로젝트 구현시 필요한 인증 및 인가 관리를 security, jwt, redis를 활용하여 구현한 프로젝트 입니다 또한 외부 API인 kakao login api를 활용해 소셜로그인도 구현 했습니다, 사용자의 중복된 요청을 막기 위해 redis의 cache를 활용해 중복된 요청을 막아 웹 프로젝트의 속도를 향상 시켰습니다.

## Environment
> OS => Window  
> DB => MySql 8.0 version  
> Compile => java 1.8 version  
> FrameWork => Spring boot  
> API TEST => Postman

## Prerequisite
```
Redis-x64-3.0.504 install
```
[Redis 설치를 해주세요.](https://github.com/microsoftarchive/redis/releases)
```
git clone -b as --single-branch https://github.com/kim5288403/SpringBootProject.git
```  
 
repository를 clone 하고 IDE에서 애플리케이션을 실행해주세요.

## Files
`KakaoService.java` : Kakao Open API File  
`JwtAuthenticationFilter` : Jwt Data Validation File  
`SecurityConfiguration` : Security Config File

## Usage

* login API : */api/member/login*  
  * method : post
  * body : { "email" : "clackr123@naver.com", "password" : "xptmxm123" }


* logout API : /api/member/logout  
  * method : get  

* refresh token API : */api/token/refresh*   
  * method : post  
  * Headers : { "RefreshToken" : tokenData }  

* token test API : */test*
  * method : post  
  * Headers : { "Authorization" : "Bearer " + tokenData, "RefreshToken" : tokenData }  

## Detail  
`상세설명` => https://limjyeok.tistory.com/m/category/Spring%20boot


