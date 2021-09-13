# Markup calculator service

## About

Service to calculate the sale price of products from the Markup index. If the product is imported, calculate the cost based on the purchase price + shipping in dollars, obtaining the currency quote from a central bank webservice from the purchase date.

## Tech stack
> Kotlin (chosen language)  
> Gradle (build tool)  
> Javalin (web framework)  
> Hexagonal (architecture)  
> Exposed (ORM)  
> CockroachDB (database)  
> Koin (dependency injection)  
> Fuel (HTTP client)  
> Jackson + Kotlin Bindings (object serialization)  
> Joda Money (money lib)  
> Kotest (test framework)  
> Testcontainer (integration test library)  
> log4j2 (logs)  
> Swagger/Open API (documentation)  
> Kafka (messageing)  
> NATS (messageing/streaming)

## Quick note

Project in early stage of development!