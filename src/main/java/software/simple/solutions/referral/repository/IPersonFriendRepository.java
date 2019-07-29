package software.simple.solutions.referral.repository;

import java.util.List;

import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.IGenericRepository;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.model.FriendModel;

public interface IPersonFriendRepository extends IGenericRepository {

	Long getTotalFriends(Long personId) throws FrameworkException;

	List<FriendModel> findFriendsByReferrer(Long personId) throws FrameworkException;

	PersonFriend getActiveAsFriend(Long personId) throws FrameworkException;

	PersonFriend findReferrerOfFriend(Long friendId) throws FrameworkException;

}
