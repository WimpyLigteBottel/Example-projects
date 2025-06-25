import { useState } from 'react'
import './Example.css'


export function V1Example() {
  const [counter, setCounter] = useState(0)

  const handleButtonAction = (value) => {
    setCounter(counter + value)

    // 1. What will the counter be?

    // 2. What will it be now?
    // setCounter(counter + value)
  }

  return (
    <>
      <h1>V1 - react counter gotcha!</h1>
      <div className="card">
        <button onClick={() => handleButtonAction(1)} className={"button-green"}>
          +
        </button>

        <button onClick={() => handleButtonAction(-1)} className={"button-red"}>
          -
        </button>
      </div>

      <h1>{counter}</h1>
    </>
  )
}

export default V1Example
