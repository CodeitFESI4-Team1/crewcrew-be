package com.crewcrew.domain.crew.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
import com.crewcrew.domain.crew.mapper.CrewMapper;
import com.crewcrew.domain.crew.repository.*;
import com.crewcrew.domain.member.entity.Member;
import com.crewcrew.domain.member.repository.MemberRepository;

class CrewServiceTest {

  @Mock private CrewRepository crewRepository;
  @Mock private MemberRepository memberRepository;
  @Mock private ImageRepository imageRepository;
  @Mock private CrewMapper mapper;

  @InjectMocks private CrewService crewService;
  private Member member;

  private static final Long MEMBER_ID = 1L;
  private static final String TEST_IMAGE_PATH = "src/test/resources/images/test_image.jpg";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    member = createTestMember();
  }

  @Test
  @DisplayName("크루 생성 후 responseDTO 반환")
  void testCreateCrew() {
    CrewCreateRequestDTO request = createCrewCreateRequest();
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));
    Crew savedCrew = createCrew(1L, request.name(), 0);

    when(mapper.toEntity(any(CrewCreateRequestDTO.class), any(Member.class))).thenReturn(savedCrew);
    when(crewRepository.save(any(Crew.class))).thenReturn(savedCrew);
    setupImageRepository(savedCrew.getId());
    when(mapper.crewResponseDTO(any(Crew.class), any(List.class)))
        .thenReturn(createCrewResponseDTO(savedCrew));

    CrewResponseDTO response = crewService.createCrew(request);
    validateResponse(response, savedCrew);
  }

  @Test
  @DisplayName("크루 조회 시 인기순 정렬 적용")
  void testGetCrewByPopularity() {
    CrewFss fss = new CrewFss(null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10, Sort.by("participantCount").descending());

    Crew crew1 = createCrew(1L, "축구 동호회", 5);
    Crew crew2 = createCrew(2L, "농구 동호회", 10);

    when(crewRepository.findFilteredCrews(fss, pageable))
        .thenReturn(createCrewSlice(List.of(crew2, crew1), pageable));

    mockCrewListResponseDTO(crew1);
    mockCrewListResponseDTO(crew2);

    Slice<CrewListResponseDTO> response = crewService.getCrew(fss, pageable);
    assertThat(response).isNotNull();
    assertThat(response.getContent()).hasSize(2);
    assertThat(response.getContent().get(0).name()).isEqualTo("농구 동호회");
    assertThat(response.getContent().get(1).name()).isEqualTo("축구 동호회");
  }

  @Test
  @DisplayName("주최자가 생성한 크루 조회")
  void testGetCreatedCrewsWithFiltering() {
    Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());

    Crew crew1 = createCrew(1L, "축구 동호회", 5);
    Crew crew2 = createCrew(2L, "농구 동호회", 10);
    Crew crew3 = createCrew(3L, "테니스 동호회", 7); // 비주최자

    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));
    when(crewRepository.findByMember(member, pageable))
        .thenReturn(createCrewSlice(List.of(crew1, crew2), pageable));

    mockCrewListResponseDTO(crew1);
    mockCrewListResponseDTO(crew2);

    Slice<CrewListResponseDTO> response = crewService.getCreatedCrew(pageable);

    assertThat(response).isNotNull();
    assertThat(response.getContent()).hasSize(2);
    assertThat(response.getContent())
        .extracting(CrewListResponseDTO::name)
        .containsExactlyInAnyOrder("축구 동호회", "농구 동호회");
    assertThat(response.getContent()).doesNotContain(createCrewListResponseDTO(crew3));
  }

  @Test
  @DisplayName("주최자가 크루 업데이트")
  void testUpdateCrewSuccess() {
    Long crewId = 1L;
    Crew existingCrew = createCrew(crewId, "축구 동호회", 5);

    CrewUpdateRequestDTO updateRequest =
        new CrewUpdateRequestDTO(null, null, null, null, "주말 축구 동호회", "업데이트된 설명", null);

    when(crewRepository.findById(crewId)).thenReturn(Optional.of(existingCrew));
    when(crewRepository.existsByIdAndMemberId(crewId, MEMBER_ID)).thenReturn(true);

    existingCrew.update(updateRequest);

    when(crewRepository.save(existingCrew)).thenReturn(existingCrew);
    when(mapper.crewResponseDTO(any(Crew.class), anyList()))
        .thenReturn(createCrewResponseDTO(existingCrew));

    CrewResponseDTO response = crewService.updateCrew(crewId, updateRequest);

    assertThat(response).isNotNull();
    assertThat(response.name()).isEqualTo("주말 축구 동호회");
    assertThat(response.description()).isEqualTo("업데이트된 설명");
    assertThat(response.capacity()).isEqualTo(existingCrew.getCapacity());
  }

  private CrewCreateRequestDTO createCrewCreateRequest() {
    return new CrewCreateRequestDTO(
        Category.BALL_SPORTS, SubCategory.SOCCER, "축구 동호회", "재미있는 축구 팀", "강남구 논현로", "운동장", 20);
  }

  private void setupImageRepository(Long crewId) {
    Image image = createTestImage();
    when(imageRepository.findByReferenceIdAndImageType(crewId, ImageType.CREW))
        .thenReturn(List.of(image));
  }

  private Image createTestImage() {
    return Image.builder().imagePath(TEST_IMAGE_PATH).imageType(ImageType.CREW).build();
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
    assertThat(response.images().get(0).imagePath()).isEqualTo(TEST_IMAGE_PATH);
  }

  private Slice<Crew> createCrewSlice(List<Crew> content, Pageable pageable) {
    return new SliceImpl<>(content, pageable, false);
  }

  private Member createTestMember() {
    return Member.builder().id(MEMBER_ID).name("테스트 회원").build();
  }

  private Crew createCrew(Long id, String name, int capacity) {
    return Crew.builder()
        .id(id)
        .name(name)
        .description("재미있는 " + name)
        .type(Category.BALL_SPORTS)
        .subType(name.equals("축구 동호회") ? SubCategory.SOCCER : SubCategory.BASKETBALL)
        .location("서울특별시")
        .detailedLocation(name.equals("축구 동호회") ? "강남구" : "마포구")
        .capacity(capacity)
        .participantCount(0)
        .member(member)
        .build();
  }

  private CrewListResponseDTO createCrewListResponseDTO(Crew crew) {
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
        List.of(new ImageResponseDTO(TEST_IMAGE_PATH)),
        crew.getMember().getId(),
        crew.getCreatedAt(),
        crew.getUpdatedAt(),
        crew.getCanceledAt(),
        crew.getIsConfirmed());
  }

  private CrewResponseDTO createCrewResponseDTO(Crew crew) {
    return new CrewResponseDTO(
        crew.getId(),
        crew.getType(),
        crew.getSubType(),
        crew.getName(),
        crew.getDescription(),
        crew.getLocation(),
        crew.getDetailedLocation(),
        crew.getParticipantCount(),
        crew.getCapacity(),
        List.of(new ImageResponseDTO(TEST_IMAGE_PATH)),
        crew.getMember().getId());
  }

  private void mockCrewListResponseDTO(Crew crew) {
    when(mapper.crewListResponseDTO(eq(crew), anyList()))
        .thenReturn(createCrewListResponseDTO(crew));
  }
}
