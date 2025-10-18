import './Example.css';
import { useEffect, useState } from "react";
import { useQuery } from '@tanstack/react-query'
import { usePerson } from './PersonUtil'



export function V6_Details() {
  const { isPending, isError, data, error } = usePerson();

  if (isPending) return <div>Loading...</div>;
  if (isError) return <div>Error: {error.message}</div>;

  return (
    <div className="p-4 space-y-4">
       <h1>Person Details</h1>
       {JSON.stringify(data, null, 2)}
    </div>
  );
}


export default V6_Details;
