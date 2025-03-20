package com.quipux.playlist.service.impl;

import com.quipux.playlist.model.Song;
import com.quipux.playlist.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SongServiceImplTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongServiceImpl songService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSong() {
        Song song = new Song();
        song.setTitulo("Canción 1");
        song.setArtista("Artista 1");

        when(songRepository.findByArtistaAndTitulo(any(String.class), any(String.class))).thenReturn(Collections.emptyList());
        when(songRepository.save(any(Song.class))).thenReturn(song);

        Song savedSong = songService.addSong(song);

        assertNotNull(savedSong);
        assertEquals("Canción 1", savedSong.getTitulo());
        assertEquals("Artista 1", savedSong.getArtista());
        verify(songRepository, times(1)).save(any(Song.class));
    }

    @Test
    void getAllSongs() {
        Song song = new Song();
        song.setTitulo("Canción 1");
        song.setArtista("Artista 1");

        when(songRepository.findAll()).thenReturn(Collections.singletonList(song));

        List<Song> songs = songService.getAllSongs();

        assertNotNull(songs);
        assertEquals(1, songs.size());
        assertEquals("Canción 1", songs.get(0).getTitulo());
        verify(songRepository, times(1)).findAll();
    }

    @Test
    void getSongsByArtist() {
        Song song = new Song();
        song.setTitulo("Canción 1");
        song.setArtista("Artista 1");

        when(songRepository.findByArtista(any(String.class))).thenReturn(Collections.singletonList(song));

        List<Song> songs = songService.getSongsByArtist("Artista 1");

        assertNotNull(songs);
        assertEquals(1, songs.size());
        assertEquals("Artista 1", songs.get(0).getArtista());
        verify(songRepository, times(1)).findByArtista(any(String.class));
    }
}