package software.simple.solutions.referral.service.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.UI;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.facade.SuperServiceFacade;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.model.FriendModel;
import software.simple.solutions.referral.service.IPersonFriendService;

public class PersonFriendServiceFacade extends SuperServiceFacade<IPersonFriendService>
		implements IPersonFriendService {

	private static final long serialVersionUID = -360592525044181436L;

	public PersonFriendServiceFacade(UI ui, Class<IPersonFriendService> s) {
		super(ui, s);
	}

	public static PersonFriendServiceFacade get(UI ui) {
		return new PersonFriendServiceFacade(ui, IPersonFriendService.class);
	}

	@Autowired
	public Long getTotalFriends(Long personId) throws FrameworkException {
		return service.getTotalFriends(personId);
	}

	public List<FriendModel> findFriendsByReferrer(Long personId) throws FrameworkException {
		return service.findFriendsByReferrer(personId);
	}

	@Override
	public PersonFriend getActiveAsFriend(Long personId) throws FrameworkException {
		return service.getActiveAsFriend(personId);
	}

	public PersonFriend findReferrerOfFriend(Long friendId) throws FrameworkException {
		return service.findReferrerOfFriend(friendId);
	}

	@Override
	public PersonFriend deactivateAsFriend(Long id) throws FrameworkException {
		return service.deactivateAsFriend(id);
	}

	@Override
	public void sendInvitationToPerson(PersonFriend personFriend, Long userId) throws FrameworkException {
		service.sendInvitationToPerson(personFriend, userId);
	}

}
