import './Example.css';
import { useEffect, useState } from "react";
import { usePerson, useUpdatePersonMutation  } from './PersonUtil'

export function V6Example() {
    const { isPending, isError, data, error } = usePerson();
    const mutation = useUpdatePersonMutation();

  const [person, setPerson] = useState({
    name: '',
    surname: '',
    age: ''
  });

    if (isPending) return <div>Loading...</div>;
    if (isError) return <div>Error: {error.message}</div>;

  // when data loads from API, populate local state
  useEffect(() => {
    if (data) setPerson(data);
  }, [data]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPerson(prev => ({
      ...prev,
      [name]: name === 'age' ? Number(value) : value
    }));
  };

 const handleUpdate = async () => {
    try {
      await mutation.mutateAsync(person); // ✅ Trigger the mutation properly
    } catch (err) {
      alert("Update failed: " + err.message);
    }
  };

  return (
    <div className="p-4 space-y-4">
      <div>
          <h1>V6 - Cache me baby one more time?</h1>
                <div className="space-y-2">
                  <label>
                    Name:{" "}
                    <input type="text" name="name" value={person.name} onChange={handleChange} />
                  </label>
                  <br />
                  <label>
                    Surname:{" "}
                    <input type="text" name="surname" value={person.surname} onChange={handleChange} />
                  </label>
                  <br />
                  <label>
                    Age:{" "}
                    <input  type="number"  name="age"  value={person.age}  onChange={handleChange}/>
                  </label>
                </div>
              <button onClick={handleUpdate} disabled={mutation.isPending}>
                {mutation.isPending ? "Updating..." : "Update Person"}
              </button>
      </div>
    </div>
  );
}

export default V6Example;
