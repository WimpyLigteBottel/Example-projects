"use client";
import { useState, useEffect } from "react";
import {
    usePerson,
    invalidatePerson,
  createPerson,
  createRandomPerson,
  deletePerson,
} from "./DataFetcher";
import { Person } from "./Person";
import useSWR, { SWRConfig } from 'swr'


export default function Home() {
  const {persons, isLoading} = usePerson()

    if(isLoading){
        return <div> Loading... </div>
    }

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div>
        <span>People</span>
        {persons!.map((person,index) => (
          <div key={index} className={"person"}>
            <span>name:{person.name}</span>
            <span>age: {person.age}</span>
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              onClick={async () => {
                await deletePerson(person.id);
                await invalidatePerson();
              }}
            >
              Remove
            </button>
            <br />
          </div>
        ))}
      </div>

      <button
        className="clickMeBtn"
        onClick={async () => {
          await createRandomPerson();
          await invalidatePerson();
        }}
      >
        Click me!
      </button>
    </main>
  );
}
