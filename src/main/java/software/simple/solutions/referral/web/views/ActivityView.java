package software.simple.solutions.referral.web.views;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.components.CCheckBox;
import software.simple.solutions.framework.core.components.CDecimalField;
import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.components.CPopupDateField;
import software.simple.solutions.framework.core.components.FilterView;
import software.simple.solutions.framework.core.components.FormView;
import software.simple.solutions.framework.core.components.MessageWindowHandler;
import software.simple.solutions.framework.core.components.filter.CDateIntervalLayout;
import software.simple.solutions.framework.core.components.filter.CStringIntervalLayout;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.ComboItem;
import software.simple.solutions.framework.core.service.facade.PersonRelationServiceFacade;
import software.simple.solutions.framework.core.util.ColumnSort;
import software.simple.solutions.framework.core.util.ComponentUtil;
import software.simple.solutions.framework.core.util.ContextProvider;
import software.simple.solutions.framework.core.util.PropertyResolver;
import software.simple.solutions.framework.core.util.SortingHelper;
import software.simple.solutions.framework.core.web.BasicTemplate;
import software.simple.solutions.framework.core.web.lookup.PersonLookUpField;
import software.simple.solutions.referral.components.select.ActivityTypeSelect;
import software.simple.solutions.referral.constants.ReferralActivityType;
import software.simple.solutions.referral.constants.ReferralRelationType;
import software.simple.solutions.referral.constants.ReferralTables;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.entities.ActivityType;
import software.simple.solutions.referral.properties.ActivityProperty;
import software.simple.solutions.referral.service.IActivityTypeService;
import software.simple.solutions.referral.service.facade.ActivityServiceFacade;
import software.simple.solutions.referral.service.facade.ActivityTypeServiceFacade;
import software.simple.solutions.referral.service.facade.PersonRewardServiceFacade;
import software.simple.solutions.referral.valueobjects.ActivityVO;

public class ActivityView extends BasicTemplate<Activity> {

	private static final long serialVersionUID = 6503015064562511801L;

	private static final Logger logger = LogManager.getLogger(ActivityView.class);

	public ActivityView() {
		setEntityClass(Activity.class);
		setServiceClass(ActivityServiceFacade.class);
		setFilterClass(Filter.class);
		setFormClass(Form.class);
		setEntityReferenceKey(ReferralTables.ACTIVITIES_.NAME);
	}

