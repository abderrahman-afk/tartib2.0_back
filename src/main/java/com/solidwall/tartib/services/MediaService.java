package com.solidwall.tartib.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.entities.MediaEntity;
import com.solidwall.tartib.implementations.MediaImplementation;
import com.solidwall.tartib.repositories.MediaRepository;

@Service
public class MediaService implements MediaImplementation {

  @Autowired
  MediaRepository mediaRepository;

  @Override
  public MediaEntity getOne(Long id) {
    Optional<MediaEntity> media = mediaRepository.findById(id);
    if (media.isPresent()) {
      return media.get();
    } else {
      throw new NotFoundException("media not exist");
    }
  }

  @Override
  public MediaEntity findOne() {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public List<MediaEntity> findAll() {
    if (!mediaRepository.findAll().isEmpty()) {
      return mediaRepository.findAll();
    } else {
      throw new NotFoundException("not exist any media ");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<MediaEntity> article = mediaRepository.findById(id);
    if (article.isPresent()) {
      mediaRepository.deleteById(id);
    } else {
      throw new NotFoundException("media not exist");
    }
  }

  @Override
  public MediaEntity create(MediaEntity data) {
    return mediaRepository.save(data);
  }

  @Override
  public MediaEntity update(Long id, MediaEntity data) {
    Optional<MediaEntity> media = mediaRepository.findById(id);
    if (media.isPresent()) {
      MediaEntity updateMedia = media.get();
      updateMedia.setExtention(data.getExtention());
      updateMedia.setSize(data.getSize());
      updateMedia.setSource(data.getSource());
      updateMedia.setType(data.getType());
      return mediaRepository.save(updateMedia);
    } else {
      throw new NotFoundException("media not found");
    }
  }
}
