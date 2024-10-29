package com.crewcrew.domain.crew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crewcrew.domain.crew.entity.Image;
import com.crewcrew.domain.crew.enums.ImageType;

public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findByReferenceIdAndImageType(Long referenceId, ImageType imageType);
}
