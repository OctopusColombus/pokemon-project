import './card.style.css';
import * as React from 'react';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import '../../shared/loading/loading.style.css';


const Card = ({ monster, className, afterRenameHandler }) => {

    const [open, setOpen] = React.useState(false);
    const [openRelease, setOpenRelease] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleOpenRelease = () => { setOpenRelease(true); }
    const handleCloseRelease = () => { setOpenRelease(false); window.location.reload(); }
    const [newName, setNewName] = React.useState(null);
    const [message, setMessage] = React.useState("");
    const [loading, setLoading] = React.useState(false);

    const renamehandler = (event) => {
        const newName = event.target.value;
        setNewName(newName);
    }

    console.log(monster);
    const { id, name, detail } = monster;

    const deleteButtonHandler = async () => {
        setLoading(true);
        if (id != null) {
            fetch("http://localhost:8081/releasePokemon/?id=" + id, {
                method: "DELETE",
                headers: {
                    Accept: "application/json",
                    "Access-Control-Allow-Origin": "*",
                    "Content-Type": "application/json",
                },
            }).then((response) => response.json())
                .then((message) => { setMessage(message.message) })
                .then(() => {setLoading(false)})
                .finally(handleOpenRelease);
        }
    }

    const renameButtonhandler = async () => {
        setLoading(true);
        if (newName !== null && newName.length !== 0 && newName !== name) {
            fetch("http://localhost:8081/renamePokemon/?id=" + id + "&name=" + newName, {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Access-Control-Allow-Origin": "*",
                    "Content-Type": "application/json",
                },
            }) 
                .then(() => {setNewName(null); setLoading(false);})
                .finally(afterRenameHandler)
        } else {
            setLoading(false);
        }
    }

    return (
        <div className="card-container" key={id}>
            {loading ?
                (<div className="loader-container">
                    <div className="spinner"></div>
                </div>)
                :
                (<div>
                    <center>
                        <img
                            width={120}
                            height={120}
                            alt={`monster ${name} back`}
                            src={detail.sprites.back_default} />
                        <img
                            width={120}
                            height={120}
                            alt={`monster ${name} back`}
                            src={detail.sprites.front_default} />
                        <input
                            className={`rename-box ${className}`}
                            type="search"
                            placeholder={name.charAt(0).toUpperCase() + name.slice(1)}
                            onChange={renamehandler}
                        />

                    </center>
                    <div>
                    <button
                        className={`detail-button ${className}`}
                        onClick={handleOpen}
                    >Detail</button>
                    </div>
                    <div>
                    <button
                        className={`delete-button ${className}`}
                        onClick={deleteButtonHandler}
                    >Release Pokemon</button>
                    </div>
                    <div>
                    <button
                        className={`rename-button ${className}`}
                        onClick={renameButtonhandler}
                    >Rename Pokemon</button>
                    </div>

                    <Modal
                        open={open}
                        onClose={handleClose}
                        aria-labelledby="modal-modal-title"
                        aria-describedby="modal-modal-description"
                    >
                        <Box className="modal-body">
                            <div>
                                <center>
                                    <img
                                        width={200}
                                        height={200}
                                        alt={`monster ${name} back`}
                                        src={detail.sprites.back_default} />
                                    <img
                                        width={200}
                                        height={200}
                                        alt={`monster ${name} back`}
                                        src={detail.sprites.front_default} />
                                </center>
                                <h2>{name.charAt(0).toUpperCase() + name.slice(1)}</h2>
                            </div>
                            <div className='detail'>
                                <div className='detail-content'>
                                    <span>
                                        <h3>Weight : {detail.weight}</h3>
                                        <h3>Height : {detail.height}</h3>
                                        <h3>Experience : {detail.base_experience}</h3>
                                    </span>
                                </div>
                                <div className='detail-content'>
                                    <span>
                                        <h3>Type : {detail.types.map((type, i) => {
                                            if (i !== (detail.types.length - 1)) {
                                                return type.type.name.charAt(0).toUpperCase() + type.type.name.slice(1) + ", ";
                                            } else {
                                                return type.type.name.charAt(0).toUpperCase() + type.type.name.slice(1);
                                            }
                                        })}
                                        </h3>
                                        <h3>Abilities : {detail.abilities.map((ability, i) => {
                                            if (i !== (detail.abilities.length - 1)) {
                                                return ability.ability.name.charAt(0).toUpperCase() + ability.ability.name.slice(1) + ", ";
                                            } else {
                                                return ability.ability.name.charAt(0).toUpperCase() + ability.ability.name.slice(1);
                                            }
                                        })}
                                        </h3>
                                    </span>
                                </div>
                            </div>
                        </Box>
                    </Modal>


                    <Modal
                        open={openRelease}
                        onClose={handleCloseRelease}
                        aria-labelledby="modal-modal-title"
                        aria-describedby="modal-modal-description"
                    >
                        <Box className="modal-body">
                            <div>
                                <center>
                                    <img
                                        width={200}
                                        height={200}
                                        alt={`monster ${name} back`}
                                        src={detail.sprites.back_default} />
                                    <img
                                        width={200}
                                        height={200}
                                        alt={`monster ${name} back`}
                                        src={detail.sprites.front_default} />
                                </center>
                                <h2>{message}</h2>
                            </div>
                        </Box>
                    </Modal>
                    </div>
                )}
        </div>
    )
}

export default Card;