import './navbar.styles.css'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

const Navbar = ({ className, searchClickHandler, myClickHandler }) => {
  return (
    <nav className="navbar">
      <div className="container">
        <div className="logo">
          Poke Dex
        </div>
        <div className="nav-elements">
          <ul>
            <Link to="/">
              <button
                className={`nav-button ${className}`}
                onClick={searchClickHandler}
              >Search Pokemon</button>
              Home</Link>
            <Link to="/my-pokemon">
              <button
                className={`nav-button ${className}`}
                onClick={myClickHandler}
              >My Pokemon</button>
              Home</Link>
          </ul>
        </div>
      </div>
    </nav>
  )
}

export default Navbar