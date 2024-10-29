package com.crewcrew.domain.crew.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.crewcrew.domain.crew.dto.request.*;
import com.crewcrew.domain.crew.dto.response.*;
import com.crewcrew.domain.crew.entity.*;
import com.crewcrew.domain.crew.enums.*;
import com.crewcrew.domain.crew.repository.*;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;

class CrewServiceTest {

  @Mock private CrewRepository crewRepository;

  @Mock private MemberRepository memberRepository;

  @Mock private ImageRepository imageRepository;

  @InjectMocks private CrewService crewService;

  private Member member;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    member = createTestMember();
  }

  private Member createTestMember() {
    return Member.builder().id(1L).name("테스트 회원").build();
  }

  @Test
  @DisplayName("크루 생성 후 responseDTO 반환")
  void testCreateCrew() {
    // given
    CrewCreateRequestDTO request = createCrewCreateRequest();
    when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

    Crew savedCrew = createSavedCrew(request);
    when(crewRepository.save(any(Crew.class))).thenReturn(savedCrew);

    Image image = createTestImage();
    when(imageRepository.findByReferenceIdAndImageType(savedCrew.getId(), ImageType.CREW))
        .thenReturn(List.of(image));

    // when
    CrewResponseDTO response = crewService.createCrew(request);

    // then
    validateResponse(response, savedCrew);
  }

  private CrewCreateRequestDTO createCrewCreateRequest() {
    return new CrewCreateRequestDTO(
        Category.BALL_SPORTS, SubCategory.SOCCER, "축구 동호회", "재미있는 축구 팀", "강남구 논현로", "운동장", 20);
  }

  private Crew createSavedCrew(CrewCreateRequestDTO request) {
    return Crew.builder()
        .id(1L)
        .name(request.name())
        .type(request.type())
        .subType(request.subType())
        .location(request.location())
        .detailedLocation(request.detailedLocation())
        .capacity(request.capacity())
        .member(member)
        .build();
  }

  private Image createTestImage() {
    return Image.builder().imagePath("test_image_path").imageType(ImageType.CREW).build();
  }

  private void validateResponse(CrewResponseDTO response, Crew savedCrew) {
    assertThat(response).isNotNull();
    assertThat(response.crewId()).isEqualTo(savedCrew.getId());
    assertThat(response.name()).isEqualTo(savedCrew.getName());
    assertThat(response.type()).isEqualTo(savedCrew.getType());
    assertThat(response.subType()).isEqualTo(savedCrew.getSubType());
    assertThat(response.location()).isEqualTo(savedCrew.getLocation());
    assertThat(response.detailedLocation()).isEqualTo(savedCrew.getDetailedLocation());
    assertThat(response.capacity()).isEqualTo(savedCrew.getCapacity());
    assertThat(response.images()).hasSize(1);
    assertThat(response.images().get(0).imagePath()).isEqualTo("test_image_path");
  }
}
