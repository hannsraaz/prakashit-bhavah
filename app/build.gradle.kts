plugins { id("com.android.application"); id("org.jetbrains.kotlin.plugin.compose") }

android { namespace = "com.hansraj.choptorch"; compileSdk = 36
 defaultConfig { applicationId = "com.hansraj.choptorch"; minSdk = 26; targetSdk = 36; versionCode = 1; versionName = "1.0.0" }
}
dependencies {
 implementation(platform("androidx.compose:compose-bom:2025.10.01"))
 implementation("androidx.core:core-ktx:1.17.0")
 implementation("androidx.activity:activity-compose:1.12.1")
 implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
 implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
 implementation("androidx.compose.material3:material3")
 implementation("androidx.compose.material:material-icons-extended")
 implementation("androidx.compose.ui:ui"); implementation("androidx.compose.ui:ui-tooling-preview")
 implementation("androidx.datastore:datastore-preferences:1.2.0")
 testImplementation("junit:junit:4.13.2")
 testImplementation("org.jetbrains.kotlin:kotlin-test:2.3.0")
 debugImplementation("androidx.compose.ui:ui-tooling")
}
