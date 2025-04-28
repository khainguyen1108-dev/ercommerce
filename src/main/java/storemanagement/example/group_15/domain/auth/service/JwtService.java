package storemanagement.example.group_15.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Data
public class JwtService {

    // Sử dụng khóa bảo mật mạnh (trong ứng dụng thực tế nên lưu trong biến môi trường)
    private static final String SECRET_KEY = "GROUP_15SIEUVIPPRO1111111111111111111111";

    // Thời gian hết hạn của Access Token và Refresh Token
    public final long ACCESS_TOKEN_VALIDITY = 10 * 60 * 60 * 1000; // 10 giờ
    public final long REFRESH_TOKEN_VALIDITY = 30 * 60 * 60 * 1000; // 30 giờ

    // Tạo Access Token cho người dùng
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, ACCESS_TOKEN_VALIDITY);
    }

    // Tạo Refresh Token cho người dùng
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, REFRESH_TOKEN_VALIDITY);
    }

    public Map<String, String> generateTokens(String username) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", generateAccessToken(username));
        tokens.put("refreshToken", generateRefreshToken(username));
        return tokens;
    }

    // Tạo token với thời gian hết hạn
    private String createToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // Kiểm tra và tạo lại Access Token từ Refresh Token
    public String refreshAccessToken(String refreshToken) {
        try {
            String username = getUsernameFromToken(refreshToken);
            if (username != null && validateToken(refreshToken)) {
                return generateAccessToken(username);  // Tạo Access Token mới
            }
        } catch (Exception e) {
            throw new RuntimeException("Refresh token is invalid.");
        }
        return null;
    }

    // Xác thực token
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Lấy username từ token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Lấy ngày hết hạn từ token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Kiểm tra token hết hạn
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Lấy claim từ token
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Giải mã token để lấy thông tin
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
