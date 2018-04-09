Fork of https://github.com/jasonwyatt/Android-DebugPort

Changes:
- Starts servers by default
- Sets "setAccessibility(true)" in interpreter by default.
- Provides access for application to set object that can be accessed from debugging console. Use DebugPort.set("object", someObject).

### Configure Your Dependencies

Add the jitpack.io repository to your root `build.gradle`:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

In your application's `build.gradle` file, add a dependency for Android DebugPort:

```groovy
    debugImplementation 'com.github.toomask.Android-DebugPort:lib:3.0.2'
    releaseImplementation 'com.github.toomask.Android-DebugPort:lib-noop:3.0.2'
```

