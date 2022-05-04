package Capston.camo.controller;

import Capston.camo.domain.Account;
import Capston.camo.domain.Role;
import Capston.camo.service.UserServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<List<Account>> getUsers(){
        return ResponseEntity.ok().body(userService.getAccounts());
    }

    @PostMapping("/user/save")
    public ResponseEntity<Account> saveAccount(@RequestBody Account account){
        //DTO로 바꿔야됨
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveAccount(account));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        //DTO로 바꿔야됨
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm roleToUserForm){
        //DTO로 바꿔야됨
        userService.addRoleToAccount(roleToUserForm.getNickname(),roleToUserForm.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        log.info("{} 리프레쉬 코드",authorizationHeader);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            //verity 하는 과정

            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("HelloIsEverythingofCafeSecret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                Account user = userService.getAccount(email);
                //username이 고유이고 이걸로 식별한다.

                //username을 나는 email로 바꿨음 email이 unique 한 거임


                String access_token = JWT.create().withSubject(user.getEmail()) // 10분
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);


                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);


            }catch (Exception exception){ //token이 정상적이지 않을 때
                log.error("Error 로그인 {}",exception.getMessage());

                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("usercontroller error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }else{
            //refreshtoken이 있어야 갱신 가능
            throw new RuntimeException("Refresh token이 없습니다");
        }

    }

    @Data
    class RoleToUserForm{
        private String nickname;
        private String roleName;
    }

}


