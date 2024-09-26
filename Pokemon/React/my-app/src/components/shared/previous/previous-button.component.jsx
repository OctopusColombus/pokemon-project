import './previous-button.styles.css';

const PreviousButton = ({ className, onClickHandler }) => (
    <button
        className={`previous-button ${className}`}
        onClick={onClickHandler}
    >Prev</button>
)

export default PreviousButton;