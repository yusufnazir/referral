package software.simple.solutions.referral.web.views;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.components.CButton;
import software.simple.solutions.framework.core.components.CCheckBox;
import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.components.CLabel;
import software.simple.solutions.framework.core.components.CPasswordField;
import software.simple.solutions.framework.core.components.CPopupDateField;
import software.simple.solutions.framework.core.components.CTextField;
import software.simple.solutions.framework.core.components.MessageWindowHandler;
import software.simple.solutions.framework.core.components.NotificationWindow;
import software.simple.solutions.framework.core.components.select.GenderSelect;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.constants.Style;
import software.simple.solutions.framework.core.entities.Configuration;
import software.simple.solutions.framework.core.events.SimpleSolutionsEvent.UserLoginRequestedEvent;
import software.simple.solutions.framework.core.events.SimpleSolutionsEventBus;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.SecurityValidation;
import software.simple.solutions.framework.core.properties.ConfigurationProperty;
import software.simple.solutions.framework.core.properties.RegistrationProperty;
import software.simple.solutions.framework.core.properties.SystemProperty;
import software.simple.solutions.framework.core.service.IApplicationUserService;
import software.simple.solutions.framework.core.service.facade.ConfigurationServiceFacade;
import software.simple.solutions.framework.core.util.ContextProvider;
import software.simple.solutions.framework.core.util.PropertyResolver;
import software.simple.solutions.framework.core.valueobjects.ApplicationUserVO;
import software.simple.solutions.framework.core.web.view.password.RequestPasswordResetLayout;
import software.simple.solutions.referral.constants.ReferralStyle;

