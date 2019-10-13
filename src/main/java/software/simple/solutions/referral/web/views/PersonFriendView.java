package software.simple.solutions.referral.web.views;

import java.util.UUID;

import com.vaadin.data.ValueProvider;

import software.simple.solutions.framework.core.components.CGridLayout;
import software.simple.solutions.framework.core.components.CPopupDateField;
import software.simple.solutions.framework.core.components.CTextField;
import software.simple.solutions.framework.core.components.FilterView;
import software.simple.solutions.framework.core.components.FormView;
import software.simple.solutions.framework.core.components.filter.CDateIntervalLayout;
import software.simple.solutions.framework.core.components.filter.CStringIntervalLayout;
import software.simple.solutions.framework.core.constants.Constants;
import software.simple.solutions.framework.core.constants.ReferenceKey;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.util.ColumnSort;
import software.simple.solutions.framework.core.util.ComponentUtil;
import software.simple.solutions.framework.core.util.SortingHelper;
import software.simple.solutions.framework.core.web.BasicTemplate;
import software.simple.solutions.framework.core.web.lookup.PersonLookUpField;
import software.simple.solutions.referral.constants.ReferralReferenceKey;
import software.simple.solutions.referral.entities.PersonFriend;
import software.simple.solutions.referral.properties.ActivityTypeProperty;
import software.simple.solutions.referral.properties.PersonFriendProperty;
import software.simple.solutions.referral.service.facade.PersonFriendServiceFacade;
import software.simple.solutions.referral.valueobjects.PersonFriendVO;

public class PersonFriendView extends BasicTemplate<PersonFriend> {

	private static final long serialVersionUID = 6503015064562511801L;

	public PersonFriendView() {
		setEntityClass(PersonFriend.class);
		setServiceClass(PersonFriendServiceFacade.class);
		setFilterClass(Filter.class);
		setFormClass(Form.class);
		setEntityReferenceKey(ReferralReferenceKey.PERSON_FRIEND);
	}

	@Override
	public boolean isSubMenuValid() {
		Object referenceKey = getReferenceKey(ReferralReferenceKey.PERSON);
		return referenceKey != null;
	}

	@Override
	public void setUpCustomColumns() {
		addContainerProperty(new ValueProvider<PersonFriend, String>() {

			private static final long serialVersionUID = -5627063079895357896L;

			@Override
			public String apply(PersonFriend source) {
				if (source.getPerson() == null) {
					return null;
				}
				return source.getPerson().getCaption();
			}
		}, PersonFriendProperty.PERSON);
		addContainerProperty(new ValueProvider<PersonFriend, String>() {

			private static final long serialVersionUID = 5680019050510937128L;

			@Override
			public String apply(PersonFriend source) {
				if (source.getFriend() == null) {
					return null;
				}
				return source.getFriend().getCaption();
			}
		}, PersonFriendProperty.FRIEND);
		addContainerProperty(new ValueProvider<PersonFriend, String>() {

			private static final long serialVersionUID = -4612378682823533771L;

			@Override
			public String apply(PersonFriend source) {
				if (source.getStartDate() == null) {
					return null;
				}
				return source.getStartDate().format(Constants.DATE_FORMATTER);
			}
		}, PersonFriendProperty.START_DATE);
		addContainerProperty(new ValueProvider<PersonFriend, String>() {

			private static final long serialVersionUID = 1660886503877071193L;

			@Override
			public String apply(PersonFriend source) {
				if (source.getEndDate() == null) {
					return null;
				}
				return source.getEndDate().format(Constants.DATE_FORMATTER);
			}
		}, PersonFriendProperty.END_DATE);
	}

	/**
	 * Filter implementation
	 * 
	 * @author yusuf
	 *
	 */
	public class Filter extends FilterView {

		private static final long serialVersionUID = 117780868467918033L;

		private CStringIntervalLayout personFirstNameFld;
		private CStringIntervalLayout personLastNameFld;
		private CStringIntervalLayout friendFirstNameFld;
		private CStringIntervalLayout friendLastNameFld;

		private CDateIntervalLayout startDateFld;
		private CDateIntervalLayout endDateFld;

