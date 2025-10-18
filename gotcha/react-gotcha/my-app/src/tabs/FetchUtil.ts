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
