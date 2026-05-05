# REWARD AUTOSEARCHER

Reward Autosearcher is an Android application developed in Kotlin, designed to automate Bing searches to efficiently accumulate Microsoft Rewards points and save time.

## ✨ Key Features

- Auto Search: Perform multiple automated search queries without manual interaction.

**Diverse Query Sources:**

- Reddit: Automatically fetch the latest post titles from subreddits (News, Technology, etc.).

- Local Data: Use a built-in keyword list `(queries.json)` with various topics.

- Google Trends / Wikipedia: Retrieve trending keywords worldwide (randomized by country).

## Smart User-Agent Simulation:

- Automatically call API to get the latest Chrome version.

- Simulate User-Agent to avoid detection.

## Flexible Customization:

- Configure the number of searches

- Set delay between searches to mimic human-like behavior and avoid spam detection.

- Randomize User-Agent during query sourcing to reduce detection risk.

- Account Management: Integrated WebView for login and checking Microsoft Rewards account status.

- Utilities: Keep screen on while running tasks, support background execution.
- Human-like scrolling.

## 🛠️ Technologies Used

| Technology    | Description                          |
| ------------- | ------------------------------------ |
| Language      | Kotlin                               |
| Networking    | Retrofit & OkHttp                    |
| JSON Parsing  | Kotlinx Serialization & Gson         |
| UI Components | Android Views (XML), Material Design |
| Build System  | Gradle (Kotlin DSL)                  |

## 📷 Screenshot

<p align="center">
  <img src="demo1.jpg" alt="Cambridge Page" width="700">
</p>
<p align="center">
  <img src="demo2.jpg" alt="Cambridge Page" width="700">
</p>

## 📂 Project Structure

\*Important source files include:\*\*

- `MainActivity.kt`: Core logic, UI handling, and automation flow.

- `assets/queries.json`: Offline keyword dataset.

**🌳 Project tree:**

```
├── app
│   ├── src
│   │   ├── androidTest
│   │   │   └── java
│   │   │       └── com
│   │   │           └── tinkismee
│   │   │               └── microsort_reward_autosearcher
│   │   │                   └── ExampleInstrumentedTest.kt
│   │   ├── main
│   │   │   ├── assets
│   │   │   │   └── queries.json
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── tinkismee
│   │   │   │           └── microsort_reward_autosearcher
│   │   │   │               ├── API_getChromeVersion.kt
│   │   │   │               ├── AutoSearchService.kt
│   │   │   │               ├── MainActivity.kt
│   │   │   │               ├── RetrofitClient_getChromeVersion.kt
│   │   │   │               ├── chromeVersionResponse.kt
│   │   │   │               └── localQueryDataClass.kt
│   │   │   ├── res
│   │   │   │   ├── drawable
│   │   │   │   │   ├── bars.xml
│   │   │   │   │   ├── ic_launcher_background.xml
│   │   │   │   │   ├── ic_launcher_foreground.xml
│   │   │   │   │   ├── login.xml
│   │   │   │   │   ├── logout.xml
│   │   │   │   │   ├── unitedstates.png
│   │   │   │   │   └── vietnam.jpg
│   │   │   │   ├── font
│   │   │   │   │   └── cambo.ttf
│   │   │   │   ├── layout
│   │   │   │   │   └── activity_main.xml
│   │   │   │   ├── menu
│   │   │   │   │   └── navigation_menu.xml
│   │   │   │   ├── mipmap-anydpi
│   │   │   │   │   ├── ic_launcher.xml
│   │   │   │   │   └── ic_launcher_round.xml
│   │   │   │   ├── mipmap-hdpi
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-mdpi
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-xhdpi
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-xxhdpi
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── mipmap-xxxhdpi
│   │   │   │   │   ├── ic_launcher.webp
│   │   │   │   │   └── ic_launcher_round.webp
│   │   │   │   ├── values
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── style.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   ├── values-night
│   │   │   │   │   └── themes.xml
│   │   │   │   ├── values-vi
│   │   │   │   │   └── strings.xml
│   │   │   │   └── xml
│   │   │   │       ├── backup_rules.xml
│   │   │   │       └── data_extraction_rules.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── tinkismee
│   │                   └── microsort_reward_autosearcher
│   │                       └── ExampleUnitTest.kt
│   └── proguard-rules.pro
├── gradle
│   ├── wrapper
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml
├── .gitignore
├── README.md
├── README_en.md
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

## 🚀 Installation & Usage

### Install:

- [` Microsoft-Reward-Autosearcher-v1.0.0.apk`](https://github.com/tinkismeeee/Microsoft-Reward-Autosearcher/releases/download/v1.0.0/Microsoft-Reward-Autosearcher-v1.0.0.apk)

### Requirement:

- A smartphone running the Android operating system (Android 9.0 or higher).

### ⚠️ Note:

- **This application was developed on Android 11. Newer Android versions may encounter unexpected issues that are outside the developer’s control.**

## Steps:

1. Open the **Reward Autosearcher** app on your device.

2. Tap **Login** to sign in (if not already).

3. Enter the desired number of searches and delay.

4. Choose query sources (Reddit, Google Trends, Wikipedia, Newspaper) — optional.

5. Press **Start** to begin.

# ⚠️ Disclaimer

This application is developed for educational and research purposes in Android development, networking, and task automation. Using automated tools may violate **[Microsoft Rewards](https://www.microsoft.com/vi-vn/servicesagreement?utm_source=copilot.com#13l_MicrosoftRewards)** terms of service. If your account is suspended or restricted, the developers are not responsible.

To fetch queries from Newspaper sources, an API key is required. Currently, the API key is provided by the developer. In future versions, users may use their own API key. Free API keys allow up to 100 queries/day. Source: **[News API](https://newsapi.org/)**

# Developed by [[thaikhang113](https://github.com/thaikhang113)/[Tinkismee](https://github.com/tinkismeeee)]
