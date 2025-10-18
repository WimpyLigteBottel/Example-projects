import './Example.css';
import { useEffect, useState } from "react";
import { useQuery } from '@tanstack/react-query'

async function fetchPerson() {
  const res = await fetch(`http://localhost:8080/person`);
  if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
  return await res.json();
}

async function updatePerson(person) {
  const res = await fetch(
    `http://localhost:8080/person?name=${person.name}&surname=${person.surname}&age=${person.age}`,
    { method: "PUT" }
  );
  if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
  return await res.json();
}

function usePerson() {
  const { isPending, isError, data, error } = useQuery({
    queryKey: ['person'],
    queryFn: fetchPerson,
  });

  return { isPending, isError, data, error };
}

export function V6Example() {
  const { isPending, isError, data, error } = usePerson();

  const [person, setPerson] = useState({
    name: '',
    surname: '',
    age: ''
  });

  // when data loads from API, populate local state
  useEffect(() => {
    if (data) setPerson(data);
  }, [data]);

  if (isPending) return <div>Loading...</div>;
  if (isError) return <div>Error: {error.message}</div>;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPerson(prev => ({
      ...prev,
      [name]: name === 'age' ? Number(value) : value
    }));
  };

  const handleUpdate = async () => {
    try {
      const updated = await updatePerson(person);
      alert("Person updated!");
      console.log(updated);
    } catch (err) {
      alert("Update failed: " + err.message);
    }
  };

  return (
    <div className="p-4 space-y-4">
      <h1>V6 - Person Editor</h1>

      <div className="space-y-2">
        <label>
          Name:{" "}
          <input
            type="text"
            name="name"
            value={person.name}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Surname:{" "}
          <input
            type="text"
            name="surname"
            value={person.surname}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Age:{" "}
          <input
            type="number"
            name="age"
            value={person.age}
            onChange={handleChange}
          />
        </label>
      </div>

      <button onClick={handleUpdate}>Update Person</button>

      <pre>{JSON.stringify(person, null, 2)}</pre>
    </div>
  );
}

export default V6Example;
