package six.daoyun.service.CourseTaskServiceImpl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.entity.CommitTask;
import six.daoyun.entity.CourseTask;
import six.daoyun.entity.User;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.CommitTaskRepository;
import six.daoyun.repository.CourseTaskRepository;
import six.daoyun.service.CourseTaskService;


@Service
public class CourseTaskServiceImpl implements CourseTaskService {
    @Autowired
    private CourseTaskRepository taskRepository;
    @Autowired
    private CommitTaskRepository commitRespository;

	@Override
	public long createCourseTask(CourseTask task) {
        final  CourseTask t = this.taskRepository.save(task);
		return t.getId();
	}

	@Override
	public void updateCourseTask(CourseTask task) {
        this.taskRepository.save(task);
	}

	@Override
	public void deleteCourseTask(long taskId) {
        this.taskRepository.deleteById(taskId);
	}

	@Override
	public Optional<CourseTask> getCourseTask(long taskId) {
        return this.taskRepository.findById(taskId);
	}

	@Override
	public long commitTask(long taskId, User student, CommitTask ctask) {
        final CourseTask task = this.getCourseTask(taskId)
            .orElseThrow(() -> new NotFound("班课任务不存在"));

        if(!task.getCourse().getStudents().contains(student)) {
            throw new NotFound("班课中不存在该用户");
        }

        ctask.setStudent(student);
        ctask.setCourse(task);

        final CommitTask cc = this.commitRespository.save(ctask);
		return cc.getId();
	}

	@Override
	public void deleteCommitTask(long commitTaskId) {
        this.commitRespository.deleteById(commitTaskId);
	}

	@Override
	public void updateCommitTask(CommitTask ctask) {
        this.commitRespository.save(ctask);
	}

	@Override
	public Optional<CommitTask> getCommitTask(long commitTaskId) {
        return this.commitRespository.findById(commitTaskId);
	}

	@Override
	public Optional<CommitTask> getCommitTaskByTaskAndStudent(long taskId, User student) {
        return this.commitRespository.findByCourseTask_IdAndStudent(taskId, student);
	}

	@Override
	public Collection<CommitTask> getCommitTasks(long taskId) {
        return this.commitRespository.findByCourseTask_Id(taskId);
	}

	@Override
	public void commitTaskScore(long commitTaskId, long grade, String teacherDo) {
        final CommitTask ctask = this.getCommitTask(commitTaskId)
            .orElseThrow(() -> new NotFound("班课任务不存在"));
        ctask.setGrade(grade);
        if(teacherDo != null) {
            ctask.setTeacherDoThis(teacherDo);
        }
        this.commitRespository.save(ctask);
	}
}

