package project.hrpjt.organization.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.hrpjt.employee.entity.Employee;
import project.hrpjt.exception.NoAuthorityException;
import project.hrpjt.organization.dto.*;
import project.hrpjt.organization.entity.Organization;
import project.hrpjt.organization.service.OrganizationService;

@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/role_adm/organization") //조직 추가
    @ApiOperation(value = "조직 추가")
    public Organization save(@RequestBody OrganizationSaveDto dto) {
        return organizationService.save(dto);
    }

    @GetMapping("/role_emp/organization") // 조직도 조회
    @ApiOperation(value = "조직도 조회")
    public Page<OrganizationFindDto> findAll(Long orgId, Pageable pageable) {
        return organizationService.findAll(orgId, pageable);
    }

//  조직원 조회
//  ORG_LEADER -> 본인 부서 포함 하위 조직의 조직원 조회 가능.
//  SYS_ADMIN / CEO -> 모든 부서 조회 가능.
//  EMPLOYEE -> 본인 부서만 조회 가능.
    @ApiOperation(value = "조직원 조회")
    @GetMapping("/role_emp/organization/employees")
    public Page<OrganizerFindDto> findEmployee(Pageable pageable) {
        return organizationService.findOrganizerByParam(pageable);
    }

//  조직 폐쇄(종료일을 현재일로 update)
//  상위 조직 폐쇄 시 하위 조직까지 모두 종료일을 현재일 -1일로 업데이트함.
    @DeleteMapping("/role_adm/organization")
    @ApiOperation(value = "조직 폐쇄")
    public Page<OrganizationFindDto> close(@RequestBody OrganizationFindParamDto dto, Pageable pageable) {
        return organizationService.close(dto, pageable);
    }

//  조직넘버, 조직명, 조직에 직원 추가 / 삭제, 조직 구조 변경 가능
//  조직장 -> 조직 정보에 대한 수정은 불가능함. 해당 조직에 대해서 직원을 추가하거나 삭제만 가능.
//  시스템 관리자 -> 모든 정보 수정 가능.
    @PutMapping("/role_lead/organization")
    @ApiOperation(value = "조직 정보 수정")
    public Page<OrganizerFindDto> editOrganization(@RequestBody OrganizationUpdateDto dto, Pageable pageable) {
        return organizationService.edit(dto, pageable);
    }
}
