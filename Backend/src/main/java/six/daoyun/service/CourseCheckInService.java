package six.daoyun.service;

import java.util.Collection;
import java.util.Optional;

import six.daoyun.entity.CheckInTask;
import six.daoyun.entity.CourseCheckIn;
import six.daoyun.entity.User;

public interface CourseCheckInService {
    long createCourseCheckIn(CourseCheckIn checkIn);
    void updateCourseCheckIn(CourseCheckIn checkIn);
    void deleteCourseCheckIn(long checkInId);
    Optional<CourseCheckIn> getCourseCheckIn(long checkInId);

    long commitCheckIn(long checkInId, User student, CheckInTask checkIn);
    void deleteCheckInTask(long commitCheckInId);
    void updateCheckInTask(CheckInTask checkIn);
    Optional<CheckInTask> getCheckInTask(long commitCheckInId);
    Optional<CheckInTask> getCheckInTaskByCheckInAndStudent(long checkInId, User student);
    Collection<CheckInTask> getCheckInTasks(long checkInId);
}

