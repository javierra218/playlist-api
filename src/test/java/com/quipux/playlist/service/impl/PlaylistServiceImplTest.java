package com.quipux.playlist.service.impl;

import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.repository.PlaylistRepository;
import com.quipux.playlist.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistServiceImplTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        when(playlistRepository.findByNombre(any(String.class))).thenReturn(Optional.empty());
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        Playlist createdPlaylist = playlistService.createPlaylist(playlist);

        assertNotNull(createdPlaylist);
        assertEquals("Mi Playlist", createdPlaylist.getNombre());
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void addSongToPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setNombre("Mi Playlist");

        Song song = new Song();
        song.setTitulo("Canción 1");
        song.setArtista("Artista 1");

        when(playlistRepository.findById(any(Long.class))).thenReturn(Optional.of(playlist));
        when(songRepository.findByArtistaAndTitulo(any(String.class), any(String.class))).thenReturn(Collections.emptyList());
        when(songRepository.save(any(Song.class))).thenReturn(song);
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        Playlist updatedPlaylist = playlistService.addSongToPlaylist(1L, song);

        assertNotNull(updatedPlaylist);
        assertEquals(1, updatedPlaylist.getCanciones().size());
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void getAllPlaylists() {
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        when(playlistRepository.findAll()).thenReturn(Collections.singletonList(playlist));

        List<Playlist> playlists = playlistService.getAllPlaylists();

        assertNotNull(playlists);
        assertEquals(1, playlists.size());
        assertEquals("Mi Playlist", playlists.get(0).getNombre());
        verify(playlistRepository, times(1)).findAll();
    }

    @Test
    void getPlaylistByName() {
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        when(playlistRepository.findByNombre(any(String.class))).thenReturn(Optional.of(playlist));

        Optional<Playlist> foundPlaylist = playlistService.getPlaylistByName("Mi Playlist");

        assertTrue(foundPlaylist.isPresent());
        assertEquals("Mi Playlist", foundPlaylist.get().getNombre());
        verify(playlistRepository, times(1)).findByNombre(any(String.class));
    }

    @Test
    void deletePlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setNombre("Mi Playlist");

        when(playlistRepository.findById(any(Long.class))).thenReturn(Optional.of(playlist));
        doNothing().when(playlistRepository).delete(any(Playlist.class));

        playlistService.deletePlaylist(1L);

        verify(playlistRepository, times(1)).delete(any(Playlist.class));
    }
}