package com.edusync.api.course.group.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.group.dto.*;
import com.edusync.api.course.group.enums.GroupMemberRole;
import com.edusync.api.course.group.model.CourseGroup;
import com.edusync.api.course.group.model.CourseGroupMember;
import com.edusync.api.course.group.repo.CourseGroupMemberRepository;
import com.edusync.api.course.group.repo.CourseGroupRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseGroupService {

    private final CourseGroupRepository repository;
    private final CourseGroupMemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;

    public GroupResponse create(GroupRequest.Create request) {
        var course = findCourse(request.courseUuid());
        CourseModule module = null;
        if (request.moduleUuid() != null) {
            module = findModule(request.moduleUuid());
        }
        var createdBy = findAppUser(request.createdByUuid());

        validateUniqueNamePerCourseAndModule(course.getId(), module != null ? module.getId() : null, request.name());

        var group = CourseGroup.builder()
                .course(course)
                .module(module)
                .name(request.name())
                .description(request.description())
                .maxMembers(request.maxMembers())
                .createdBy(createdBy)
                .build();

        group = repository.save(group);
        return GroupResponse.from(group, 0);
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> findAllByCourse(UUID courseUuid, String search) {
        var course = findCourse(courseUuid);
        var spec = Specification.where(GroupSpec.hasCourseId(course.getId()))
                .and(GroupSpec.searchByName(search));
        return repository.findAll(spec).stream()
                .map(group -> GroupResponse.from(group, (int) memberRepository.countByGroupId(group.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public GroupResponse findByUuid(UUID groupUuid) {
        var group = findGroup(groupUuid);
        return GroupResponse.from(group, (int) memberRepository.countByGroupId(group.getId()));
    }

    public GroupResponse update(UUID groupUuid, GroupRequest.Update request) {
        var group = findGroup(groupUuid);

        if (!group.getName().equals(request.name())) {
            validateUniqueNamePerCourseAndModule(
                    group.getCourse().getId(),
                    group.getModule() != null ? group.getModule().getId() : null,
                    request.name()
            );
        }

        group.setName(request.name());
        group.setDescription(request.description());
        group.setMaxMembers(request.maxMembers());
        group = repository.save(group);
        return GroupResponse.from(group, (int) memberRepository.countByGroupId(group.getId()));
    }

    public void delete(UUID groupUuid) {
        var group = findGroup(groupUuid);
        repository.delete(group);
    }

    public GroupMemberResponse addMember(UUID groupUuid, GroupRequest.AddMember request) {
        var group = findGroup(groupUuid);
        var student = findStudent(request.studentUuid());

        if (memberRepository.existsByGroupIdAndStudentId(group.getId(), student.getId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Student is already a member of this group");
        }

        if (group.getMaxMembers() != null) {
            long currentCount = memberRepository.countByGroupId(group.getId());
            if (currentCount >= group.getMaxMembers()) {
                throw new ServiceException(HttpStatus.CONFLICT, "Group has reached its maximum number of members");
            }
        }

        var role = request.role() != null ? request.role() : GroupMemberRole.MEMBER;

        var member = CourseGroupMember.builder()
                .group(group)
                .student(student)
                .role(role)
                .build();

        return GroupMemberResponse.from(memberRepository.save(member));
    }

    public void removeMember(UUID groupUuid, UUID studentUuid) {
        var group = findGroup(groupUuid);
        var student = findStudent(studentUuid);

        var member = memberRepository.findByGroupIdAndStudentId(group.getId(), student.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Group member was not found"));

        memberRepository.delete(member);
    }

    public GroupMemberResponse updateMemberRole(UUID groupUuid, UUID studentUuid, GroupRequest.UpdateMemberRole request) {
        var group = findGroup(groupUuid);
        var student = findStudent(studentUuid);

        var member = memberRepository.findByGroupIdAndStudentId(group.getId(), student.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Group member was not found"));

        member.setRole(request.role());
        return GroupMemberResponse.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<GroupMemberResponse> findMembers(UUID groupUuid) {
        var group = findGroup(groupUuid);
        return memberRepository.findByGroupId(group.getId()).stream()
                .map(GroupMemberResponse::from)
                .toList();
    }

    private CourseGroup findGroup(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Group was not found"));
    }

    private Course findCourse(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private CourseModule findModule(UUID uuid) {
        return moduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private void validateUniqueNamePerCourseAndModule(Long courseId, Long moduleId, String name) {
        var spec = Specification.where(GroupSpec.hasCourseId(courseId))
                .and((root, query, cb) -> {
                    if (moduleId == null) {
                        return cb.isNull(root.get("module"));
                    }
                    return cb.equal(root.get("module").get("id"), moduleId);
                })
                .and((root, query, cb) -> cb.equal(cb.lower(root.get("name")), name.toLowerCase()));

        if (repository.count(spec) > 0) {
            throw new ServiceException(HttpStatus.CONFLICT, "A group with this name already exists in this course and module");
        }
    }
}
