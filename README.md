# 🎬 CineVerse – Aplikasi Film Android

Aplikasi Android modern untuk menjelajahi katalog film populer, sedang tayang, rating tertinggi, dan trending minggu ini yang dibangun menggunakan **Jetpack Compose** dan terintegrasi dengan **The Movie Database (TMDB) API**.

Dibuat untuk memenuhi kriteria penilaian **Project Pemrograman Mobile Kelas XII (November 2026)**.

---

## 📱 Tampilan Layar (Screenshots)

| Dashboard (Night Mode) | Dashboard (Not Night) | Halaman Detail Film | Halaman Pengaturan |
|:---:|:---:|:---:|:---:|
| ![Dashboard Night](screenshots/dashboard-night.png) | ![Dashboard Light](screenshots/dashboard-light.png) | ![Detail Film](screenshots/detail-film.png) | ![Pengaturan](screenshots/pengaturan.png) |

---

## ✨ Fitur Utama

### 1. Dashboard Interaktif (Bobot 30%)
- **Trending Banner**: Menampilkan film trending minggu ini dengan gradien rose/magenta yang elegan, judul tebal, rating bintang, dan tahun rilis.
- **Section Kategori**:
  - **Populer** (`/movie/popular`)
  - **Sedang Tayang** (`/movie/now_playing`)
  - **Rating Tertinggi** (`/movie/top_rated`)
- Scroll horizontal (`LazyRow`) yang responsif dan mulus.

### 2. Desain MovieCard Modern (Bobot 25%)
- Poster film beresolusi tinggi di-load dan di-cache secara otomatis oleh **Coil**.
- Sudut membulat (*rounded corners* 16dp) dengan elevasi Material 3.
- Informasi judul rapi dengan pemotongan teks (*ellipsis*), rating bintang (*star rating*), serta tahun rilis.

### 3. Multi Bahasa (Bobot 15%)
- Tersedia dalam **Bahasa Indonesia** dan **English**.
- Semua string UI dikelola terpusat melalui `res/values/strings.xml` dan `res/values-in/strings.xml`.
- Mengubah bahasa di halaman Pengaturan secara instan memperbarui teks antarmuka dan mengambil kembali data film dari TMDB menggunakan parameter `language=id-ID` atau `language=en-US`.

### 4. Mode Terang & Gelap / Night & Not Night
- Penerapan tema Material 3 menggunakan `lightColorScheme` dan `darkColorScheme`.
- Tiga opsi tema di halaman Pengaturan:
  - **Terang (Light Mode)**
  - **Gelap (Dark Mode)**
  - **Sistem (Mengikuti tema perangkat)**
- Palet warna konsisten: Aksen Crimson / Rose (`#E11D48`), Dark Surface (`#181A22`), Light Surface (`#FFFFFF`).

### 5. Dukungan Rotasi Layar (Portrait & Landscape)
- Seluruh layar dibungkus dalam container yang dapat di-scroll (`LazyColumn` dan `verticalScroll`).
- Orientasi layar dapat diubah (portrait / landscape) tanpa terjadi crash, tanpa pemotongan layout, dan status scroll tetap terjaga berkat arsitektur MVVM + `StateFlow`.

### 6. Halaman Detail Film & Pencarian
- **Detail Film**: Backdrop film resolusi tinggi, floating poster dengan elevasi, badge genre (*outlined chips*), serta sinopsis lengkap.
- **Pencarian Film**: Live search dengan debounce untuk mencari film favorit langsung dari TMDB.
- **Graceful Offline Fallback**: Tetap dapat menampilkan data demo offline jika koneksi internet terputus saat presentasi.

---

## 🛠️ Tech Stack & Library

- **Bahasa & UI**: Kotlin, Jetpack Compose, Material 3
- **Arsitektur**: MVVM (Model-View-ViewModel) + StateFlow
- **Networking**: Retrofit 2, OkHttp 4 (dengan Logging Interceptor), Gson
- **Image Loader**: Coil Compose
- **Navigasi**: Navigation Compose
- **Penyimpanan Lokal**: AndroidX DataStore Preferences (menyimpan preferensi Bahasa dan Tema)

---

## 📂 Struktur Proyek

