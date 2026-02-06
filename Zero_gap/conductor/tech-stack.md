# Tech Stack: Zero_gap

## Core Technologies
- **Programming Language:** Kotlin (v2.0.21)
- **Platform:** Android (minSdk: 24, targetSdk: 36, compileSdk: 36)
- **Build System:** Gradle (Kotlin DSL) using Version Catalogs (`libs.versions.toml`)

## Frameworks & Libraries
- **UI Framework:** Android View System (XML-based layouts)
- **Design System:** Material Design 3 (com.google.android.material:material:1.13.0)
- **AndroidX Components:**
    - Core KTX (androidx.core:core-ktx:1.17.0)
    - AppCompat (androidx.appcompat:appcompat:1.7.1)
    - Activity (androidx.activity:activity:1.12.3)
    - ConstraintLayout (androidx.constraintlayout:constraintlayout:2.2.1)

## Testing
- **Unit Testing:** JUnit 4 (junit:junit:4.13.2)
- **Instrumentation Testing:** 
    - AndroidX Test JUnit (androidx.test.ext:junit:1.3.0)
    - Espresso Core (androidx.test.espresso:espresso-core:3.7.0)
