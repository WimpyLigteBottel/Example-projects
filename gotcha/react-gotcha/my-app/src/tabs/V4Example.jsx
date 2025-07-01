import './Example.css'
import {useEffect, useRef, useState} from "react";


async function fetchInformation(delay,abortController) {
    return await fetch(`https://dummyjson.com/products/?delay=${delay}&limit=5`, {signal: abortController.signal})
        .then(res => res.json())
        .catch((error)=>{
            console.error('FAILED: ',error)
            return {products: []}
        })
}


export function V4Example() {

    const [products, setProducts] = useState([])
    const [loading, setLoading] = useState(false)
    let abortController = useRef(null);


    useEffect(() => {
        return () => {
            // Cleanup function to abort any pending requests
            if (abortController.current) {
                abortController.current.abort();
            }
        };
    }, []);

    // 1. multi fetching
    // 2, how to fix?
    // 3. Do the secret thing ;) Nav


    const goFetchHandler = async () => {
        console.log('Gotcha V4: Start')
        setLoading(true)

        if(abortController.current){
            abortController.current.abort('Another request happened')
        }

        let newC = new AbortController()
        abortController.current = newC

        let fetchData = await fetchInformation(2000,newC)
            .finally(() => {
                setLoading(false)
            })

        setProducts(fetchData["products"])

    }

    return (
        <>
            <h1>V4 - Double click submit problem!!!!</h1>
            <div className="card">
                <button  onClick={() => goFetchHandler()} className={"button-green"}>
                    Fetch data
                </button>



                <br/>
                <br/>
                <br/>
                {loading ? 'loading' : itemListDisplay(products)}
            </div>
        </>
    )
}

function itemListDisplay(items) {
    return <ul>
        {items.map((item,index) => {
            return (
                <li key={index}>
                    {item.id} - {item.title}
                </li>
            );
        })}
    </ul>;
}


export default V4Example
