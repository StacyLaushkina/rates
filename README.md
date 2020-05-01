# Rates app

Small application with one screen, which shows 168 different currency values.
It automatically updates approximately every 2 hours. It is possible to manually request a new update, but delay between them must be 30 minutes or more.
By changing the value of one currency, you can see how the values of others change accordingly.

## Getting Started

No special actions required. After cloning repository you can immediately start working with it.

## Running the tests

Tests cover UI and domain logic.

```
 ./gradlew app:test
```

## Libraries used

* [RxJava2] - Reactive extensions for easier threads managing
* [Retrofit] - Network requests manager
* [Room] - Persistence Library
* [Work-rxjava] - Worker manager for tasks scheduling. Uses AlarmManager and JobScheduler depending on android version
* [Dagger2] - DI framework
* [Junit4] - Testing engine
* [Mockitokotlin2] - Extensions for mocking in Kotlin tests

## Versioning

I used [SemVer](http://semver.org/) for versioning.

## Authors

* **Anastasia Laushkina** - *Initial work*


## Acknowledgments

* Inspired by Revolut
