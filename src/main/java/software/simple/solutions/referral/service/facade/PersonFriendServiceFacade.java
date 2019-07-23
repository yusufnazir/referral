package software.simple.solutions.referral.service.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.UI;

import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.facade.SuperServiceFacade;
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

	public List<FriendModel> findFriendsByPerson(Long personId) throws FrameworkException {
		return service.findFriendsByPerson(personId);
	}

	@Override
	public Person getActiveByPerson(Long personId) throws FrameworkException {
		return service.getActiveByPerson(personId);
	}

}
