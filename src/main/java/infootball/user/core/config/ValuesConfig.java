package infootball.user.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValuesConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.author}")
    private String author;


}
