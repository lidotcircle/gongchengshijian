package six.daoyun.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.CommitTask;
import six.daoyun.entity.CourseTask;
import six.daoyun.entity.User;


public interface CommitTaskRepository extends PagingAndSortingRepository<CommitTask, Long> {
    Optional<CommitTask> findByCourseTaskAndStudent(CourseTask task, User student);
    Optional<CommitTask> findByCourseTask_IdAndStudent(long taskId, User student);
    Collection<CommitTask> findByCourseTask_Id(long taskId);
}

