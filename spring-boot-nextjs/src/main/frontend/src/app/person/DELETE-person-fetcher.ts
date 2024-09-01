"use client"
import { Person } from "./Person";
import { v4 as uuidv4 } from "uuid";
import { invalidatePerson } from "./GET-person-fetcher";

export function deletePerson(id: string) {
  fetch(`http://localhost:8080/v1/person/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((resp) => {
    invalidatePerson();
  });
}
