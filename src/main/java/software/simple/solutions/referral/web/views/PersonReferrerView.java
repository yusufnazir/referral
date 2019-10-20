package software.simple.solutions.referral.web.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.components.AbstractBaseView;
import software.simple.solutions.framework.core.components.CButton;
import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.components.CLabel;
import software.simple.solutions.framework.core.components.ConfirmWindow;
import software.simple.solutions.framework.core.components.ConfirmWindow.ConfirmationHandler;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.entities.PersonRelation;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.properties.SystemProperty;
import software.simple.solutions.framework.core.service.facade.PersonRelationServiceFacade;
import software.simple.solutions.framework.core.valueobjects.PersonRelationVO;
import software.simple.solutions.referral.constants.ReferralReferenceKey;
import software.simple.solutions.referral.constants.ReferralRelationType;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.entities.PersonReward;
import software.simple.solutions.referral.properties.ReferrerProperty;
import software.simple.solutions.referral.service.facade.PersonFriendServiceFacade;
import software.simple.solutions.referral.service.facade.PersonRewardServiceFacade;

public class PersonReferrerView extends AbstractBaseView {

	private static final long serialVersionUID = 2155260956785400977L;

	private static final Logger logger = LogManager.getLogger(PersonReferrerView.class);

	private Person person;
	private PersonRelation personRelation;

	private CButton toggleReferrerFld;
	private Label totalRewardAmount;
	private Label currentReferrerFld;
	private Label tokenFld;

	@Override
	public void executeBuild() throws FrameworkException {
		setHeight("100%");
		person = getReferenceKey(ReferralReferenceKey.PERSON);
		buildMainLayout();
	}

	@Override
	public boolean isSubMenuValid() {
		Object referenceKey = getReferenceKey(ReferralReferenceKey.PERSON);
		return referenceKey != null;
	}

