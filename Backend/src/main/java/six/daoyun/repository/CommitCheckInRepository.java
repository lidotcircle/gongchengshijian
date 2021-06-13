package six.daoyun.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.CheckInTask;
import six.daoyun.entity.CourseCheckIn;
import six.daoyun.entity.User;


public interface CommitCheckInRepository extends PagingAndSortingRepository<CheckInTask, Long> {
    Optional<CheckInTask>   findByCourseCheckInAndStudent(CourseCheckIn task, User student);
    Optional<CheckInTask>   findByCourseCheckIn_IdAndStudent(long checkInId, User student);
    Collection<CheckInTask> findByCourseCheckIn_Id(long checkInId);
}

