
# GitHub

An app for viewing cryptocurrency related info like market capitalisation of the coin or the info about it.

Created with clean architecture in mind, divided to framework, presentation, data, domain and usecases layers.

The project consists of two modules.
- core
- app

### Core
Core module is a Java library that contains the core logic of the app wrapped in data, domain and usecases layers.

Data layer consists data sources and repositories.
Repositories are classes that aggregate data sources defined as interfaces in the same layer.
This way we inverse dependencies, so core logic is separated from external dependencies and expose the interfaces the implementatiosn have to follow.

Domain layer consists the models definitions.

Usecases layer contains use cases which represent separate action that user can invoke on the app.

Because core library is Java library without any android dependencies, it can be shared between projects, it can be implemented for example as an web application.


### App
App layer contains framework and presentation layers. Here Dagger is configured to inject all the dependencies from core module.

Framework layer has definitions of database and Retrofit data sources implementations.

Presentation layer consists of Activity, View Models and composables definitions.

### External data provider

Project uses Coinmarketcap api (https://coinmarketcap.com/api/documentation/v1/). Api often sends a HTTP 429 error when facing too many requests within too little timeframe.
This is represented as an exception in code, which is handled quietly and printed in logcat.
App doesn't crash but it can happen that it will stop displaying some crypto results. In that case user should wait a bit before sending another request.
The fix would be to move to different data provider, although Coinmarketcap seems to have best API among all the provides.
