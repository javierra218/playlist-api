package com.quipux.playlist.controller;

import com.quipux.playlist.dto.PlaylistDTO;
import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.service.PlaylistService;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/lists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final ModelMapper modelMapper;

    public PlaylistController(PlaylistService playlistService, ModelMapper modelMapper) {
        this.playlistService = playlistService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistDTO playlistDTO) {
        Playlist playlist = modelMapper.map(playlistDTO, Playlist.class);

        // crear la playlist
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);

        // hay canciones en el DTO, agregarlas a la playlist
        if (playlistDTO.getCanciones() != null && !playlistDTO.getCanciones().isEmpty()) {
            playlistDTO.getCanciones().forEach(songDTO -> {
                Song song = modelMapper.map(songDTO, Song.class);
                playlistService.addSongToPlaylist(createdPlaylist.getId(), song);
            });
        }

        // playlist actualizada con las canciones
        Playlist updatedPlaylist = playlistService.getPlaylistByName(createdPlaylist.getNombre())
                .orElse(createdPlaylist);
        PlaylistDTO response = modelMapper.map(updatedPlaylist, PlaylistDTO.class);
        return ResponseEntity.status(201).body(response);
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
