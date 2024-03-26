package pro.hexa.backend.main.api.domain.seminar.service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.seminar.domain.Seminar;
import pro.hexa.backend.domain.seminar.repository.SeminarRepository;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.config.security.dto.CustomUserDetails;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminCreateSeminarRequestDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminModifySeminarRequestDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarDetailResponse;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarDto;
import pro.hexa.backend.main.api.domain.seminar.dto.AdminSeminarListResponse;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeminarAdminPageService {

    private final SeminarRepository seminarRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    public AdminSeminarListResponse getAdminSeminarList(Integer pageNum, Integer perPage) {

        List<Seminar> seminarList = seminarRepository.findAllInAdminPage(pageNum, perPage);
        if (seminarList.isEmpty()) {
            return AdminSeminarListResponse.builder()
                    .totalPage(0)
                    .list(new ArrayList<>(0))
                    .build();
        }
        int maxPage = seminarRepository.getAdminMaxPage(perPage);
        List<AdminSeminarDto> seminars = seminarList.stream()
                .map(seminar ->
                {
                    AdminSeminarDto adminSeminarDto = new AdminSeminarDto();
                    adminSeminarDto.fromSeminar(seminar);
                    return adminSeminarDto;
                })
                .collect(Collectors.toList());

        return AdminSeminarListResponse
                .builder()
                .totalPage(maxPage)
                .list(seminars)
                .build();
    }

    public AdminSeminarDetailResponse getAdminSeminarDetail(Long seminarId) {
        Optional<Seminar> seminar = seminarRepository.findById(seminarId);
        if (seminar.isEmpty()) {
            throw new BadRequestException(BadRequestType.SEMINAR_NOT_FOUND);
        }

        AdminSeminarDetailResponse adminSeminarDetailResponse = new AdminSeminarDetailResponse();
        adminSeminarDetailResponse.fromSeminar(seminar.get());
        return adminSeminarDetailResponse;
    }

    @Transactional
    public void adminCreateSeminar(AdminCreateSeminarRequestDto adminCreateSeminarRequestDto) {
        validateAdminCreateSeminarRequest(adminCreateSeminarRequestDto);

        List<Attachment> attachmentList = new ArrayList<>();


        if (adminCreateSeminarRequestDto.getAttachments() != null) {
            for (Long attachmentId : adminCreateSeminarRequestDto.getAttachments()) {
                Attachment attachment = attachmentRepository.findById(attachmentId)
                        .orElseThrow(() -> new BadRequestException(BadRequestType.ATTACHMENT_NOT_EXIST));
                attachmentList.add(attachment);
            }
        }

        LocalDateTime seminarDate = adminCreateSeminarRequestDto.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findById(username).orElse(null);

        Seminar seminar = Seminar.create(
                seminarDate,
                user,
                attachmentList,
                adminCreateSeminarRequestDto.getTitle(),
                adminCreateSeminarRequestDto.getContent()
        );

        seminarRepository.save(seminar);
    }
    private void validateAdminCreateSeminarRequest(AdminCreateSeminarRequestDto adminCreateSeminarRequestDto) {

        if ((StringUtil.isNullOrEmpty(adminCreateSeminarRequestDto.getTitle()))
                || (adminCreateSeminarRequestDto.getDate() == null)
                || (adminCreateSeminarRequestDto.getContent() == null)
        ) {
            throw new BadRequestException(BadRequestType.NULL_VALUE);
        }
    }


    @Transactional
    public void adminModifySeminar(AdminModifySeminarRequestDto adminModifySeminarRequestDto) {
        Seminar seminar = seminarRepository.findByQuery(adminModifySeminarRequestDto.getSeminarId())
                .orElseThrow(() -> new BadRequestException(BadRequestType.SEMINAR_NOT_FOUND));

        validateAdminModifySeminarRequest(adminModifySeminarRequestDto);

        adminModifySeminarRequestDto.setTitle(
                Optional.ofNullable(adminModifySeminarRequestDto.getTitle())
                        .orElseGet(seminar::getTitle)
        );


        if (CollectionUtils.isEmpty(adminModifySeminarRequestDto.getAttachments())) {
            adminModifySeminarRequestDto.setAttachments(
                    seminar.getAttachments().stream()
                            .map(Attachment::getId)
                            .collect(Collectors.toList())
            );
        }

        adminModifySeminarRequestDto.setContent(
                Optional.ofNullable(adminModifySeminarRequestDto.getContent())
                        .orElseGet(seminar::getContent)
        );

        adminModifySeminarRequestDto.setDate(
                Optional.ofNullable(adminModifySeminarRequestDto.getDate())
                        .orElseGet(seminar::getDate)
        );

        List<Attachment> attachmentList = new ArrayList<>();

        if (adminModifySeminarRequestDto.getAttachments() != null) {
            for (Long attachmentId :adminModifySeminarRequestDto.getAttachments()) {
                Attachment attachment = attachmentRepository.findById(attachmentId)
                        .orElseThrow(() -> new BadRequestException(BadRequestType.ATTACHMENT_NOT_EXIST));
                attachmentList.add(attachment);
            }
        }

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findById(username).orElse(null);

        seminar.update(
                adminModifySeminarRequestDto.getDate(),
                user,
                attachmentList,
                adminModifySeminarRequestDto.getTitle(),
                adminModifySeminarRequestDto.getContent()
        );
    }
    private void validateAdminModifySeminarRequest(AdminModifySeminarRequestDto adminModifySeminarRequestDto) {
        if ((adminModifySeminarRequestDto.getTitle() == null)
                && (adminModifySeminarRequestDto.getSeminarId() == null)
                && (adminModifySeminarRequestDto.getDate() == null)
                && (adminModifySeminarRequestDto.getAttachments().isEmpty())
                && (adminModifySeminarRequestDto.getContent() == null)) {
            throw new BadRequestException(BadRequestType.NULL_MODIFY_SEMINAR_VALUES);
        }
    }

    @Transactional
    public void adminDeleteSeminar(Long seminarId) {
        seminarRepository.deleteById(seminarId);
    }
}