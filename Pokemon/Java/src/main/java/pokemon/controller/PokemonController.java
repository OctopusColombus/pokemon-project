package pokemon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pokemon.domain.ActionResponse;
import pokemon.domain.CatchRequest;
import pokemon.domain.MyPokemonResponseList;
import pokemon.domain.PokeDetailResponse;
import pokemon.domain.PokemonResponse;
import pokemon.entity.MyPokemonList;
import pokemon.services.PokemonServices;

import java.util.Random;

@RestController
@Slf4j
@RequestMapping(value = "")
@CrossOrigin(origins = "http://localhost:3000")
public class PokemonController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyPokemonList myPokemonList;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("https://pokeapi.co/api/v2/pokemon-form")
    private String pokemonListUrl;

    @Value("https://pokeapi.co/api/v2/pokemon/")
    private String pokemonDetailUrl;

    @Autowired
    private PokemonServices pokemonServices;

    private static final Random rand = new Random();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getPokemonList")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PokemonResponse getPokemon(@RequestParam String offset) {
        return pokemonServices.getPokemon(offset);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getPokemonDetail")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PokeDetailResponse getPokemonDetail(@RequestParam String id) throws InterruptedException {
        return pokemonServices.getPokemonDetail(id);
    }

    @PostMapping("/catchPokemon")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse catchPokemon(@RequestBody CatchRequest request) throws JsonProcessingException, InterruptedException {
        return pokemonServices.catchPokemon(request);
    }

    @DeleteMapping("/releasePokemon")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse releasePokemon(@RequestParam Long id) throws JsonProcessingException, InterruptedException {
        return pokemonServices.releasePokemon(id);
    }

    @PostMapping("/renamePokemon")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse renamePokemon(@RequestParam Long id, String name) throws InterruptedException {
        return pokemonServices.renamePokemon(id, name);
    }

    @GetMapping("/getMyPokemon")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MyPokemonResponseList getMyPokemon(@RequestParam(required = false) Integer page, @RequestParam(required = false) String name) throws JsonProcessingException, InterruptedException {
        return pokemonServices.getMyPokemon(page, name);
    }
}
