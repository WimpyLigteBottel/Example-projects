import { Person } from "./Person";
import { v4 as uuidv4 } from "uuid";

export async function helloName(name: string) {
  try {
    let url = `http://localhost:8080/v1/hello?name=${name}`;
    const response = await fetch(url);

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.text();
    return JSON.stringify(data); // Convert data to a string
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
    return ""; // Return an empty string in case of error
  }
}

export async function getPersons(): Promise<Person[]> {
  try {
    let url = `http://localhost:8080/v1/person/`;
    const response = await fetch(url);

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
    return [];
  }
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

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
    return null;
  }
}
