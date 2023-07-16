package pokemon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pokemon.domain.*;
import pokemon.entity.MyPokemon;
import pokemon.entity.MyPokemonList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@Slf4j
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

    private static final Random rand = new Random();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getPokemonList")
    public PokemonResponse getPokemon(@RequestParam String offset) {
        PokeApiResponse pokeApiResponse = restTemplate.getForObject(pokemonListUrl+"/?offset="+offset+"&limit=12", PokeApiResponse.class);
        PokemonResponse response = new PokemonResponse();
        response.setPokemonList(new ArrayList<>());

        if (pokeApiResponse != null) {
            for (Pokemon result : pokeApiResponse.getResults()) {
                PokeFormResponse pokeFormResponse = restTemplate.getForObject(result.getUrl(), PokeFormResponse.class);
                Pokemon pokemon = new Pokemon();
                if (pokeFormResponse != null) {
                    pokemon.setName(pokeFormResponse.getName());
                    pokemon.setUrl(pokeFormResponse.getSprites().getFront_default());
                    pokemon.setId(pokeFormResponse.getId());
                }
                response.setOffset(Integer.parseInt(offset));
                response.getPokemonList().add(pokemon);
                response.setNext(pokeApiResponse.getNext());
                response.setPrevious(pokeApiResponse.getPrevious());
            }
        }

        return  response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getPokemonDetail")
    public PokeDetailResponse getPokemonDetail(@RequestParam String id) {
        PokeDetailResponse pokeApiResponse = restTemplate.getForObject(pokemonDetailUrl+id, PokeDetailResponse.class);
        return  pokeApiResponse;
    }

    @PostMapping("/catchPokemon")
    public ActionResponse catchPokemon(@RequestBody CatchRequest request) throws JsonProcessingException {
        ActionResponse actionResponse = new ActionResponse();
        actionResponse.setName(request.getPokemonDetail().getName());
        if (rand.nextInt(2) == 0) {
            MyPokemon myPokemon = MyPokemon.builder()
                    .name(request.getPokemonDetail().getName())
                    .detail(objectMapper.writeValueAsString(request.getPokemonDetail()))
                    .renameCounter(0)
                    .build();
            myPokemonList.save(myPokemon);
            actionResponse.setSuccess(true);
        }
        return actionResponse;
    }

    @DeleteMapping("/releasePokemon")
    public ActionResponse releasePokemon(@RequestParam Long id) throws JsonProcessingException {
        ActionResponse actionResponse = new ActionResponse();
        MyPokemon myPokemon = myPokemonList.findById(id).orElse(new MyPokemon());
        PokeDetailResponse pokeDetailResponse = objectMapper.readValue(myPokemon.getDetail(), PokeDetailResponse.class);
        actionResponse.setName(pokeDetailResponse.getName());
        actionResponse.setSuccess(true);
        myPokemonList.deleteById(id);
        return actionResponse;
    }

    @PostMapping("/renamePokemon")
    public ActionResponse renamePokemon(@RequestParam Long id, String name) {
        ActionResponse actionResponse = new ActionResponse();
        MyPokemon myPokemon = myPokemonList.findById(id).orElse(new MyPokemon());
        if (name == null || name.equalsIgnoreCase("") || name.equalsIgnoreCase(myPokemon.getName())) {
            return new ActionResponse(myPokemon.getName(), false);
        }
        actionResponse.setName(name);
        actionResponse.setSuccess(true);
        myPokemon.setName(name);
        myPokemon.setRenameCounter(0);
        myPokemonList.save(myPokemon);
        return actionResponse;
    }

    @GetMapping("/getMyPokemon")
    public MyPokemonResponseList getMyPokemon(@RequestParam(required = false) Integer page, @RequestParam(required = false) String name) throws JsonProcessingException {
        List<MyPokemonResponse> responses = new ArrayList<>();

        if (name == null || name.equalsIgnoreCase("")) {
            List<MyPokemon> myPokemon = myPokemonList.findAll(Pageable.ofSize(12).withPage(page)).stream().collect(Collectors.toList());
            for (MyPokemon pokemon : myPokemon) {
                MyPokemonResponse myPokemonResponse = new MyPokemonResponse();
                myPokemonResponse.setId(pokemon.getId());
                myPokemonResponse.setName(pokemon.getName());
                myPokemonResponse.setRenameCounter(pokemon.getRenameCounter());
                myPokemonResponse.setDetail(objectMapper.readValue(pokemon.getDetail(), PokeDetailResponse.class));
                responses.add(myPokemonResponse);
            }
        } else {
            if (page == null) {
                page = 0;
            }
            name = "%".concat(name).concat("%");
            List<MyPokemon> myPokemon = myPokemonList.findByName(name, Pageable.ofSize(12).withPage(page));
            if (myPokemon != null && !myPokemon.isEmpty()) {
                for (MyPokemon pokemon : myPokemon) {
                    MyPokemonResponse myPokemonResponse = new MyPokemonResponse();
                    myPokemonResponse.setId(pokemon.getId());
                    myPokemonResponse.setName(pokemon.getName());
                    myPokemonResponse.setRenameCounter(pokemon.getRenameCounter());
                    myPokemonResponse.setDetail(objectMapper.readValue(pokemon.getDetail(), PokeDetailResponse.class));
                    responses.add(myPokemonResponse);
                }
            }
        }

        return new MyPokemonResponseList(page, responses);
    }

    private static int fib(int n)
    {
        if (n==0||n==1)
            return 0;
        else if(n==2)
            return 1;
        return fib(n - 1) + fib(n - 2);
    }



//    @PostMapping("/getPokemonDetail/{pokemonName}")
//    public Pokemon saveContact(@RequestParam String pokemonName) {
//        contactList.save(contactRequest);
//
//        return contactRequest;
//    }
//
//    @DeleteMapping("/deleteContact")
//    public void deleteContact(Long id) {
//        contactList.deleteById(id);
//    }
}
