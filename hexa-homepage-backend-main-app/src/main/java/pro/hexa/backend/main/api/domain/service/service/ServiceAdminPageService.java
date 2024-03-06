package pro.hexa.backend.main.api.domain.service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pro.hexa.backend.domain.attachment.domain.Attachment;
import pro.hexa.backend.domain.attachment.repository.AttachmentRepository;
import pro.hexa.backend.domain.service.domain.Service;
import pro.hexa.backend.domain.service.repository.ServiceRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.domain.service.dto.AdminCreateServiceRequestDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminModifyServiceRequestDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceDetailResponse;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceListResponse;

@org.springframework.stereotype.Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ServiceAdminPageService {
    private final ServiceRepository serviceRepository;
    private final AttachmentRepository attachmentRepository;

    public AdminServiceListResponse getList() {
        // get
        List<Service> serviceList = serviceRepository.getAll();
        List<AdminServiceDto> dtoList = serviceList.stream()
            .map(service -> AdminServiceDto.builder()
                .serviceId(service.getId())
                .title(service.getTitle())
                .thumbnail(service.getThumbnail().getId())
                .build())
            .collect(Collectors.toList());

        return AdminServiceListResponse.builder()
            .list(dtoList)
            .build();
    }

    public AdminServiceDetailResponse getDetail(Long serviceId) {
        // get
        Service service = serviceRepository.findByIdByQuery(serviceId)
            .orElseThrow(() -> new BadRequestException(BadRequestType.SERVICE_NOT_FOUND));

        Long thumbnailId = Optional.ofNullable(service.getThumbnail())
            .map(Attachment::getId)
            .orElse(null);

        return AdminServiceDetailResponse.builder()
            .title(service.getTitle())
            .thumbnail(thumbnailId)
            .content(service.getContent())
            .siteLink(service.getSiteLink())
            .githubLink(service.getGithubLink())
            .build();
    }

    @Transactional
    public void createService(AdminCreateServiceRequestDto dto) {
        // post
        Attachment attachment = attachmentRepository.findById(dto.getThumbnail())
            .orElseThrow(() -> new BadRequestException(BadRequestType.ATTACHMENT_NOT_EXIST));
        Service newService = Service.create(attachment, dto.getSiteLink(), dto.getGithubLink(), dto.getTitle(), dto.getContent());
        serviceRepository.save(newService);
    }

    @Transactional
    public void modifyService(AdminModifyServiceRequestDto dto) {
        // post
        Service service = serviceRepository.findByIdByQuery(dto.getServiceId())
            .orElseThrow(() -> new BadRequestException(BadRequestType.SERVICE_NOT_FOUND));
        Attachment attachment = attachmentRepository.findById(dto.getThumbnail())
            .orElseThrow(() -> new BadRequestException(BadRequestType.ATTACHMENT_NOT_EXIST));
        service.update(attachment, dto.getSiteLink(), dto.getGithubLink(), dto.getTitle(), dto.getContent());
    }

    @Transactional
    public void delete(Long serviceId) {
        // post
        serviceRepository.deleteById(serviceId);
    }
}
