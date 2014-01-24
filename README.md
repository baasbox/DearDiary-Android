# Dear Diary BaasBox Sample

BaasBox is a tool that allows you to quickly build a back end for your app.
You can install it on your local machine (ideal for development and testing) or your own server.
It comes with an SDK that simplifies the integration of your Android app with the server.
In this tutorial you will learn how to integrate an existing Android application with BaasBox.

To complete this walk through you need a working copy of the Android SDK.
You can download the starter project from [DearDiary-Android-Starter]().

Unzip and run the project to see if it works as expected, it's a simple personal diary
composed of three activities, a list of your notes, the details, and an activity to add
new notes to the diary.

Even though this pretty simple app works, it has a big shortcoming, if you uninstall it from your
device, you will loose all your notes, and there is no way to restore them.

Wouldn’t it be cool if we could save them on a back end? That’s exactly what we are going to do in this tutorial.

## Installing BaasBox

The first step is to install BaasBox. For sake of simplicity we will show how to install it on a local machine. You will see that’s very easy. Download the latest version of BaasBox from here. Unzip the file, open Terminal, go to the directory unzipped, type “./start” and hit return. BaasBox is now running on your local machine. To test visit the following link: **http://localhost:9000/** and you should see the following screen.


## Importing the sdk
If you are on eclipse you can download the sdk from [here](). Put the jar in to your libs folder
and you are ready to go.
Using gradle is even simpler you just need to add the following line to your dependencies

```groovy
compile 'com.baasbox:baasbox-android:0.7.3-alpha'
```
After refreshing your ide, you should be able to use the sdk classes in your project

## Setup the client library
The BaasBox service could be installed anywhere on the web
so you probably need to configure it a bit.
We suggest to initialize the client as soon as possible in your
*Application* class. The starter project includes the DearDiary.
Open the java file and