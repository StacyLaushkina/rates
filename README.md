# Rates app

Small application with one screen, which shows 32 different currency values.
It automatically updates every second.
By changing the value of one currency, you can see how the values of others change accordingly.

## Getting Started

No special actions required. After cloning repository you can immediately start working with it.

## Running the tests

Tests cover only UI logic.

```
 ./gradlew app:test
```

## Libraries used

* [RxJava2] - Reactive extensions for easier threads managing
* [Retrofit] - Network requests manager
* [Room] - Persistence Library
* [Work-rxjava] - Worker manager for tasks scheduling. Uses AlarmManager and JobScheduler depending on android version
* [Junit] - Testing engine
* [Mockitokotlin2] - Extensions for mocking in Kotlin tests

## Versioning

I used [SemVer](http://semver.org/) for versioning.

## Authors

* **Anastasia Laushkina** - *Initial work*


## Acknowledgments

* Inspired by Revolut
