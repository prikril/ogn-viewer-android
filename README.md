# OGN Viewer - FLARM Radar

## Description
Android app that visualizes the aircraft traffic from [The Open Glider Network project (OGN)](http://glidernet.org) on a map.

OGN can receive signals from gliders, tow planes, helicopters, etc.

This app is a flight radar for small aircraft with FLARM equipment.

The decoded signals can be used with apps like XCSoar on TCP port 4353.

Current version: 1.4.2

See [release notes](release-notes.md) for details.

Download the app at https://play.google.com/store/apps/details?id=com.meisterschueler.ognviewer


## Features
Track aircraft with FLARM devices via the OGN and see OGN receivers on a map.

See flight paths of aircraft - track only (default) or with heights (multicolor option in settings).

Use APRS filter for filtering oder long click on the map to set a radius filter.

App can run in background and listen to OGN messages. Use exit function to save battery and data!

Other apps can connect on TCP port 4353 (must be activated in settings).

Manage known aircraft in "ManageIDs" activity or click long on the info window of an aircraft.


## Dependencies
The app uses two repositories from Meisterschueler:

https://github.com/Meisterschueler/ogn-commons-java

and

https://github.com/Meisterschueler/ogn-client-java

Clone the Java7 files from "Android" branch!

You need Eclipse and Maven to build the jar files.

Use "maven install" to build them.


## Building
Use Android Studio to build the app.

Replace the google_maps_key in google_maps_api.xml with your own values.

Go to https://console.developer.google.com and sign in with your credentials.

Add a new project for ogn-viewer.

Generate a new api key and copy it to the xml file.

Add the package name "com.meisterschueler.ognviewer" to your api key.

You need your own SHA1 fingerprint too.

Use `keytool -list -v -keystore mykeystorepath.keystore` to get it.


