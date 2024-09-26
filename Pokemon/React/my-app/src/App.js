import { useState, useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import PokemonList from "./components/pokemon-list/PokemonList";
import MyPokemon from "./components/my-pokemon/MyPokemon";
import Navbar from "./components/shared/navbar/navbar-component.jsx"

const App = () => {
  const [listState, setListState] = useState(true);
  const [myState, setMyState] = useState(false);

  const searchClickHandler = async () => {
      setListState(true);
      setMyState(false);
  };
  const myClickHandler = async () => {
    setListState(false);
    setMyState(true);
};

  return (
    <div className="App">
      
       <Navbar
         searchClickHandler={searchClickHandler}
         myClickHandler={myClickHandler}
         className="monster-nav-button"/>
      <Routes>
        <Route path="/" element={ <PokemonList
       listState={listState}/> } />
        <Route path="/my-pokemon" element={ <MyPokemon
      myState={myState}/> } />
      </Routes>
    </div>
    // <div className="App">
    //   <PokemonList
    //    listState={listState}/>
    //   <MyPokemon
    //   myState={myState}/>
    // </div>
  );
};

export default App;
