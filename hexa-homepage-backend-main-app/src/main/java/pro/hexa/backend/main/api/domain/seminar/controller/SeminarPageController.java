package pro.hexa.backend.main.api.domain.seminar.controller;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.hexa.backend.main.api.domain.seminar.dto.SeminarListResponse;
import pro.hexa.backend.main.api.domain.seminar.service.SeminarPageService;

@RestController
@RequestMapping("/seminar")
@RequiredArgsConstructor
public class SeminarPageController {

    private final SeminarPageService SeminarPageService;

    @Operation(description = "세미나 리스트 조회")
    @GetMapping("/query")
    public ResponseEntity<SeminarListResponse> getSeminarListResponse(
        @RequestParam(required = false, defaultValue = "") String searchText,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer pageNum,
        @RequestParam(required = false) @Valid @Min(value = 1) Integer page
    ) {
        return new ResponseEntity<>(SeminarPageService.getSeminarListResponse(searchText, year, pageNum, page), HttpStatus.OK);
    }
}
