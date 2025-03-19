package com.quipux.playlist.controller;

import com.quipux.playlist.dto.SongDTO;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.service.SongService;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;
    private final ModelMapper modelMapper;

    public SongController(SongService songService, ModelMapper modelMapper) {
        this.songService = songService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<SongDTO> addSong(@Valid @RequestBody SongDTO songDTO) {
        Song song = modelMapper.map(songDTO, Song.class);
        Song savedSong = songService.addSong(song);
        SongDTO response = modelMapper.map(savedSong, SongDTO.class);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        List<SongDTO> response = songs.stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<SongDTO>> getSongsByArtist(@PathVariable String artist) {
        List<Song> songs = songService.getSongsByArtist(artist);
        List<SongDTO> response = songs.stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .toList();
        return ResponseEntity.ok(response);
    }
}
