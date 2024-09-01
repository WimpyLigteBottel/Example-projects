"use client";
import { useState, useEffect } from "react";
import { usePerson } from "./person/GET-person-fetcher";
import { createRandomPerson } from "./person/POST-person-fetcher";
import { deletePerson } from "./person/DELETE-person-fetcher";

import { Person } from "./person/person";
import CreateButton from "./create-button";

function Home() {
  const { persons, isLoading } = usePerson();

  if (isLoading) {
    return <div> Loading... </div>;
  }

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div>
        <span>People</span>
        {persons!.map((person, index) => (
          <div key={index} className={"person"}>
            <span>name:{person.name}</span>
            <span>age: {person.age}</span>
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              onClick={() => {
                deletePerson(person.id);
              }}
            >
              Remove
            </button>
            <br />
          </div>
        ))}
      </div>

      <CreateButton />
    </main>
  );
}

export default Home;
