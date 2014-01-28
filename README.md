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
We suggest to initialize the client in your
*Application* onCreate method.
The starter project includes an empty DearDiary class.
Open the java file and add the following lines:
```java

    private BaasBox box;

    @Override
    public void onCreate() {
        super.onCreate();
        BaasBox.Config config = new BaasBox.Config();
        config.API_DOMAIN = "10.0.0.2"; // the host address
        config.APP_CODE = "1234567890"; // your appcode
        config.HTTP_PORT = 9000; // your port

        box = BaasBox.initDefault(this,config);
    }
```

Also don't forget to add the required internet permissions
to your manifest:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

After all you are going to connect to a backend!!!

With this setup you are ready to use the sdk in your app.
Don't forget to customize the configuration to adapt it to your environment.

## Authenticate with the server
BaasBox provides means for authenticatication, so that different user
can be distinguished on the backed.
The sample project includes a barebone LoginActivity, we are going to integrate it in
the app.

The first thing to do is to check if there is alrady an an authenticated user for this device.
Open the NoteListActivity file and right at the start of onCreate, where is the *todo 1* mark
add the following lines.

```java
if (BaasUser.current() == null){
            startLoginScreen();
            return;
}
```

BaasBox sdk remembers the currently logged in user.
```java BaasUser.current() ``` will return the current one
or null if no one is logged in, in this case we will start the login screen.

Now in the LoginActivity search for the two todo items for you.
This is your first call to BaasBox:

```java
private void signupWithBaasBox(boolean newUser){
        //todo 3.1
        BaasUser user = BaasUser.withUserName(mUsername);
        user.setPassword(mPassword);
        if (newUser) {
            user.signup(onComplete);
        } else {
            user.login(onComplete);
        }
    }
//todo 3.2
private final BaasHandler<BaasUser> onComplete =
    new BaasHandler<BaasUser>() {
        @Override
        public void handle(BaasResult<BaasUser> result) {
            if(result.isSuccess()){
                completeLogin(true);
            } else {
                Log.d(TAG,result.error().toString());
                completeLogin(false);
            }
        }
    };
```

you can login or signup using methods of the BaasUser class,they obviously are
*signup* and *login*.
You obtain an instance of a user through the factory method:
```java
BaasUser.withUserName(...);
```

These methods are executed asynchronously so you need to pass
a callback to be invoked upon completion, in this case the **onComplete**
variable. The callback will receive the result of the rest api call, or possibly
an error you can inspect docs of the methods of BaasResult<T> to see how it works in details.
