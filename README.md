# Paragon Dining - Restaurant Management App

**Module:** COMP2000 Software Engineering 2  
**Assessment:** 2 - Application Development  
**Student ID:** 10913245  

---

## 📱 Project Overview

**Paragon Dining** is a native Android application designed to streamline the reservation process for restaurant guests and operational management for staff. Built with a focus on modern Android architecture patterns, the app features a bespoke "Paragon" theme (Charcoal/Maroon/Gold) and robust offline capabilities.

This project demonstrates proficiency in:
*   **MVVM Architecture** (Model-View-ViewModel)
*   **Offline Data Persistence** (Room Database)
*   **Background Synchronization** (WorkManager & Retrofit)
*   **Material Design 3** (Custom UI/UX)

---

## ✨ Features

### 👤 Guest Experience
*   **User Accounts:** Register and Login securely (Offline authentication supported).
*   **Dashboard:** Intuitive card-based navigation.
*   **Table Booking:** Seamless reservation form with Date/Time pickers.
*   **My Reservations:** View upcoming bookings.
*   **Menu Browsing:** Visual menu with rich imagery.

### 👨‍🍳 Staff Experience
*   **Secure Access:** Role-based dashboard (Staff accounts only).
*   **Reservation Management:**
    *   View all guest bookings.
    *   **Modify Status:** Approve ("Confirmed") or reject ("Cancelled") bookings via a dedicated staff-only dropdown.
*   **Menu Management:** Add, Update, or Delete items from the digital menu.

---

## 🛠️ Tech Stack

*   **Language:** Java 17
*   **Minimum SDK:** API 24 (Android 7.0)
*   **Target SDK:** API 34+
*   **Architecture:** MVVM (Model-View-ViewModel)

### Key Libraries
*   **Android Jetpack:**
    *   **Room:** Local SQLite database abstraction.
    *   **ViewModel & LiveData:** Lifecycle-aware components.
    *   **WorkManager:** Reliable background processing for data sync.
    *   **ViewBinding:** Type-safe view interaction.
*   **Networking:**
    *   **Retrofit 2:** REST API client.
    *   **Gson:** JSON parsing.
*   **UI/Media:**
    *   **Material Components:** Modern UI widgets.
    *   **Glide:** Efficient image loading and caching.

---

## 🚀 Setup & Installation

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/Plymouth-COMP2000/design-exercises-sgharshan.git
    ```
2.  **Open in Android Studio**
    *   Launch Android Studio -> `File` -> `Open` -> Select the cloned directory.
3.  **Sync Gradle**
    *   Allow the project to download dependencies.
4.  **Run the App**
    *   Select an Emulator (e.g., Pixel 6 API 34) or Physical Device.
    *   Click the green **Run (▶)** button.

### ⚠️ Note on API Sync
The app is configured to sync with the University User API.
*   The `SyncWorker` runs automatically every **15 minutes**.
*   It requires an active internet connection to fetch initial data.
*   Once synced, the app works fully **offline**.

---

## 🧪 Testing

*   **Unit Tests:** Located in `src/test/java`
*   **Instrumentation Tests:** Located in `src/androidTest/java`

---

## 📄 License
This project is submitted for academic assessment purposes.
