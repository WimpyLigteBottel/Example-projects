"use client";
import { invalidatePerson } from "@/integration/GET-person-fetcher";
import { baseUrl } from "@/integration/properties";

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
