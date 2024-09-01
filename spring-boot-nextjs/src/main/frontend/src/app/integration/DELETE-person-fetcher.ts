"use client";
import { invalidatePerson } from "@/integration/GET-person-fetcher";

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
