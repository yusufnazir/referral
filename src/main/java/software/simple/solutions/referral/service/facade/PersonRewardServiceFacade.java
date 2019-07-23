package software.simple.solutions.referral.service.facade;

import java.math.BigDecimal;

import com.vaadin.ui.UI;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.facade.SuperServiceFacade;
import software.simple.solutions.referral.entities.PersonReward;
import software.simple.solutions.referral.service.IPersonRewardService;

public class PersonRewardServiceFacade extends SuperServiceFacade<IPersonRewardService>
		implements IPersonRewardService {

	private static final long serialVersionUID = -360592525044181436L;

	public PersonRewardServiceFacade(UI ui, Class<IPersonRewardService> s) {
		super(ui, s);
	}

	public static PersonRewardServiceFacade get(UI ui) {
		return new PersonRewardServiceFacade(ui, IPersonRewardService.class);
	}

	@Override
	public BigDecimal getPersonCumulativeReward(Long personId) throws FrameworkException {
		return service.getPersonCumulativeReward(personId);
	}

	@Override
	public PersonReward updatePersonReward(Long referrerId, BigDecimal activityRewardAmount,
			BigDecimal usedRewardAmount) throws FrameworkException {
		return service.updatePersonReward(referrerId, activityRewardAmount, usedRewardAmount);
	}

}
