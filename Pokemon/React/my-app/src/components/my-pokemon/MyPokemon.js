import { useState, useEffect } from "react";

import CardList from "./card-list/card-list.component";
import SearchBox from "./search-box/search-box.component.jsx";
import Next from "../shared/next/next-button.component.jsx";
import Previous from "../shared/previous/previous-button.component.jsx";
import "./MyPokemon.css";
import "../shared/loading/loading.style.css";

const MyPokemon = ({ myState }) => {
  const [monsters, setMonsters] = useState([]);
  const [offset, setOffset] = useState(0);
  const [display, setDisplay] = useState("");
  const [searchField, setSearchField] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    myState = true;
    if (myState) {
      setDisplay("");
    } else {
      setDisplay("none");
    }
  }, [myState]);

  useEffect(() => {
    setLoading(true);
    if (myState) {
      fetch("http://localhost:8081/getMyPokemon?page=0")
        .then((response) => response.json())
        .then((poke) => {
          setMonsters(poke.myPokemonList);
          setOffset(poke.page);
          setLoading(false);
        });
    }
  }, [myState]);

  const onSearchChange = (event) => {
    const searchFieldString = event.target.value.toLocaleLowerCase();
    setSearchField(searchFieldString);
    if (searchFieldString.length !== 0) {
      fetch("http://localhost:8081/getMyPokemon?name=" + searchFieldString)
        .then((response) => response.json())
        .then((poke) => {
          poke.myPokemonList.length === 0
            ? setMonsters(monsters)
            : setMonsters(poke.myPokemonList);
          poke.myPokemonList.length === 0
            ? setOffset(offset)
            : setOffset(parseInt(poke.page));

        });
    } else {
      fetch("http://localhost:8081/getMyPokemon?page=0")
        .then((response) => response.json())
        .then((poke) => {
          setMonsters(poke.myPokemonList);
          setOffset(poke.page);
        });
    }
  };

  const afterRenameHandler = async () => {
    fetch("http://localhost:8081/getMyPokemon?page=0")
      .then((response) => response.json())
      .then((poke) => {
        setMonsters(poke.myPokemonList);
        setOffset(poke.page);
      })
      .then(() => {
        window.location.reload();
      });
  };

  const onClickNext = async () => {
    setLoading(true);
    var finalOffset = offset + 1;
    if (searchField.length === 0) {
      fetch("http://localhost:8081/getMyPokemon?page=" + finalOffset)
        .then((response) => response.json())
        .then((poke) => {
          poke.myPokemonList.length === 0
            ? setMonsters(monsters)
            : setMonsters(poke.myPokemonList);
          setLoading(false);
          poke.myPokemonList.length === 0
            ? setOffset(offset)
            : setOffset(parseInt(poke.page));
          setLoading(false);
        });
    } else {
      fetch(
        "http://localhost:8081/getMyPokemon?page=" +
          finalOffset +
          "&name=" +
          searchField
      )
        .then((response) => response.json())
        .then((poke) => {
          poke.myPokemonList.length === 0
            ? setMonsters(monsters)
            : setMonsters(poke.myPokemonList);
          setLoading(false);
          poke.myPokemonList.length === 0
            ? setOffset(offset)
            : setOffset(parseInt(poke.page));
          setLoading(false);
        });
    }
  };

  const onClickPrev = async () => {
    setLoading(true);
    var finalOffset = offset - 1;
    if (finalOffset < 0) {
      finalOffset = 0;
    }
    if (searchField.length === 0) {
      fetch("http://localhost:8081/getMyPokemon?page=" + finalOffset)
        .then((response) => response.json())
        .then((poke) => {
          poke.myPokemonList.length === 0
            ? setMonsters(monsters)
            : setMonsters(poke.myPokemonList);
          setLoading(false);
          poke.myPokemonList.length === 0
            ? setOffset(offset)
            : setOffset(parseInt(poke.page));
          setLoading(false);
        });
    } else {
      fetch(
        "http://localhost:8081/getMyPokemon?page=" +
          finalOffset +
          "&name=" +
          searchField
      )
        .then((response) => response.json())
        .then((poke) => {
          poke.myPokemonList.length === 0
            ? setMonsters(monsters)
            : setMonsters(poke.myPokemonList);
          setLoading(false);
          poke.myPokemonList.length === 0
            ? setOffset(offset)
            : setOffset(parseInt(poke.page));
          setLoading(false);
        });
    }
  };

  return (
    <div style={{ display: display }} className="App">
      {loading ? (
        <div className="loader-container">
          <h1 className="search-title">Preparing The Zoo</h1>
          <div className="spinner"></div>
        </div>
      ) : (
        <div>
          <h1 className="app-title">My Pokemon</h1>

          <SearchBox
            placeholder="Search Pokemon"
            onChangeHandler={onSearchChange}
            className="monster-search-box"
          />

          <CardList
            monsters={monsters}
            afterRenameHandler={afterRenameHandler}
          />

          <center>
            <Previous
              onClickHandler={onClickPrev}
              className="monster-previous-button"
            />
            <Next
              onClickHandler={onClickNext}
              className="monster-next-button"
            />
          </center>
        </div>
      )}
    </div>
  );
};

export default MyPokemon;
