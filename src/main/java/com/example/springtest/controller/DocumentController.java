package com.example.springtest.controller;

import com.example.springtest.model.DocumentDTO;
import com.example.springtest.model.entity.Document;
import com.example.springtest.model.entity.User;
import com.example.springtest.model.service.DocumentService;
import com.example.springtest.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocumento(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = com.google.common.io.Files.getFileExtension(fileName).toLowerCase();

        if (!(fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") || fileExtension.equals("pdf"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten imágenes y PDFs");
        }

        try {
            Path path = Paths.get(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), path);

            Document document = new Document();
            document.setFileName(fileName);
            document.setType(fileExtension);
            document.setUrl(path.toString());
            document.setUser(userOptional.get());

            documentService.save(document);

            return ResponseEntity.ok("Documento subido exitosamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el documento");
        }
    }

    @GetMapping("/user/{userId}/last")
    public ResponseEntity<?> getUltimoDocumento(@PathVariable Long userId) {
        List<Document> documents = documentService.findLatestByUsuarioId(userId);
        if (!documents.isEmpty()) {
            Document lastDocument = documents.get(0);
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setId(lastDocument.getId());
            documentDTO.setFileName(lastDocument.getFileName());
            documentDTO.setType(lastDocument.getType());
            documentDTO.setUrl(lastDocument.getUrl());
            documentDTO.setUploadDate(lastDocument.getUploadDate());

            return ResponseEntity.ok(documentDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron documentos para el usuario con ID: " + userId);
        }
    }
}


