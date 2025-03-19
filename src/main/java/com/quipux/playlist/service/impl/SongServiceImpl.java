package com.quipux.playlist.service.impl;

import com.quipux.playlist.model.Song;
import com.quipux.playlist.repository.SongRepository;
import com.quipux.playlist.service.SongService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public Song addSong(Song song) {
        // Verificar si ya existe una canción con el mismo título y artista
        if (songRepository.findByArtistaAndTitulo(song.getArtista(), song.getTitulo()).isPresent()) {
            throw new IllegalArgumentException("La canción con este título y artista ya existe");
        }
        return songRepository.save(song);
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> getSongsByArtist(String artist) {
        return songRepository.findByArtista(artist);
    }
}
