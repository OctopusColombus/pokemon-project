package pokemon.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MyPokemonList extends PagingAndSortingRepository<MyPokemon, Long> {
    @Query(value = "select * from my_pokemon WHERE lower(name) like lower(?1)", nativeQuery = true)
    List<MyPokemon> findByName(String name, Pageable pageable);
}
