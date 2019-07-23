package software.simple.solutions.referral.repository.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.repository.impl.GenericRepository;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.repository.IActivityTypeRepository;

@Repository
public class ActivityTypeRepository extends GenericRepository implements IActivityTypeRepository {

	private static final long serialVersionUID = -3488915576893451895L;

	@Override
	public <T, R> List<R> getForListing(Class<T> cl, List<Long> ids, Boolean active) throws FrameworkException {
		ConcurrentMap<String, Object> paramMap = createParamMap();
		String query = "select new software.simple.solutions.framework.core.pojo.ComboItem(id,name) from ActivityType where 1=1 ";
		query += " order by id ";

		return createListQuery(query, paramMap);
	}

	@Override
	public List<ActivityType> findByCodes(List<String> codes) throws FrameworkException {
		String query = "from ActivityType where code in (:codes)";
		ConcurrentMap<String, Object> paramMap = createParamMap();
		paramMap.put("codes", codes);
		return createListQuery(query, paramMap);
	}

}
