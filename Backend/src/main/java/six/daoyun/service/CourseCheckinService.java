package six.daoyun.service;

import java.util.Collection;
import java.util.Optional;

import six.daoyun.entity.CheckinTask;
import six.daoyun.entity.CourseCheckin;
import six.daoyun.entity.User;

public interface CourseCheckinService {
    long createCourseCheckin(CourseCheckin checkin);
    void updateCourseCheckin(CourseCheckin checkin);
    void deleteCourseCheckin(long checkinId);
    Optional<CourseCheckin> getCourseCheckin(long checkinId);

    long commitCheckin(long checkinId, User student, CheckinTask checkin);
    void deleteCheckinTask(long commitCheckinId);
    void updateCheckinTask(CheckinTask checkin);
    Optional<CheckinTask> getCheckinTask(long commitCheckinId);
    Optional<CheckinTask> getCheckinTaskByCheckinAndStudent(long checkinId, User student);
    Collection<CheckinTask> getCheckinTasks(long checkinId);
}

