package software.simple.solutions.referral.web.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.switchui.Switch;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import software.simple.solutions.framework.core.components.AbstractBaseView;
import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.entities.PersonRelation;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.facade.PersonRelationServiceFacade;
import software.simple.solutions.framework.core.valueobjects.PersonRelationVO;
import software.simple.solutions.referral.constants.ReferralRelationType;
import software.simple.solutions.referral.properties.ReferrerProperty;
import software.simple.solutions.referral.service.facade.PersonRewardServiceFacade;

public class PersonReferrerView extends AbstractBaseView {

	private static final long serialVersionUID = 2155260956785400977L;

	private static final Logger logger = LogManager.getLogger(PersonReferrerView.class);

	private Person person;

	private Switch toggleReferrerFld;
	private Label totalRewardAmount;

	@Override
	public void executeBuild() throws FrameworkException {
		setHeight("100%");
		person = getParentEntity();
		buildMainLayout();
	}

	private void buildMainLayout() {
		CGridLayout gridLayout = new CGridLayout();
		gridLayout.setSpacing(true);
		addComponent(gridLayout);
		toggleReferrerFld = gridLayout.addField(Switch.class, ReferrerProperty.TOGGLE_REFERRER, 0, 0);
		totalRewardAmount = gridLayout.addField(Label.class, ReferrerProperty.TOTAL_REWARD_AMOUNT, 0, 1);

		try {
			setValues();
		} catch (FrameworkException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void setValues() throws FrameworkException {
		PersonRelationServiceFacade personRelationServiceFacade = PersonRelationServiceFacade.get(UI.getCurrent());
		PersonRelation personRelation = personRelationServiceFacade.getByPerson(person.getId(),
				ReferralRelationType.REFERRER);
		if (personRelation != null) {
			toggleReferrerFld.setValue(true);

			PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade.get(UI.getCurrent());
			BigDecimal cumulativeReward = personRewardServiceFacade.getPersonCumulativeReward(person.getId());
			totalRewardAmount.setValue(Constants.DF.format(cumulativeReward));
		}

		toggleReferrerFld.addValueChangeListener(new ValueChangeListener<Boolean>() {

			private static final long serialVersionUID = -159923326817373889L;

			@Override
			public void valueChange(ValueChangeEvent<Boolean> event) {
				Boolean selected = event.getValue();
				if (selected) {
					if (personRelation == null) {
						PersonRelationVO personRelationVO = new PersonRelationVO();
						personRelationVO.setNew(true);
						personRelationVO.setPersonId(person.getId());
						personRelationVO.setRelationTypeId(ReferralRelationType.REFERRER);
						personRelationVO.setStartDate(LocalDate.now());
						try {
							personRelationServiceFacade.updateSingle(personRelationVO);

							PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade
									.get(UI.getCurrent());
							BigDecimal cumulativeReward = personRewardServiceFacade
									.getPersonCumulativeReward(person.getId());
							totalRewardAmount.setValue(Constants.DF.format(cumulativeReward));
						} catch (FrameworkException e) {
							logger.error(e.getMessage(), e);
						}
					} else {
						PersonRelationVO personRelationVO = new PersonRelationVO();
						personRelationVO.setId(personRelation.getId());
						personRelationVO.setPersonId(person.getId());
						personRelationVO.setRelationId(ReferralRelationType.REFERRER);
						personRelationVO.setStartDate(personRelation.getStartDate());
						personRelationVO.setEndDate(null);
						try {
							personRelationServiceFacade.updateSingle(personRelationVO);

							PersonRewardServiceFacade personRewardServiceFacade = PersonRewardServiceFacade
									.get(UI.getCurrent());
							BigDecimal cumulativeReward = personRewardServiceFacade
									.getPersonCumulativeReward(person.getId());
							totalRewardAmount.setValue(Constants.DF.format(cumulativeReward));
						} catch (FrameworkException e) {
							logger.error(e.getMessage(), e);
						}
					}
				} else {
					PersonRelationVO personRelationVO = new PersonRelationVO();
					personRelationVO.setId(personRelation.getId());
					personRelationVO.setPersonId(person.getId());
					personRelationVO.setRelationId(ReferralRelationType.REFERRER);
					personRelationVO.setStartDate(personRelation.getStartDate());
					personRelationVO.setEndDate(null);
					try {
						personRelationServiceFacade.updateSingle(personRelationVO);
					} catch (FrameworkException e) {
						logger.error(e.getMessage(), e);
					}
				}

			}
		});

	}

}
