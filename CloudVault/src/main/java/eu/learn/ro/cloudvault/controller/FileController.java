package eu.learn.ro.cloudvault.controller;

import eu.learn.ro.cloudvault.model.FileMetadata;
import eu.learn.ro.cloudvault.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<FileMetadata> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType
    ) throws IOException {
        System.out.println("Request received for uploading a file");
        System.out.println("File parameter: " + (file == null ? "null" : file.getOriginalFilename()));
        System.out.println("File type parameter: " + fileType);
        System.out.println("File size: " + file.getSize() + " bytes");

        if (file == null || file.isEmpty() || fileType.isBlank()) {
            System.err.println("Invalid input: File or fileType is missing.");
            return ResponseEntity.badRequest().body(null);
        }

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(fileType);
        metadata.setFileSize(file.getSize());

        FileMetadata savedMetadata = fileService.saveFile(metadata, file.getBytes());
        return ResponseEntity.ok(savedMetadata);
    }

    @GetMapping("/download/{fileName}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            System.out.println("Request received to download file: " + fileName);
            System.out.println("Attempting to retrieve file metadata for: " + fileName);

            byte[] fileData = fileService.getFile(fileName);

            System.out.println("File data retrieved successfully for: " + fileName);
            System.out.println("File size: " + fileData.length + " bytes");

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileData);
        } catch (IOException e) {
            System.err.println("Error retrieving file: " + fileName);
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Unexpected error during file download: " + fileName);
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{fileName}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        System.out.println("\"Admin attempting to delete file: {}\" + fileName");

        try {
            fileService.deleteFile(fileName);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting file.");
        }
    }
}