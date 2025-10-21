# 🎌 Hindi Anime TV App 📺 by Rekibur Uddin

A **Modern Android Application** built using **MVVM Architecture**, featuring **Retrofit API integration**, **Glide**, **LifecycleScope**, and **YouTube Video Player** to deliver a seamless streaming experience for anime lovers in **Hindi**.

---

## 🧩 Overview

The **Hindi Anime TV App** allows users to watch Hindi-dubbed anime easily through YouTube integration.  
All anime content is fetched dynamically from a **JSON file hosted on GitHub (Raw file)** using **Retrofit API** with **MVVM architecture**.

---

## 🚀 Key Features

✅ **MVVM Architecture** – Clean, maintainable, and testable structure  
✅ **Retrofit API** – Fetch anime list from a GitHub JSON file  
✅ **YouTube Player Integration** – Stream anime directly inside the app  
✅ **Glide Library** – Fast and efficient image loading  
✅ **LifecycleScope** – Manage coroutines safely with lifecycle awareness  
✅ **RecyclerView + ViewBinding** – Smooth UI and easy data binding  
✅ **Fully Responsive UI** – Works on phones and tablets  
✅ **Dark Mode Ready** 🌙  

---

## 🧠 Tech Stack

| Component | Technology Used |
|------------|----------------|
| **Language** | Kotlin |
| **Architecture** | MVVM |
| **API Client** | Retrofit |
| **Image Loading** | Glide |
| **Coroutine Handling** | LifecycleScope |
| **JSON Source** | GitHub Raw JSON File |
| **Video Player** | YouTube Player API |
| **UI** | XML Layout + Material Design |

---

## 🗂️ Project Structure

```
Hindi Anime TV App/
├── data/
│   ├── model/              # Data classes for API
│   ├── network/            # Retrofit setup and API interface
├── repository/             # Data handling logic
├── ui/
│   ├── view/               # Activities / Fragments
│   ├── viewmodel/          # ViewModels (MVVM)
├── utils/                  # Helper classes and constants
└── assets/
    └── raw/                # JSON file (if needed locally)
```

---

## 🧾 JSON Data Source (Example)

Your JSON is hosted on GitHub (Raw file), like:
```
https://raw.githubusercontent.com/rekibur-uddin/Hindi-Anime-Tv-App/main/anime_list.json
```

**Sample Structure:**
```json
[
  {
    "title": "Naruto (Hindi Dubbed)",
    "thumbnail": "https://example.com/naruto.jpg",
    "videoId": "abcd1234"
  },
  {
    "title": "Attack on Titan (Hindi Dubbed)",
    "thumbnail": "https://example.com/aot.jpg",
    "videoId": "efgh5678"
  }
]
```

---

## 🧩 Libraries Used

```gradle
// Retrofit
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// Glide
implementation 'com.github.bumptech.glide:glide:4.16.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

// YouTube Player
implementation 'com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0'

// Coroutines + Lifecycle
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.0'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

// ViewBinding
buildFeatures {
    viewBinding true
}
```

---

## 📱 Screenshots

| Home Screen | Video Player |
|--------------|--------------|
| ![Home](screenshots/home.jpg) | ![Player](screenshots/player.jpg) |

> Add your screenshots in the `/screenshots` folder and link them here.

---

## 🌍 Play Store

📦 **Download Now on Play Store:**  
👉 [Hindi Anime TV App](https://play.google.com/store/apps/details?id=com.yourpackagename)

---

## ⚙️ How to Run Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/rekibur-uddin/Hindi-Anime-Tv-App.git
   ```
2. Open in **Android Studio**
3. Wait for Gradle to sync
4. Run the app on an emulator or real device 🎉

---

## 🧑‍💻 Developed By

👨‍💻 **Rekibur Uddin**  
📍 Android Developer | Kotlin | MVVM | Firebase | REST API  
🔗 [GitHub Profile](https://github.com/rekibur-uddin)  
🌐 [Portfolio](https://rekiburuddin.blogspot.com/)  
📧 rekibdev@gmail.com | 📱 WhatsApp: +91 6003583469  

---

## ⚖️ Commercial Use

This project is open for learning and personal use.  
🚫 **Commercial use requires permission** from the developer.  
For business or resale permission, contact:  
📧 **rekibdev@gmail.com** | 📱 **+91 6003583469**

---

## ⭐ Support

If you like this project, **give it a star** ⭐ on GitHub — it helps a lot!  
You can also share feedback or suggestions in the **Issues** tab.

---

> “Anime is not just entertainment — it’s emotion in motion.” 🎌
