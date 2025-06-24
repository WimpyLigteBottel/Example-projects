import { useState } from 'react'
import './App.css'
import { V1Example } from './tabs/V1Example'
import { V2Example } from './tabs/V2Example'
import { V3Example } from './tabs/V3Example'
import { V4Example } from './tabs/V4Example'
import { V5Example } from './tabs/V5Example'




function getView(displayView) {
  switch (displayView) {
    case "V1": {
      return <V1Example />
    }
    case "V2": {
      return <V2Example />
    }
    case "V3": {
      return <V3Example />
    }
    case "V4": {
      return (
        <div>
          <V4Example waitTime={1000} />
        </div>
      )
    }

    case "V5": {
      return (
        <div>
          <V5Example waitTime={1000} />
          <V5Example waitTime={1000} />
        </div>
      )
    }
  }
}


function App() {
  const [displayView, setDisplayView] = useState("V1")

  return <>
    <div>
      <button onClick={() => setDisplayView("V1")}>1</button>
      <button onClick={() => setDisplayView("V2")}>2</button>
      <button onClick={() => setDisplayView("V3")}>3</button>
      <button onClick={() => setDisplayView("V4")}>4</button>
      <button onClick={() => setDisplayView("V5")}>5</button>
    </div>
    <div>
      {getView(displayView)}
    </div>
  </>

}








export default App
