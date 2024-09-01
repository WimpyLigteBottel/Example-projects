"use client";

import { Person } from "@person/person";
import DeleteButton from "@/component/delete-button";
import Spinner from "@/component/spinner/spinner";
import { usePerson } from "@/integration/GET-person-fetcher";

function displayPerson(person: Person, index: number) {
  return (
    <div key={index} className={"person"}>
      <span>name:{person.name}</span>
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

  return (
    <div>
      <span>People</span>
      {persons!.map((person, index) => displayPerson(person, index))}
    </div>
  );
};

export default PersonsComponent;
