package software.simple.solutions.referral.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import software.simple.solutions.framework.core.annotations.ServiceRepository;
import software.simple.solutions.framework.core.entities.Gender;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.Arg;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.PagingResult;
import software.simple.solutions.framework.core.pojo.SecurityValidation;
import software.simple.solutions.framework.core.properties.RegistrationProperty;
import software.simple.solutions.framework.core.properties.SystemMessageProperty;
import software.simple.solutions.framework.core.service.IApplicationUserService;
import software.simple.solutions.framework.core.service.IPersonRelationService;
import software.simple.solutions.framework.core.service.IPersonService;
import software.simple.solutions.framework.core.service.impl.SuperService;
import software.simple.solutions.framework.core.valueobjects.PersonVO;
import software.simple.solutions.framework.core.valueobjects.SuperVO;
import software.simple.solutions.referral.constants.ReferralActivityType;
import software.simple.solutions.referral.constants.ReferralRelationType;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.entities.PersonReward;
import software.simple.solutions.referral.properties.ActivityProperty;
import software.simple.solutions.referral.repository.IActivityRepository;
import software.simple.solutions.referral.service.IActivityService;
import software.simple.solutions.referral.service.IActivityTypeService;
import software.simple.solutions.referral.service.IPersonFriendService;
import software.simple.solutions.referral.service.IPersonRewardService;
import software.simple.solutions.referral.valueobjects.ActivityVO;
import software.simple.solutions.referral.valueobjects.PersonFriendVO;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
@ServiceRepository(claz = IActivityRepository.class)
public class ActivityService extends SuperService implements IActivityService {

	private static final long serialVersionUID = 2401669851924947487L;

	@Autowired
	private IActivityRepository activityRepository;

	@Autowired
	private IPersonRewardService personRewardService;

	@Autowired
	private IPersonFriendService personFriendService;

	@Autowired
	private IPersonRelationService personRelationService;

	@Autowired
	private IActivityTypeService activityTypeService;

	@Autowired
	private IApplicationUserService applicationUserService;

	@Autowired
	private IPersonService personService;

