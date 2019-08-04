package software.simple.solutions.referral.service;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.PagingResult;
import software.simple.solutions.framework.core.pojo.SecurityValidation;
import software.simple.solutions.framework.core.service.ISuperService;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.valueobjects.ActivityVO;
import software.simple.solutions.referral.valueobjects.PersonFriendVO;

public interface IActivityService extends ISuperService {

	PagingResult<Activity> findReferrerRelatedActivity(ActivityVO vo) throws FrameworkException;

	SecurityValidation registerFriend(PersonFriendVO vo) throws FrameworkException;

}
