package software.simple.solutions.referral.service.facade;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.UI;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.PagingResult;
import software.simple.solutions.framework.core.service.facade.SuperServiceFacade;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.service.IActivityService;
import software.simple.solutions.referral.valueobjects.ActivityVO;

public class ActivityServiceFacade extends SuperServiceFacade<IActivityService> implements IActivityService {

	private static final long serialVersionUID = -360592525044181436L;

	public ActivityServiceFacade(UI ui, Class<IActivityService> s) {
		super(ui, s);
	}

	public static ActivityServiceFacade get(UI ui) {
		return new ActivityServiceFacade(ui, IActivityService.class);
	}

	@Autowired
	public PagingResult<Activity> findReferrerRelatedActivity(ActivityVO vo) throws FrameworkException {
		return service.findReferrerRelatedActivity(vo);
	}

}
