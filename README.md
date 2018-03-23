# OGN Viewer - FLARM Radar

## Description
Android app that visualizes the aircraft traffic from [The Open Glider Network project (OGN)](http://glidernet.org) on a map.

OGN can receive signals from gliders, tow planes, helicopters, etc.

This app is a flight radar for small aircraft with FLARM equipment.

The decoded signals can be used with apps like XCSoar on TCP Port 4353.

Current version: 1.3.6

See [release notes](release-notes.md) for details.

Download the app at https://play.google.com/store/apps/details?id=com.meisterschueler.ognviewer

## Dependencies
The app uses two repositories from glidernet:

https://github.com/glidernet/ogn-commons-java

and

https://github.com/glidernet/ogn-client-java

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

