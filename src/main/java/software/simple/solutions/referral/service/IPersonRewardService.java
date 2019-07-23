package software.simple.solutions.referral.service;

import java.math.BigDecimal;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.ISuperService;
import software.simple.solutions.referral.entities.PersonReward;

public interface IPersonRewardService extends ISuperService {

	PersonReward updatePersonReward(Long referrerId, BigDecimal activityRewardAmount, BigDecimal usedRewardAmount) throws FrameworkException;

	BigDecimal getPersonCumulativeReward(Long personId) throws FrameworkException;

}
