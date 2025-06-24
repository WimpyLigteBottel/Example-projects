import { useEffect, useState } from 'react'
import { useFetchData } from './FetchUtil'
import './Example.css'
import { useQueryClient } from '@tanstack/react-query'

export function V5Example({ waitTime }) {
  const [waitAmount, setWaitAmount] = useState(1000)

  const client = useQueryClient()

  const { data, isLoading, isError } = useFetchData(waitAmount, false,)

  if (isError) {
    return <div>'Error!...' </div>
  }


  if (isLoading) {
    return <div>'Loading...' </div>
  }

  return (
    <>
      <h2>V5 - reactquery (caching)</h2>
      <div className="card">
        <button onClick={() => setWaitAmount(waitAmount + 100)}>
          Increase wait amount ({waitAmount}ms)
        </button>
        <br /><br /><br />

        <button onClick={() => {
          setWaitAmount(1000)
        }} className={"button-green"}>
          reset
        </button>

        <button onClick={() => {
          client.removeQueries({ queryKey: [`fetchdata-${waitAmount}`] })
        }} className={"button-red"}>
          Clear current cache!!!
        </button>

        <button onClick={() => {
          client.removeQueries()
        }} className={"button-red"}>
          Clear ALL!!!
        </button>
      </div >
    </>
  )
}

export default V5Example
