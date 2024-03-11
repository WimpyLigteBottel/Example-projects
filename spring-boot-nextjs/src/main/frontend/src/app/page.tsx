"use client";
import { useState, useEffect } from "react";
import {
  helloName,
  getPersons,
  createPerson,
  createRandomPerson,
  deletePerson,
} from "./DataFetcher";
import { Person } from "./Person";
import { v4 as uuidv4 } from "uuid";

export default function Home() {
  const [persons, setPersons] = useState<Person[]>([]);

  useEffect(() => {
    const setupPersons = async () => {
      let tempPersons = await getPersons();
      setPersons(tempPersons);
    };

    setupPersons();
  }, []); // Empty dependency array to run effect only once when component mounts

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div>
        {persons.map((person) => (
          <div key={uuidv4()}>
            <span>name:{person.name} </span>
            <span>age: {person.age} </span>
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              onClick={async () => {
                await deletePerson(person.id);
                setPersons(await getPersons());
              }}
            >
              Remove
            </button>
            <br />
          </div>
        ))}
      </div>

      <button
        onClick={async () => {
          await createRandomPerson();
          setPersons(await getPersons());
        }}
      >
        Click me!
      </button>
    </main>
  );
}
