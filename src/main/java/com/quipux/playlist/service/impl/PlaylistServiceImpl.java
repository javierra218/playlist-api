package com.quipux.playlist.service.impl;

import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.repository.PlaylistRepository;
import com.quipux.playlist.repository.SongRepository;
import com.quipux.playlist.service.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    @Override
    @Transactional
    public Playlist createPlaylist(@Valid Playlist playlist) {
        // Verificar si ya existe una lista con el mismo nombre
        if (playlistRepository.findByNombre(playlist.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una lista con este nombre");
        }
        return playlistRepository.save(playlist);
    }

    @Override
    @Transactional
    public Playlist addSongToPlaylist(Long playlistId, @Valid Song song) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (playlistOptional.isEmpty()) {
            throw new IllegalArgumentException("La lista de reproducción no existe");
        }

        Playlist playlist = playlistOptional.get();

        // verificar si la canción ya existe en la base de datos
        List<Song> existingSongs = songRepository.findByArtistaAndTitulo(song.getArtista(), song.getTitulo());
        Song songToAdd;

        if (!existingSongs.isEmpty()) {
            songToAdd = existingSongs.get(0);
        } else {
            songToAdd = songRepository.save(song);
        }

        playlist.addSong(songToAdd);
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
    @Transactional
    public void deletePlaylist(Long id) {
        playlistRepository.findById(id).ifPresent(playlist -> {
            Set<Song> canciones = new HashSet<>(playlist.getCanciones());
            canciones.forEach(song -> song.removePlaylist(playlist));
            playlistRepository.delete(playlist);
        });
    }
}
