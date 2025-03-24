import { deletePerson } from "../integration/DELETE-person-fetcher";
import { useState } from "react";
import Spinner from "./spinner/spinner";

const DeleteButton = ({ person }) => {
  const [isLoading, setIsLoading] = useState(false);

  return (
    <button
      className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
      onClick={() => {
        setIsLoading(!isLoading);
        deletePerson(person.id);
      }}
    >
      {!isLoading ? "Remove" : <Spinner />}
    </button>
  );
};

export default DeleteButton;
