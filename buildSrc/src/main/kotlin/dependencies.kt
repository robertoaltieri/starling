@file:Suppress("unused")

object Core {
    const val coroutineCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutineCore}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.viewModel}"

    object Version {
        const val coroutineCore = "1.6.0"
        const val viewModel = "2.6.0"
    }
}

object GoogleAPI {
    const val auth = "com.google.android.gms:play-services-auth:${Version.auth}"

    object Version {
        const val auth = "20.1.0"
    }
}

object AndroidX {
    const val core = "androidx.core:core-ktx:${Version.core}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleRuntime}"
    const val multidex = "androidx.multidex:multidex:${Version.multidex}"
    const val navigationTesting = "androidx.navigation:navigation-testing:${Version.navigationTesting}"
    object Version {
        const val core = "1.8.0"
        const val multidex = "2.0.1"
        const val lifecycleRuntime = "2.3.1"
        const val navigationTesting = "2.5.3"
    }
}

object Dagger {
    const val hiltAndroid = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Version.hilt}"
    const val navigationCompose = "androidx.hilt:hilt-navigation-compose:${Version.navigationCompose}"
    const val androidxHiltCompiler = "androidx.hilt:hilt-compiler:${Version.androidxHiltCompiler}"
    object Version {
        const val hilt = "2.44"
        const val hiltCompiler = "2.44"
        const val navigationCompose = "1.0.0"
        const val androidxHiltCompiler = "1.0.0"
    }
}

object InstrumentationTest {
    const val uiTest = "androidx.compose.ui:ui-test-junit4"
    const val robolectric = "org.robolectric:robolectric:${Version.robolectric}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Version.mockWebServer}"
    const val testMonitor = "androidx.test:monitor:${Version.testMonitor}"
    const val coreTesting = "android.arch.core:core-testing:${Version.coreTesting}"

    object Version {
        const val espresso = "3.5.1"
        const val robolectric = "4.6"
        const val mockWebServer = "4.0.1"
        const val testMonitor = "1.5.0"
        const val coreTesting = "1.1.1"
    }
}

object Compose {
    const val bom = "androidx.compose:compose-bom:${Version.bom}"
    const val activity = "androidx.activity:activity-compose:${Version.activity}"
    const val uiTooling = "androidx.compose.ui:ui-tooling"
    const val material3 = "androidx.compose.material3:material3"
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val uiGraphics = "androidx.compose.ui:ui-graphics"
    const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.viewModelCompose}"
    const val ui = "androidx.compose.ui:ui"
    const val navigation = "androidx.navigation:navigation-compose:${Version.navigation}"
    object Version {
        const val bom = "2022.10.00"
        const val activity = "1.5.1"
        const val viewModelCompose = "2.5.1"
        const val navigation = "2.5.3"
    }
}

object Image {
    const val image = "io.coil-kt:coil:${Version.image}"
    const val imageCompose = "io.coil-kt:coil-compose:${Version.image}"
    const val imageComposeBase = "io.coil-kt:coil-compose-base:${Version.image}"

    object Version {
        const val image = "2.2.2"
    }
}

object UnitTest {
    const val junit = "junit:junit:${Version.junit}"
    const val junitExt = "androidx.test.ext:junit:${Version.junitExt}"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"

    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesTest}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"

    const val mockitoCore = "org.mockito:mockito-core:${Version.mockito}"
    const val mockitoAndroid = "org.mockito:mockito-android:${Version.mockito}"
    const val mockKAndroid = "io.mockk:mockk-android:${Version.mockk}"
    const val kotlinJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlinJunit}"

    object Version {
        const val junit = "4.12"
        const val junitExt = "1.1.1"
        const val coroutinesTest = "1.5.2"
        const val mockk = "1.13.3"
        const val mockito = "5.0.0"
        const val kotlinJunit = "1.8.10"
    }
}

object OkHttp {
    const val okHttp = "com.squareup.okhttp3:okhttp:${Version.okHttp}"
    const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okHttp}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Version.okHttp}@jar"

    object Version {
        const val okHttp = "4.9.2"
    }
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"
    const val scalarConverter = "com.squareup.retrofit2:converter-scalars:${Version.scalarConverter}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"
    const val moshiKotlinCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:${Version.moshi}"
    const val moshi = "com.squareup.moshi:moshi:${Version.moshi}"

    object Version {
        const val retrofit = "2.9.0"
        const val scalarConverter = "2.5.0"
        const val moshi = "1.13.0"
    }
}

object DataStore {
    const val preferences = "androidx.datastore:datastore-preferences:${Version.preferences}"

    object Version {
        const val preferences = "1.0.0"
    }
}

object Room {
    const val room = "androidx.room:room-ktx:${Version.room}"
    const val runtime = "androidx.room:room-runtime:${Version.room}"
    const val compiler = "androidx.room:room-compiler:${Version.room}"

    object Version {
        const val room = "2.4.3"
    }
}

object Other {
    const val jwtDecode = "com.auth0.android:jwtdecode:${Version.jwtDecode}"

    object Version {
        const val jwtDecode = "2.0.1"
    }
}
