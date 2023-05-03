# Starling coding challenge

To test and build the app is enough to run

`./gradlew test`

`./gradlew assembleDebug`
## Define the access and refresh token
Since I'm not handling the authentication, the app uses two workarounds to get the access and refresh tokens.
### access-token.properties file
The app uses a client_id, a client_secret, an access_token and a refresh_token that are defined in the property file `access-token.properties` in the root of the project.

This means that you need to build the app with the correct values.
### Deep link
You can also update the tokens triggering a deep link with the ADB. 
- Connect the device to the adb.
- Kill the app and
- run: 
`
adb shell am start -a android.intent.action.VIEW -d "https://www.altieri.org/cc1/tokens/yournewaccesstoken/yournewrefreshtoken/yournewexpiresin" com.altieri.starling
`

## Refreshing the token
The app will eventually refresh the token if it performs a network operation near the expiry time.

Currently it doesn't refresh the tokens with an alarm.

## Assumptions:
- I considered only the transactions that are OUT and not for SAVING when calculating the roundUp. I'm assuming that the requirement as described isn't complete because it doesn't sound right to roundup the income or a transaction to save money in the goal
- Assuming same currency in the account and in all the transactions and goals
- I'm assuming that the APIs that return a "success" field will not return a 2xx in case success = false

## Known issues:
- The coverage for for the integration and ui automated test is small. There are missing unit tests.
- This implementation does not refresh the token in the background so if you don't perform network operations for too long the access token will expire
- For now all the errors are handled showing a generic error message
- Not implemented the ssl-pinning
- The saving goal functionalities should have had a separate view model
- I haven't split the VMs in two parts: one for the proper framework-unaware VM and the other one for the Androidx VM. If you want to be able to support other platforms like Windows you need to split them.
- I have hardcoded the goal name
- TokenRepositoryImpl should have been in the data layer and should have used a DataSource class to access the framework