public class LoginView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1969692676006992700L;
	private static final Logger logger = LogManager.getLogger(LoginView.class);
	private Component applicationLabelsLayout;
	private VerticalLayout registrationFieldsLayout;
	private Component registrationSuccessfullLayout;
	private Configuration enableRegistrationConfig;
	private VerticalLayout bannerLayout;

	@Override
	public void enter(ViewChangeEvent event) {
		String parameters = event.getParameters();
		System.out.println(parameters);
	}

	public LoginView() throws FrameworkException {
		addStyleName("landing-page");
		setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);

		Component loginForm = buildLoginLayout();
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.TOP_CENTER);
	}

	private Component buildLoginLayout() throws FrameworkException {
		final VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setWidth("-1px");
		loginPanel.setSpacing(false);
		loginPanel.setMargin(false);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName(Style.LANDING_PAGE_PANEL);
		loginPanel.addStyleName("fade-in");

		enableRegistrationConfig = ConfigurationServiceFacade.get(UI.getCurrent())
				.getByCode(ConfigurationProperty.APPLICATION_ENABLE_REGISTRATION);

		applicationLabelsLayout = buildApplicationLabels();
		bannerLayout = buildBannerLayout();

		registrationSuccessfullLayout = buildRegistrationSuccessfullLayout();
		registrationSuccessfullLayout.setVisible(false);

		registrationFieldsLayout = buildRegistrationFields();

		loginPanel.addComponent(applicationLabelsLayout);
		loginPanel.addComponent(bannerLayout);

		loginPanel.addComponent(registrationSuccessfullLayout);
		loginPanel.setComponentAlignment(registrationSuccessfullLayout, Alignment.TOP_CENTER);

		loginPanel.addComponent(registrationFieldsLayout);
		loginPanel.setComponentAlignment(registrationFieldsLayout, Alignment.TOP_CENTER);

		return loginPanel;
	}

	private VerticalLayout buildBannerLayout() {
		VerticalLayout bannerLayout = new VerticalLayout();
		bannerLayout.setWidth("100%");
		bannerLayout.setSpacing(true);
		bannerLayout.setMargin(false);

		Image image = new Image();
		image.setHeight("-1px");
		image.setWidth("100%");
		image.setSource(new ThemeResource("img/banner.jpg"));
		// image.addStyleName("appbar-profile-image");
		bannerLayout.addComponent(image);

		Label messageLbl = new Label();
		messageLbl.setWidth("80%");
		messageLbl.setContentMode(ContentMode.HTML);
		messageLbl.addStyleName(ValoTheme.LABEL_COLORED);
		messageLbl.setValue("&nbsp;<h1 style=\"color:#a04d7d;\"><b>Get rewarded and make your friends love you!</b></h1>"
				+ "<h2 style=\"color:#a04d7d;\"><p>Let your friends use your refferal discount code "
				+ "@ each purchase order and they wil receive the same discount as you. "
				+ "For every purchase they make with a value of $100 or more, you get a $5 reward.</p><br></h2>"
				+ "<h2 style=\"color:#a04d7d;\"><b>Conditions:</b><br>&nbsp;" 
				+ "<ul>"
				+ "<li style=\"padding-bottom:0.5em\">You must be a registered referal member. Cost to register TBD (1st 10-15 kandidates free admission).</li>"
				+ "<li style=\"padding-bottom:0.5em\">Your friend must match your full name and discount code @ each purchase order.</li>"
				+ "<li style=\"padding-bottom:0.5em\">If unspend your accumulated reward will max out @ US$ 100.</li>"
				+ "<li style=\"padding-bottom:0.5em\">Use your reward to discount the Costs for your next order (Price USA or EU = Price SU) on items with a shipping weight equal or less than 2 lbs and up to a purchase order of max. US$ 100.</li>"
				+ "</ul></h2>");
		bannerLayout.addComponent(messageLbl);
		bannerLayout.setComponentAlignment(messageLbl, Alignment.TOP_CENTER);

		return bannerLayout;
	}

	private Component buildLoginFields() throws FrameworkException {
		CGridLayout fields = new CGridLayout();
		fields.setMargin(true);
		fields.setSpacing(true);
		fields.setHeight("100%");
		fields.addStyleName("fields");

		final CTextField username = new CTextField();
		username.setSizeFull();
		username.addStyleName(ValoTheme.TEXTFIELD_SMALL);
		username.setCaptionByKey(SystemProperty.LOGIN_USERNAME);
		username.setPlaceholder(PropertyResolver.getPropertyValueByLocale(SystemProperty.LOGIN_USERNAME));
		username.setIcon(FontAwesome.USER);
		fields.addComponent(username, 0, 0);
		// username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final CPasswordField password = new CPasswordField();
		password.setSizeFull();
		password.addStyleName(ValoTheme.TEXTFIELD_SMALL);
		password.setCaptionByKey(SystemProperty.LOGIN_PASSWORD);
		password.setPlaceholder(PropertyResolver.getPropertyValueByLocale(SystemProperty.LOGIN_PASSWORD));
		password.setIcon(FontAwesome.LOCK);
		fields.addComponent(password, 1, 0);
		// password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		Configuration smtpEnableConfig = ConfigurationServiceFacade.get(UI.getCurrent())
				.getByCode(ConfigurationProperty.SMTP_ENABLE);
		if (smtpEnableConfig != null && smtpEnableConfig.getBoolean()) {

			CButton resetBtn = new CButton();
			resetBtn.setSizeUndefined();
			resetBtn.setCaptionByKey(SystemProperty.SYSTEM_RESET_PASSWORD);
			resetBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			resetBtn.addStyleName(ValoTheme.BUTTON_LINK);
			fields.addComponent(resetBtn, 1, 1);

			resetBtn.addClickListener(new ClickListener() {

				private static final long serialVersionUID = -3880882585537226501L;

				@Override
				public void buttonClick(ClickEvent event) {
					new RequestPasswordResetLayout();
				}
			});
		}

		final CButton signin = new CButton();
		signin.setSizeFull();
		signin.addStyleName(ValoTheme.BUTTON_SMALL);
		signin.setCaptionByKey(SystemProperty.LOGIN_BUTTON_LOGIN);
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();
		fields.addComponent(signin, 2, 0);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		final CButton registerFld = new CButton();
		registerFld.addStyleName(ValoTheme.BUTTON_SMALL);
		registerFld.setCaptionByKey(SystemProperty.LOGIN_REGISTER_HERE);
		registerFld.addStyleName(ValoTheme.BUTTON_LINK);
		registerFld.addStyleName(Style.REGISTER_LINK_BUTTON);
		registerFld.setVisible(false);
		fields.addComponent(registerFld, 3, 0);
		fields.setComponentAlignment(registerFld, Alignment.BOTTOM_LEFT);

		if (enableRegistrationConfig != null && enableRegistrationConfig.getBoolean() != null
				&& enableRegistrationConfig.getBoolean()) {
			registerFld.setVisible(true);
		}

		signin.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -232691329181636057L;

			@Override
			public void buttonClick(final ClickEvent event) {
				try {
					IApplicationUserService applicationUserService = ContextProvider
							.getBean(IApplicationUserService.class);
					SecurityValidation securityValidation = applicationUserService.validateLogin(username.getValue(),
							password.getValue());
					if (securityValidation.isSuccess()) {
						String applicationUsername = postProcessLdapUsername(username.getValue());
						SimpleSolutionsEventBus
								.post(new UserLoginRequestedEvent(applicationUsername, password.getValue()));
					} else {
						NotificationWindow.notificationErrorWindow(
								PropertyResolver.getPropertyValueByLocale(securityValidation.getMessageKey()),
								UI.getCurrent().getLocale());
					}
				} catch (FrameworkException e) {
					new MessageWindowHandler(e);
					logger.error(e.getMessage(), e);
				}
			}
		});

		registerFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -3880882585537226501L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().scrollIntoView(registrationFieldsLayout);
				// registrationFieldsLayout.setVisible(true);
				// registrationSuccessfullLayout.setVisible(false);
			}
		});
		return fields;
	}

	private VerticalLayout buildRegistrationFields() {
		registrationFieldsLayout = new VerticalLayout();
		registrationFieldsLayout.setMargin(true);
		registrationFieldsLayout.setSpacing(true);
		registrationFieldsLayout.setWidth("600px");
		registrationFieldsLayout.setVisible(false);
		if (enableRegistrationConfig != null && enableRegistrationConfig.getBoolean() != null
				&& enableRegistrationConfig.getBoolean()) {
			registrationFieldsLayout.setVisible(true);
		}

		Label registrationHeaderFld = new Label(
				PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_HEADER));
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);

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

		final CPasswordField passwordFld = new CPasswordField();
		passwordFld.setSizeFull();
		passwordFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		passwordFld.setCaptionByKey(RegistrationProperty.REGISTER_PASSWORD);
		passwordFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_PASSWORD));
		passwordFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		passwordFld.setRequiredIndicatorVisible(true);

		final CPasswordField confirmPasswordFld = new CPasswordField();
		confirmPasswordFld.setSizeFull();
		confirmPasswordFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		confirmPasswordFld.setCaptionByKey(RegistrationProperty.REGISTER_CONFIRM_PASSWORD);
		confirmPasswordFld.setPlaceholder(
				PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_CONFIRM_PASSWORD));
		confirmPasswordFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		confirmPasswordFld.setRequiredIndicatorVisible(true);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		CCheckBox termsAndConditionCheckFld = new CCheckBox();
		horizontalLayout.addComponent(termsAndConditionCheckFld);
		Link link = new Link();
		link.setCaption(
				PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_AGREE_TERMS_AND_CONDITIONS));
		link.addStyleName(Style.REGISTER_LINK);
		horizontalLayout.addComponent(link);

		final CButton registerFld = new CButton();
		registerFld.setSizeFull();
		registerFld.addStyleName(ValoTheme.BUTTON_LARGE);
		registerFld.setCaptionByKey(RegistrationProperty.REGISTER_BUTTON_CONFIRM);
		registerFld.setIcon(FontAwesome.USER);
		registerFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		registrationFieldsLayout.addComponents(registrationHeaderFld, errorLbl, firstNameFld, lastNameFld, dobFld,
				contactNumberFld, emailFld, passwordFld, confirmPasswordFld, horizontalLayout, registerFld);

		registerFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7736352842423878879L;

			@Override
			public void buttonClick(ClickEvent event) {
				registerUser();
			}

			private void clearFields() {
				firstNameFld.clear();
				lastNameFld.clear();
				dobFld.clear();
				contactNumberFld.clear();
				genderFld.clear();
				emailFld.clear();
				passwordFld.clear();
				confirmPasswordFld.clear();
				termsAndConditionCheckFld.clear();
			}

			private void registerUser() {
				errorLbl.setVisible(false);

				IApplicationUserService applicationUserService = ContextProvider.getBean(IApplicationUserService.class);
				ApplicationUserVO vo = new ApplicationUserVO();
				vo.setFirstName(firstNameFld.getValue());
				vo.setLastName(lastNameFld.getValue());
				vo.setDateOfBirth(dobFld.getValue());
				vo.setMobileNumber(contactNumberFld.getValue());
				vo.setGenderId(genderFld.getLongValue());
				vo.setUsername(emailFld.getValue());
				vo.setEmail(emailFld.getValue());
				vo.setPassword(passwordFld.getValue());
				vo.setPasswordConfirm(confirmPasswordFld.getValue());
				vo.setTermsAccepted(termsAndConditionCheckFld.getValue());
				try {
					SecurityValidation securityValidation = applicationUserService.registerUser(vo);
					if (!securityValidation.isSuccess()) {
						errorLbl.setVisible(true);
						errorLbl.setValue(PropertyResolver.getPropertyValueByLocale(securityValidation.getMessageKey(),
								securityValidation.getArgs(t -> {
									if (t.isKey()) {
										return PropertyResolver.getPropertyValueByLocale(t.getValue());
									} else {
										return t.getValue();
									}
								})));
					} else {
						clearFields();
						// registrationFieldsLayout.setVisible(false);
						registrationSuccessfullLayout.setVisible(true);
					}
				} catch (FrameworkException e) {
					logger.error(e.getMessage(), e);
				}
			}
		});

		return registrationFieldsLayout;
	}

	private Component buildRegistrationSuccessfullLayout() {
		VerticalLayout fields = new VerticalLayout();
		fields.setMargin(true);
		fields.setSpacing(true);
		fields.setWidth("600px");

		Label registrationHeaderFld = new Label(
				PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_HEADER_SUCCESSFULL));
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);

		Label successLbl = new Label();
		successLbl.setContentMode(ContentMode.HTML);
		successLbl.addStyleName(ValoTheme.LABEL_SUCCESS);
		successLbl.addStyleName(ValoTheme.LABEL_H2);
		successLbl.setSizeFull();
		successLbl.setValue(
				PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_USER_CREATED_SUCCESSFULLY));

		final CButton closeLayoutFld = new CButton();
		closeLayoutFld.addStyleName(ValoTheme.BUTTON_LARGE);
		closeLayoutFld.addStyleName(ValoTheme.BUTTON_LINK);
		closeLayoutFld.setIcon(VaadinIcons.THUMBS_UP);

		fields.addComponents(registrationHeaderFld, successLbl, closeLayoutFld);
		fields.setComponentAlignment(closeLayoutFld, Alignment.BOTTOM_RIGHT);

		closeLayoutFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7736352842423878879L;

			@Override
			public void buttonClick(ClickEvent event) {
				registrationSuccessfullLayout.setVisible(false);
			}
		});

		return fields;
	}

	protected String postProcessLdapUsername(String username) {
		String applicationUserName = username;
		if (username.contains("@")) {
			applicationUserName = username.substring(0, username.indexOf('@'));
		} else if (username.contains("\\")) {
			applicationUserName = username.substring(username.indexOf('\\') + 1, username.length());
		}
		return applicationUserName;
	}

	private Component buildApplicationLabels() throws FrameworkException {
		HorizontalLayout labels = new HorizontalLayout();
		labels.setMargin(true);
		labels.setSizeFull();
		labels.addStyleName(ReferralStyle.LANDING_PAGE_HEADER);

		CLabel welcomeApplicationNameLbl = new CLabel();
		welcomeApplicationNameLbl.setValueByKey(SystemProperty.LOGIN_WELCOME);
		welcomeApplicationNameLbl.setSizeUndefined();
		welcomeApplicationNameLbl.addStyleName(ValoTheme.LABEL_H1);
		// welcomeApplicationNameLbl.addStyleName(ValoTheme.LABEL_COLORED);
		labels.addComponent(welcomeApplicationNameLbl);
		labels.setExpandRatio(welcomeApplicationNameLbl, 1f);

		Component loginFields = buildLoginFields();
		labels.addComponent(loginFields);
		labels.setComponentAlignment(loginFields, Alignment.MIDDLE_LEFT);

		Configuration applicationNameConfig = ConfigurationServiceFacade.get(UI.getCurrent())
				.getByCode(ConfigurationProperty.APPLICATION_NAME);
		if (applicationNameConfig != null && applicationNameConfig.getValue() != null) {
			welcomeApplicationNameLbl.setValue(applicationNameConfig.getValue());
		}
		return labels;
	}

}
