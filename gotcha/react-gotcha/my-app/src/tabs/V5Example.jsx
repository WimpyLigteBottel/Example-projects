import './Example.css'
import {useEffect, useState} from "react";


export function V5Example() {

    const [counter, setCounter] = useState('blank')

    console.log('1', counter)
    useEffect(() => {
        console.log('2', counter)
        return () => {
            console.log('3', counter)
        }
    }, []);

    return (
        <>
            <h1>V5 - Console log twice?!</h1>

            <button onClick={() => setCounter(crypto.randomUUID())} className={"button-green"}>
                Console log me!
            </button>
        </>
    )
}


export default V5Example
