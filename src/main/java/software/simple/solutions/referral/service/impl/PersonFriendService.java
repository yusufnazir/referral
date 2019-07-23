package software.simple.solutions.referral.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Transactional
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
	public List<FriendModel> findFriendsByPerson(Long personId) throws FrameworkException {
		return personFriendRepository.findFriendsByPerson(personId);
	}

	@Override
	public Person getActiveByPerson(Long personId) throws FrameworkException {
		return personFriendRepository.getActiveByPerson(personId);
	}

}
