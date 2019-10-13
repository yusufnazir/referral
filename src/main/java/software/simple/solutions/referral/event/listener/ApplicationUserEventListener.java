package software.simple.solutions.referral.event.listener;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import software.simple.solutions.framework.core.entities.ApplicationUser;
import software.simple.solutions.framework.core.entities.Configuration;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.event.ApplicationUserEvent;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.properties.ConfigurationProperty;
import software.simple.solutions.framework.core.service.IConfigurationService;
import software.simple.solutions.framework.core.service.IPersonRelationService;
import software.simple.solutions.framework.core.service.IUserRoleService;
import software.simple.solutions.framework.core.valueobjects.PersonRelationVO;
import software.simple.solutions.framework.core.valueobjects.UserRoleVO;
import software.simple.solutions.referral.constants.ReferralRelationType;
import software.simple.solutions.referral.service.IPersonFriendService;

@Component
public class ApplicationUserEventListener implements ApplicationListener<ApplicationUserEvent> {

	private static final Logger logger = LogManager.getLogger(ApplicationUserEventListener.class);

	@Autowired
	private IPersonRelationService personRelationService;

	@Autowired
	private IPersonFriendService personFriendService;

	@Autowired
	private IUserRoleService userRoleService;

	@Autowired
	private IConfigurationService configurationService;

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
				setUserRole(applicationUser);
			} catch (FrameworkException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void setUserRole(ApplicationUser applicationUser) throws FrameworkException {
		Configuration configuration = configurationService
				.getByCode(ConfigurationProperty.APPLICATION_DEFAULT_USER_ROLE);
		if (configuration != null && configuration.getLong() != null) {
			UserRoleVO vo = new UserRoleVO();
			vo.setNew(true);
			vo.setUserId(applicationUser.getId());
			vo.setRoleId(configuration.getLong());
			vo.setActive(true);
			userRoleService.updateSingle(vo);
		}
	}

}
