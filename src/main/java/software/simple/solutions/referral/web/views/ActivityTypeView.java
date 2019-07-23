package software.simple.solutions.referral.web.views;

import software.simple.solutions.framework.core.components.CCheckBox;
import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.components.CTextArea;
import software.simple.solutions.framework.core.components.CTextField;
import software.simple.solutions.framework.core.components.FilterView;
import software.simple.solutions.framework.core.components.FormView;
import software.simple.solutions.framework.core.components.filter.CStringIntervalLayout;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.properties.SystemProperty;
import software.simple.solutions.framework.core.util.ColumnSort;
import software.simple.solutions.framework.core.util.ComponentUtil;
import software.simple.solutions.framework.core.util.SortingHelper;
import software.simple.solutions.framework.core.web.BasicTemplate;
import software.simple.solutions.referral.constants.ReferralTables;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.properties.ActivityTypeProperty;
import software.simple.solutions.referral.service.facade.ActivityTypeServiceFacade;
import software.simple.solutions.referral.valueobjects.ActivityTypeVO;

public class ActivityTypeView extends BasicTemplate<ActivityType> {

	private static final long serialVersionUID = 6503015064562511801L;

	public ActivityTypeView() {
		setEntityClass(ActivityType.class);
		setServiceClass(ActivityTypeServiceFacade.class);
		setFilterClass(Filter.class);
		setFormClass(Form.class);
		setEntityReferenceKey(ReferralTables.ACTIVITY_TYPES_.NAME);
	}

	@Override
	public void setUpCustomColumns() {
		addContainerProperty(ActivityType::getCode, ActivityTypeProperty.CODE);
		addContainerProperty(ActivityType::getName, ActivityTypeProperty.NAME);
		addContainerProperty(ActivityType::getDescription, ActivityTypeProperty.DESCRIPTION);
	}

	/**
	 * Filter implementation
	 * 
	 * @author yusuf
	 *
	 */
	public class Filter extends FilterView {

		private static final long serialVersionUID = 117780868467918033L;

		private CStringIntervalLayout codeFld;
		private CStringIntervalLayout nameFld;
		private CStringIntervalLayout descriptionFld;

		@Override
		public void executeBuild() {

			codeFld = addField(CStringIntervalLayout.class, ActivityTypeProperty.CODE, 0, 0);

			nameFld = addField(CStringIntervalLayout.class, ActivityTypeProperty.NAME, 0, 1);

			descriptionFld = addField(CStringIntervalLayout.class, ActivityTypeProperty.DESCRIPTION, 1, 0);
		}

		@Override
		public Object getCriteria() {
			ActivityTypeVO vo = new ActivityTypeVO();
			vo.setCodeInterval(codeFld.getValue());
			vo.setNameInterval(nameFld.getValue());
			vo.setDescriptionInterval(descriptionFld.getValue());
			SortingHelper sortingHelper = new SortingHelper();
			sortingHelper.addColumnSort(new ColumnSort(ActivityTypeProperty.ID, SortingHelper.DESCENDING));
			vo.setSortingHelper(sortingHelper);
			return vo;
		}
	}

	public class Form extends FormView {

		private static final long serialVersionUID = 6109727427163734676L;

		private CGridLayout formGrid;
		private CTextField codeFld;
		private CTextField nameFld;
		private CTextArea descriptionFld;
		private CCheckBox activeFld;

		private ActivityType activityType;

		@Override
		public void executeBuild() {
			formGrid = ComponentUtil.createGrid();
			addComponent(formGrid);

			codeFld = formGrid.addField(CTextField.class, ActivityTypeProperty.CODE, 0, 0);
			codeFld.setRequiredIndicatorVisible(true);

			nameFld = formGrid.addField(CTextField.class, ActivityTypeProperty.NAME, 0, 1);
			nameFld.setRequiredIndicatorVisible(true);

			descriptionFld = formGrid.addField(CTextArea.class, ActivityTypeProperty.DESCRIPTION, 0, 2);

			activeFld = formGrid.addField(CCheckBox.class, SystemProperty.SYSTEM_ENTITY_ACTIVE, 1, 0);
		}

		@Override
		public void handleNewForm() throws FrameworkException {
			activeFld.setValue(true);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ActivityType setFormValues(Object entity) throws FrameworkException {
			activityType = (ActivityType) entity;
			codeFld.setValue(activityType.getCode());
			nameFld.setValue(activityType.getName());
			descriptionFld.setValue(activityType.getDescription());
			activeFld.setValue(activityType.getActive());
			return activityType;
		}

		@Override
		public Object getFormValues() throws FrameworkException {
			ActivityTypeVO vo = new ActivityTypeVO();

			vo.setId(activityType == null ? null : activityType.getId());
			vo.setCode(codeFld.getValue());
			vo.setName(nameFld.getValue());
			vo.setDescription(descriptionFld.getValue());
			vo.setActive(activeFld.getValue());
			return vo;
		}
	}

}
