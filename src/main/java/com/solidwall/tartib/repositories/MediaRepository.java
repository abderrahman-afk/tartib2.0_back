package com.solidwall.tartib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.MediaEntity;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {

}
