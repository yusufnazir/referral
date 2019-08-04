package software.simple.solutions.referral.service.impl;

import java.time.LocalDateTime;
import java.util.List;

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
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.model.FriendModel;
import software.simple.solutions.referral.properties.PersonFriendProperty;
import software.simple.solutions.referral.repository.IPersonFriendRepository;
import software.simple.solutions.referral.service.IPersonFriendService;
import software.simple.solutions.referral.valueobjects.PersonFriendVO;

@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
@ServiceRepository(claz = IPersonFriendRepository.class)
public class PersonFriendService extends SuperService implements IPersonFriendService {

	private static final long serialVersionUID = 2401669851924947487L;

	@Autowired
	private IPersonFriendRepository personFriendRepository;

	@Override
	public <T, R extends SuperVO> T updateSingle(R valueObject) throws FrameworkException {
		PersonFriendVO vo = (PersonFriendVO) valueObject;

		if (vo.getPersonId() == null) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(PersonFriendProperty.PERSON));
		}

		if (vo.getFriendId() == null) {
			throw new FrameworkException(SystemMessageProperty.FIELD_IS_REQUIRED,
					new Arg().key(PersonFriendProperty.FRIEND));
		}

		PersonFriend personFriend = new PersonFriend();
		if (!vo.isNew()) {
			personFriend = get(PersonFriend.class, vo.getId());
		}
		personFriend.setPerson(get(Person.class, vo.getPersonId()));
		personFriend.setFriend(get(Person.class, vo.getFriendId()));
		personFriend.setStartDate(vo.getStartDate());
		personFriend.setEndDate(vo.getEndDate());

		return (T) saveOrUpdate(personFriend, vo.isNew());
	}

	@Override
	public Long getTotalFriends(Long personId) throws FrameworkException {
		Long totalFriends = personFriendRepository.getTotalFriends(personId);
		if (totalFriends == null) {
			totalFriends = 0L;
		}
		return totalFriends;
	}

	@Override
	public List<FriendModel> findFriendsByReferrer(Long personId) throws FrameworkException {
		return personFriendRepository.findFriendsByReferrer(personId);
	}

	@Override
	public PersonFriend getActiveAsFriend(Long personId) throws FrameworkException {
		return personFriendRepository.getActiveAsFriend(personId);
	}

	@Override
	public PersonFriend findReferrerOfFriend(Long friendId) throws FrameworkException {
		return personFriendRepository.findReferrerOfFriend(friendId);
	}

	@Override
	public PersonFriend deactivateAsFriend(Long id) throws FrameworkException {
		PersonFriend personFriend = getActiveAsFriend(id);
		if (personFriend != null) {
			personFriend.setEndDate(LocalDateTime.now());
			saveOrUpdate(personFriend, false);
		}
		return personFriend;
	}

}
