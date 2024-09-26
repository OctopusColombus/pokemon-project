package pokemon.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pokemon.config.RestTemplateRequestResponseLogger;
import pokemon.domain.ActionResponse;
import pokemon.domain.CatchRequest;
import pokemon.domain.MyPokemonResponse;
import pokemon.domain.MyPokemonResponseList;
import pokemon.domain.PokeApiResponse;
import pokemon.domain.PokeDetailResponse;
import pokemon.domain.PokeFormResponse;
import pokemon.domain.Pokemon;
import pokemon.domain.PokemonResponse;
import pokemon.entity.MyPokemon;
import pokemon.entity.MyPokemonList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PokemonServices {

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

    public PokemonResponse getPokemon(String offset) {
        PokeApiResponse pokeApiResponse = restTemplate.exchange(pokemonListUrl+"/?offset="+offset+"&limit=12", HttpMethod.GET, null, PokeApiResponse.class).getBody();
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
        return response;
    }

    public ActionResponse catchPokemon(CatchRequest request) throws JsonProcessingException, InterruptedException {
        Thread.sleep(1000);
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
            actionResponse.setMessage("You have caught " + actionResponse.getName() + ", You can check it on your pokemon list");

            return actionResponse;
        }
        actionResponse.setSuccess(false);
        actionResponse.setMessage(actionResponse.getName() + " is stronger than you! Wanna try again?");

        return actionResponse;
    }

    public PokeDetailResponse getPokemonDetail(String id) throws InterruptedException {
        Thread.sleep(1000);
        return restTemplate.getForObject(pokemonDetailUrl+id, PokeDetailResponse.class);
    }

    public ActionResponse releasePokemon(Long id) throws InterruptedException, JsonProcessingException {
        Thread.sleep(1000);
        System.out.println("CEK");
        ActionResponse actionResponse = new ActionResponse();
        MyPokemon myPokemon = myPokemonList.findById(id).orElse(new MyPokemon());
        if (fib(rand.nextInt(10)) % 2 == 0) {
            return new ActionResponse(myPokemon.getName(), "Your Pokemon Still Loves You!", false);
        }
        PokeDetailResponse pokeDetailResponse = objectMapper.readValue(myPokemon.getDetail(), PokeDetailResponse.class);
        actionResponse.setName(pokeDetailResponse.getName());
        actionResponse.setSuccess(true);
        actionResponse.setMessage("Why did you release it? :(");
        myPokemonList.deleteById(id);
        return actionResponse;
    }

    public ActionResponse renamePokemon(Long id, String name) throws InterruptedException {
        Thread.sleep(1000);
        ActionResponse actionResponse = new ActionResponse();
        MyPokemon myPokemon = myPokemonList.findById(id).orElse(new MyPokemon());
        if (name == null || name.equalsIgnoreCase("") || name.equalsIgnoreCase(myPokemon.getName())) {
            return new ActionResponse(myPokemon.getName(), "", false);
        }
        actionResponse.setName(name);
        actionResponse.setSuccess(true);
        myPokemon.setName(name);
        myPokemon.setRenameCounter(0);
        myPokemonList.save(myPokemon);
        return actionResponse;
    }

    public MyPokemonResponseList getMyPokemon(Integer page, String name) throws InterruptedException, JsonProcessingException {
        Thread.sleep(1000);
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
}
