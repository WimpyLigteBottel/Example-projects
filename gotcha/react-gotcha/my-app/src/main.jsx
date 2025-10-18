import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import ProviderContext from './ProviderContext.jsx'


createRoot(document.getElementById('root')).render(
    <StrictMode>
        <ProviderContext/>
    </StrictMode>,
)
