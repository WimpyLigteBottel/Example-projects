"use client";

import { Person } from "@person/person";
import DeleteButton from "./delete-button";
import Spinner from "./spinner/spinner";
import { usePerson } from "../integration/GET-person-fetcher";
import { v4 as uuid4 } from "uuid";

function displayPerson(person: Person, index: number) {
  return (
    <div key={uuid4()} className={"person"}>
      <span>name: {person.name}</span>
      <span>age: {person.age}</span>
      <br />
      <DeleteButton person={person} />
    </div>
  );
}

const PersonsComponent = () => {
  const { persons, isLoading } = usePerson<Person>();

  if (isLoading) {
    return <Spinner />;
  }

  if (!persons || persons.length == 0) {
    return <div> no people.... </div>;
  }

  return (
    <div>{persons!.map((person, index) => displayPerson(person, index))}</div>
  );
};

export default PersonsComponent;
