# WeatherAPP JPMC

## Description
Android weather application that shows the weather of the current location using MVVM as part of a challenge
following the known Model-view-ViewModel (MVVM) architecture pattern, The App request current location and retrieve
`Current Weather`, `Dayly Weather Forecast` which is the forecast for the next 24 hours in range of 3 hours 
and `Week Weather Forecast` that is the forecast for the next 5 days.



## Funcionality
- WeatherAPP request current location in order to show the weather information related to your current address
- If user denied current location the App will show a weather from a custom city.
- If Location is disabled the app request enable location
- Also WeatherAPP is able to show the `Current Weather` from any city of US.
- If a city is not found the App will show a weather from a custom city.


## Open Weather API
[Open Weather Map API](https://openweathermap.org/api) is the API to get the real weather information of a city 

## Screenshots

## Architecture

The most important principle to follow is separation of concerns. It's a common mistake to write all your code in an Activity or a Fragment. 
These UI-based classes should only contain logic that handles UI and operating system interactions. 
By keeping these classes as lean as possible, you can avoid many problems related to the component lifecycle, and improve the testability of these classes.

Another important principle is that you should drive your UI from data models, preferably persistent models. Data models represent the data of an app. 
They're independent from the UI elements and other components in your app. This means that they are not tied to the UI and app component lifecycle, 
but will still be destroyed when the OS decides to remove the app's process from memory.


There are three layers:

UI Layer (MainActivity
Domain Layer (optional)
Data Layer



## Features
|-------------------------|:---------------------------:|
| `Coding Language:`      |           Kotlin            |
| `Architecture Pattern:` | Model-View-ViewModel (MVVM) |
| `Network library:`      |          Retrofit           |
| `Network concurrency:`  |      Kotlin Coroutines      |
| `UI:`                   |         ViewBinding         |
| `Dependency Injection:` |         Dagger-Hilt         |
| `Unit test:`            |                             |

