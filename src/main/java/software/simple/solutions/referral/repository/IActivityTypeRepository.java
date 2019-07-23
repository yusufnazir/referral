package software.simple.solutions.referral.repository;

import java.util.List;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.IGenericRepository;
import software.simple.solutions.referral.entities.ActivityType;

public interface IActivityTypeRepository extends IGenericRepository {

	List<ActivityType> findByCodes(List<String> codes) throws FrameworkException;

}
