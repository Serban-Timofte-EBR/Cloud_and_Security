package eu.learn.ro.cloudvault.controller;

import eu.learn.ro.cloudvault.model.FileMetadata;
import eu.learn.ro.cloudvault.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType
    ) throws IOException {
        System.out.println("Uploading file: " + file.getOriginalFilename());
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(fileType);
        metadata.setFileSize(file.getSize());

        try {
            FileMetadata savedMetadata = fileService.saveFile(metadata, file.getBytes());
            return ResponseEntity.ok(savedMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error uploading file", e);
        }
    }

    @GetMapping("/download/{fileName}")
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
}