	@Override
	public <T, R extends SuperVO> T updateSingle(R valueObject) throws FrameworkException {
		ActivityVO vo = (ActivityVO) valueObject;

		if (vo.getPersonId() == null) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(ActivityProperty.PERSON_ID));
		}

		if (vo.getDateOfActivity() == null) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(ActivityProperty.DATE_OF_ACTIVITY));
		}

		if (vo.getActivityTypeId() == null) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(ActivityProperty.ACTIVITY_TYPE_ID));
		}

		Activity activity = new Activity();
		if (vo.isNew()) {
			activity.setCumulativeRewardAmount(vo.getCumulativeRewardAmount());

			PersonFriend personFriend = personFriendService.getActiveAsFriend(vo.getPersonId());
			if (personFriend != null) {
				activity.setReferrerPerson(personFriend.getPerson());

				// activity.setUseReward(vo.getUseReward());
				// activity.setUsedRewardAmount(vo.getUsedReward());
				// activity.setActivityRewardAmount(vo.getActivityRewardAmount());

				BigDecimal cumulativeReward = personRewardService
						.getPersonCumulativeReward(personFriend.getPerson().getId());
				activity.setCumulativeRewardAmount(cumulativeReward
						.subtract(vo.getUsedReward() == null ? BigDecimal.ZERO : vo.getUsedReward())
						.add(vo.getActivityRewardAmount() == null ? BigDecimal.ZERO : vo.getActivityRewardAmount()));

				activity.setActive(vo.getActive());
				activity.setActivityAmount(vo.getActivityAmount());
				activity.setActivityRewardAmount(vo.getActivityRewardAmount());
				activity.setActivityType(get(ActivityType.class, vo.getActivityTypeId()));
				activity.setDateOfActivity(vo.getDateOfActivity());
				activity.setPerson(get(Person.class, vo.getPersonId()));

				activity = saveOrUpdate(activity, vo.isNew());
				personRewardService.updatePersonReward(personFriend.getPerson().getId(), vo.getActivityRewardAmount(),
						vo.getUsedReward());
				createRewardActivity(vo, activity);
			}

			Boolean isPersonReferrer = personRelationService.isPersonOfType(vo.getPersonId(),
					ReferralRelationType.REFERRER);
			if (isPersonReferrer) {
				// activity.setReferrerPerson(referrer);

				activity.setUseReward(vo.getUseReward());
				activity.setUsedRewardAmount(vo.getUsedReward());
				activity.setActivityRewardAmount(vo.getActivityRewardAmount());

				BigDecimal cumulativeReward = personRewardService.getPersonCumulativeReward(vo.getPersonId());
				activity.setCumulativeRewardAmount(cumulativeReward
						.subtract(vo.getUsedReward() == null ? BigDecimal.ZERO : vo.getUsedReward())
						.add(vo.getActivityRewardAmount() == null ? BigDecimal.ZERO : vo.getActivityRewardAmount()));

				activity.setActive(vo.getActive());
				activity.setActivityAmount(vo.getActivityAmount());
				activity.setActivityType(get(ActivityType.class, vo.getActivityTypeId()));
				activity.setDateOfActivity(vo.getDateOfActivity());
				activity.setPerson(get(Person.class, vo.getPersonId()));

				activity = saveOrUpdate(activity, vo.isNew());
				personRewardService.updatePersonReward(vo.getPersonId(), vo.getActivityRewardAmount(),
						vo.getUsedReward());
				// createRewardActivity(vo, activity);
			}
			return (T) activity;
		} else {
			return (T) get(Activity.class, vo.getId());
		}
	}

	private void createRewardActivity(ActivityVO vo, Activity activity) throws FrameworkException {
		Boolean useReward = vo.getUseReward();
		Person referrerPerson = activity.getReferrerPerson();
		ActivityType activityType = activity.getActivityType();
		if (referrerPerson != null && ReferralActivityType.ITEM_PURCHASED.equalsIgnoreCase(activityType.getCode())) {
			Activity rewardActivity = new Activity();
			rewardActivity.setActivityAmount(activity.getActivityRewardAmount());
			rewardActivity.setPerson(referrerPerson);
			rewardActivity.setDateOfActivity(activity.getDateOfActivity());
			rewardActivity.setActivityAmount(vo.getActivityRewardAmount());

			ActivityType activityTypeByCode = activityTypeService.getByCode(ActivityType.class,
					ReferralActivityType.DISCOUNT_REWARD_EARNED);
			rewardActivity.setActivityType(activityTypeByCode);

			BigDecimal cumulativeReward = personRewardService.getPersonCumulativeReward(referrerPerson.getId());
			rewardActivity.setCumulativeRewardAmount(cumulativeReward);
			rewardActivity.setMainActivity(activity);
			saveOrUpdate(rewardActivity, true);
		} else if (ReferralActivityType.ITEM_PURCHASED.equalsIgnoreCase(activityType.getCode()) && useReward) {
			Activity rewardActivity = new Activity();
			rewardActivity.setActivityAmount(activity.getActivityRewardAmount() == null ? BigDecimal.ZERO
					: activity.getActivityRewardAmount().negate());
			rewardActivity.setPerson(referrerPerson);
			rewardActivity.setDateOfActivity(activity.getDateOfActivity());

			ActivityType activityTypeByCode = activityTypeService.getByCode(ActivityType.class,
					ReferralActivityType.DISCOUNT_REWARD_USED);
			rewardActivity.setActivityType(activityTypeByCode);

			BigDecimal cumulativeReward = personRewardService.getPersonCumulativeReward(referrerPerson.getId());
			rewardActivity.setCumulativeRewardAmount(cumulativeReward);
			rewardActivity.setMainActivity(activity);
			saveOrUpdate(rewardActivity, true);
		}
	}

	@Override
	public PagingResult<Activity> findReferrerRelatedActivity(ActivityVO vo) throws FrameworkException {
		return activityRepository.findReferrerRelatedActivity(vo);
	}

	@Override
	public SecurityValidation registerFriend(PersonFriendVO vo) throws FrameworkException {
		PersonVO personVO = vo.getPersonVO();
		if (StringUtils.isBlank(personVO.getFirstName())) {
			return SecurityValidation.build(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(RegistrationProperty.REGISTER_FIRST_NAME));
		}
		if (StringUtils.isBlank(personVO.getLastName())) {
			return SecurityValidation.build(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(RegistrationProperty.REGISTER_LAST_NAME));
		}
		if (personVO.getDateOfBirth() == null) {
			return SecurityValidation.build(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(RegistrationProperty.REGISTER_DATE_OF_BIRTH));
		}
		if (personVO.getMobileNumber() == null) {
			return SecurityValidation.build(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(RegistrationProperty.REGISTER_MOBILE_NUMBER));
		}
		if (StringUtils.isBlank(personVO.getEmail())) {
			return SecurityValidation.build(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(RegistrationProperty.REGISTER_EMAIL));
		}

		Boolean codeUnique = personService.isCodeUnique(Person.class, personVO.getEmail());
		if (!codeUnique) {
			return SecurityValidation.build(RegistrationProperty.REGISTER_USER_ALREADY_EXISTS,
					new Arg().norm(personVO.getEmail()));
		}

		Person person = new Person();
		person.setActive(true);
		person.setCode(personVO.getEmail());
		person.setDateOfBirth(personVO.getDateOfBirth());
		person.setFirstName(personVO.getFirstName());
		person.setLastName(personVO.getLastName());
		person.setGender(get(Gender.class, personVO.getGenderId()));
		person = saveOrUpdate(person, true);

		personService.createPersonImage(person);

		personService.updatePersonEmail(person.getId(), personVO.getEmail());
		personService.updatePersonMobileNumber(person.getId(), personVO.getMobileNumber());

		PersonReward personReward = personRewardService.getByPerson(vo.getPersonId());

		PersonFriendVO personFriendVO = new PersonFriendVO();
		personFriendVO.setPersonId(vo.getPersonId());
		personFriendVO.setFriendId(person.getId());
		personFriendVO.setStartDate(LocalDateTime.now());
		personFriendVO.setNew(true);
		personFriendVO.setToken(personReward.getToken());
		PersonFriend personFriend = personFriendService.updateSingle(personFriendVO);
		personFriendService.sendInvitationToPerson(personFriend, vo.getCurrentUserId());
		// applicationUserService.sendRegistrationMailToNewUser(applicationUser,
		// vo);

		return new SecurityValidation(true);
	}

}
