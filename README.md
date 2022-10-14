# SpringBootProject
spring boot, security, jwt, redis, kakao login api 활용한 rest full 프로젝트

## Description  
웹 프로젝트에 필요한 security, jwt, redis를 활용한 인증 및 인가 관리와 보안 그리고 security, jwt, kakao login open api를 활용한 소셜 로그인을 구현한 프로젝트입니다.

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





