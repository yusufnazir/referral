package software.simple.solutions.referral.repository;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.IGenericRepository;
import software.simple.solutions.referral.entities.PersonReward;

public interface IPersonRewardRepository extends IGenericRepository {

	PersonReward getByPerson(Long referrerId) throws FrameworkException;

}
