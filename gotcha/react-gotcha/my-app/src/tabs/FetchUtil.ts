import { useQuery } from "@tanstack/react-query"

export async function fetchData(amount, shouldFail) {
    console.log(`"CALLED!" ${amount}`)
    const data = await fetch(`http://localhost:8080/delay?amount=${amount}`)
        .then(res => {
            if (shouldFail) {
                throw Error("Failed")
            }

            console.log('DONE!')
            return res.text()
        }
        )
        .catch(error => {
            throw error
        })

    return data
}



export async function fetchDataV2(amount, shouldFail) {
    console.log(`"Fetch with delay called!" ${amount}`)

    const data = await fetch(`http://localhost:8080/delay?amount=${amount}`)
        .then(res => {

            if (shouldFail) {
                throw Error("Failed")
            }

            return res.text()
        }
        )
        .catch(error => {
            throw error
        }).finally(() => {
            console.log('DONE!')
        })

    return data
}





export function useFetchData(waitAmount, shouldFail) {
    let data = useQuery({
        queryKey: [`fetchdata-${waitAmount}`],
        queryFn: async () => await fetchDataV2(waitAmount, shouldFail),
        enabled: true,
    })


    return data;
}
