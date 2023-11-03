# What is this project about?
- This example project showcasing how to use graphql
- How to create spring boot project exposing graphql


### _What dependencies is used?_
- graphql (starter)
- h2 (database in-memory)
- jpa (quering th database)
- caching (caffeine)



### Why using the certain dependencies?
- graphql (starter)
  - This should be fairly obvious i wanted to play around with it and learn more aobut how it works

- h2 (in-memory database)
  - I did not want to spin up entire database and felt that h2 as in memory database was good enough

- jpa (quering the database)
  - Just existing orm to use to query the db

- caching (caffeine)
  - I have noticed that depending on how the  query is setup you can do 
  a lot of queries to the database which could result in N+1 queries. I believe
  that graphql has caching libaries but i have not touched it yet.




### Security wise?
By default my project is not secured by spring-web-security at all but there is few things to be
aware of when setting up grahpql project.


Always check owasp to get idea of the common attack vectors.

- Common Attacks
- Injection - this usually includes but is not limited to:
  - SQL and NoSQL injection
  - OS Command injection
  - SSRF and CRLF injection/Request Smuggling
- DoS (Denial of Service)
- Abuse of broken authorization: either improper or excessive access, including IDOR
- Batching Attacks, a GraphQL-specific method of brute force attack
- Abuse of insecure default configurations
  


### Gotcha's to be aware off?

You need to be very careful to use `@SchemaMapping` because it can easily cause
N+1 Queries if you are not careful.


```
    @QueryMapping
    fun findAuthor(
        @Argument id: Long? = null,
        @Argument firstName: String? = null,
        @Argument lastName: String? = null
    ) = authorService.findAll(...).firstOrNull()
    
    @SchemaMapping)
    fun books(author: Author) = bookRepo.findAllByAuthor(author)
```

In the case below if you do the following query you will get the author
and it will get the books for that author. That is where the schemamapping will kicking

```graphql
query findAuthor{
  findAuthor(id: 1){
    id
    lastName
    firstName
    books{
      id
      name
      pageCount
    }
  }
}

```

The result of the above query

```json
{
  "data": {
    "findAuthor": {
      "id": "1",
      "lastName": "lastname-30845",
      "firstName": "firstName-5618",
      "books": [
        {
          "id": "1",
          "name": "Effective Java 9199",
          "pageCount": 4
        },
        {
          "id": "2",
          "name": "Effective Java 2084",
          "pageCount": 280
        }
      ]
    }
  }
}
```

Where the important part kicks in is below with the hibernate queries that gets executed

```
Hibernate: select a1_0.id,a1_0.first_name,a1_0.last_name from author a1_0
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
```
Nothing seems out of place yet but what happens if you query multiple authors?

```graphql
query findAUTHORS{
  findAuthors(pageSize: 10){
    id
    lastName
    firstName
    books{
      id
      name
      pageCount
    }
  }
}
```

Below sql queries kicking off:

```
2023-11-03T10:36:26.508+01:00  INFO 10607 --- [nio-8080-exec-1] c.e.demo.author.AuthorGraphQLController  : findAuthors [page=null;pageSize=10]
Hibernate: select a1_0.id,a1_0.first_name,a1_0.last_name from author a1_0
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
```

### Why is this happening?
Due to the way my `@SchemaMapping` is setup. If i am trying to access the books
of the author then the schema mapping will get all the books for that author IF it
is mentioned in the query.


### But WAIT THERE IS MORE...
How many sql queries do you think will happen when doing the following query?

```graphql
query findAuthors{
  findAuthors(pageSize: 2){
    id
    lastName
    firstName
    books{
      id
      name
      pageCount
      author{
        id
        books{
          id
        }
      }
    }
  }
}
```
We are doing 6 queries. (3 more)
```
findAuthors [page=null;pageSize=2]
Hibernate: select a1_0.id,a1_0.first_name,a1_0.last_name from author a1_0
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
```
where if we had caching it looked as follows:
```
findAuthors [page=null;pageSize=2]
Hibernate: select a1_0.id,a1_0.first_name,a1_0.last_name from author a1_0
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
```

And if we were to update to find 4 authors without caching its 10 queries!

```
findAuthors [page=null;pageSize=4]
Hibernate: select a1_0.id,a1_0.first_name,a1_0.last_name from author a1_0
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
```


and if we were to do it with caching its 5 queries

```
findAuthors [page=null;pageSize=4]
Hibernate: select a1_0.id,a1_0.first_name,a1_0.last_name from author a1_0
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
Hibernate: select b1_0.id,b1_0.author_id,b1_0.name,b1_0.page_count from book b1_0 where b1_0.author_id=?
```