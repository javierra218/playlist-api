package com.quipux.playlist.controller;

import com.quipux.playlist.dto.PlaylistDTO;
import com.quipux.playlist.model.Playlist;
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
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);
        PlaylistDTO response = modelMapper.map(createdPlaylist, PlaylistDTO.class);
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
