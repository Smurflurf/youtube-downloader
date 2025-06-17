# YouTube to MP3 Downloader Web App

A self-hosted web application built with Java and Spring Boot that provides a simple webpage for downloading YouTube videos as MP3 audio files. This project is intended for personal and educational use.

## Features

-   **Simple Web Interface:** A clean, minimalist UI with a single input field and a download button.
-   **Backend Processing:** All downloading and conversion logic is handled by a Spring Boot backend.
-   **Direct MP3 Download:** The converted MP3 file is sent directly to the user's browser for download.
-   **Dynamic Naming:** Files are automatically named based on the YouTube video title.

## How It Works

The project consists of two main parts:
1.  **Frontend:** A single `index.html` page served by Spring Boot. It uses JavaScript `fetch API` to send the user's request to the backend without reloading the page and handles the file download in the browser.
2.  **Backend:** A Spring Boot application with a REST API endpoint (`/api/download`). It receives the YouTube URL, uses the `yt-dlp-java` library to orchestrate the download and conversion process, and streams the final MP3 file back to the user.

## Technology Stack

-   **Backend:** Java 21, Spring Boot 3
-   **Frontend:** HTML5, CSS3, JavaScript
-   **YouTube Interaction:** [yt-dlp-java](https://github.com/cicidi/youtubedl-java) (A Java wrapper for `yt-dlp`)
-   **Build Tool:** Apache Maven

## Prerequisites

To run this project, you need to have two external command-line tools installed and available in your system's `PATH`.

1.  **yt-dlp:** The core utility for downloading video content.
    -   [Installation Instructions](https://github.com/yt-dlp/yt-dlp#installation)
2.  **FFmpeg:** Required by `yt-dlp` for audio extraction and conversion.
    -   [Installation Instructions](https://ffmpeg.org/download.html)

## How to Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Smurflurf/youtube-downloader
    ```

2.  **Run the application:**
    Either execute `DownloaderApplication.java` or build a jar using Maven and run it.

4.  **Access the Web App:**
    Open your web browser and navigate to:
    [http://localhost:8080](http://localhost:8080)

You should now see the YouTube Downloader interface and can start downloading.

## Disclaimer

This tool is intended for personal and educational use only. Please respect copyright laws. The user is solely responsible for any copyright infringements.
Running it on a commercial server probably will not work due to YouTubes bot-protection. 
My attempts to bypass the bot protection (via cookies, a user-agent or vpn) were not successful. 
This might be adressed in the future, but right now use on personal machines with residential IP addresses is recommended.
