package dev.hust.simpleasia.service;

import dev.hust.simpleasia.entity.domain.Video;
import dev.hust.simpleasia.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;

    public List<Video> getVideoList(Long movieId) {
        return videoRepository.getVideosByMovieId(movieId);
    }

    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    public void saveAllVideos(List<Video> videoList) {
        videoRepository.saveAll(videoList);
    }
}
