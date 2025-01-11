package eu.learn.ro.cloudvault.repository;

import eu.learn.ro.cloudvault.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    void deleteByFileName(String fileName);
}