package infootball.user.core.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import infootball.user.core.config.ValuesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {


    @Autowired
    private final ValuesConfig valuesConfig;

    public JwtUtil(ValuesConfig valuesConfig) {
        this.valuesConfig = valuesConfig;
    }


    public String createToken(String email){
        return JWT.create().
                withSubject(email).
                withIssuer(this.valuesConfig.getAuthor()).
                withIssuedAt(new Date()).
                withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15))).sign(Algorithm.HMAC256(this.valuesConfig.getJwtSecret()));

    }


    public boolean isValid(String jwt){
        try{
            JWT.require(Algorithm.HMAC256(this.valuesConfig.getJwtSecret()))
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e){
            return false;
        }
    }

    public String getEmail(String jwt){
        return JWT.require(Algorithm.HMAC256(this.valuesConfig.getJwtSecret()))
                .build()
                .verify(jwt)
                .getSubject();
    }
}
