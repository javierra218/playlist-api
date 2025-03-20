package com.quipux.playlist.controller;

import com.quipux.playlist.dto.PlaylistDTO;
import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.service.PlaylistService;
import com.quipux.playlist.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistControllerTest {

    @Mock
    private PlaylistService playlistService;

    @Mock
    private SongService songService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PlaylistController playlistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlaylist() {
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setNombre("Mi Playlist");

        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        when(modelMapper.map(any(PlaylistDTO.class), eq(Playlist.class))).thenReturn(playlist);
        when(modelMapper.map(any(Playlist.class), eq(PlaylistDTO.class))).thenReturn(playlistDTO);

        when(playlistService.createPlaylist(any(Playlist.class))).thenReturn(playlist);

        ResponseEntity<PlaylistDTO> response = playlistController.createPlaylist(playlistDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mi Playlist", response.getBody().getNombre());
    }

    @Test
    void getAllPlaylists() {
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setNombre("Mi Playlist");

        when(modelMapper.map(any(Playlist.class), eq(PlaylistDTO.class))).thenReturn(playlistDTO);

        when(playlistService.getAllPlaylists()).thenReturn(Collections.singletonList(playlist));

        ResponseEntity<List<PlaylistDTO>> response = playlistController.getAllPlaylists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Mi Playlist", response.getBody().get(0).getNombre());
    }

    @Test
    void getPlaylistByName() {
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setNombre("Mi Playlist");

        when(modelMapper.map(any(Playlist.class), eq(PlaylistDTO.class))).thenReturn(playlistDTO);

        when(playlistService.getPlaylistByName(any(String.class))).thenReturn(Optional.of(playlist));

        ResponseEntity<PlaylistDTO> response = playlistController.getPlaylistByName("Mi Playlist");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mi Playlist", response.getBody().getNombre());
    }

    @Test
    void deletePlaylist() {
        Playlist playlist = new Playlist();
        playlist.setNombre("Mi Playlist");

        when(playlistService.getPlaylistByName(any(String.class))).thenReturn(Optional.of(playlist));
        doNothing().when(playlistService).deletePlaylist(any(Long.class));

        ResponseEntity<Void> response = playlistController.deletePlaylist("Mi Playlist");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
