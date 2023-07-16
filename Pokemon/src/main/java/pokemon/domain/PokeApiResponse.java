package pokemon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokeApiResponse {
    private int count;
    private String next;
    private String previous;
    private List<Pokemon> results;
}
