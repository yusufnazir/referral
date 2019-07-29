package software.simple.solutions.referral.service;

import java.util.List;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.ISuperService;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.model.FriendModel;

public interface IPersonFriendService extends ISuperService {

	Long getTotalFriends(Long personId) throws FrameworkException;

	List<FriendModel> findFriendsByReferrer(Long personId) throws FrameworkException;

	PersonFriend getActiveAsFriend(Long personId) throws FrameworkException;

	PersonFriend findReferrerOfFriend(Long friendId) throws FrameworkException;

	PersonFriend deactivateAsFriend(Long id) throws FrameworkException;

}