	private void buildMainLayout() {
		CGridLayout gridLayout = new CGridLayout();
		gridLayout.setSpacing(true);
		addComponent(gridLayout);
		currentReferrerFld = gridLayout.addField(CLabel.class, ReferrerProperty.CURRENT_REFERRER, 0, 0);
		toggleReferrerFld = gridLayout.addField(CButton.class, ReferrerProperty.TOGGLE_REFERRER, 0, 1);
		toggleReferrerFld.addStyleName(ValoTheme.BUTTON_LARGE);
		toggleReferrerFld.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		toggleReferrerFld.addStyleName(ValoTheme.BUTTON_LINK);

		totalRewardAmount = gridLayout.addField(CLabel.class, ReferrerProperty.TOTAL_REWARD_AMOUNT, 0, 2);

		tokenFld = gridLayout.addField(Label.class, ReferrerProperty.TOKEN, 0, 3);

		try {
			setValues();
		} catch (FrameworkException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void toggleReferrerFldStyle(boolean selected) {
		if (selected) {
			toggleReferrerFld.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
			toggleReferrerFld.addStyleName(ValoTheme.BUTTON_DANGER);
			toggleReferrerFld.setCaptionByKey(ReferrerProperty.STOP_BEING_REFERRER);
			toggleReferrerFld.setIcon(FontAwesome.BAN);
		} else {
			toggleReferrerFld.removeStyleName(ValoTheme.BUTTON_DANGER);
			toggleReferrerFld.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			toggleReferrerFld.setCaptionByKey(ReferrerProperty.START_BEING_REFERRER);
			toggleReferrerFld.setIcon(FontAwesome.CHECK_CIRCLE_O);
		}
	}

	private void setValues() throws FrameworkException {
		toggleReferrerFldStyle(false);
		toggleReferrerFld.setData(false);

		PersonFriendServiceFacade personFriendServiceFacade = PersonFriendServiceFacade.get(UI.getCurrent());
		PersonFriend personFriend = personFriendServiceFacade.findReferrerOfFriend(person.getId());
		currentReferrerFld.setVisible(false);
		if (personFriend != null) {
			currentReferrerFld.setVisible(true);
			currentReferrerFld.setValue(personFriend.getPerson().getCaption());
			totalRewardAmount.setVisible(false);
		}

		PersonRelationServiceFacade personRelationServiceFacade = PersonRelationServiceFacade.get(UI.getCurrent());
		personRelation = personRelationServiceFacade.getByPerson(person.getId(), ReferralRelationType.REFERRER);
		if (personRelation != null) {
			currentReferrerFld.setVisible(false);
			toggleReferrerFld.setData(true);
			toggleReferrerFldStyle(true);

			totalRewardAmount.setVisible(true);

			PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade.get(UI.getCurrent());
			PersonReward personReward = personRewardServiceFacade.getByPerson(person.getId());
			// BigDecimal cumulativeReward =
			// personRewardServiceFacade.getPersonCumulativeReward(person.getId());
			BigDecimal cumulativeReward = (personReward == null || personReward.getCumulativeReward() == null)
					? BigDecimal.ZERO : personReward.getCumulativeReward();
			totalRewardAmount.setValue(Constants.DF.format(cumulativeReward));

			if (personReward != null) {
				tokenFld.setValue(personReward.getToken());
			}
		}

		toggleReferrerFld.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 6072678866838425735L;

			public void disableAsReferrer() throws FrameworkException {
				PersonRelationVO personRelationVO = new PersonRelationVO();
				personRelationVO.setId(personRelation.getId());
				personRelationVO.setPersonId(person.getId());
				personRelationVO.setRelationId(ReferralRelationType.REFERRER);
				personRelationVO.setStartDate(personRelation.getStartDate());
				personRelationVO.setEndDate(null);
				personRelation = personRelationServiceFacade.updateSingle(personRelationVO);

				currentReferrerFld.setVisible(true);
				totalRewardAmount.setVisible(false);
			}

			public void enableAsReferrer() throws FrameworkException {

				if (personRelation == null) {
					PersonRelationVO personRelationVO = new PersonRelationVO();
					personRelationVO.setNew(true);
					personRelationVO.setPersonId(person.getId());
					personRelationVO.setRelationTypeId(ReferralRelationType.REFERRER);
					personRelationVO.setStartDate(LocalDate.now());
					personRelation = personRelationServiceFacade.updateSingle(personRelationVO);
					

					PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade
							.get(UI.getCurrent());
					BigDecimal cumulativeReward = personRewardServiceFacade.getPersonCumulativeReward(person.getId());
					totalRewardAmount.setValue(Constants.DF.format(cumulativeReward));
					
					personRewardServiceFacade.createToken(person.getId());
				} else {
					PersonRelationVO personRelationVO = new PersonRelationVO();
					personRelationVO.setId(personRelation.getId());
					personRelationVO.setPersonId(person.getId());
					personRelationVO.setRelationId(ReferralRelationType.REFERRER);
					personRelationVO.setStartDate(personRelation.getStartDate());
					personRelationVO.setEndDate(null);
					personRelation = personRelationServiceFacade.updateSingle(personRelationVO);

					/*
					 * Deactivate being a friend.
					 */
					personFriendServiceFacade.deactivateAsFriend(person.getId());

					PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade
							.get(UI.getCurrent());
					BigDecimal cumulativeReward = personRewardServiceFacade.getPersonCumulativeReward(person.getId());
					totalRewardAmount.setValue(Constants.DF.format(cumulativeReward));
				}

				currentReferrerFld.setVisible(false);
				totalRewardAmount.setVisible(true);
			}

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean referrerEnabled = (Boolean) toggleReferrerFld.getData();
				if (referrerEnabled) {
					ConfirmWindow confirmWindow = new ConfirmWindow(ReferrerProperty.DISABLE_AS_REFERRER_HEADER,
							ReferrerProperty.DISABLE_AS_REFERRER_CONFIRMATION, SystemProperty.CONFIRM,
							SystemProperty.CANCEL);
					confirmWindow.execute(new ConfirmationHandler() {

						@Override
						public void handlePositive() {
							try {
								disableAsReferrer();
								toggleReferrerFldStyle(false);
								toggleReferrerFld.setData(false);
							} catch (FrameworkException e) {
								logger.error(e.getMessage(), e);
							}
						}

						@Override
						public void handleNegative() {
							// TODO Auto-generated method stub

						}
					});
				} else {
					ConfirmWindow confirmWindow = new ConfirmWindow(ReferrerProperty.ENABLE_AS_REFERRER_HEADER,
							ReferrerProperty.ENABLE_AS_REFERRER_CONFIRMATION, SystemProperty.CONFIRM,
							SystemProperty.CANCEL);
					confirmWindow.execute(new ConfirmationHandler() {

						@Override
						public void handlePositive() {
							try {
								enableAsReferrer();
								toggleReferrerFldStyle(true);
								toggleReferrerFld.setData(true);
							} catch (FrameworkException e) {
								logger.error(e.getMessage(), e);
							}
						}

						@Override
						public void handleNegative() {
							// TODO Auto-generated method stub

						}
					});
				}
			}
		});
	}

}
