package software.simple.solutions.referral.service;

import java.util.List;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.ISuperService;
import software.simple.solutions.referral.entities.ActivityType;

public interface IActivityTypeService extends ISuperService {

	List<ActivityType> findByCodes(List<String> codes) throws FrameworkException;

}
