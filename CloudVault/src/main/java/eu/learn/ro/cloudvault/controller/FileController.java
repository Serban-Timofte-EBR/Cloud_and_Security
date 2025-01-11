package eu.learn.ro.cloudvault.controller;

import eu.learn.ro.cloudvault.model.FileMetadata;
import eu.learn.ro.cloudvault.service.FileService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<FileMetadata> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType
    ) throws IOException {
        logger.info("Received request to upload file: {}", file.getOriginalFilename());
        logger.debug("File type: {}, File size: {} bytes", fileType, file.getSize());


        if (file == null || file.isEmpty() || fileType.isBlank()) {
            logger.warn("Upload failed: Missing file or file type.");
            return ResponseEntity.badRequest().body(null);
        }

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(fileType);
        metadata.setFileSize(file.getSize());

        FileMetadata savedMetadata = fileService.saveFile(metadata, file.getBytes());
        logger.info("File uploaded successfully: {}", savedMetadata.getFileName());
        return ResponseEntity.ok(savedMetadata);
    }

    @GetMapping("/download/{fileName}")
    @Secured({"USER", "ADMIN"})
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        logger.info("Received request to download file: {}", fileName);
        try {
            byte[] fileData = fileService.getFile(fileName);

            logger.info("File downloaded successfully: {}", fileName);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileData);
        } catch (IOException e) {
            logger.error("Error retrieving file: {}", fileName, e);
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error during file download: {}", fileName, e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{fileName}")
    @Secured("ADMIN")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        logger.info("Admin attempting to delete file: {}", fileName);

        try {
            fileService.deleteFile(fileName);
            logger.info("File deleted successfully: {}", fileName);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (IOException e) {
            logger.error("Error deleting file: {}", fileName, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error during file deletion: {}", fileName, e);
            return ResponseEntity.internalServerError().body("Error deleting file.");
        }
    }
}