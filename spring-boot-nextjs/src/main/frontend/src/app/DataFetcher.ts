import { Person } from "./Person";
import { v4 as uuidv4 } from "uuid";
import useSWR from 'swr'
import { mutate } from "swr"



export function usePerson() {
  const { data, error, isLoading } = useSWR(`/v1/person/`, getPersons)

  return {
    persons: data,
    isLoading,
    isError: error
  }
}


export function invalidatePerson(){
   mutate('v1/person/')
}

export async function getPersons(): Promise<Person[]> {
  return await fetch(`http://localhost:8080/v1/person/`)
    .then((res) => res.json())
    .catch(err => {
         console.error("There was a problem with the fetch operation:", err);
         return [];
    })
}

export async function deletePerson(id: string) {
  try {
    let url = `http://localhost:8080/v1/person/${id}`;
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    invalidatePerson()

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
    return false;
  }
  return true;
}

export async function createRandomPerson() {
  let randomAge = Math.floor(Math.random() * 100);

  await createPerson(new Person("", uuidv4(), randomAge));
}

export async function createPerson(person: Person) {
  try {
    let url = `http://localhost:8080/v1/person/`;

    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(person),
    });

    invalidatePerson()


    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
    return null;
  }
}
