package software.simple.solutions.referral.repository.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.impl.GenericRepository;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.model.FriendModel;
import software.simple.solutions.referral.repository.IPersonFriendRepository;

@Repository
public class PersonFriendRepository extends GenericRepository implements IPersonFriendRepository {

	private static final long serialVersionUID = -3488915576893451895L;

	@Override
	public Long getTotalFriends(Long personId) throws FrameworkException {
		String query = "select count(pf) from PersonFriend pf where pf.person.id=:id";
		ConcurrentMap<String, Object> paramMap = createParamMap();
		paramMap.put("id", personId);
		return getByQuery(query, paramMap);
	}

	@Override
	public List<FriendModel> findFriendsByReferrer(Long personId) throws FrameworkException {
		String query = "select new software.simple.solutions.referral.model.FriendModel(pf.friend,pi) from PersonFriend pf "
				+ "left join PersonInformation pi on pi.person.id=pf.friend.id " + "where pf.person.id=:id";
		ConcurrentMap<String, Object> paramMap = createParamMap();
		paramMap.put("id", personId);
		return createListQuery(query, paramMap);
	}

	@Override
	public PersonFriend getActiveAsFriend(Long friendId) throws FrameworkException {
		String query = "from PersonFriend pf where pf.friend.id=:id and pf.endDate is null";
		ConcurrentMap<String, Object> paramMap = createParamMap();
		paramMap.put("id", friendId);
		return getByQuery(query, paramMap);
	}

	@Override
	public PersonFriend findReferrerOfFriend(Long friendId) throws FrameworkException {
		String query = "from PersonFriend pf where pf.friend.id=:id and pf.endDate is null";
		ConcurrentMap<String, Object> paramMap = createParamMap();
		paramMap.put("id", friendId);
		return getByQuery(query, paramMap);
	}

}
