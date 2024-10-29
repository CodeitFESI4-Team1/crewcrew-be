package com.crewcrew.domain.crew.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

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
    setupImageRepository(savedCrew.getId());

    // when
    CrewResponseDTO response = crewService.createCrew(request);

    // then
    validateResponse(response, savedCrew);
  }

  @Test
  @DisplayName("크루 조회 시 인기순 정렬 적용")
  void testGetCrewByPopularity() {
    // given
    CrewFss fss = new CrewFss(null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10, Sort.by("participantCount").descending());

    Crew crew1 = createCrew(1L, "축구 동호회", 5);
    Crew crew2 = createCrew(2L, "농구 동호회", 10);

    when(crewRepository.findFilteredCrews(fss, pageable))
        .thenReturn(createCrewSlice(List.of(crew2, crew1), pageable));

    // when
    Slice<CrewListResponseDTO> response = crewService.getCrew(fss, pageable);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getContent()).hasSize(2);
    assertThat(response.getContent().get(0).name()).isEqualTo("농구 동호회");
    assertThat(response.getContent().get(1).name()).isEqualTo("축구 동호회");
  }

  @Test
  @DisplayName("크루 조회 시 키워드 필터링 적용")
  void testGetCrewByKeyword() {
    // given
    CrewFss fss = new CrewFss(null, null, "마포구", null, "농구");
    Pageable pageable = PageRequest.of(0, 10, Sort.by("participantCount").descending());

    Crew crew1 = createCrew(1L, "축구 동호회", 5);
    Crew crew2 = createCrew(2L, "농구 동호회", 10);

    when(crewRepository.findFilteredCrews(fss, pageable))
        .thenReturn(createCrewSlice(List.of(crew2), pageable));

    // when
    Slice<CrewListResponseDTO> response = crewService.getCrew(fss, pageable);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getContent()).hasSize(1);
    assertThat(response.getContent().get(0).name()).isEqualTo("농구 동호회");
    assertThat(response.getContent()).doesNotContain(convertToListDTO(crew1));
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

  private void setupImageRepository(Long crewId) {
    Image image = createTestImage();
    when(imageRepository.findByReferenceIdAndImageType(crewId, ImageType.CREW))
        .thenReturn(List.of(image));
  }

  private Crew createCrew(Long id, String name, int participantCount) {
    return Crew.builder()
        .id(id)
        .name(name)
        .description("재미있는 " + name)
        .type(Category.BALL_SPORTS)
        .subType(name.equals("축구 동호회") ? SubCategory.SOCCER : SubCategory.BASKETBALL)
        .location("서울특별시")
        .detailedLocation(name.equals("축구 동호회") ? "강남구" : "마포구")
        .capacity(20)
        .participantCount(participantCount)
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

  private CrewListResponseDTO convertToListDTO(Crew crew) {
    return new CrewListResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getDescription(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        List.of(),
        crew.getMember().getId(),
        crew.getCreatedAt(),
        crew.getUpdatedAt(),
        crew.getCanceledAt(),
        crew.getIsConfirmed());
  }

  private Slice<Crew> createCrewSlice(List<Crew> content, Pageable pageable) {
    return new SliceImpl<>(content, pageable, false);
  }
}
