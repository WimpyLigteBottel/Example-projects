import { useState } from 'react'
import { fetchDataV2 as fetchData } from './FetchUtil'
import './Example.css'




export function V2Example() {
  const [waitAmount, setWaitAmount] = useState(1000)
  const [isLoading, setIsLoading] = useState(false)

  const handleFetch = async (waitAmount, shouldFail) => {
    setIsLoading(true)
    await fetchData(waitAmount, shouldFail)
    setIsLoading(false)
  }

  if (isLoading) {
    return <div>'Loading...' </div>
  }

  return (
    <>
      <h1>V2 - Loading included</h1>
      <div className="card">
        <button onClick={() => setWaitAmount(waitAmount + 1000)}>
          Increase wait amount ({waitAmount}ms)
        </button><br /><br /><br />

        <button
          onClick={() => handleFetch(waitAmount, false)}
          disabled={isLoading}
          className={"button-green"}>
          {"SUCCESS"}
        </button>

        <button
          onClick={() => handleFetch(waitAmount, true)}
          disabled={isLoading}
          className={"button-red"}>
          {"I WILL ALWAYS FAIL!"}
        </button>
      </div>
    </>
  )
}

export default V2Example
