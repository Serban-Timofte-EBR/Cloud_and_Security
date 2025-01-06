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
            @RequestParam("file") MultipartFile file, // MultipartFile represents an uploaded file received in a multipart request.
            @RequestParam("fileType") String fileType
    ) throws IOException {
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(fileType);
        metadata.setFileSize(file.getSize());

        FileMetadata savedMetadata = null;
        try {
            savedMetadata = fileService.saveFile(metadata, file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(savedMetadata);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) throws IOException {
        byte[] fileData = fileService.getFile(fileName);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(fileData);
    }
}