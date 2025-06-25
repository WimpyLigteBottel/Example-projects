import { useState } from 'react'
import { fetchDataV2 as fetchData } from './FetchUtil'
import './Example.css'

export function V3Example() {
  const [waitAmount, setWaitAmount] = useState(1000)

  const handleFetch = async () => {

  }

  return (
    <>
      <h1>V3 - Loading try catch</h1>
      <div className="card">
        <button onClick={() => setWaitAmount(waitAmount + 1000)}>
          Increase wait amount ({waitAmount}ms)
        </button>
      </div>
    </>
  )
}

export default V3Example
