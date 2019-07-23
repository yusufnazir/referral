package software.simple.solutions.referral.repository;

import java.util.List;

import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.IGenericRepository;
import software.simple.solutions.referral.model.FriendModel;

public interface IPersonFriendRepository extends IGenericRepository {

	Long getTotalFriends(Long personId) throws FrameworkException;

	List<FriendModel> findFriendsByPerson(Long personId) throws FrameworkException;

	Person getActiveByPerson(Long personId) throws FrameworkException;

}
