package software.simple.solutions.referral.service.facade;

import java.util.List;

import com.vaadin.ui.UI;

import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.facade.SuperServiceFacade;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.service.IActivityTypeService;

public class ActivityTypeServiceFacade extends SuperServiceFacade<IActivityTypeService>
		implements IActivityTypeService {

	private static final long serialVersionUID = -360592525044181436L;

	public ActivityTypeServiceFacade(UI ui, Class<IActivityTypeService> s) {
		super(ui, s);
	}

	public static ActivityTypeServiceFacade get(UI ui) {
		return new ActivityTypeServiceFacade(ui, IActivityTypeService.class);
	}

	public List<ActivityType> findByCodes(List<String> codes) throws FrameworkException {
		return service.findByCodes(codes);
	}

}
