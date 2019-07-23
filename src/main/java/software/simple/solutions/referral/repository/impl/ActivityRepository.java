package software.simple.solutions.referral.repository.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.PagingResult;
import software.simple.solutions.framework.core.pojo.PagingSetting;
import software.simple.solutions.framework.core.repository.impl.GenericRepository;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.repository.IActivityRepository;
import software.simple.solutions.referral.valueobjects.ActivityVO;

@Repository
public class ActivityRepository extends GenericRepository implements IActivityRepository {

	private static final long serialVersionUID = -3488915576893451895L;

	@Override
	public <T, R> List<R> getForListing(Class<T> cl, List<Long> ids, Boolean active) throws FrameworkException {
		ConcurrentMap<String, Object> paramMap = createParamMap();
		String query = "select new software.simple.solutions.framework.core.pojo.ComboItem(id,name) from Activity where 1=1 ";
		query += " order by id ";

		return createListQuery(query, paramMap);
	}

	@Override
	public PagingResult<Activity> findReferrerRelatedActivity(ActivityVO vo) throws FrameworkException {
		String query = "from Activity where person.id=:id order by id desc";
		ConcurrentMap<String, Object> paramMap = createParamMap();
		paramMap.put("id", vo.getPersonId());
		PagingSetting pagingSetting = new PagingSetting(vo.getPagingInfo().getStartPosition(),
				vo.getPagingInfo().getMaxResult());
		List<Activity> activities = createListQuery(query, paramMap, pagingSetting);
		PagingResult<Activity> pagingResult = new PagingResult<Activity>();
		pagingResult.setPagingSetting(pagingSetting);
		pagingResult.setResult(activities);
		return pagingResult;
	}

}
