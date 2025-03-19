package com.quipux.playlist.service;

import com.quipux.playlist.model.Song;
import java.util.List;

public interface SongService {
    Song addSong(Song song);

    List<Song> getAllSongs();

    List<Song> getSongsByArtist(String artist);
}
