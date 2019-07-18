package software.simple.solutions.referral.web.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.components.AbstractBaseView;
import software.simple.solutions.framework.core.components.CButton;
import software.simple.solutions.framework.core.components.CPopupDateField;
import software.simple.solutions.framework.core.components.CTextField;
import software.simple.solutions.framework.core.components.select.GenderSelect;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.properties.RegistrationProperty;
import software.simple.solutions.framework.core.properties.SystemProperty;
import software.simple.solutions.framework.core.util.PropertyResolver;
import software.simple.solutions.referral.entities.Activity;
import software.simple.solutions.referral.properties.HomeProperty;

public class HomeView extends AbstractBaseView implements View {

	private static final long serialVersionUID = -1503998511461165836L;

	private static final Logger logger = LogManager.getLogger(HomeView.class);

	private VerticalLayout headerLayout;
	private VerticalLayout activitiesLayout;
	private Label totalFriendsLbl;
	private Label totalRewardsLbl;

	public HomeView() {
//		setSizeUndefined();
//		setWidth("100%");
//		addStyleName("v-scrollable");
	}

	private VerticalLayout createHeaderLayout() {
		headerLayout = new VerticalLayout();
		headerLayout.setSpacing(true);
		headerLayout.setMargin(true);
		headerLayout.addStyleName(ValoTheme.LAYOUT_CARD);

		totalFriendsLbl = new Label();
		totalFriendsLbl.setContentMode(ContentMode.HTML);
		totalFriendsLbl.setValue(PropertyResolver.getPropertyValueByLocale(HomeProperty.TOTAL_FRIENDS_COUNT));
		totalFriendsLbl.addStyleName(ValoTheme.LABEL_H1);
		totalFriendsLbl.addStyleName(ValoTheme.LABEL_COLORED);
		headerLayout.addComponent(totalFriendsLbl);

		totalRewardsLbl = new Label();
		totalRewardsLbl.setContentMode(ContentMode.HTML);
		totalRewardsLbl.setValue(PropertyResolver.getPropertyValueByLocale(HomeProperty.TOTAL_REWARDS));
		totalRewardsLbl.addStyleName(ValoTheme.LABEL_H1);
		totalRewardsLbl.addStyleName(ValoTheme.LABEL_COLORED);
		headerLayout.addComponent(totalRewardsLbl);

		return headerLayout;
	}

	private VerticalLayout createActivitiesLayout() {
		activitiesLayout = new VerticalLayout();
		activitiesLayout.setSpacing(true);
		activitiesLayout.setMargin(false);

		Label activitiesLayoutHeaderFld = new Label(PropertyResolver.getPropertyValueByLocale(HomeProperty.ACTIVITIES));
		activitiesLayoutHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		activitiesLayoutHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);
		activitiesLayoutHeaderFld.addStyleName(ValoTheme.LABEL_COLORED);
		activitiesLayout.addComponent(activitiesLayoutHeaderFld);

		Grid<Activity> activitiesGrid = new Grid<Activity>();
		activitiesGrid.setWidth("100%");
		Column<Activity, String> personColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 8663784377110755656L;

			@Override
			public String apply(Activity source) {
				return source.getPerson() == null ? null
						: (source.getPerson().getFirstName() + " " + source.getPerson().getLastName());
			}
		});
		personColumn.setCaption("Person");

		Column<Activity, String> dateOfActivityColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = -1639183548006847346L;

			@Override
			public String apply(Activity source) {
				return source.getDateOfActivity() == null ? null
						: source.getDateOfActivity().format(Constants.DATE_FORMATTER);
			}
		});
		dateOfActivityColumn.setCaption("Activity Date");

		Column<Activity, String> activityTypeColumn = activitiesGrid.addColumn(new ValueProvider<Activity, String>() {

			private static final long serialVersionUID = 9125613838555952320L;

			@Override
			public String apply(Activity source) {
				return source.getActivityType() == null ? null : source.getActivityType().getName();
			}
		});
		activityTypeColumn.setCaption("Type of Activity");

		Column<Activity, BigDecimal> activityAmountColumn = activitiesGrid
				.addColumn(new ValueProvider<Activity, BigDecimal>() {

					private static final long serialVersionUID = -4353655908297580L;

					@Override
					public BigDecimal apply(Activity source) {
						return source.getActivityAmount() == null ? null
								: source.getActivityAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}
				});
		activityAmountColumn.setCaption("Amount Spent");

		Column<Activity, BigDecimal> activityRewardAmoundColumn = activitiesGrid
				.addColumn(new ValueProvider<Activity, BigDecimal>() {

					private static final long serialVersionUID = -4740033061911964158L;

					@Override
					public BigDecimal apply(Activity source) {
						return source.getReferrerActivityReward() == null ? null
								: source.getReferrerActivityReward().setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}
				});
		activityRewardAmoundColumn.setCaption("Activity Reward");

		Column<Activity, BigDecimal> referrerRewardAmountColumn = activitiesGrid
				.addColumn(new ValueProvider<Activity, BigDecimal>() {

					private static final long serialVersionUID = -4372342667475526343L;

					@Override
					public BigDecimal apply(Activity source) {
						return source.getReferrerRewardAmount() == null ? null
								: source.getReferrerRewardAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}
				});
		referrerRewardAmountColumn.setCaption("Total Reward");

		activitiesLayout.addComponent(activitiesGrid);

		return activitiesLayout;
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
		for (int i = 0; i <= 10; i++) {
			FriendCard friendCard = new FriendCard();
			friendCard.setWidth("100%");
			verticalLayout.addComponent(friendCard);
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
