package pro.hexa.backend.main.api.domain.seminar.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.hexa.backend.domain.seminar.domain.Seminar;
import pro.hexa.backend.domain.seminar.repository.SeminarRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarDto;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarListResponse;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeminarPageService {

    private final SeminarRepository seminarRepository;

    public SeminarListResponse getSeminarListResponse(String searchText, Integer year, Integer pageNum, Integer perPage) {
        if (pageNum < 1) {
            throw new BadRequestException(BadRequestType.INVALID_PAGE_NUM);
        }

        List<Seminar> seminarList = seminarRepository.findAllByQuery(searchText, year, pageNum, perPage);
        List<SeminarDto> seminars = seminarList.stream()
            .map(seminar -> {
                SeminarDto seminarDto = new SeminarDto();
                seminarDto.fromSeminar(seminar);
                return seminarDto;
            })
            .collect(Collectors.toList());

        int maxPage = seminarRepository.getMaxPage(searchText, year, perPage);

        return SeminarListResponse.builder()
            .seminars(seminars)
            .page(perPage)
            .maxPage(maxPage)
            .build();
    }
}
