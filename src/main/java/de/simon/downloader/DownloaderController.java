package de.simon.downloader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.simon.downloader.payload.DownloadRequest;
import de.simon.downloader.service.YoutubeDownloadService;

@Controller
public class DownloaderController {

    private final YoutubeDownloadService youtubeDownloadService;

    public DownloaderController(YoutubeDownloadService youtubeDownloadService) {
        this.youtubeDownloadService = youtubeDownloadService;
    }
	
	@GetMapping("")
    public String viewHomepage() {
        return "index";
    }
	
	@PostMapping("/api/download")
    public ResponseEntity<Resource> downloadEndpoint(@RequestBody DownloadRequest request) {
        File downloadedFile = null;
        try {
            downloadedFile = youtubeDownloadService.downloadAsMp3(request.url());

            Path filePath = downloadedFile.toPath();
            byte[] data = Files.readAllBytes(filePath);
            
            Files.delete(filePath);
            
            Resource resource = new ByteArrayResource(data);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + 
            downloadedFile.getName().substring(0, downloadedFile.getName().lastIndexOf("-"))
            + ".mp3\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Verarbeiten der Anfrage: " + e.getMessage());
        } 
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return ResponseEntity
                .status(500)
                .body(Map.of("message", message));
    }
}
