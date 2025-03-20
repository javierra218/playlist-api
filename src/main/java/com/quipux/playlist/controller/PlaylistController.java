package com.quipux.playlist.controller;

import com.quipux.playlist.dto.PlaylistDTO;
import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.service.PlaylistService;
import com.quipux.playlist.service.SongService;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/lists")
@Validated
public class PlaylistController {

    private final PlaylistService playlistService;
    private final SongService songService;
    private final ModelMapper modelMapper;

    public PlaylistController(PlaylistService playlistService, SongService songService, ModelMapper modelMapper) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistDTO playlistDTO) {
        // Crear la playlist sin canciones primero
        Playlist playlist = modelMapper.map(playlistDTO, Playlist.class);
        playlist.setCanciones(new HashSet<>());
        
        // Crear la playlist
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);

        // Si hay canciones en el DTO, agregarlas a la playlist
        if (playlistDTO.getCanciones() != null && !playlistDTO.getCanciones().isEmpty()) {
            for (var songDTO : playlistDTO.getCanciones()) {
                Song song = modelMapper.map(songDTO, Song.class);
                playlistService.addSongToPlaylist(createdPlaylist.getId(), song);
            }
        }

        // Obtener la playlist actualizada
        Playlist updatedPlaylist = playlistService.getPlaylistByName(createdPlaylist.getNombre())
                .orElse(createdPlaylist);
        
        return ResponseEntity.status(201).body(modelMapper.map(updatedPlaylist, PlaylistDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.getAllPlaylists();
        List<PlaylistDTO> response = playlists.stream()
                .map(playlist -> modelMapper.map(playlist, PlaylistDTO.class))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{listName}")
    public ResponseEntity<PlaylistDTO> getPlaylistByName(@PathVariable String listName) {
        Playlist playlist = playlistService.getPlaylistByName(listName).orElse(null);
        if (playlist != null) {
            PlaylistDTO response = modelMapper.map(playlist, PlaylistDTO.class);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String listName) {
        Playlist playlist = playlistService.getPlaylistByName(listName).orElse(null);
        if (playlist != null) {
            playlistService.deletePlaylist(playlist.getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
