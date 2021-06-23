package six.daoyun.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.CheckinTask;
import six.daoyun.entity.CourseCheckin;
import six.daoyun.entity.User;


public interface CommitCheckinRepository extends PagingAndSortingRepository<CheckinTask, Long> {
    Optional<CheckinTask>   findByCourseCheckinAndStudent(CourseCheckin task, User student);
    Optional<CheckinTask>   findByCourseCheckin_IdAndStudent(long checkinId, User student);
    Collection<CheckinTask> findByCourseCheckin_Id(long checkinId);
}

