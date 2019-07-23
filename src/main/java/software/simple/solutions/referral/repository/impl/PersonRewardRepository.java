package software.simple.solutions.referral.repository.impl;

import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.impl.GenericRepository;
import software.simple.solutions.referral.entities.PersonReward;
import software.simple.solutions.referral.repository.IPersonRewardRepository;

@Repository
public class PersonRewardRepository extends GenericRepository implements IPersonRewardRepository {

	private static final long serialVersionUID = -3488915576893451895L;

	@Override
	public PersonReward getByPerson(Long referrerId) throws FrameworkException {
		String query = "from PersonReward where person.id=:id";
		ConcurrentMap<String,Object> paramMap = createParamMap();
		paramMap.put("id", referrerId);
		return getByQuery(query, paramMap);
	}

}
