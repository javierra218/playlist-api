package com.quipux.playlist.dto;

import lombok.Data;

@Data
public class SongDTO {
    private String titulo;
    private String artista;
    private String album;
    private int anno;
    private String genero;
}
