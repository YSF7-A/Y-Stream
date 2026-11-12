# AniLab Android Clone (Kotlin + Jetpack Compose + Media3)

A modern, high-performance Android anime streaming app clone inspired by **AniLab**, built with **Native Android (Kotlin)**, **Jetpack Compose**, and **Media3 / ExoPlayer**.

---

## ✨ Features

- **AniLab Signature Dark UI**: Designed with sleek deep blacks (`#0F1015`), vivid red accents, and smooth Material3 transitions.
- **Home Feed**:
  - **Spotlight Banner Carousel**: Hero banner featuring top trending anime with backdrop art, rating badge, synopsis, and instant "Watch Now" action.
  - **Categorized Sections**: *Trending Now*, *Top Airing*, *Latest Episodes*, and *Most Popular*.
- **Anime Details Page**:
  - Banner & poster art display.
  - Formatted metadata: genres, studio, TV/Movie format, rating, and status.
  - Expandable overview/synopsis.
  - Paginated Episode Selector (e.g. 1–25, 26–50) with filler badges.
  - Recommended anime carousel.
- **Media3 / ExoPlayer Video Player**:
  - Fullscreen video playback with gesture controls.
  - Custom AniLab controls overlay (auto-hides after inactivity).
  - **10s Rewind & Fast Forward** buttons.
  - **Playback Speed Controller** (1.0x, 1.25x, 1.5x, 2.0x).
  - **Skip Intro (⏭)** button for episode openings.
  - Interactive scrubbing seekbar with live timestamps.
- **Search & Filter**:
  - Real-time instant search with debounce.
  - Filter chips for popular genres (*Action*, *Fantasy*, *Romance*, *Sci-Fi*, *Shounen*, etc.).
  - Responsive multi-column grid layout.
- **My Library**:
  - Watchlist, History, and Downloaded tabs.
- **Offline / Fallback Resilience**:
  - Pre-configured with automatic offline mock feeds and sample video streams, ensuring the app runs smoothly out of the box even before configuring an external backend.

---

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material 3
- **Video Engine**: [AndroidX Media3 ExoPlayer](https://developer.android.com/media/media3/exoplayer) with HLS (`m3u8`) & MP4 streaming support
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) & [OkHttp 4](https://square.github.io/okhttp/) with Kotlinx Serialization
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture
- **Language**: Kotlin 2.0 (Target SDK 34, Min SDK 24 / Android 7.0+)

---

## 🚀 How to Build the APK

### Method 1: Build Using Android Studio (Recommended)
1. Install [Android Studio](https://developer.android.com/studio) if you haven't already.
2. Open Android Studio and choose **Open**, then select the `anilab-android` folder:
   ```
   /home/yousuf/Downloads/anilab-android
   ```
3. Let Gradle sync and download dependencies.
4. From the top menu bar, click:
   **Build > Build Bundle(s) / APK(s) > Build APK(s)**
5. Once the build finishes, click **locate** in the popup notification to get your `app-debug.apk`.

---

### Method 2: Build Using Command Line (Terminal)
If you have JDK 17 and Android SDK installed on your machine:
```bash
cd /home/yousuf/Downloads/anilab-android
./gradlew assembleDebug
```
The output APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

### Method 3: Cloud Build via GitHub Actions (Zero Local Setup)
A GitHub Actions workflow is pre-configured at [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml).
1. Initialize a git repo and push to GitHub:
   ```bash
   cd /home/yousuf/Downloads/anilab-android
   git init
   git add .
   git commit -m "Initial commit of AniLab Android Clone"
   git branch -M main
   git remote add origin https://github.com/<your-username>/<your-repo-name>.git
   git push -u origin main
   ```
2. Go to the **Actions** tab on your GitHub repository.
3. The **Build AniLab APK** workflow will run automatically.
4. Click on the completed run, and download the ready-to-install **`anilab-debug-apk`** from the **Artifacts** section!

---

## 🌐 Customizing the API & Video Sources

The networking client is defined in [`RetrofitClient.kt`](app/src/main/java/com/anilab/clone/data/api/RetrofitClient.kt).
By default, it is configured to interface with standard HiAnime / Consumet anime endpoints:
```kotlin
// Change this to your self-hosted instance or custom proxy URL
private var baseUrl: String = "https://api-aniwatch.onrender.com/"
```
Supported endpoint contract:
- `GET /api/v2/hianime/home` - Spotlight, trending, latest releases
- `GET /api/v2/hianime/anime/info?id={id}` - Anime synopsis, episodes list
- `GET /api/v2/hianime/search?q={query}` - Search results
- `GET /api/v2/hianime/episode/sources?animeEpisodeId={id}` - HLS `m3u8` streams and subtitles
