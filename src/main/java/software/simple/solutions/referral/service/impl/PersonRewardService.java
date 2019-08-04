package software.simple.solutions.referral.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import software.simple.solutions.framework.core.annotations.ServiceRepository;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.Arg;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.properties.SystemMessageProperty;
import software.simple.solutions.framework.core.service.impl.SuperService;
import software.simple.solutions.framework.core.valueobjects.SuperVO;
import software.simple.solutions.referral.entities.PersonReward;
import software.simple.solutions.referral.properties.PersonRewardProperty;
import software.simple.solutions.referral.properties.ReferralMessageProperty;
import software.simple.solutions.referral.repository.IPersonRewardRepository;
import software.simple.solutions.referral.service.IPersonRewardService;
import software.simple.solutions.referral.valueobjects.PersonRewardVO;

@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
@ServiceRepository(claz = IPersonRewardRepository.class)
public class PersonRewardService extends SuperService implements IPersonRewardService {

	private static final long serialVersionUID = 2401669851924947487L;

	@Autowired
	private IPersonRewardRepository personRewardRepository;

	@Override
	public <T, R extends SuperVO> T updateSingle(R valueObject) throws FrameworkException {
		PersonRewardVO vo = (PersonRewardVO) valueObject;

		if (vo.getPersonId() == null) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(PersonRewardProperty.PERSON));
		}

		PersonReward personReward = new PersonReward();
		if (!vo.isNew()) {
			personReward = get(PersonReward.class, vo.getId());
		}
		personReward.setPerson(get(Person.class, vo.getPersonId()));
		personReward.setCumulativeReward(vo.getCumulativeReward());

		return (T) saveOrUpdate(personReward, vo.isNew());
	}

	@Override
	public PersonReward updatePersonReward(Long referrerId, BigDecimal activityRewardAmount,
			BigDecimal usedRewardAmount) throws FrameworkException {
		PersonReward personReward = personRewardRepository.getByPerson(referrerId);
		boolean isNew = false;
		BigDecimal cumulativeAmount = (personReward != null && personReward.getCumulativeReward() != null)
				? personReward.getCumulativeReward() : BigDecimal.ZERO;

		if (usedRewardAmount != null && usedRewardAmount.compareTo(BigDecimal.ZERO) > 0
				&& cumulativeAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new FrameworkException(ReferralMessageProperty.INSUFFICIENT_FUNDS);
		} else if (personReward == null) {
			isNew = true;
			personReward = new PersonReward();
			personReward.setPerson(get(Person.class, referrerId));
			personReward.setCumulativeReward(activityRewardAmount);
		} else {
			personReward.setCumulativeReward(cumulativeAmount
					.subtract(usedRewardAmount == null ? BigDecimal.ZERO : usedRewardAmount)
					.add(activityRewardAmount == null ? BigDecimal.ZERO : activityRewardAmount));
		}
		return saveOrUpdate(personReward, isNew);
	}

	@Override
	public BigDecimal getPersonCumulativeReward(Long personId) throws FrameworkException {
		if (personId == null) {
			return BigDecimal.ZERO;
		}
		PersonReward personReward = personRewardRepository.getByPerson(personId);
		if (personReward == null || personReward.getCumulativeReward() == null) {
			return BigDecimal.ZERO;
		}

		return personReward.getCumulativeReward();
	}

}
