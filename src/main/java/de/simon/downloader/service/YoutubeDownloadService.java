package de.simon.downloader.service;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

@Service
public class YoutubeDownloadService {

    private static final Path DOWNLOAD_DIRECTORY = Path.of(System.getProperty("java.io.tmpdir"), "yt-downloads");
    private static final Pattern YOUTUBE_VIDEO_ID_PATTERN = Pattern.compile("([a-zA-Z0-9_-]+)");
    private static final String YOUTUBE_VIDEO_PRE_URL = "https://www.youtube.com/watch?v=";

    public YoutubeDownloadService() {
        File dir = DOWNLOAD_DIRECTORY.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public File downloadAsMp3(String youtubeUrl) throws Exception {
    	youtubeUrl = sanitize(youtubeUrl);    	
        YoutubeDLRequest request = new YoutubeDLRequest(youtubeUrl, DOWNLOAD_DIRECTORY.toString());
        
        request.setOption("extract-audio");
        request.setOption("audio-format", "mp3");
        request.setOption("audio-quality", 0);
        request.setOption("output", "%(title)s-%(id)s.%(ext)s");

        YoutubeDLResponse response = YoutubeDL.execute(request);

        if (response.getExitCode() != 0) {
            throw new RuntimeException("Download fehlgeschlagen: " + response.getErr());
        }

        String fileName = extractFilePath(response.getOut());
        File downloadedFile = new File(DOWNLOAD_DIRECTORY + File.separator + fileName);
        
        if (!downloadedFile.exists()) {
             throw new RuntimeException("Die konvertierte MP3-Datei wurde nicht gefunden. Output von yt-dlp: " + response.getOut());
        }

        return downloadedFile;
    }

    private String sanitize(String url) {
    	Matcher matcher = YOUTUBE_VIDEO_ID_PATTERN.matcher(url.replace(YOUTUBE_VIDEO_PRE_URL, ""));
    	if(matcher.find()) {
    		System.out.println(matcher.group());
    		return YOUTUBE_VIDEO_PRE_URL + matcher.group();
    	}
    	return url;
    }
    
    private String extractFilePath(String ytDlpOutput) {
        String finalFilePath = null;
        for (String line : ytDlpOutput.split("\n")) {
            if (line.contains("Destination: ")) {
                finalFilePath = line.split("Destination: ")[1].trim();
            }
        }

        if (finalFilePath == null) {
            throw new RuntimeException("Konnte den Dateipfad aus dem yt-dlp Output nicht extrahieren.");
        }
        
        return finalFilePath;
    }
}