"use client";
import { createRandomPerson } from "@/integration/POST-person-fetcher";

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
