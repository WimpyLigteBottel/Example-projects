import './Example.css';
import { useEffect, useState } from "react";
import { useQuery } from '@tanstack/react-query'

export async function fetchPerson() {
    const data = fetch(`http://localhost:8080/person`)
        .then(res => res)
        .catch(error => {
            throw error
        })

    return data
}

export function usePerson() {
    let data = useQuery({
        queryKey: [`person`],
        queryFn: async () =>  await fetchPerson(),
    })

    return data;
}



export function V6Example() {

    let blankPerson = {
        name: 'marco'
    }

    const [person, setPerson] = useState(blankPerson)

    return (
        <>
         <div>
             <h1>V6 - You gotta cache them all?!</h1>
             {JSON.stringify(blankPerson)}
          </div>
        </>
    )
}


export default V6Example
