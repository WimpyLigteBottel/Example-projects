"use client"
import { Person } from "./Person";
import { v4 as uuidv4 } from "uuid";
import { invalidatePerson } from "./GET-person-fetcher";

export function createRandomPerson() {
  let randomAge = Math.floor(Math.random() * 100);

  const newPerson = new Person("", uuidv4(), randomAge);

  createPerson(newPerson).then((response) => {
    invalidatePerson();
  });
}

export function createPerson(person: Person) {
  return fetch(`http://localhost:8080/v1/person/`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(person),
  });
}
