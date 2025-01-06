package eu.learn.ro.cloudvault.service;

import eu.learn.ro.cloudvault.dto.FileMetadataRequestDTO;
import eu.learn.ro.cloudvault.dto.FileMetadataResponseDTO;
import eu.learn.ro.cloudvault.model.FileMetadata;
import eu.learn.ro.cloudvault.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    private final String storageDirectory = "storage";

    public FileMetadataResponseDTO saveFileMetadata(FileMetadataRequestDTO requestDTO) {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(requestDTO.getFileName());
        fileMetadata.setFileType(requestDTO.getFileType());
        fileMetadata.setFileSize(requestDTO.getFileSize());

        FileMetadata savedMetadata = fileMetadataRepository.save(fileMetadata);

        FileMetadataResponseDTO responseDTO = new FileMetadataResponseDTO();
        responseDTO.setId(savedMetadata.getId());
        responseDTO.setFileName(savedMetadata.getFileName());
        responseDTO.setFileType(savedMetadata.getFileType());
        responseDTO.setFileSize(savedMetadata.getFileSize());

        return responseDTO;
    }

    public FileMetadata saveFile(FileMetadata metadata, byte[] fileData) throws Exception {
        // Save file to disk
        File dir = new File(storageDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(storageDirectory + File.separator + metadata.getFileName());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData);
        }

        // Save metadata to the database
        return fileMetadataRepository.save(metadata);
    }

    public byte[] getFile(String fileName) throws IOException {
        File file = new File(storageDirectory + File.separator + fileName);
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        return java.nio.file.Files.readAllBytes(file.toPath());
    }
}