"use client";
import { invalidatePerson } from "./GET-person-fetcher";
import { baseUrl } from "./properties";

export function deletePerson(id: string) {
  fetch(`${baseUrl}/v1/person/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((resp) => {
    invalidatePerson();
  });
}
