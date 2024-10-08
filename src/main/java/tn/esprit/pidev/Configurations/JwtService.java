package tn.esprit.pidev.Configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private  String SECRET_KEY;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private   long refreshExpiration;
    @Value("${application.security.jwt.PasswordReset-token.expiration}")
    private long jwtPassExpriration;
    public String extractLogin(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generatePasswordRessetToken(UserDetails userDetails){
        log.info(jwtPassExpriration+"++++++++++++++++++++++++++++");
        return buildToken(new HashMap<>(),userDetails,jwtPassExpriration);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims ,
            UserDetails userDetails
    ){
        return buildToken(new HashMap<>(),userDetails,jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(), userDetails , refreshExpiration);
    }

    private String buildToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ){
        log.info(new Date(System.currentTimeMillis())+"aaaaaaaaaaaaaaaaaaa");
        log.info(new Date(System.currentTimeMillis()+expiration)+"bbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        extraClaims.put("role",userDetails.getAuthorities());
        log.info(extraClaims.get("role")+"---------------------------------------------***");
        return Jwts
        .builder()
                .setClaims(extraClaims)
                .setSubject(
                        userDetails.getUsername()
                )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValide(String token , UserDetails userDetails){
        final String userLogin  = extractLogin(token);
        return (userLogin.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    private Key getSignInKey() { // create secret key for token as sign
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
