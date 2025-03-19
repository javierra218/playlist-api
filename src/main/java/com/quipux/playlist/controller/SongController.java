package com.quipux.playlist.controller;

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
    public ResponseEntity<Song> addSong(@Valid @RequestBody Song song) {
        return ResponseEntity.status(201).body(songService.addSong(song));
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable String artist) {
        List<Song> songs = songService.getSongsByArtist(artist);
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songs);
    }
}
