import './next-button.styles.css';

const NextButton = ({ className, onClickHandler }) => (
    <button
        className={`next-button ${className}`}
        onClick={onClickHandler}
    >Next</button>
)

export default NextButton;