	@Override
	public void setUpCustomColumns() {
		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 2142926032875419763L;

			@Override
			public String apply(Activity source) {
				Person person = source.getPerson();
				if (person == null) {
					return null;
				}
				return WordUtils.capitalize(person.getFirstName() + " " + person.getLastName());
			}
		}, ActivityProperty.PERSON_ID);
		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 1368388180555862745L;

			@Override
			public String apply(Activity source) {
				LocalDateTime dateOfActivity = source.getDateOfActivity();
				if (dateOfActivity == null) {
					return null;
				}
				return dateOfActivity.format(Constants.DATE_FORMATTER);
			}
		}, ActivityProperty.DATE_OF_ACTIVITY);
		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 7250139840841052152L;

			@Override
			public String apply(Activity source) {
				ActivityType activityType = source.getActivityType();
				if (activityType == null) {
					return null;
				}
				return "[" + activityType.getCode() + "] " + activityType.getName();
			}
		}, ActivityProperty.ACTIVITY_TYPE_ID);

		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 9173905799580680657L;

			@Override
			public String apply(Activity source) {
				BigDecimal activityAmount = source.getActivityAmount();
				if (activityAmount == null) {
					return null;
				}
				return Constants.DF.format(activityAmount);
			}
		}, ActivityProperty.ACTIVITY_AMOUNT);
		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 8929944786058678454L;

			@Override
			public String apply(Activity source) {
				Person referrerPerson = source.getReferrerPerson();
				if (referrerPerson == null) {
					return null;
				}
				return WordUtils.capitalize(referrerPerson.getFirstName() + " " + referrerPerson.getLastName());
			}
		}, ActivityProperty.REFERRER_PERSON_ID);
		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 9173905799580680657L;

			@Override
			public String apply(Activity source) {
				BigDecimal referrerActivityReward = source.getActivityRewardAmount();
				if (referrerActivityReward == null) {
					return null;
				}
				return Constants.DF.format(referrerActivityReward);
			}
		}, ActivityProperty.ACTIVITY_REWARD_AMOUNT);

		addContainerProperty(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 9173905799580680657L;

			@Override
			public String apply(Activity source) {
				BigDecimal cumulativeRewardAmount = source.getCumulativeRewardAmount();
				if (cumulativeRewardAmount == null) {
					return null;
				}
				return Constants.DF.format(cumulativeRewardAmount);
			}
		}, ActivityProperty.CUMULATIVE_REWARD_AMOUNT);
	}

	/**
	 * Filter implementation
	 * 
	 * @author yusuf
	 *
	 */
	public class Filter extends FilterView {

		private static final long serialVersionUID = 117780868467918033L;

		private CStringIntervalLayout friendFirstNameFld;
		private CStringIntervalLayout friendLastNameFld;

		private CStringIntervalLayout referrerFirstNameFld;
		private CStringIntervalLayout referrerLastNameFld;

		private CDateIntervalLayout dateOfActivityFld;
		private ActivityTypeSelect activityTypeFld;

		@Override
		public void executeBuild() {

			friendFirstNameFld = addField(CStringIntervalLayout.class, ActivityProperty.FRIEND_FIRST_NAME, 0, 0);
			friendLastNameFld = addField(CStringIntervalLayout.class, ActivityProperty.FRIEND_LAST_NAME, 0, 1);
			referrerFirstNameFld = addField(CStringIntervalLayout.class, ActivityProperty.REFERRER_FIRST_NAME, 0, 2);
			referrerLastNameFld = addField(CStringIntervalLayout.class, ActivityProperty.REFERRER_LAST_NAME, 0, 3);

			dateOfActivityFld = addField(CDateIntervalLayout.class, ActivityProperty.DATE_OF_ACTIVITY, 1, 0);
			activityTypeFld = addField(ActivityTypeSelect.class, ActivityProperty.ACTIVITY_TYPE_ID, 1, 1);
			initActivityTypeFld();
		}

		private void initActivityTypeFld() {
			IActivityTypeService activityTypeService = ContextProvider.getBean(IActivityTypeService.class);
			List<ComboItem> items;
			try {
				items = activityTypeService.getForListing(ActivityType.class, true);
				activityTypeFld.setItems(items);
			} catch (FrameworkException e) {
				new MessageWindowHandler(e);
			}
		}

		@Override
		public Object getCriteria() {
			ActivityVO vo = new ActivityVO();
			vo.setPersonFirstName(friendFirstNameFld.getValue());
			vo.setPersonLastName(friendLastNameFld.getValue());
			vo.setReferrerFirstName(referrerFirstNameFld.getValue());
			vo.setReferrerLastName(referrerLastNameFld.getValue());
			vo.setDateOfActivityInterval(dateOfActivityFld.getValue());
			vo.setActivityTypeId(activityTypeFld.getLongValue());
			SortingHelper sortingHelper = new SortingHelper();
			sortingHelper.addColumnSort(new ColumnSort(ActivityProperty.ID, SortingHelper.DESCENDING));
			vo.setSortingHelper(sortingHelper);
			return vo;
		}
	}

	public class Form extends FormView {

		private static final long serialVersionUID = 6109727427163734676L;

		private HorizontalLayout formMainLayout;
		private CGridLayout personLayout;
		// private CGridLayout referrerLayout;
		private PersonLookUpField personFld;
		private ActivityTypeSelect activityTypeFld;
		private CPopupDateField dateOfActivityFld;
		private CDecimalField spentAmountFld;

		// private PersonLookUpField referrerFld;
		private CDecimalField activityRewardAmountFld;
		private Label buyerAvailableRewardFld;
		private Label referrerCumulativeReward;
		private CGridLayout buyerAvailableRewardLayout;
		private HorizontalLayout buyerUseRewardLayout;
		private CDecimalField usedRewardAmountFld;
		private CCheckBox useRewardFld;

		private Activity activity;

		@Override
		public void executeBuild() {
			formMainLayout = new HorizontalLayout();
			formMainLayout.setSpacing(true);
			addComponent(formMainLayout);

			personLayout = createPersonLayout();
			personLayout.addStyleName(ValoTheme.LAYOUT_CARD);
			personLayout.setCaption(PropertyResolver.getPropertyValueByLocale(ActivityProperty.BUYERS_INFO,
					UI.getCurrent().getLocale()));
			formMainLayout.addComponent(personLayout);

			// referrerLayout = createReferrerLayout();
			// referrerLayout.addStyleName(ValoTheme.LAYOUT_CARD);
			// referrerLayout.setCaption(PropertyResolver.getPropertyValueByLocale(ActivityProperty.REFFERER_INFO,
			// UI.getCurrent().getLocale()));
			// formMainLayout.addComponent(referrerLayout);

			buyerAvailableRewardLayout = createBuyerAvailableRewardLayout();
			buyerAvailableRewardLayout.setVisible(false);
			buyerAvailableRewardLayout.addStyleName(ValoTheme.LAYOUT_CARD);
			buyerAvailableRewardLayout.setCaption(PropertyResolver.getPropertyValueByLocale(
					ActivityProperty.BUYERS_AVAILABLE_REWARD_INFO, UI.getCurrent().getLocale()));
			formMainLayout.addComponent(buyerAvailableRewardLayout);

		}

		private CGridLayout createPersonLayout() {
			// common part: create layout
			personLayout = ComponentUtil.createGrid();
			personLayout.setWidth("-1px");
			personLayout.setHeight("-1px");
			personLayout.setMargin(true);
			personLayout.setSpacing(true);

			personFld = personLayout.addField(PersonLookUpField.class, ActivityProperty.PERSON_ID, 0, 0);

			activityTypeFld = personLayout.addField(ActivityTypeSelect.class, ActivityProperty.ACTIVITY_TYPE_ID, 0, 1);
			activityTypeFld.setWidth("100%");

			dateOfActivityFld = personLayout.addField(CPopupDateField.class, ActivityProperty.DATE_OF_ACTIVITY, 0, 2);
			dateOfActivityFld.setWidth("-1px");
			dateOfActivityFld.setDateFormat(Constants.SIMPLE_DATE_FORMAT.toPattern());

			spentAmountFld = personLayout.addField(CDecimalField.class, ActivityProperty.ACTIVITY_AMOUNT, 0, 3);
			spentAmountFld.setMinimumFractionDigits(2);
			spentAmountFld.setDecimalPrecision(2);

			activityRewardAmountFld = personLayout.addField(CDecimalField.class,
					ActivityProperty.ACTIVITY_REWARD_AMOUNT, 0, 4);
			activityRewardAmountFld.setVisible(false);
			activityRewardAmountFld.setMinimumFractionDigits(2);
			activityRewardAmountFld.setDecimalPrecision(2);

			return personLayout;
		}

		// private CGridLayout createReferrerLayout() {
		// // common part: create layout
		// referrerLayout = ComponentUtil.createGrid();
		// referrerLayout.setWidth("-1px");
		// referrerLayout.setHeight("-1px");
		// referrerLayout.setMargin(true);
		// referrerLayout.setSpacing(true);
		//
		// referrerFld = referrerLayout.addField(PersonLookUpField.class,
		// ActivityProperty.REFERRER_PERSON_ID, 0, 0);
		//
		// activityRewardAmountFld =
		// referrerLayout.addField(CDecimalField.class,
		// ActivityProperty.ACTIVITY_REWARD_AMOUNT, 0, 1);
		// activityRewardAmountFld.setMinimumFractionDigits(2);
		// activityRewardAmountFld.setDecimalPrecision(2);
		//
		// referrerCumulativeReward = referrerLayout.addField(Label.class,
		// ActivityProperty.CUMULATIVE_REWARD_AMOUNT,
		// 0, 2);
		//
		// return referrerLayout;
		// }

		private CGridLayout createBuyerAvailableRewardLayout() {
			// common part: create layout
			buyerAvailableRewardLayout = ComponentUtil.createGrid();
			buyerAvailableRewardLayout.addStyleName(ValoTheme.LAYOUT_CARD);
			buyerAvailableRewardLayout.setWidth("-1px");
			buyerAvailableRewardLayout.setHeight("-1px");
			buyerAvailableRewardLayout.setMargin(true);
			buyerAvailableRewardLayout.setSpacing(true);

			buyerAvailableRewardFld = buyerAvailableRewardLayout.addField(Label.class,
					ActivityProperty.BUYERS_AVAILABLE_REWARD, 0, 0);

			buyerUseRewardLayout = buyerAvailableRewardLayout.addField(HorizontalLayout.class,
					ActivityProperty.BUYERS_USE_REWARD, 0, 1);

			useRewardFld = new CCheckBox();
			buyerUseRewardLayout.addComponent(useRewardFld);

			usedRewardAmountFld = new CDecimalField();
			usedRewardAmountFld.setVisible(false);
			usedRewardAmountFld.setMinimumFractionDigits(2);
			usedRewardAmountFld.setDecimalPrecision(2);
			buyerUseRewardLayout.addComponent(usedRewardAmountFld);

			return buyerAvailableRewardLayout;
		}

		@Override
		public void handleNewForm() throws FrameworkException {
			personFld.setRequiredIndicatorVisible(true);
			activityTypeFld.setRequiredIndicatorVisible(true);
			spentAmountFld.setRequiredIndicatorVisible(true);
			dateOfActivityFld.setRequiredIndicatorVisible(true);
			personFld.addValueChangeListener(new ValueChangeListener<Object>() {

				private static final long serialVersionUID = 4166603070240524743L;

				@Override
				public void valueChange(ValueChangeEvent<Object> event) {
					Person person = (Person) personFld.getValue();
					handleForReferrer(person == null ? null : person.getId());
				}
			});
			useRewardFld.addValueChangeListener(new ValueChangeListener<Boolean>() {

				private static final long serialVersionUID = 9081708413766014561L;

				@Override
				public void valueChange(ValueChangeEvent<Boolean> event) {
					usedRewardAmountFld.setVisible(false);
					usedRewardAmountFld.setBigDecimalValue(BigDecimal.ZERO);
					Boolean useReward = useRewardFld.getValue();
					if (useReward) {
						usedRewardAmountFld.setVisible(true);
					}
				}
			});
		}

		protected void handleForReferrer(Long personId) {
			buyerAvailableRewardLayout.setVisible(false);
			activityRewardAmountFld.setVisible(false);

			if (personId != null) {
				PersonRelationServiceFacade personRelationServiceFacade = PersonRelationServiceFacade
						.get(UI.getCurrent());
				try {
					Boolean isReferrer = personRelationServiceFacade.isPersonOfType(personId,
							ReferralRelationType.REFERRER);
					if (isReferrer) {
						buyerAvailableRewardLayout.setVisible(true);

						PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade
								.get(UI.getCurrent());
						BigDecimal personCumulativeReward = personRewardServiceFacade
								.getPersonCumulativeReward(personId);
						buyerAvailableRewardFld.setValue(Constants.DF.format(personCumulativeReward));

						ActivityTypeServiceFacade activityTypeServiceFacade = ActivityTypeServiceFacade
								.get(UI.getCurrent());
						List<ActivityType> activityTypes = activityTypeServiceFacade
								.findByCodes(Arrays.asList(ReferralActivityType.YEARLY_SUBSCRIPTION,
										ReferralActivityType.DISCOUNT_COUPON, ReferralActivityType.ITEM_PURCHASED));
						activityTypeFld.setValues(activityTypes);
					} else {
						activityRewardAmountFld.setVisible(true);
						ActivityTypeServiceFacade activityTypeServiceFacade = ActivityTypeServiceFacade
								.get(UI.getCurrent());
						List<ActivityType> activityTypes = activityTypeServiceFacade
								.findByCodes(Arrays.asList(ReferralActivityType.ITEM_PURCHASED));
						activityTypeFld.setValues(activityTypes);
					}
				} catch (FrameworkException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Activity setFormValues(Object entity) throws FrameworkException {
			activity = (Activity) entity;
			personFld.setValue(activity.getPerson());
			handleForReferrer(activity.getPerson().getId());

			activityTypeFld.setValue(activity.getActivityType());
			dateOfActivityFld
					.setValue(activity.getDateOfActivity() == null ? null : activity.getDateOfActivity().toLocalDate());
			spentAmountFld.setBigDecimalValue(activity.getActivityAmount());

			// referrerFld.setValue(activity.getReferrerPerson());
			activityRewardAmountFld.setBigDecimalValue(activity.getActivityRewardAmount());
			// referrerCumulativeReward.setValue(activity.getCumulativeRewardAmount()
			// == null
			// ? Constants.DF.format(BigDecimal.ZERO) :
			// Constants.DF.format(activity.getCumulativeRewardAmount()));
			useRewardFld.setValue(activity.getUseReward());
			usedRewardAmountFld.setBigDecimalValue(activity.getUsedRewardAmount());
			return activity;
		}

		@Override
		public Object getFormValues() throws FrameworkException {
			ActivityVO vo = new ActivityVO();

			vo.setId(activity == null ? null : activity.getId());
			vo.setPersonId(personFld.getItemId());
			vo.setDateOfActivity(dateOfActivityFld.getValue().atStartOfDay());
			vo.setActivityTypeId(activityTypeFld.getLongValue());
			vo.setActivityAmount(spentAmountFld.getBigDecimalValue());
			// vo.setReferrerId(referrerFld.getItemId());
			vo.setActivityRewardAmount(activityRewardAmountFld.getBigDecimalValue());
			vo.setUsedReward(usedRewardAmountFld.getBigDecimalValue());
			vo.setUseReward(useRewardFld.getValue());
			return vo;
		}
	}

}
