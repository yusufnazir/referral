package software.simple.solutions.referral.repository;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.PagingResult;
import software.simple.solutions.framework.core.repository.IGenericRepository;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.valueobjects.ActivityVO;

public interface IActivityRepository extends IGenericRepository {

	PagingResult<Activity> findReferrerRelatedActivity(ActivityVO vo) throws FrameworkException;

}
