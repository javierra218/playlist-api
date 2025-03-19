package com.quipux.playlist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table
@Data
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título de la canción es obligatorio")
    @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "El artista es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del artista debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String artista;

    @Size(max = 100, message = "El nombre del álbum no puede superar los 100 caracteres")
    private String album;

    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    private int anno;

    @Size(max = 50, message = "El género no puede superar los 50 caracteres")
    private String genero;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
}
