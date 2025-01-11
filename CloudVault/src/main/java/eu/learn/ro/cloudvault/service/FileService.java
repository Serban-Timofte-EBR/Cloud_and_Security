package eu.learn.ro.cloudvault.service;

import eu.learn.ro.cloudvault.dto.FileMetadataRequestDTO;
import eu.learn.ro.cloudvault.dto.FileMetadataResponseDTO;
import eu.learn.ro.cloudvault.model.FileMetadata;
import eu.learn.ro.cloudvault.repository.FileMetadataRepository;
import eu.learn.ro.cloudvault.security.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

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

    public FileMetadata saveFile(FileMetadata metadata, byte[] fileData) throws IOException {
        try {
            String encryptedData = EncryptionUtil.encrypt(new String(fileData));

            Path storageDir = Paths.get(storageDirectory);
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
                logger.info("Storage directory created: {}", storageDir.toAbsolutePath());
            }

            Path filePath = storageDir.resolve(metadata.getFileName());
            Files.write(filePath, encryptedData.getBytes());
            logger.info("File encrypted and saved at: {}", filePath.toAbsolutePath());

            return fileMetadataRepository.save(metadata);
        } catch (Exception e) {
            logger.error("Error saving file: {}", metadata.getFileName(), e);
            throw new RuntimeException("Error encrypting and saving file", e);
        }
    }

    public byte[] getFile(String fileName) throws IOException {
        try {
            Path filePath = Paths.get(storageDirectory, fileName);

            if (!Files.exists(filePath)) {
                logger.warn("File not found: {}", fileName);
                throw new IOException("File not found: " + fileName);
            }

            byte[] encryptedData = Files.readAllBytes(filePath);

            String decryptedData = EncryptionUtil.decrypt(new String(encryptedData));

            logger.info("File retrieved and decrypted successfully: {}", fileName);

            return decryptedData.getBytes();
        } catch (Exception e) {
            throw new IOException("Error decrypting or retrieving file", e);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        Path storageDir = Paths.get("storage");
        Path filePath = storageDir.resolve(fileName);

        if (!Files.exists(filePath)) {
            logger.warn("File not found for deletion: {}", fileName);
            throw new IOException("File not found: " + fileName);
        }

        Files.delete(filePath);

        fileMetadataRepository.deleteByFileName(fileName);
        logger.info("File and metadata deleted successfully: {}", fileName);
    }
}