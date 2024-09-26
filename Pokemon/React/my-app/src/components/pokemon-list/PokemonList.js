import { useState, useEffect } from "react";

import CardList from "./card-list/card-list.component";
import Next from "../shared/next/next-button.component.jsx";
import Previous from "../shared/previous/previous-button.component.jsx";
import "./PokemonList.css";
import "../shared/loading/loading.style.css";

const PokemonList = ({ listState }) => {
  const [monsters, setMonsters] = useState([]);
  const [offset, setOffset] = useState(0);
  const [display, setDisplay] = useState("");
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    listState = true;
    if (listState) {
      setDisplay("");
    } else {
      setDisplay("none");
    }
  }, [listState]);

  useEffect(() => {
    setLoading(true);
    fetch("http://localhost:8081/getPokemonList/?offset=0")
      .then((response) => response.json())
      .then((poke) => {
        setLoading(false);
        setMonsters(poke.pokemonList);
        setOffset(parseInt(poke.offset));
      });
  }, []);

  const onClickNext = async () => {
    setLoading(true);
    var finalOffset = offset + 12;
    fetch("http://localhost:8081/getPokemonList/?offset=" + finalOffset)
      .then((response) => response.json())
      .then((poke) => {
        setLoading(false);
        setMonsters(poke.pokemonList);
        setOffset(parseInt(poke.offset));
      });
  };

  const onClickPrev = async () => {
    setLoading(true);
    var finalOffset = offset - 12;
    if (finalOffset < 0) {
      finalOffset = 0;
    }
    fetch("http://localhost:8081/getPokemonList/?offset=" + finalOffset)
      .then((response) => response.json())
      .then((poke) => {
        setLoading(false);
        setMonsters(poke.pokemonList);
        setOffset(parseInt(poke.offset));
      });
  };

  return (
    <div style={{ display: display }} className="App">
      {loading ? (
        <div className="loader-container">
            <h1 className="search-title">Searching For Pokemon</h1>
          <div className="spinner">

          </div>
        </div>
      ) : (
        <div>
          <h1 className="app-title">Poke Dex</h1>

          <CardList monsters={monsters} />

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

export default PokemonList;
