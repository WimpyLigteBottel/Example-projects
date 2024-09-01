"use client";
import { createRandomPerson } from "@/integration/POST-person-fetcher";
import { useState } from "react";
import Spinner from "@/component/spinner/spinner";

const CreateButton = () => {
  const [isLoading, setIsLoading] = useState(false);

  return (
    <button
      className="clickMeBtn"
      disabled={isLoading}
      onClick={() => {
        setIsLoading(true);
        createRandomPerson();
        setTimeout(() => {
          setIsLoading(false);
        }, 100);
      }}
    >
      {!isLoading ? "Create random User!" : <Spinner />}
    </button>
  );
};

export default CreateButton;
