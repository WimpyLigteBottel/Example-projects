import { createRandomPerson } from "./person/POST-person-fetcher";

const CreateButton = () => {
  return (
    <button
      className="clickMeBtn"
      onClick={() => {
        createRandomPerson();
      }}
    >
      Click me!
    </button>
  );
};

export default CreateButton;
