package software.simple.solutions.referral.event.listener;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import software.simple.solutions.framework.core.entities.ApplicationUser;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.event.ApplicationUserEvent;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.IPersonRelationService;
import software.simple.solutions.framework.core.valueobjects.PersonRelationVO;
import software.simple.solutions.referral.constants.ReferralRelationType;
import software.simple.solutions.referral.service.IPersonFriendService;

@Component
public class ApplicationUserEventListener implements ApplicationListener<ApplicationUserEvent> {

	private static final Logger logger = LogManager.getLogger(ApplicationUserEventListener.class);

	@Autowired
	private IPersonRelationService personRelationService;

	@Autowired
	private IPersonFriendService personFriendService;

	@Override
	public void onApplicationEvent(ApplicationUserEvent event) {
		boolean isNew = event.isNew();
		if (isNew) {
			try {
				ApplicationUser applicationUser = (ApplicationUser) event.getSource();
				Person person = applicationUser.getPerson();

				PersonRelationVO personRelationVO = new PersonRelationVO();
				personRelationVO.setNew(true);
				personRelationVO.setPersonId(person.getId());
				personRelationVO.setRelationTypeId(ReferralRelationType.REFERRER);
				personRelationVO.setStartDate(LocalDate.now());
				personRelationService.updateSingle(personRelationVO);

				personFriendService.deactivateAsFriend(person.getId());
			} catch (FrameworkException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
