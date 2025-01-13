package eu.learn.ro.cloudvault.controller;

import eu.learn.ro.cloudvault.model.FileMetadata;
import eu.learn.ro.cloudvault.service.FileService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType
    ) {
        logger.info("Received request to upload file: {}", file.getOriginalFilename());
        logger.debug("File type: {}, File size: {} bytes", fileType, file.getSize());

        try {
            if (file == null || file.isEmpty() || fileType.isBlank()) {
                logger.warn("Upload failed: Missing file or file type.");
                return ResponseEntity.badRequest().body("Missing file or file type.");
            }

            FileMetadata metadata = new FileMetadata();
            metadata.setFileName(file.getOriginalFilename());
            metadata.setFileType(fileType);
            metadata.setFileSize(file.getSize());

            FileMetadata savedMetadata = fileService.saveFile(metadata, file.getBytes());
            logger.info("File uploaded successfully: {}", savedMetadata.getFileName());
            return ResponseEntity.ok(savedMetadata);

        } catch (RequestNotPermitted e) {
            logger.warn("Rate limit exceeded for file upload.");
            return ResponseEntity.status(429).body("Rate limit exceeded. Please try again later.");
        } catch (IOException e) {
            logger.error("Error saving file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.internalServerError().body("Error saving file.");
        } catch (Throwable t) {
            logger.error("Unexpected error during file upload: {}", file.getOriginalFilename(), t);
            return ResponseEntity.internalServerError().body("Unexpected error during file upload.");
        }
    }

    @GetMapping("/download/{fileName}")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        logger.info("Received request to download file: {}", fileName);

        try {
            byte[] fileData = fileService.getFile(fileName);

            logger.info("File downloaded successfully: {}", fileName);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileData);
        } catch (RequestNotPermitted e) {
            logger.warn("Rate limit exceeded for file download.");
            return ResponseEntity.status(429).body("Rate limit exceeded. Please try again later.");
        } catch (IOException e) {
            logger.error("Error retrieving file: {}", fileName, e);
            return ResponseEntity.notFound().build();
        } catch (Throwable t) {
            logger.error("Unexpected error during file download: {}", fileName, t);
            return ResponseEntity.internalServerError().body("Unexpected error during file download.");
        }
    }

    @DeleteMapping("/delete/{fileName}")
    @Secured("ADMIN")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        logger.info("Admin attempting to delete file: {}", fileName);

        try {
            fileService.deleteFile(fileName);
            logger.info("File deleted successfully: {}", fileName);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (RequestNotPermitted e) {
            logger.warn("Rate limit exceeded for file deletion.");
            return ResponseEntity.status(429).body("Rate limit exceeded. Please try again later.");
        } catch (IOException e) {
            logger.error("Error deleting file: {}", fileName, e);
            return ResponseEntity.notFound().build();
        } catch (Throwable t) {
            logger.error("Unexpected error during file deletion: {}", fileName, t);
            return ResponseEntity.internalServerError().body("Unexpected error during file deletion.");
        }
    }
}