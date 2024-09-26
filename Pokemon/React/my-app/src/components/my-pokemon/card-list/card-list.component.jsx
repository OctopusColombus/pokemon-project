import './card-list.styles.css';
import Card from "../card/card.component.jsx";

const CardList = ({ monsters, afterRenameHandler }) => (
    <div className="card-list">
        {monsters.map((monster) => {
            return <Card monster = {monster} afterRenameHandler={afterRenameHandler} />
        })}
    </div>
);

export default CardList;