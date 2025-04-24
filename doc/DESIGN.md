# Design Choices

## Disclaimer

I've worked for Mercado Libre as a software engineer in the Accounts team. This is the core team in charge of handling
all the movements of money in Mercado Pago. So my vision of a production system for this purpose might be somewhat biased 
by my experience. 

## Build system

For building the project I've selected Gradle over Maven as per my personal preferences. I prefer the tidiness of Gradle's
DSL over the boilerplate of XML used by Maven. Also, I like the agnostic approach of Gradle not having to preinstall the tool
as it offers the Gradle Wrapper.

## Java Version

The latest version of Java [supported by the latest version of Gradle (8.13)](https://docs.gradle.org/8.13/userguide/compatibility.html#compatibility) is Java 23, 
but I've just selected the latest LTS version of Java available which is Java 21. 
I did not make the decision based on any modern feature of the language, apart from using Records which has been available 
since Java 17.


## Web Framework

The chosen web framework was Spring, because of its wide adoption by the Java community. I'm fully aware of the increased footprint
it requires to run but at the very same time it provides a solution to some other challenges such as persistence (Spring-data-JPA)
and it's very well known by almost everyone, resulting in an easier to understand code base. 

## Other limitations, choices and tradeoffs

### Privacy of Data

As per GDPR and similar laws around the world, I've tried to keep out any data which may be labelled as personal information,
such as names, addresses, emails, phone numbers, and so on.
I assumed there is a separate service keeping that kind of data protected to comply with those regulations and there is only
one attribute connecting a user to its wallet, which I've assumed as an all-purpose userId.
That's why I request a userId to be specified when creating a wallet. 

### Multiple Wallets per user

As in some countries it's allowed to have more than one bank account per user with different currencies, I've designed the system 
so that a single userId can be associated with multiple wallets as long as the wallets have different country or currency. 
I've left out of scope the validation of currencies allowed for a certain country, because I felt I hadn't enough time to build that validation.

### Balance and Wallet 1 to 1 relationship

As the entities (and rows in the database) for wallets and balances are related 1 to 1, the same primary key can be used for both entities. 
There is a specific way of accomplishing that by using the @MapsId annotation. By that approach, the attribute annotated will be replaced by
the attribute annotated with @Id. In this way the PK is also a foreign key at the same time. This is a performance optimization.

See [article](https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/) for a deep explanation about this technique.

Both the wallet and the balance could have been the same entity and table with attributes merged together. I wanted to be able 
to tell them apart so that it would be possible to query the wallets of a user without getting its balance, and vice versa.

### Accidental Duplication of code

At a first glance, the code in the api layer might look very similar to the code in the model and entity layers.  
In my experience there are good reasons to keep the objects apart in each layer, to avoid changes in one layer unwittingly reaching other layers.
This is something that is usually overseen by most developers, but could make it difficult to evolve the system over time. 

In the book "Clean Architecture" by Robert Martin (ISBN 978-0-13-449416-6), chapter 16 ("Independence") it states: 

"... But there are different kinds of duplication. There is true duplication, in which every change to one instance necessitates 
the same change to every duplicate of that instance. Then there is false or accidental duplication. If two apparent duplicated 
sections of code evolve along different paths --if they change at different rates, and for different reasons-- then they are 
not true duplicates. Return to them in a few years, and you'll find that they are very different from each other."

I've crafted some mappers to translate these objects between all three layers (api/model/entity) as a good practice.

Unfortunately, as Java doesn't allow to define import aliases (such as Scala and Kotlin does), I had to create objects 
with different names (such as ApiWallet, Wallet, and WalletEntity) to avoid having to specify the classes by its full package name
in order to disambiguate. This became specially required in the Mapper classes in which the translation methods requires the
types to be stated. 

### Package folders (slicing)

In chapter 21 of the same Clean Architecture book, ("Screaming Architecture") the author advices to use folders for each 
use case or domain entity (Balance, Movement, Wallet, etc.) instead of just naming all the layers after the samples given by Spring.
Most developers are used to the structure given by Spring documentation (controllers, services, model, entities, api, etc.) 
So I've decided not to follow Robert Martin advice, and stayed with the Spring way of slicing folders and packages. 

This is not a good practice for larger systems, as more and more entities are added, the folders become crowded with just 
too many classes. But in this particular project, and for the sake of clarity, I've used the approach that is discouraged.

### TDD

Due to the lack of time, I've missed all unit tests in favour of creating end-to-end tests to be run with postman, curl, insomnia
or any REST tool. With the limited size of this project it might not be worth to spend time creating unit tests, except for people
writing the tests before the actual code (TDD). To properly test this project a set of combined tests would be necessary, including
unit tests, integration tests, and end-to-end tests. 

As a certified SWE by the ASQ (American Society for Quality) I usually spend time writing tests, as a way of playing with the code base, 
to stress the system to its boundaries, to prove some assumptions, to leave some working examples, and to be sure before facing any refactor.

In this particular case, I had to sacrifice the tests due to the tight due dates given to solve the challenge. 

### Optimistic Locking of Balance

The version attribute in the Balance annotated with @Version is used for optimistic locking.
This avoids unwanted and unpredictable concurrent modifications of the same balance, which could produce a corrupted balanced amount.
Optimistic locking has been selected over pessimistic locking, because it doesn't require further operations over the database engine,
and it's simpler to use in the ORM.

### Other attributes of the database

I've also put some attributes to the database layer, such as update or creation timestamps, mostly because I needed them at some point in time. 
The version attribute of the balances, apart from being useful for locking, is increased by 1 on each movement applied, 
becoming handy to identify accounts subject to high update rates. By sorting the balances by version in descending order
anyone could spot a balances with higher version numbers, which means they are updated at higher rates, thereby being a 
strong candidate for detaching the update of the balance from the insertion of the movements. 
Other attributes such as the lastMovementId might come handy if for some reason the database is corrupted, and it's needed
to perform any restoration or consistency checks.

# Time Tracking

TBD...

