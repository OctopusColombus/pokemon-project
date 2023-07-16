package pokemon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokeDetailResponse implements Serializable {
    private int weight;
    private int height;
    private int id;
    private int order;
    private boolean is_default;
    private String location_area_encounters;
    private String name;
    private String base_experience;
    private List<Abilities> abilities;
    private Sprites sprites;
    private List<Types> types;


}
