package com.quipux.playlist.service.impl;

import com.quipux.playlist.model.Playlist;
import com.quipux.playlist.model.Song;
import com.quipux.playlist.repository.PlaylistRepository;
import com.quipux.playlist.repository.SongRepository;
import com.quipux.playlist.service.PlaylistService;
import org.springframework.stereotype.Service;
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
    public Playlist createPlaylist(Playlist playlist) {
        // Verificar si ya existe una lista con el mismo nombre
        if (playlistRepository.findByNombre(playlist.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una lista con este nombre");
        }

        // Limpiar la colección de canciones antes de guardar
        Set<Song> canciones = new HashSet<>(playlist.getCanciones());
        playlist.getCanciones().clear();

        // Guardar la playlist primero
        Playlist savedPlaylist = playlistRepository.save(playlist);

        // Agregar las canciones una por una
        for (Song song : canciones) {
            addSongToPlaylist(savedPlaylist.getId(), song);
        }

        return playlistRepository.findByNombre(savedPlaylist.getNombre()).orElse(savedPlaylist);
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

        // verificar  si la canción ya existe en la base de datos
        List<Song> existingSongs = songRepository.findByArtistaAndTitulo(song.getArtista(), song.getTitulo());

        Song songToAdd;
        if (!existingSongs.isEmpty()) {
            // canción ya existe, usar la existente
            songToAdd = existingSongs.get(0);
        } else {
            // canción no existe, guardarla
            songToAdd = songRepository.save(song);
        }

        // agregar la canción a la playlist
        playlist.addSong(songToAdd);

        return playlistRepository.save(playlist);
    }
}
