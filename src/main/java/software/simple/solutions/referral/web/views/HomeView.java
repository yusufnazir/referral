package software.simple.solutions.referral.web.views;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.components.AbstractBaseView;
import software.simple.solutions.framework.core.components.CButton;
import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.components.CPopupDateField;
import software.simple.solutions.framework.core.components.CTextField;
import software.simple.solutions.framework.core.components.NotificationWindow;
import software.simple.solutions.framework.core.components.SessionHolder;
import software.simple.solutions.framework.core.components.select.GenderSelect;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.paging.PagingBar;
import software.simple.solutions.framework.core.paging.PagingSearchEvent;
import software.simple.solutions.framework.core.pojo.PagingInfo;
import software.simple.solutions.framework.core.pojo.PagingResult;
import software.simple.solutions.framework.core.properties.RegistrationProperty;
import software.simple.solutions.framework.core.properties.SystemProperty;
import software.simple.solutions.framework.core.util.PropertyResolver;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.model.FriendModel;
import software.simple.solutions.referral.properties.HomeProperty;
import software.simple.solutions.referral.service.facade.ActivityServiceFacade;
import software.simple.solutions.referral.service.facade.PersonFriendServiceFacade;
import software.simple.solutions.referral.service.facade.PersonRewardServiceFacade;
import software.simple.solutions.referral.valueobjects.ActivityVO;

public class HomeView extends AbstractBaseView implements View {

	private static final long serialVersionUID = -1503998511461165836L;

	private static final Logger logger = LogManager.getLogger(HomeView.class);

	private VerticalLayout headerLayout;
	private VerticalLayout activitiesLayout;
	private Label totalFriendsLbl;
	private Label totalFriendsFld;
	private Label totalRewardsLbl;
	private Label totalRewardsFld;
	private ListDataProvider<Activity> activityDataProvider;
	private SessionHolder sessionHolder;

	private PagingBar pagingBar;

	private Grid<Activity> activitiesGrid;

	public HomeView() {
		sessionHolder = (SessionHolder) UI.getCurrent().getData();
		activityDataProvider = DataProvider.ofItems();
	}

