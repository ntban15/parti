# Parti

Parti as in *party* and *participate*. This application allows users to host real-time events and broadcast them on Google Maps. It also features searching for nearby events and joining in chat rooms.

This application is developed under 72 hours as an assignment for my school-based mobile development course.

## Demo

[![Parti Demo](https://i.imgur.com/xVSSH5B.png)](https://youtu.be/yDbAIvNSbJI "Parti Demo")

## Screenshots

<p display="block" margin="auto">
    <img src="https://i.imgur.com/5sCBkRM.png" width="150"/>
    <img src="https://i.imgur.com/BwjXqaO.png" width="150"/>
    <img src="https://i.imgur.com/MLDtuLV.png" width="150"/>
    <img src="https://i.imgur.com/rc2Lowg.png" width="150"/>
    <img src="https://i.imgur.com/ZPclDHg.png" width="150"/>
    <img src="https://i.imgur.com/XqDmHHl.png" width="150"/>
</p>

## Features

- Hosting real-time events on Google Maps.
- Giving directions to an event based on current location.
- Real-time chat rooms.

## Applied Techniques

- Model-View-Presenter pattern.
- Communicating with Google Maps API using Retrofit integrated with GSON.
- Database management with Firebase.
- Using Bottom Sheet to display information of events.
- Customizing Google Maps to a different style.

## External Libraries, SDKs, and APIs

- Android Design Support library
- Google Maps API
- [Butterknife](http://jakewharton.github.io/butterknife/)
- [GreenRobot EventBus](http://greenrobot.org/eventbus/)
- [Retrofit](http://square.github.io/retrofit/)
- [GSON](https://github.com/google/gson)
- Firebase

## Install and build

- Clone the repository
- Generate an API key on [Google APIs](https://console.developers.google.com/apis/) using your own SHA-1 signature. More info [here](https://stackoverflow.com/questions/15727912/sha-1-fingerprint-of-keystore-certificate).
- Paste said API key into the auto-generated Google Maps API XML file.
- Compile and run.