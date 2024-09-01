import { deletePerson } from "@/integration/DELETE-person-fetcher";

const DeleteButton = ({ person }) => {
  return (
    <button
      className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
      onClick={() => {
        deletePerson(person.id);
      }}
    >
      Remove
    </button>
  );
};

export default DeleteButton;
