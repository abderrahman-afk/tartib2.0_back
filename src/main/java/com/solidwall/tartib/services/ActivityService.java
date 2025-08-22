package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.solidwall.tartib.core.exceptions.BadRequestException;
import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.activity.CreateDto;
import com.solidwall.tartib.dto.activity.UpdateDto;
import com.solidwall.tartib.entities.ActivityEntity;
import com.solidwall.tartib.entities.ComponentEntity;
import com.solidwall.tartib.implementations.ActivityImplementation;
import com.solidwall.tartib.repositories.ActivityRepository;
import com.solidwall.tartib.repositories.ComponentRepository;

@Service
public class ActivityService implements ActivityImplementation {

  @Autowired
  ActivityRepository activityRepository;

  @Autowired
  ComponentRepository componentRepository;

  @Override
  public ActivityEntity getOne(Long id) {
    Optional<ActivityEntity> activity = activityRepository.findById(id);
    if (activity.isPresent()) {
      return activity.get();
    } else {
      throw new NotFoundException("activity not exist");
    }
  }

  @Override
  public ActivityEntity findOne(Map<String, String> data) {
    throw new BadRequestException("param not exist");

  }

  @Override
  public List<ActivityEntity> findAll() {
    if (!activityRepository.findAll().isEmpty()) {
      return activityRepository.findAll();
    } else {
      throw new NotFoundException("not exist any activity");
    }
  }

  @Override
  public ActivityEntity create(CreateDto data) {
    ComponentEntity component = data.getComponent() != null
        ? componentRepository.findById(data.getComponent()).orElse(null)
        : null;
    ActivityEntity newActivity = new ActivityEntity();
    newActivity.setDescription(data.getDescription());
    newActivity.setName(data.getName());
    newActivity.setComponent(component);
    return activityRepository.save(newActivity);
  }

  @Override
  public ActivityEntity update(Long id, UpdateDto data) {

    Optional<ActivityEntity> activity = activityRepository.findById(id);
    ComponentEntity componnent = data.getComponent() != null
        ? componentRepository.findById(data.getComponent()).orElse(null)
        : null;
    if (activity.isPresent()) {
      ActivityEntity updateActivity = activity.get();
      updateActivity.setDescription(data.getDescription());
      updateActivity.setName(data.getName());
      updateActivity.setComponent(componnent);
      return activityRepository.save(updateActivity);
    } else {
      throw new NotFoundException("activity not found");
    }
  }

  @Override
  public void delete(Long id) {
    Optional<ActivityEntity> activity = activityRepository.findById(id);
    if (activity.isPresent()) {
      activityRepository.deleteById(id);
    } else {
      throw new NotFoundException("activity not exist");
    }
  }

}
