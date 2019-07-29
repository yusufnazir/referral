package software.simple.solutions.referral.web.views.home;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.components.CButton;
import software.simple.solutions.framework.core.components.CPopupDateField;
import software.simple.solutions.framework.core.components.CTextField;
import software.simple.solutions.framework.core.components.select.GenderSelect;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.pojo.SecurityValidation;
import software.simple.solutions.framework.core.properties.RegistrationProperty;
import software.simple.solutions.framework.core.properties.SystemProperty;
import software.simple.solutions.framework.core.util.ContextProvider;
import software.simple.solutions.framework.core.util.PropertyResolver;
import software.simple.solutions.framework.core.valueobjects.PersonVO;
import software.simple.solutions.referral.properties.HomeProperty;
import software.simple.solutions.referral.service.IActivityService;

public class InviteFriendLayout {

	private static final Logger logger = LogManager.getLogger(InviteFriendLayout.class);

	private VerticalLayout fields;
	private Label registrationHeaderFld;
	private VerticalLayout layout;
	private Label errorLbl;
	private CTextField firstNameFld;
	private CTextField lastNameFld;
	private CPopupDateField dobFld;
	private GenderSelect genderFld;
	private CTextField contactNumberFld;
	private CTextField emailFld;
	private HorizontalLayout actionLayout;
	private CButton inviteFriendsFld;
	private CButton clearFieldsFld;

	public VerticalLayout createInviteFriendLayout() {

		fields = new VerticalLayout();
		fields.setMargin(false);
		fields.setSpacing(true);

		registrationHeaderFld = new Label(PropertyResolver.getPropertyValueByLocale(HomeProperty.INVITE_A_FRIEND));
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_H2);
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_BOLD);
		registrationHeaderFld.addStyleName(ValoTheme.LABEL_COLORED);
		fields.addComponent(registrationHeaderFld);

		layout = new VerticalLayout();
		layout.addStyleName(ValoTheme.LAYOUT_CARD);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("600px");
		fields.addComponent(layout);

		errorLbl = new Label();
		errorLbl.setContentMode(ContentMode.HTML);
		errorLbl.addStyleName(ValoTheme.LABEL_FAILURE);
		errorLbl.addStyleName(ValoTheme.LABEL_H3);
		errorLbl.setVisible(false);
		errorLbl.setSizeFull();

		firstNameFld = new CTextField();
		firstNameFld.setSizeFull();
		firstNameFld.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		firstNameFld.setCaptionByKey(RegistrationProperty.REGISTER_FIRST_NAME);
		firstNameFld
				.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_FIRST_NAME));
		firstNameFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		firstNameFld.setRequiredIndicatorVisible(true);

		lastNameFld = new CTextField();
		lastNameFld.setSizeFull();
		lastNameFld.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		lastNameFld.setCaptionByKey(RegistrationProperty.REGISTER_LAST_NAME);
		lastNameFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_LAST_NAME));
		lastNameFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		lastNameFld.setRequiredIndicatorVisible(true);

		dobFld = new CPopupDateField();
		dobFld.setSizeFull();
		dobFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		dobFld.setCaptionByKey(RegistrationProperty.REGISTER_DATE_OF_BIRTH);
		dobFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_DATE_OF_BIRTH));
		dobFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		dobFld.setDateFormat(Constants.SIMPLE_DATE_FORMAT.toPattern());
		dobFld.setRangeEnd(LocalDate.now());
		dobFld.setRequiredIndicatorVisible(true);

		genderFld = new GenderSelect();
		genderFld.setSizeFull();
		genderFld.addStyleName(ValoTheme.COMBOBOX_LARGE);
		genderFld.setCaptionByKey(RegistrationProperty.REGISTER_GENDER);
		genderFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_GENDER));

		contactNumberFld = new CTextField();
		contactNumberFld.setSizeFull();
		contactNumberFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		contactNumberFld.setCaptionByKey(RegistrationProperty.REGISTER_MOBILE_NUMBER);
		contactNumberFld
				.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_MOBILE_NUMBER));
		contactNumberFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		contactNumberFld.setRequiredIndicatorVisible(true);

		emailFld = new CTextField();
		emailFld.setSizeFull();
		emailFld.addStyleName(ValoTheme.DATEFIELD_LARGE);
		emailFld.setCaptionByKey(RegistrationProperty.REGISTER_EMAIL);
		emailFld.setPlaceholder(PropertyResolver.getPropertyValueByLocale(RegistrationProperty.REGISTER_EMAIL));
		emailFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		emailFld.setRequiredIndicatorVisible(true);

		actionLayout = new HorizontalLayout();
		actionLayout.setMargin(false);
		actionLayout.setSpacing(true);
		actionLayout.setWidth("100%");

		inviteFriendsFld = new CButton();
		inviteFriendsFld.setSizeFull();
		inviteFriendsFld.addStyleName(ValoTheme.BUTTON_LARGE);
		inviteFriendsFld.addStyleName(ValoTheme.BUTTON_PRIMARY);
		inviteFriendsFld.setCaptionByKey(HomeProperty.INVITE_BUTTON);
		inviteFriendsFld.setIcon(FontAwesome.USER);
		inviteFriendsFld.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		actionLayout.addComponent(inviteFriendsFld);

		clearFieldsFld = new CButton();
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

			private void registerUser() {
				errorLbl.setVisible(false);

				IActivityService activityService = ContextProvider.getBean(IActivityService.class);
				PersonVO vo = new PersonVO();
				vo.setFirstName(firstNameFld.getValue());
				vo.setLastName(lastNameFld.getValue());
				vo.setDateOfBirth(dobFld.getValue());
				vo.setMobileNumber(contactNumberFld.getValue());
				vo.setGenderId(genderFld.getLongValue());
				vo.setEmail(emailFld.getValue());
				// vo.setTermsAccepted(termsAndConditionCheckFld.getValue());
				try {
					SecurityValidation securityValidation = activityService.registerFriend(vo);
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
					}
				} catch (FrameworkException e) {
					logger.error(e.getMessage(), e);
				}
			}
		});

		clearFieldsFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7736352842423878879L;

			@Override
			public void buttonClick(ClickEvent event) {
				clearFields();
			}
		});

		return fields;
	}

	private void clearFields() {
		firstNameFld.clear();
		lastNameFld.clear();
		dobFld.clear();
		contactNumberFld.clear();
		genderFld.clear();
		emailFld.clear();
	}

}
