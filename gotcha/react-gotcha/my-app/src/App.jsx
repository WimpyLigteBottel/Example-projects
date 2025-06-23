import { useState } from 'react'
import './App.css'
import { V1Example } from './tabs/V1Example'
import { V2Example } from './tabs/V2Example'
import { V3Example } from './tabs/V3Example'
import { V4Example } from './tabs/V4Example'

import { QueryClient } from '@tanstack/react-query'
import { PersistQueryClientProvider } from '@tanstack/react-query-persist-client'
import { createSyncStoragePersister } from '@tanstack/query-sync-storage-persister'



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
        <PersistQueryClientProvider
          client={queryClient}
          persistOptions={{ persister }}
        >
          <div>
            <V4Example waitTime={1000} />
            <V4Example waitTime={3000} />
          </div>
        </PersistQueryClientProvider>)
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
    </div>
    <div>
      {getView(displayView)}
    </div>
  </>

}



const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      gcTime: 1000 * 60 * 60 * 24, // 24 hours
    },
  },
})

const persister = createSyncStoragePersister({
  storage: window.localStorage,
})




export default App
