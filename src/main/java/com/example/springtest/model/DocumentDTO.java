package com.example.springtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    private Long id;
    private String fileName;
    private String type;
    private String url;
    private LocalDateTime uploadDate;



}

