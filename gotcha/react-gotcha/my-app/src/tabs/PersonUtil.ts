import {
       useQuery,
       useMutation,
       useQueryClient
     } from "@tanstack/react-query"

const PERSON_KEY = 'person'

// FETCHING PERSON
async function fetchPerson() {
  const res = await fetch(`http://localhost:8080/person`);
  if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
  return await res.json();
}

export function usePerson() {
  const { isPending, isError, data, error } = useQuery({
    queryKey: [PERSON_KEY],
    queryFn: fetchPerson,
  });

  return { isPending, isError, data, error };
}

// UPDATE PERSON

async function updatePerson(person) {
  const res = await fetch(
    `http://localhost:8080/person?name=${person.name}&surname=${person.surname}&age=${person.age}`,
    { method: "PUT" }
  );
  if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
  return await res.json();
}

export function useUpdatePersonMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (person) => updatePerson(person), // function, not call
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PERSON_KEY] });
    },
  });
}