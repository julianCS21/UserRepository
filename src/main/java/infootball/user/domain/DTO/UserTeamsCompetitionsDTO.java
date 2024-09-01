package infootball.user.domain.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTeamsCompetitionsDTO {

    private List<Long> teams = new ArrayList<>();
    private List<Long> competitions = new ArrayList<>();
}
