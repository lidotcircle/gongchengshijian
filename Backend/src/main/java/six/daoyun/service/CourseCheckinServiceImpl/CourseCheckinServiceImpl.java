package six.daoyun.service.CourseCheckinServiceImpl;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import six.daoyun.entity.CheckinTask;
import six.daoyun.entity.CourseCheckin;
import six.daoyun.entity.User;
import six.daoyun.exception.Forbidden;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.CommitCheckinRepository;
import six.daoyun.repository.CourseCheckinRepository;
import six.daoyun.repository.CourseStudentRepository;
import six.daoyun.service.CourseService;
import six.daoyun.service.SysparamService;
import six.daoyun.utils.SystemParameter;
import six.daoyun.service.CourseCheckinService;


@Service
public class CourseCheckinServiceImpl implements CourseCheckinService {
    @Autowired
    private CourseCheckinRepository checkinRepository;
    @Autowired
    private CommitCheckinRepository commitCheckinRespository;
    @Autowired
    private CourseStudentRepository courseStudentRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private SysparamService sysparamService;

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

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CourseCheckinServiceImpl.class);
    private ObjectMapper objectMapper = new ObjectMapper();
	@Override
    @Transactional
	public long commitCheckin(long checkinId, User student, CheckinTask ccheckin) {
        final CourseCheckin checkin = this.getCourseCheckin(checkinId)
            .orElseThrow(() -> new NotFound("班课任务不存在"));

        final var course = checkin.getCourse();
        if(!this.courseService.courseHasStudent(course, student)) {
            throw new NotFound("班课中不存在该用户");
        }

        if (checkin.getDeadline().before(Date.from(Instant.now().plusSeconds(3)))) {
            throw new Forbidden("已超时");
        }

        if(this.commitCheckinRespository.findByCourseCheckinAndStudent(checkin, student).isPresent()) {
            throw new Forbidden("用户已签到");
        }

        try {
            var cnode = this.objectMapper.readTree(checkin.getJsonData());
            var type  = cnode.get("type").asText();
            var node = this.objectMapper.readTree(ccheckin.getCheckinJsonData());

            if (type.equals("key")) {
                var key = cnode.get("key").asText();
                var ukey = node.get("key").asText();
                if (!key.equals(ukey)) {
                    throw new Forbidden("签到密码错误");
                }
            }
            else if (type.equals("gesture")) {
                var key = cnode.get("gesture").asText();
                var ukey = node.get("gesture").asText();
                if (!key.equals(ukey)) {
                    throw new Forbidden("签到手势错误");
                }
            }
            else if (type.equals("location") && !node.has("location")) {
                throw new Forbidden("位置签到需要携带位置信息");
            }
        } catch (JsonProcessingException e) {
            throw new Forbidden("数据格式错误");
        }

        final var cs = this.courseStudentRepository.findByCourseAndStudent(course, student)
            .orElseThrow(() -> new Forbidden("内部错误"));;
        final var score = this.sysparamService.get(SystemParameter.Experience).get();
        cs.setGrade(cs.getGrade() + Integer.valueOf(score.getParameterValue()));
        this.courseStudentRepository.save(cs);

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

