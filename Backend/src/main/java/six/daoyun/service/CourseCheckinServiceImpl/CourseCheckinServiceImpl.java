package six.daoyun.service.CourseCheckinServiceImpl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.entity.CheckinTask;
import six.daoyun.entity.CourseCheckin;
import six.daoyun.entity.User;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.CommitCheckinRepository;
import six.daoyun.repository.CourseCheckinRepository;
import six.daoyun.service.CourseService;
import six.daoyun.service.CourseCheckinService;


@Service
public class CourseCheckinServiceImpl implements CourseCheckinService {
    @Autowired
    private CourseCheckinRepository checkinRepository;
    @Autowired
    private CommitCheckinRepository commitCheckinRespository;
    @Autowired
    private CourseService courseService;

	@Override
	public long createCourseCheckin(CourseCheckin checkin) {
        final  CourseCheckin t = this.checkinRepository.save(checkin);
		return t.getId();
	}

	@Override
	public void updateCourseCheckin(CourseCheckin checkin) {
        this.checkinRepository.save(checkin);
	}

	@Override
	public void deleteCourseCheckin(long checkinId) {
        this.checkinRepository.deleteById(checkinId);
	}

	@Override
	public Optional<CourseCheckin> getCourseCheckin(long checkinId) {
        return this.checkinRepository.findById(checkinId);
	}

	@Override
	public long commitCheckin(long checkinId, User student, CheckinTask ccheckin) {
        final CourseCheckin checkin = this.getCourseCheckin(checkinId)
            .orElseThrow(() -> new NotFound("班课任务不存在"));

        if(!this.courseService.courseHasStudent(checkin.getCourse(), student)) {
            throw new NotFound("班课中不存在该用户");
        }

        ccheckin.setStudent(student);
        ccheckin.setCourseCheckin(checkin);

        final CheckinTask cc = this.commitCheckinRespository.save(ccheckin);
		return cc.getId();
	}

	@Override
	public void deleteCheckinTask(long commitCheckinId) {
        this.commitCheckinRespository.deleteById(commitCheckinId);
	}

	@Override
	public void updateCheckinTask(CheckinTask ccheckin) {
        this.commitCheckinRespository.save(ccheckin);
	}

	@Override
	public Optional<CheckinTask> getCheckinTask(long commitCheckinId) {
        return this.commitCheckinRespository.findById(commitCheckinId);
	}

	@Override
	public Optional<CheckinTask> getCheckinTaskByCheckinAndStudent(long checkinId, User student) {
        return this.commitCheckinRespository.findByCourseCheckin_IdAndStudent(checkinId, student);
	}

	@Override
	public Collection<CheckinTask> getCheckinTasks(long checkinId) {
        return this.commitCheckinRespository.findByCourseCheckin_Id(checkinId);
	}
}

