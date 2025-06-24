import { useState } from 'react'
import { fetchData } from './FetchUtil'
import './Example.css'


export function V1Example() {
  const [waitAmount, setWaitAmount] = useState(1000)

  return (
    <>
      <h1>V1 - no loading</h1>
      <div className="card">
        <button onClick={() => setWaitAmount(waitAmount + 1000)}>
          Increase wait amount ({waitAmount}ms)
        </button><br /><br /><br />

        <button onClick={() => fetchData(waitAmount, false)} className={"button-green"}>
          {"SUCCESS"}
        </button>

        <button onClick={() => fetchData(waitAmount, true)} className={"button-red"}>
          {"I WILL ALWAYS FAIL!"}
        </button>
      </div>
    </>
  )
}

export default V1Example
