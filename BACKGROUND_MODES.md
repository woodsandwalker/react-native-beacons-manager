# Background mode

## iOS background mode

In the Xcode project:

**Project settings:**
- go to `Capabilities`,
- switch on `Background Modes`
- and check both
  - `Location updates`
  - and `Uses Bluetooth LE accessories`.

![bgmode](./images/bgmode.gif)


**info.plist file:**

You need `Always authorization` (`WhenInUse` is clearly not enough):

- add `Privacy - Location Always Usage Description` key defined (*empty value or not. It is better to define a value to a custom / more user-friendly message*).
![ios: request when in use authorization](./images/plistRequireAlwaysUseAutorization.png)


**In your js code**
Use the method `requestAlwaysAuthorization`.
```javascript
Beacons.requestAlwaysAuthorization();
```

Finally when killed or sleeping and a beacon is found your whole app wont be loaded.

So do the tasks (that does not long last since iOS won't let it run more than few seconds):
```js
//...

// monitoring:
this.regionDidEnterEvent = Beacons.BeaconsEventEmitter.addListener(
   'regionDidEnter',
   (data) => {
    // good place for background tasks
    console.log('monitoring - regionDidEnter data: ', data);
    // do something
   }
 );

this.regionDidExitEvent = Beacons.BeaconsEventEmitter.addListener(
   'regionDidExit',
   (data) => {
      // good place for background tasks
      console.log('region did exit');
      // do something
    }
 );

//...
```


## Android background mode

Use headless task to support beaocn monitoring at backgorund and after device reboot
  - Add a file '{ProjectRoot}/BeaconMonitorTask.js' for region monitoring in backgorund, default at the same level of index.js of your project, for handling the headless task (default transition task name: `beacons-monitor-transition`)
```js
//...

const region = {
  identifier: 'Estimotes',
  uuid: 'B9407F30-F5F8-466E-AFF9-25556B57FE6D'
};

module.exports = async (event) => {

  if (Platform.OS === 'ios') {
    Beacons.startUpdatingLocation();
  }

  // add codes to handle events returned by the monitoring beacons
  // ... e.g.
  if (Platform.OS === 'android') {
    Beacons.BeaconsEventEmitter.removeAllListeners();
  }

  if (Platform.OS === 'android') {
    // for android, startMonitoring after beaconService is connected
    // Beacons.BeaconsEventEmitter.removeAllListeners('beaconServiceConnected');
    Beacons.BeaconsEventEmitter.addListener(
      'beaconServiceConnected',
      async () => {
        // add codes to monitor the beacons
        // ...e.g.
        Beacons.startMonitoringForRegion(region)
            .then(() => console.log(`Beacon ${region.identifier} monitoring started succesfully`))
            .catch(error => console.log(`Beacon ${region.identifier}  monitoring not started, error: ${error}`));
      },
    );
  } else {
    // add codes to monitor the beacons
    // ...e.g.
    Beacons.startMonitoringForRegion(region)
        .then(() => console.log(`Beacon ${region.identifier} monitoring started succesfully`))
        .catch(error => console.log(`Beacon ${region.identifier}  monitoring not started, error: ${error}`));
  }

  this.regionDidEnterEvent = Beacons.BeaconsEventEmitter.addListener(
    'regionDidEnter',
    (data) => {
      // good place for background tasks
      console.log('region did enter');
      // do something
    },
  );

  this.regionDidExitEvent = Beacons.BeaconsEventEmitter.addListener(
    'regionDidExit',
    (data) => {
      // good place for background tasks
      console.log('region did exit');
      // do something
    },
  );
};

//...
```