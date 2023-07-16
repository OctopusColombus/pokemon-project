package pokemon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "my_pokemon")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MyPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", length = 20, nullable = false)
    private Long id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "DETAIL", length = 10000, nullable = false)
    private String detail;

    @Column(name = "RENAME_COUNTER", length = 2, nullable = false)
    private int renameCounter;
}
