# Starry Cosmo - Cosmo Connected

Happy to share this technical test with you and I hope you'll enjoy playing with this app :)

I'll add, through this README document, some details on the choices I made and how the app works.

# How the app works

The app is pretty simple. When launched, it'll fetch cosmo devices through a remote API and display them in a clear way.
The items are displayed in simple rows on a first screen (with their model, category & mac address) and, if you want some details on a specific device, you can click on it and you'll be redirected to a detailed View.
This detailed View contains the device's category, model, mac address, serial number, firmware version, installation mode, light mode (auto, status, current brightness) & brake light status. If an information isn't provided for a given device, it simply isn't displayed.

This main navigation View can redirect to a second navigation View which contains the Bluetooth part with a screen that let you search for nearby BLE devices and click on it to connect to them and show their services & characteristics (with the type of properties & their UUID).
This nested navigation graph has been used to properly separate concerns and have a clear/understandable code.

# The choices I made

The global idea was to use a clean and modern architecture to maintain a high quality of code while not losing too much time on useless abstraction.
The project is based on clean architecture (with domain/presentation/repository layers, that I could have put into different modules but I though it'll be "too much" for such a simple project), and an MVVM pattern on the View-side.

## UI

I used Jetpack Compose to design better Views quicker, and Compose Navigation because it fits perfectly in this project.
The app is organized around a single Activity, which contains a main navigation View with two screens and a nested navigation View for the Bluetooth part. Each of these screens has a ViewModel, which map data from the repositories and make it UI-ready.
I used an observer pattern, with the View observing a State & UIActions (if needed) from the ViewModel, to perform proper actions and update UI accordingly.

I also added a "design" package which contains the ColorPalette (only in Light mode, an improvement would be to add support of Dark mode here, with proper configuration and a dedicated Palette), a custom Font I wanted to use in this project, and some customs Views as an attempt to have a very very small Design System here :)
For example, I designed the InformationCard to suit various needs and be my main component for the Views to be consistent across the app while simplifying the code.

## Data layer

I used Retrofit to easily access remote resources from the REST API and map them to an API model (the model is then mapped to the business model to have a clear separation between the model the API returns and the one we need)
I also added a DeviceRepository which is the main orchestrator between the ViewModels and the APIs (REST + Bluetooth). I could have split this part with two more sub-repositories, one for the API side, the other one for the Bluetooth side, but I though it wasn't necessary since the project is still very simple and it wasn't worth it for such a small project.
Another improvement would have been to add data persistence through Room.

## Dependency Injection

I used Hilt as a DI tool because it's the more powerful one on Android, especially with the architecture I designed, requiring a small quantity of code while filling all my needs.

## Testing

I focused the testing part on where the logic lies : the ViewModels. In this project, I didn't tested the Repositories implementation since the DeviceRepository is quite simple and essentially map data with very little logic.
To avoid UI problems, I also added previews of different states (thanks to Jetpack Compose Preview powerful system) for all the Views & screens I created to directly see how the changes I make on the code are reflected visually.

## Improvements that could be made

In the improvements side, I though of data persistence through Room, a Dark Mode, the ability to schedule updates of data instead of just displaying an error message notifying that we can't get data without an Internet connection, the detection of the return of Internet connection if getData() fail due to lack of Internet connection instead of waiting for a fixed time (5s)

# How to use the app ?

Just clone this repository, open the cloned folder with Android Studio and let the magic happens ;) 

