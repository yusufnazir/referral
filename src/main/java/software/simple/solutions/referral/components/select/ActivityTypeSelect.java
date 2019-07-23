package software.simple.solutions.referral.components.select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import software.simple.solutions.framework.core.components.CComboBox;
import software.simple.solutions.framework.core.components.MessageWindowHandler;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.ComboItem;
import software.simple.solutions.framework.core.util.ContextProvider;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.service.IActivityTypeService;

public class ActivityTypeSelect extends CComboBox {

	private static final long serialVersionUID = 8665850289294199446L;

	public void setValue(ActivityType activityType) {
		if (activityType != null) {
			setValue(activityType.getId());
		}
	}

	public void setValues(List<ActivityType> activityTypes) {
		List<ComboItem> items = activityTypes.stream().map(p -> new ComboItem(p.getId(), p.getCode(), p.getName()))
				.collect(Collectors.toList());
		setItems(items);
	}

	public void removeAllItems() {
		setItems(new ArrayList<ComboItem>());
	}

}
