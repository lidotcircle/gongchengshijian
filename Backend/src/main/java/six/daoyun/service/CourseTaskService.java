package six.daoyun.service;

import java.util.Collection;
import java.util.Optional;

import six.daoyun.entity.CommitTask;
import six.daoyun.entity.CourseTask;
import six.daoyun.entity.User;

public interface CourseTaskService {
    long createCourseTask(CourseTask task);
    void updateCourseTask(CourseTask task);
    void deleteCourseTask(long taskId);
    Optional<CourseTask> getCourseTask(long taskId);

    long commitTask(long taskId, User student, CommitTask task);
    void deleteCommitTask(long commitTaskId);
    void updateCommitTask(CommitTask task);
    Optional<CommitTask> getCommitTask(long commitTaskId);
    Optional<CommitTask> getCommitTaskByTaskAndStudent(long taskId, User student);
    Collection<CommitTask> getCommitTasks(long taskId);
    void commitTaskScore(long commitTaskId, long grade, String teacherDo);
}

