import { useEffect, useMemo, useState } from 'react';
import './Example.css'




export function V3Example() {
  const [counter, setCounter] = useState(0)
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')

  const person = {
    firstName,
    lastName
  }

  useEffect(() => {
    // Warning this is a mean one :P 
    // 1. How often will this print
    // 2. When i click green button what will happen?
    // 3. (@marco make the change)
    console.log(person)
  }, [firstName, lastName])

  return (
    <>
      <h1>V3 - use effect!</h1>
      <div className="card">
        <input value={firstName} onChange={event => setFirstName(event.target.value)} />
        <input value={lastName} onChange={event => setLastName(event.target.value)} />

        <button
          onClick={() => setCounter(counter + 1)}
          className={"button-green"}>
          {counter}
        </button>
      </div >
    </>
  )
}


export default V3Example
