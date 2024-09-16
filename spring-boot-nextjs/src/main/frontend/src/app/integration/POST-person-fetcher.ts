"use client";
import { Person } from "@/model/Person";
import { v4 as uuidv4 } from "uuid";
import { invalidatePerson } from "@/integration/GET-person-fetcher";
import { baseUrl } from "@/integration/properties";

export function createRandomPerson() {
  const newPerson = new Person("", uuidv4(), Math.floor(Math.random() * 100));

  createPerson(newPerson).then((response) => {
    invalidatePerson();
  });
}

export function createPerson(person: Person) {
  return fetch(`${baseUrl}/v1/person/`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(person),
  });
}
