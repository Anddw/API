package com.example.springtest.model.repository;

import com.example.springtest.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUserIdOrderByUploadDateDesc(Long userId);
}