```text
com.athalia.cineverse/
├── MainActivity.kt                      # Activity utama & host Compose NavGraph
├── data/
│   ├── api/
│   │   ├── ApiClient.kt                 # Retrofit builder & OkHttp interceptor
│   │   └── TmdbApiService.kt            # Interface endpoint TMDB
│   ├── model/
│   │   └── MovieModels.kt               # Movie, MovieDetail, MovieResponse, Genre
│   └── repository/
│       ├── MovieRepository.kt           # Repository pengambilan data API & fallback
│       └── UserPreferencesRepository.kt # DataStore preferensi tema & bahasa
├── ui/
│   ├── components/
│   │   ├── CategorySection.kt           # Section horizontal daftar film
│   │   ├── CommonStates.kt              # LoadingState & ErrorState
│   │   ├── GenreChip.kt                 # Chip genre bergaris luar
│   │   ├── MovieCard.kt                 # Card film dengan poster & rating
│   │   └── TrendingBanner.kt            # Banner hero film trending
│   ├── navigation/
│   │   ├── AppNavGraph.kt               # NavHost navigasi Compose
│   │   └── Screen.kt                    # Rute navigasi (Dashboard, Detail, Settings, Search)
│   ├── screens/
│   │   ├── DashboardScreen.kt           # Layar utama
│   │   ├── DetailScreen.kt              # Layar detail film
│   │   ├── SearchScreen.kt              # Layar pencarian film
│   │   └── SettingsScreen.kt            # Layar pengaturan bahasa & tema
│   └── theme/
│       ├── Color.kt                     # Palet warna Light & Dark
│       ├── Theme.kt                     # CineVerseTheme (Material 3)
│       └── Type.kt                      # Tipografi
├── util/
│   └── LocaleHelper.kt                  # Utilitas pergantian locale bahasa aplikasi
└── viewmodel/
    ├── MovieDetailViewModel.kt
    ├── MovieViewModel.kt
    ├── SearchViewModel.kt
    ├── SettingsViewModel.kt
    └── ViewModelFactory.kt
```

---

## 🔑 Konfigurasi TMDB API Key

API Key disimpan di dalam file `local.properties` (dan otomatis diabaikan oleh `.gitignore` sehingga aman tidak terunggah ke repositori publik):

1. Buka atau buat file `local.properties` di direktori *root* proyek.
2. Tambahkan baris berikut:
   ```properties
   TMDB_API_KEY=8ae7fc88adb3092cc38ca4012b1afb91
   ```
3. Gradle akan membaca kunci ini dan menyediakannya melalui `BuildConfig.TMDB_API_KEY`.

---

## 🚀 Cara Menjalankan Aplikasi

1. Clone repositori ini:
   ```bash
   git clone <url-repository-github>
   cd CineVerse
   ```
2. Pastikan file `local.properties` memiliki `sdk.dir` dan `TMDB_API_KEY`.
3. Buka proyek di **Android Studio**.
4. Lakukan sinkronisasi Gradle (*Sync Project with Gradle Files*).
5. Jalankan aplikasi di emulator Android atau perangkat fisik:
   ```bash
   ./gradlew assembleDebug
   ```
6. File APK debug hasil kompilasi tersedia di:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 📊 Pemetaan Rubrik Penilaian

| Kriteria | Bobot | Implementasi di CineVerse |
|:---|:---:|:---|
| **Desain Dashboard** | 30% | Banner trending minggu ini dengan gradien rose, kategori Populer, Sedang Tayang, Top Rated, tipografi dan warna konsisten sesuai mockup |
| **Desain Card** | 25% | Poster proporsional, sudut membulat 16dp, elevasi Material 3, judul rapi dengan ellipsis, rating bintang merah/pink dan tahun |
| **Multi Bahasa** | 15% | Tersedia Bahasa Indonesia (`values-in`) dan English (`values`), dapat diganti secara langsung dari Pengaturan, memperbarui UI dan parameter API |
| **Presentasi & Fungsi** | 30% | Demo alur data Retrofit/TMDB, ganti tema Terang/Gelap/Sistem instan, rotasi layar portrait/landscape mulus tanpa crash |

---

## 📋 Pengumpulan Proyek

- **Batas Pengumpulan**: 26 November 2026
- **Tautan Google Form**: [https://s.id/asesmengasalmobileXII26](https://s.id/asesmengasalmobileXII26)
