package com.quipux.playlist.controller;

import com.quipux.playlist.dto.PlaylistDTO;
import com.quipux.playlist.dto.SongDTO;
import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistDTO playlistDTO) {
        Playlist playlist = convertToEntity(playlistDTO);  // Convertir DTO a entidad
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);
        PlaylistDTO response = convertToDTO(createdPlaylist);  // Convertir la entidad a DTO para la respuesta
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.getAllPlaylists();
        List<PlaylistDTO> response = playlists.stream()
                                              .map(this::convertToDTO)
                                              .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{listName}")
    public ResponseEntity<PlaylistDTO> getPlaylistByName(@PathVariable String listName) {
        Optional<Playlist> playlist = playlistService.getPlaylistByName(listName);
        return playlist.map(p -> ResponseEntity.ok(convertToDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String listName) {
        Optional<Playlist> playlist = playlistService.getPlaylistByName(listName);
        if (playlist.isPresent()) {
            playlistService.deletePlaylist(playlist.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Métodos de conversión entre DTOs y entidades
    private Playlist convertToEntity(PlaylistDTO dto) {
        Playlist playlist = new Playlist();
        playlist.setNombre(dto.getNombre());
        playlist.setDescripcion(dto.getDescripcion());
        // Convertir las canciones si es necesario
        return playlist;
    }

    private PlaylistDTO convertToDTO(Playlist playlist) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setNombre(playlist.getNombre());
        dto.setDescripcion(playlist.getDescripcion());
        dto.setCanciones(playlist.getCanciones().stream().map(this::convertToDTO).toList());
        return dto;
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
