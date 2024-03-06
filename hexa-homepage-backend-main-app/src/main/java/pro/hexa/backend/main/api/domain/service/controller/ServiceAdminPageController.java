package pro.hexa.backend.main.api.domain.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.hexa.backend.main.api.domain.service.dto.AdminCreateServiceRequestDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminModifyServiceRequestDto;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceDetailResponse;
import pro.hexa.backend.main.api.domain.service.dto.AdminServiceListResponse;
import pro.hexa.backend.main.api.domain.service.service.ServiceAdminPageService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ServiceAdminPageController {
    private final ServiceAdminPageService serviceAdminPageService;

    @Operation(description = "서비스 리스트 조회")
    @GetMapping("/serviceList")
    public ResponseEntity<AdminServiceListResponse> getAdminServiceList() {

        return ResponseEntity.ok(serviceAdminPageService.getList());
    }

    @Operation(description = "서비스 수정 창에서 보여줄 정보 조회")
    @GetMapping("/serviceDetail")
    public ResponseEntity<AdminServiceDetailResponse> getAdminServiceDetail(
        @RequestParam Long serviceId
    ) {
        return ResponseEntity.ok(serviceAdminPageService.getDetail(serviceId));
    }

    @Operation(description = "서비스 생성 요청")
    @PostMapping("/createService")
    public ResponseEntity<Boolean> adminCreateService(@RequestBody AdminCreateServiceRequestDto adminCreateServiceRequestDto) {
        serviceAdminPageService.createService(adminCreateServiceRequestDto);
        return ResponseEntity.ok(true);
    }

    @Operation(description = "서비스 수정 요청")
    @PostMapping("/modifyService")
    public ResponseEntity<Boolean> adminModifyService(@RequestBody AdminModifyServiceRequestDto adminModifyServiceRequestDto) {
        serviceAdminPageService.modifyService(adminModifyServiceRequestDto);
        return ResponseEntity.ok(true);
    }

    @Operation(description = "서비스 삭제")
    @DeleteMapping("/deleteService")
    public ResponseEntity<Boolean> adminDeleteService(
        @RequestParam Long serviceId
    ) {
        serviceAdminPageService.delete(serviceId);
        return ResponseEntity.ok(true);
    }
}
