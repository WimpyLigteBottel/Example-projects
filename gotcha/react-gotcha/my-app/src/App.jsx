import { useState } from 'react'
import './App.css'
import { V1Example } from './tabs/V1Example'
import { V2Example } from './tabs/V2Example'
import { V3Example } from './tabs/V3Example'



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
  }
}


function App() {
  const [displayView, setDisplayView] = useState("V1")

  return <>
    <div>
      <button onClick={() => setDisplayView("V1")}>1</button>
      <button onClick={() => setDisplayView("V2")}>2</button>
      <button onClick={() => setDisplayView("V3")}>3</button>
    </div>
    <div>
      {getView(displayView)}
    </div>
  </>

}








export default App