	private VerticalLayout createHeaderLayout() {
		headerLayout = new VerticalLayout();
		headerLayout.setSpacing(true);
		headerLayout.setMargin(true);
		headerLayout.addStyleName(ValoTheme.LAYOUT_CARD);

		CGridLayout headerGridLayout = new CGridLayout();
		headerGridLayout.setSpacing(true);
		headerLayout.addComponent(headerGridLayout);

		totalFriendsLbl = new Label();
		totalFriendsLbl.setContentMode(ContentMode.HTML);
		totalFriendsLbl.setValue(PropertyResolver.getPropertyValueByLocale(HomeProperty.TOTAL_FRIENDS_COUNT));
		totalFriendsLbl.addStyleName(ValoTheme.LABEL_H1);
		totalFriendsLbl.addStyleName(ValoTheme.LABEL_COLORED);
		headerGridLayout.addComponent(totalFriendsLbl, 0, 0);

		totalFriendsFld = new Label();
		totalFriendsFld.setContentMode(ContentMode.HTML);
		totalFriendsFld.addStyleName(ValoTheme.LABEL_H1);
		totalFriendsFld.addStyleName(ValoTheme.LABEL_COLORED);
		totalFriendsFld.addStyleName(ValoTheme.LABEL_BOLD);
		totalFriendsFld.setValue(String.valueOf(0L));
		headerGridLayout.addComponent(totalFriendsFld, 1, 0);

		totalRewardsLbl = new Label();
		totalRewardsLbl.setContentMode(ContentMode.HTML);
		totalRewardsLbl.setValue(PropertyResolver.getPropertyValueByLocale(HomeProperty.TOTAL_REWARDS));
		totalRewardsLbl.addStyleName(ValoTheme.LABEL_H1);
		totalRewardsLbl.addStyleName(ValoTheme.LABEL_COLORED);
		headerGridLayout.addComponent(totalRewardsLbl, 0, 1);

		totalRewardsFld = new Label();
		totalRewardsFld.setContentMode(ContentMode.HTML);
		totalRewardsFld.addStyleName(ValoTheme.LABEL_H1);
		totalRewardsFld.addStyleName(ValoTheme.LABEL_COLORED);
		totalRewardsFld.addStyleName(ValoTheme.LABEL_BOLD);
		totalRewardsFld.setValue(Constants.DF.format(BigDecimal.ZERO));
		headerGridLayout.addComponent(totalRewardsFld, 1, 1);

		if (sessionHolder.getApplicationUser().getPerson() != null) {
			PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade.get(UI.getCurrent());
			Long personId = sessionHolder.getApplicationUser().getPerson().getId();
			try {
				BigDecimal cumulativeRewardAmount = personRewardServiceFacade.getPersonCumulativeReward(personId);
				totalRewardsFld.setValue(Constants.DF.format(cumulativeRewardAmount));
			} catch (FrameworkException e) {
				logger.error(e.getMessage(), e);
			}

			try {
				PersonFriendServiceFacade personFriendServiceFacade = PersonFriendServiceFacade.get(UI.getCurrent());
				Long totalFriends = personFriendServiceFacade.getTotalFriends(personId);
				totalFriendsFld.setValue(String.valueOf(totalFriends));
			} catch (FrameworkException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return headerLayout;
	}

	private VerticalLayout createActivitiesLayout() {
		activitiesLayout = new VerticalLayout();
		activitiesLayout.setSpacing(true);
		activitiesLayout.setMargin(false);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth("100%");
		activitiesLayout.addComponent(horizontalLayout);

		Label activitiesLayoutHeaderFld = new Label(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITIES));
		activitiesLayoutHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		activitiesLayoutHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);
		activitiesLayoutHeaderFld.addStyleName(ValoTheme.LABEL_COLORED);
		horizontalLayout.addComponent(activitiesLayoutHeaderFld);
		horizontalLayout.setComponentAlignment(activitiesLayoutHeaderFld, Alignment.BOTTOM_LEFT);

		pagingBar = new PagingBar();
		pagingBar.setItemsPerPage(10);
		horizontalLayout.addComponent(pagingBar);
		horizontalLayout.setComponentAlignment(pagingBar, Alignment.BOTTOM_RIGHT);
		PagingSearchEvent pagingSearchEvent = new PagingSearchEvent() {

			@Override
			public void handleSearch(int currentPage, int maxResult) {

				try {
					handleViewSearch();
				} catch (FrameworkException e) {
					logger.error(e.getMessage(), e);
					// updateErrorContent(e);
					// new MessageWindowHandler(e);
				}
			}

			@Override
			public Long count() throws FrameworkException {
				return handleCount(false);
			}

			private Long handleCount(boolean showCount) throws FrameworkException {
				Long count = 0L;
				pagingBar.setTotalRows(count);

				if (showCount) {
					NotificationWindow.notificationNormalWindow(SystemProperty.TOTAL_RECORDS_FOUND,
							new Object[] { count });
				}

				return count;
			}
		};

		pagingBar.addPagingSearchEvent(pagingSearchEvent);

		activitiesGrid = new Grid<Activity>();
		activitiesGrid.setHeightMode(HeightMode.UNDEFINED);
		activitiesGrid.setWidth("100%");
		Column<Activity, String> personColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 8663784377110755656L;

			@Override
			public String apply(Activity source) {
				return source.getPerson() == null ? null
						: (source.getPerson().getFirstName() + " " + source.getPerson().getLastName());
			}
		});
		personColumn.setCaption(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITY_PERSON));

		Column<Activity, String> dateOfActivityColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = -1639183548006847346L;

			@Override
			public String apply(Activity source) {
				return source.getDateOfActivity() == null ? null
						: source.getDateOfActivity().format(Constants.DATE_FORMATTER);
			}
		});
		dateOfActivityColumn.setCaption(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITY_DATE));

		Column<Activity, String> activityTypeColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 9125613838555952320L;

			@Override
			public String apply(Activity source) {
				return source.getActivityType() == null ? null : source.getActivityType().getName();
			}
		});
		activityTypeColumn.setCaption(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITY_TYPE));

		Column<Activity, String> activityAmountColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = -4353655908297580L;

			@Override
			public String apply(Activity source) {
				return source.getActivityAmount() == null ? Constants.DF.format(BigDecimal.ZERO)
						: Constants.DF.format(source.getActivityAmount());
			}
		});
		activityAmountColumn.setCaption(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITY_REWARD));

		Column<Activity, String> activityRewardAmoundColumn = activitiesGrid
				.addColumn(new ValueProvider<Activity, String>() {

					private static final long serialVersionUID = -4740033061911964158L;

					@Override
					public String apply(Activity source) {
						return source.getCumulativeRewardAmount() == null ? Constants.DF.format(BigDecimal.ZERO)
								: Constants.DF.format(source.getCumulativeRewardAmount());
					}
				});
		activityRewardAmoundColumn
				.setCaption(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITY_CUMULATIVE_AMOUNT));

		activitiesLayout.addComponent(activitiesGrid);
		activitiesGrid.setDataProvider(activityDataProvider);
		try {
			handleViewSearch();
		} catch (FrameworkException e) {
			logger.error(e.getMessage(), e);
		}

		return activitiesLayout;
	}

	private void handleViewSearch() throws FrameworkException {
		if (sessionHolder.getApplicationUser().getPerson() != null) {
			activitiesLayout.setVisible(true);
			ActivityServiceFacade activityServiceFacade = ActivityServiceFacade.get(UI.getCurrent());
			ActivityVO vo = new ActivityVO();
			vo.setPersonId(sessionHolder.getApplicationUser().getPerson().getId());
			PagingInfo pagingInfo = getPagingInfo();
			vo.setPagingInfo(pagingInfo);
			PagingResult<Activity> results = activityServiceFacade.findReferrerRelatedActivity(vo);
			activityDataProvider = DataProvider.ofCollection((Collection<Activity>) results.getResult());
			activitiesGrid.setDataProvider(activityDataProvider);
			activityDataProvider.refreshAll();
		} else {
			activitiesLayout.setVisible(false);
		}
	}

	protected PagingInfo getPagingInfo() {
		PagingInfo pagingInfo = new PagingInfo();
		pagingInfo.setMaxResult(pagingBar.getMaxResult());
		pagingInfo.setStartPosition(pagingBar.getStartPosition());
		return pagingInfo;
	}

	private HorizontalLayout createFriendsLayout() {
		HorizontalLayout friendsLayout = new HorizontalLayout();
		friendsLayout.setSpacing(true);
		friendsLayout.setMargin(false);
		friendsLayout.setHeightUndefined();

		VerticalLayout myFriendsLayout = createFriendsPanel();
		myFriendsLayout.setHeight("100%");
		friendsLayout.addComponent(myFriendsLayout);

		VerticalLayout inviteFriendLayout = createInviteFriendLayout();
		friendsLayout.addComponent(inviteFriendLayout);

		return friendsLayout;
	}

	private VerticalLayout createFriendsPanel() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(true);
		layout.setVisible(false);

		Label friendsLayoutHeaderFld = new Label(PropertyResolver.getPropertyValueByLocale(HomeProperty.MY_FRIENDS));
		friendsLayoutHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		friendsLayoutHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);
		friendsLayoutHeaderFld.addStyleName(ValoTheme.LABEL_COLORED);
		layout.addComponent(friendsLayoutHeaderFld);

		Panel panel = new Panel();
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.setWidth("400px");
		panel.setHeight("100%");
		// panel.setHeight("400px");
		layout.addComponent(panel);
		layout.setExpandRatio(panel, 1);

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100%");
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(true);
		panel.setContent(verticalLayout);

		if (sessionHolder.getApplicationUser().getPerson() != null) {
			try {
				PersonFriendServiceFacade personFriendServiceFacade = PersonFriendServiceFacade.get(UI.getCurrent());
				Long personId = sessionHolder.getApplicationUser().getPerson().getId();
				List<FriendModel> friends = personFriendServiceFacade.findFriendsByPerson(personId);
				if (friends == null || friends.isEmpty()) {
					layout.setVisible(false);
				} else {
					layout.setVisible(true);
					for (FriendModel friend : friends) {
						FriendCard friendCard = new FriendCard(friend);
						friendCard.setWidth("100%");
						verticalLayout.addComponent(friendCard);
					}
				}
			} catch (FrameworkException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return layout;
	}

	public VerticalLayout createInviteFriendLayout() {

		VerticalLayout fields = new VerticalLayout();
		fields.setMargin(false);
		fields.setSpacing(true);

		Label registrationHeaderFld = new Label(
				PropertyResolver.getPropertyValueByLocale(HomeProperty.INVITE_A_FRIEND));
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_COLORED);
		fields.addComponent(registrationHeaderFld);

		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(ValoTheme.LAYOUT_CARD);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("600px");
		fields.addComponent(layout);

		Label errorLbl = new Label();
		errorLbl.setContentMode(ContentMode.HTML);
		errorLbl.addStyleName(ValoTheme.LABEL_FAILURE);
		errorLbl.addStyleName(ValoTheme.LABEL_H3);
		errorLbl.setVisible(false);
		errorLbl.setSizeFull();

		final CTextField firstNameFld = new CTextField();
		firstNameFld.setSizeFull();
		firstNameFld.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		firstNameFld.setCaptionByKey(RegistrationProperty.REGISTER_FIRST_NAME);
		firstNameFld
				.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_FIRST_NAME));
		firstNameFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		firstNameFld.setRequiredIndicatorVisible(true);

		final CTextField lastNameFld = new CTextField();
		lastNameFld.setSizeFull();
		lastNameFld.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		lastNameFld.setCaptionByKey(RegistrationProperty.REGISTER_LAST_NAME);
		lastNameFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_LAST_NAME));
		lastNameFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		lastNameFld.setRequiredIndicatorVisible(true);

		final CPopupDateField dobFld = new CPopupDateField();
		dobFld.setSizeFull();
		dobFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		dobFld.setCaptionByKey(RegistrationProperty.REGISTER_DATE_OF_BIRTH);
		dobFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_DATE_OF_BIRTH));
		dobFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		dobFld.setDateFormat(Constants.SIMPLE_DATE_FORMAT.toPattern());
		dobFld.setRangeEnd(LocalDate.now());
		dobFld.setRequiredIndicatorVisible(true);

		final GenderSelect genderFld = new GenderSelect();
		genderFld.setSizeFull();
		genderFld.addStyleName(ValoTheme.COMBOBOX_LARGE);
		genderFld.setCaptionByKey(RegistrationProperty.REGISTER_GENDER);
		genderFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_GENDER));

		final CTextField contactNumberFld = new CTextField();
		contactNumberFld.setSizeFull();
		contactNumberFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		contactNumberFld.setCaptionByKey(RegistrationProperty.REGISTER_MOBILE_NUMBER);
		contactNumberFld
				.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_MOBILE_NUMBER));
		contactNumberFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		contactNumberFld.setRequiredIndicatorVisible(true);

		final CTextField emailFld = new CTextField();
		emailFld.setSizeFull();
		emailFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		emailFld.setCaptionByKey(RegistrationProperty.REGISTER_EMAIL);
		emailFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_EMAIL));
		emailFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		emailFld.setRequiredIndicatorVisible(true);

		HorizontalLayout actionLayout = new HorizontalLayout();
		actionLayout.setMargin(false);
		actionLayout.setSpacing(true);
		actionLayout.setWidth("100%");

		final CButton inviteFriendsFld = new CButton();
		inviteFriendsFld.setSizeFull();
		inviteFriendsFld.addStyleName(ValoTheme.BUTTON_LARGE);
		inviteFriendsFld.addStyleName(ValoTheme.BUTTON_PRIMARY);
		inviteFriendsFld.setCaptionByKey(HomeProperty.INVITE_BUTTON);
		inviteFriendsFld.setIcon(FontAwesome.USER);
		inviteFriendsFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		actionLayout.addComponent(inviteFriendsFld);

		final CButton clearFieldsFld = new CButton();
		clearFieldsFld.setSizeFull();
		clearFieldsFld.addStyleName(ValoTheme.BUTTON_LARGE);
		clearFieldsFld.setCaptionByKey(SystemProperty.SYSTEM_BUTTON_CLEAR);
		clearFieldsFld.addStyleName(ValoTheme.BUTTON_DANGER);
		clearFieldsFld.setIcon(VaadinIcons.ERASER);
		actionLayout.addComponent(clearFieldsFld);

		layout.addComponents(firstNameFld, lastNameFld, dobFld, contactNumberFld, emailFld, actionLayout);

		inviteFriendsFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7736352842423878879L;

			@Override
			public void buttonClick(ClickEvent event) {
				// registerUser();
			}

			private void clearFields() {
				firstNameFld.clear();
				lastNameFld.clear();
				dobFld.clear();
				contactNumberFld.clear();
				genderFld.clear();
				emailFld.clear();
			}

			// private void registerUser() {
			// errorLbl.setVisible(false);
			//
			// IApplicationUserService applicationUserService =
			// ContextProvider.getBean(IApplicationUserService.class);
			// ApplicationUserVO vo = new ApplicationUserVO();
			// vo.setFirstName(firstNameFld.getValue());
			// vo.setLastName(lastNameFld.getValue());
			// vo.setDateOfBirth(dobFld.getValue());
			// vo.setMobileNumber(contactNumberFld.getValue());
			// vo.setGenderId(genderFld.getLongValue());
			// vo.setUsername(emailFld.getValue());
			// vo.setEmail(emailFld.getValue());
			// vo.setPassword(passwordFld.getValue());
			// vo.setPasswordConfirm(confirmPasswordFld.getValue());
			// vo.setTermsAccepted(termsAndConditionCheckFld.getValue());
			// try {
			// SecurityValidation securityValidation =
			// applicationUserService.registerUser(vo);
			// if (!securityValidation.isSuccess()) {
			// errorLbl.setVisible(true);
			// errorLbl.setValue(PropertyResolver.getPropertyValueByLocale(securityValidation.getMessageKey(),
			// securityValidation.getArgs(t -> {
			// if (t.isKey()) {
			// return PropertyResolver.getPropertyValueByLocale(t.getValue());
			// } else {
			// return t.getValue();
			// }
			// })));
			// } else {
			// clearFields();
			// }
			// } catch (FrameworkException e) {
			// logger.error(e.getMessage(), e);
			// }
			// }
		});

		clearFieldsFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7736352842423878879L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});

		return fields;

	}

	public static class Friend {
		private String name;
		private String email;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}

	@Override
	public void executeBuild() throws FrameworkException {
		Panel panel = new Panel();
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.setSizeFull();
		addComponent(panel);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		VerticalLayout layout = new VerticalLayout();
		panel.setContent(layout);
		layout.setWidth("100%");

		headerLayout = createHeaderLayout();
		layout.addComponent(headerLayout);
		layout.setComponentAlignment(headerLayout, Alignment.MIDDLE_CENTER);

		activitiesLayout = createActivitiesLayout();
		layout.addComponent(activitiesLayout);

		HorizontalLayout friendsLayout = createFriendsLayout();
		layout.addComponent(friendsLayout);
	}
}
