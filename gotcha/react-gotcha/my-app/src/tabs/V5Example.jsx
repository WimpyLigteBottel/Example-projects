import './Example.css'
import { useEffect, useState } from "react";


export function V5Example() {
    const [text, setText] = useState('blank')

    //This is mean one
    // 1. Will the printout be, if i open the page?
    // A. 1,2,3
    // B. 3,2,1
    // C. 1,2,3 1,2,3
    // D. something else

    // 2. When i click console log (what will be printed?)

    // A 1,2,3
    // B 3,2,1
    // C 1,2,3 1,2,3
    // D something else

    console.log('1', text)
    useEffect(() => {
        console.log('2', text)
        return () => { console.log('3', text) }
    }, []);


    const updateCounter = () => {
        setText(crypto.randomUUID())
    }

    return (
        <>
            <h1>V5 - Console log twice?!</h1>

            <button onClick={updateCounter} className={"button-green"}>
                Console log me!
            </button>
        </>
    )
}


export default V5Example
