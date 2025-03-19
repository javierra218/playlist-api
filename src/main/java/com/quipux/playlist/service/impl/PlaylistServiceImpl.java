package com.quipux.playlist.service.impl;

import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.repository.PlaylistRepository;
import com.quipux.playlist.service.PlaylistService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) {
        // Verificar si ya existe una lista con el mismo nombre
        if (playlistRepository.findByNombre(playlist.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una lista con este nombre");
        }
        return playlistRepository.save(playlist);
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    @Override
    public Optional<Playlist> getPlaylistByName(String name) {
        return playlistRepository.findByNombre(name);
    }

    @Override
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    @Override
    public Playlist addSongToPlaylist(Long playlistId, Song song) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (playlistOptional.isEmpty()) {
            throw new IllegalArgumentException("La lista de reproducción no existe");
        }

        Playlist playlist = playlistOptional.get();
        playlist.getCanciones().add(song);
        return playlistRepository.save(playlist);
    }
}
