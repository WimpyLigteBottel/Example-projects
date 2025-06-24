import { useState } from 'react'
import { useFetchData } from './FetchUtil'
import './Example.css'

export function V4Example({ waitTime }) {
  const [waitAmount, setWaitAmount] = useState(1000)
  const [isFailed, setFailed] = useState(false)

  const { data, isLoading, isError } = useFetchData(waitAmount, isFailed,)

  if (isError) {
    return <div>'Error!...' </div>
  }

  if (isLoading) {
    return <div>'Loading...' </div>
  }

  return (
    <>
      <h2>V4 - reactquery</h2>
      <h3>Is it going to fail? ({`${isFailed}`})</h3>
      <div className="card">
        <button onClick={() => setWaitAmount(waitAmount + 100)}>
          Increase wait amount ({waitAmount}ms)
        </button>
        <br /><br /><br />

        <button onClick={() => {
          setFailed(false)
        }} className={"button-green"}>
          SUCCESS
        </button>


        <button
          onClick={() => setFailed(true)}
          className={"button-red"}>
          {"I WILL ALWAYS FAIL!"}
        </button>
      </div >
    </>
  )
}

export default V4Example
