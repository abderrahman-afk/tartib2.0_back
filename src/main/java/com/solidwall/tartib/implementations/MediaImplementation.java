package com.solidwall.tartib.implementations;

import java.util.List;
import com.solidwall.tartib.entities.MediaEntity;

public interface MediaImplementation {

  List<MediaEntity> findAll();

  MediaEntity findOne();

  MediaEntity getOne(Long id);

  MediaEntity create(MediaEntity data);

  MediaEntity update(Long id, MediaEntity data);

  void delete(Long id);

}
