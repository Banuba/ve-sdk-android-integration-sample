# API for using token from Remote Server in the AI Video Editor SDK

Banuba token can be stored on Remote Server. [Retrofit](https://github.com/square/retrofit) library is used to get token data.

### Step 1

Add following dependency to top-level ```build.gradle```:

```groovy
 implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
```

Currently used ```retrofitVersion``` in the AI Video Editor SDK you can find on [all used dependencies](all_dependencies.md).

### Step 2

Provide ```RemoteTokenApi``` implementation. ```getToken``` method should return token response data from the remote server.
**Note** that ```RemoteTokenApi``` implementation should be Retrofit service interface. It means that ```getToken``` should be overridden using appropriate request annotation.
For example, if the token is stored on the remote server like txt file and available simply by URL (like ```http://<remote_server_url>/token.txt```), ```RemoteTokenApi``` implementation could be as follows:

```kotlin
interface CustomRemoteTokenApi : RemoteTokenApi {
    @Streaming
    @GET("token.txt")
    override suspend fun getToken(): ResponseBody
}
```

The returned ```ResponseBody``` type here - is the [type](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/) of [OkHttp](https://github.com/square/okhttp) library and used as an example. You can use any other type and then convert it to the token ```String``` type using your overridden implementation of ```RemoteTokenMapper```.

### Step 3

Provide ```RemoteTokenMapper``` implementation. ```map``` method should convert type returned by ```getToken``` method of ```RemoteTokenApi``` into the token ```String``` type that is used in the AI Video Editor SDK.
For example, if the token is stored on the remote server like txt file and available simply by URL (like ```http://<remote_server_url>/token.txt```), ```RemoteTokenMapper``` implementation could be as follows:

```kotlin
object CustomRemoteTokenMapper : RemoteTokenMapper {
    override fun map(params: Any): String =
        if (params is ResponseBody) params.charStream().readText().trim()
        else ""
}
```

### Step 4

Configure dependencies in DI layer.

```kotlin
class VideoEditorKoinModule {

    val module = module {
    ...
        single {
            TokenType.REMOTE
        }

        single(named("remoteTokenUrl")) {
            "http://<remote_server_url>"
        }

        single<Class<*>>(named("remoteTokenRetrofitClass")) {
            CustomRemoteTokenApi::class.java
        }

        single<RemoteTokenMapper>(named("remoteTokenMapper")) {
            CustomRemoteTokenMapper
        }
    ...
    }
    ...
}
```

### Step 5 (*Optional*)

If you use [ResponseBody](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-response-body/) of [OkHttp](https://github.com/square/okhttp) library, add following dependency to top-level ```build.gradle```:

```groovy
 implementation "com.squareup.okhttp3:okhttp:$okHttpVersion"
```

Currently used ```okHttpVersion``` in the AI Video Editor SDK you can find on [all used dependencies](all_dependencies.md).

### Note:
To verify that token is ready, you can use `VideoEditorLicenceUtils.isSupportsVeSdk` before launch `VideoCreationActivity`. If method returns true - you can safely launch video editor, otherwise exception is raised.
We recommend to follow [this guide](../README.md#integration) to finish the setup.
