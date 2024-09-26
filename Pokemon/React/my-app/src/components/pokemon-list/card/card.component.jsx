import './card.style.css';
import * as React from 'react';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import "../../shared/loading/loading.style.css";

const Card = ({ monster }) => {
    const { id, name, url } = monster;
    const [openRelease, setOpenRelease] = React.useState(false);
    const [detail, setDetail] = React.useState(null);
    const handleOpenRelease = () => { setOpenRelease(true); }
    const handleCloseRelease = () => { setOpenRelease(false); }
    const [message, setMessage] = React.useState("");
    const [loading, setLoading] = React.useState(false);

    //need to add modal popup if catch fail / success
    const catchHandler = async () => {
        setLoading(true);
        fetch("http://localhost:8081/getPokemonDetail?id=" + monster.id, {
            method: "GET",
            headers: {
                Accept: "application/json",
                "Access-Control-Allow-Origin": "*",
                "Content-Type": "application/json",
            },
        })
            .then((response) => response.json())
            .then((pokemonDetail) => {
                fetch("http://localhost:8081/catchPokemon", {
                    method: "POST",
                    headers: {
                        Accept: "application/json",
                        "Access-Control-Allow-Origin": "*",
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ pokemonDetail })
                })
                    .then(response => response.json())
                    .then(message => { setMessage(message.message); 
                        setLoading(false); })
                    .finally(handleOpenRelease);
            })
    }

    return (
        <div className="card-container" key={id}>
            {
                loading ?
                    (
                        <div className="loader-container">
                            <div className="spinner"></div>
                        </div>
                    )
                    :
                    (
                        <div>
                            <center>
                                <img
                                    width={120}
                                    height={120}
                                    alt={`monster ${name}`}
                                    src={url} />

                            </center>
                            <h2>{name.charAt(0).toUpperCase() + name.slice(1)}</h2>
                            <button
                                className={`catch-button`}
                                onClick={catchHandler}
                            >Catch Pokemon</button>

                            <Modal
                                open={openRelease}
                                onClose={handleCloseRelease}
                                aria-labelledby="modal-modal-title"
                                aria-describedby="modal-modal-description"
                            >
                                <Box className="modal-body">
                                    <div>
                                        <h2>{message}</h2>
                                    </div>
                                </Box>
                            </Modal>
                        </div>
                    )
            }

        </div>)
}

export default Card;