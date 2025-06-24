import { QueryClient, useQuery, useQueryClient } from '@tanstack/react-query'
// import { createSyncStoragePersister } from '@tanstack/query-sync-storage-persister'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      gcTime: 1000 * 60 * 60 * 24, // 24 hours
    },
  },
})

// const persister = createSyncStoragePersister({
//   storage: window.localStorage,
// })

function usePosts(amount) {
  return useQuery({
    queryKey: ['delay'],
    queryFn: async (): Promise<String> => {
          return (await fetch(`http://localhost:8080/delay?amount=${amount}`)).text()
    },
  })
}
