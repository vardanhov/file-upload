package com.example.uploadfile.repo;

import com.example.uploadfile.domain.FileOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileOwnerRepository extends JpaRepository<FileOwner, Long> {
    FileOwner findFileOwnerByPath(String path);
}
