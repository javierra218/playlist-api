package com.quipux.playlist.controller;

import com.quipux.playlist.dto.SongDTO;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.service.SongService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public ResponseEntity<SongDTO> addSong(@Valid @RequestBody SongDTO songDTO) {
        Song song = convertToEntity(songDTO); // Convertir DTO a entidad
        Song savedSong = songService.addSong(song);
        SongDTO response = convertToDTO(savedSong); // Convertir la entidad a DTO para la respuesta
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        List<SongDTO> response = songs.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<SongDTO>> getSongsByArtist(@PathVariable String artist) {
        List<Song> songs = songService.getSongsByArtist(artist);
        List<SongDTO> response = songs.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    // Métodos de conversión entre DTOs y entidades
    private Song convertToEntity(SongDTO dto) {
        Song song = new Song();
        song.setTitulo(dto.getTitulo());
        song.setArtista(dto.getArtista());
        song.setAlbum(dto.getAlbum());
        song.setAnno(dto.getAnno());
        song.setGenero(dto.getGenero());
        return song;
    }

    private SongDTO convertToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setTitulo(song.getTitulo());
        dto.setArtista(song.getArtista());
        dto.setAlbum(song.getAlbum());
        dto.setAnno(song.getAnno());
        dto.setGenero(song.getGenero());
        return dto;
    }
}