		@Override
		public void executeBuild() {
			Person person = getReferenceKey(ReferenceKey.PERSON);
			personFirstNameFld = addField(CStringIntervalLayout.class, PersonFriendProperty.PERSON_FIRST_NAME, 0, 0);
			personLastNameFld = addField(CStringIntervalLayout.class, PersonFriendProperty.PERSON_LAST_NAME, 0, 1);
			if (person != null) {
				personFirstNameFld.setValue(person.getFirstName());
				personFirstNameFld.setReadOnly(true);
				personLastNameFld.setValue(person.getLastName());
				personLastNameFld.setReadOnly(true);
			}

			friendFirstNameFld = addField(CStringIntervalLayout.class, PersonFriendProperty.FRIEND_FIRST_NAME, 0, 2);
			friendLastNameFld = addField(CStringIntervalLayout.class, PersonFriendProperty.FRIEND_LAST_NAME, 0, 3);

			startDateFld = addField(CDateIntervalLayout.class, PersonFriendProperty.START_DATE, 1, 0);
			endDateFld = addField(CDateIntervalLayout.class, PersonFriendProperty.END_DATE, 1, 1);
		}

		@Override
		public Object getCriteria() {
			PersonFriendVO vo = new PersonFriendVO();
			Person person = getReferenceKey(ReferenceKey.PERSON);
			vo.setPersonId(person == null ? null : person.getId());
			vo.setPersonFirstName(personFirstNameFld.getValue());
			vo.setPersonLastName(personLastNameFld.getValue());
			vo.setReferrerFirstName(friendFirstNameFld.getValue());
			vo.setReferrerLastName(friendLastNameFld.getValue());
			vo.setStartDateInterval(startDateFld.getValue());
			vo.setEndDateInterval(endDateFld.getValue());
			SortingHelper sortingHelper = new SortingHelper();
			sortingHelper.addColumnSort(new ColumnSort(ActivityTypeProperty.ID, SortingHelper.DESCENDING));
			vo.setSortingHelper(sortingHelper);
			return vo;
		}
	}

	public class Form extends FormView {

		private static final long serialVersionUID = 6109727427163734676L;

		private CGridLayout formGrid;
		private PersonLookUpField personFld;
		private PersonLookUpField friendFld;
		private CPopupDateField startDateFld;
		private CPopupDateField endDateFld;
		private CTextField tokenFld;

		private PersonFriend personFriend;

		@Override
		public void executeBuild() {
			formGrid = ComponentUtil.createGrid();
			addComponent(formGrid);

			personFld = formGrid.addField(PersonLookUpField.class, PersonFriendProperty.PERSON, 0, 0);
			personFld.setRequiredIndicatorVisible(true);
			personFld.handleForParentEntity(getReferenceKey(ReferenceKey.PERSON));

			friendFld = formGrid.addField(PersonLookUpField.class, PersonFriendProperty.FRIEND, 0, 1);
			friendFld.setRequiredIndicatorVisible(true);

			tokenFld = formGrid.addField(CTextField.class, PersonFriendProperty.TOKEN, 0, 2);
			tokenFld.setRequiredIndicatorVisible(true);

			startDateFld = formGrid.addField(CPopupDateField.class, PersonFriendProperty.START_DATE, 1, 0);
			startDateFld.setRequiredIndicatorVisible(true);

			endDateFld = formGrid.addField(CPopupDateField.class, PersonFriendProperty.END_DATE, 1, 1);
		}

		@Override
		public void handleNewForm() throws FrameworkException {
			tokenFld.setReadOnly(true);
			tokenFld.setValue(UUID.randomUUID().toString());
		}

		@SuppressWarnings("unchecked")
		@Override
		public PersonFriend setFormValues(Object entity) throws FrameworkException {
			personFriend = (PersonFriend) entity;
			personFld.setValue(personFriend.getPerson());
			friendFld.setValue(personFriend.getFriend());
			startDateFld
					.setValue(personFriend.getStartDate() == null ? null : personFriend.getStartDate().toLocalDate());
			endDateFld.setValue(personFriend.getEndDate() == null ? null : personFriend.getEndDate().toLocalDate());
			tokenFld.setValue(personFriend.getToken());
			return personFriend;
		}

		@Override
		public Object getFormValues() throws FrameworkException {
			PersonFriendVO vo = new PersonFriendVO();

			vo.setId(personFriend == null ? null : personFriend.getId());
			vo.setPersonId(personFld.getItemId());
			vo.setFriendId(friendFld.getItemId());
			vo.setStartDate(startDateFld.getValue() == null ? null : startDateFld.getValue().atStartOfDay());
			vo.setEndDate(endDateFld.getValue() == null ? null : endDateFld.getValue().atStartOfDay());
			vo.setToken(tokenFld.getValue());
			return vo;
		}
	}

}
