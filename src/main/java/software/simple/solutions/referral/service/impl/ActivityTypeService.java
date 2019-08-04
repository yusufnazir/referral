package software.simple.solutions.referral.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import software.simple.solutions.framework.core.annotations.ServiceRepository;
import software.simple.solutions.framework.core.exceptions.Arg;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.properties.SystemMessageProperty;
import software.simple.solutions.framework.core.service.impl.SuperService;
import software.simple.solutions.framework.core.valueobjects.SuperVO;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.properties.ActivityTypeProperty;
import software.simple.solutions.referral.repository.IActivityTypeRepository;
import software.simple.solutions.referral.service.IActivityTypeService;
import software.simple.solutions.referral.valueobjects.ActivityTypeVO;

@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
@ServiceRepository(claz = IActivityTypeRepository.class)
public class ActivityTypeService extends SuperService implements IActivityTypeService {

	private static final long serialVersionUID = 2401669851924947487L;

	@Autowired
	private IActivityTypeRepository activityTypeRepository;

	@Override
	public <T, R extends SuperVO> T updateSingle(R valueObject) throws FrameworkException {
		ActivityTypeVO vo = (ActivityTypeVO) valueObject;

		if (StringUtils.isBlank(vo.getName())) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(ActivityTypeProperty.NAME));
		}

		if (StringUtils.isBlank(vo.getCode())) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(ActivityTypeProperty.CODE));
		}

		ActivityType activityType = new ActivityType();
		if (!vo.isNew()) {
			activityType = get(ActivityType.class, vo.getId());
		}
		activityType.setName(vo.getCode());
		activityType.setName(vo.getName());
		activityType.setDescription(vo.getDescription());
		activityType.setActive(vo.getActive());

		return (T) saveOrUpdate(activityType, vo.isNew());
	}

	@Override
	public List<ActivityType> findByCodes(List<String> codes) throws FrameworkException {
		return activityTypeRepository.findByCodes(codes);
	}

}
