package six.daoyun.service.CourseCheckInServiceImpl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.entity.CheckInTask;
import six.daoyun.entity.CourseCheckIn;
import six.daoyun.entity.User;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.CommitCheckInRepository;
import six.daoyun.repository.CourseCheckInRepository;
import six.daoyun.service.CourseService;
import six.daoyun.service.CourseCheckInService;


@Service
public class CourseCheckInServiceImpl implements CourseCheckInService {
    @Autowired
    private CourseCheckInRepository checkInRepository;
    @Autowired
    private CommitCheckInRepository commitCheckInRespository;
    @Autowired
    private CourseService courseService;

	@Override
	public long createCourseCheckIn(CourseCheckIn checkIn) {
        final  CourseCheckIn t = this.checkInRepository.save(checkIn);
		return t.getId();
	}

	@Override
	public void updateCourseCheckIn(CourseCheckIn checkIn) {
        this.checkInRepository.save(checkIn);
	}

	@Override
	public void deleteCourseCheckIn(long checkInId) {
        this.checkInRepository.deleteById(checkInId);
	}

	@Override
	public Optional<CourseCheckIn> getCourseCheckIn(long checkInId) {
        return this.checkInRepository.findById(checkInId);
	}

	@Override
	public long commitCheckIn(long checkInId, User student, CheckInTask ccheckIn) {
        final CourseCheckIn checkIn = this.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new NotFound("班课任务不存在"));

        if(!this.courseService.courseHasStudent(checkIn.getCourse(), student)) {
            throw new NotFound("班课中不存在该用户");
        }

        ccheckIn.setStudent(student);
        ccheckIn.setCourseCheckIn(checkIn);

        final CheckInTask cc = this.commitCheckInRespository.save(ccheckIn);
		return cc.getId();
	}

	@Override
	public void deleteCheckInTask(long commitCheckInId) {
        this.commitCheckInRespository.deleteById(commitCheckInId);
	}

	@Override
	public void updateCheckInTask(CheckInTask ccheckIn) {
        this.commitCheckInRespository.save(ccheckIn);
	}

	@Override
	public Optional<CheckInTask> getCheckInTask(long commitCheckInId) {
        return this.commitCheckInRespository.findById(commitCheckInId);
	}

	@Override
	public Optional<CheckInTask> getCheckInTaskByCheckInAndStudent(long checkInId, User student) {
        return this.commitCheckInRespository.findByCourseCheckIn_IdAndStudent(checkInId, student);
	}

	@Override
	public Collection<CheckInTask> getCheckInTasks(long checkInId) {
        return this.commitCheckInRespository.findByCourseCheckIn_Id(checkInId);
	}
}

