package com.example.springtest.model.service;

import com.example.springtest.model.entity.Document;
import com.example.springtest.model.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document save(Document document) {
        document.setUploadDate(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public List<Document> findLatestByUsuarioId(Long usuarioId) {
        return documentRepository.findByUserIdOrderByUploadDateDesc(usuarioId);
    }
}

