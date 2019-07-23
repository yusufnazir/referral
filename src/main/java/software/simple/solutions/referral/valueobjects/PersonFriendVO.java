package software.simple.solutions.referral.valueobjects;

import java.time.LocalDateTime;

import software.simple.solutions.framework.core.annotations.FilterFieldProperty;
import software.simple.solutions.framework.core.pojo.DateInterval;
import software.simple.solutions.framework.core.pojo.StringInterval;
import software.simple.solutions.framework.core.valueobjects.SuperVO;
import software.simple.solutions.referral.properties.PersonFriendProperty;

public class PersonFriendVO extends SuperVO {

	private static final long serialVersionUID = -2008276944089219114L;

	private Long id;
	private Long personId;
	private Long friendId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	@FilterFieldProperty(fieldProperty = PersonFriendProperty.FRIEND_FIRST_NAME)
	private StringInterval personFirstName;
	@FilterFieldProperty(fieldProperty = PersonFriendProperty.FRIEND_LAST_NAME)
	private StringInterval personLastName;
	@FilterFieldProperty(fieldProperty = PersonFriendProperty.REFERRER_FIRST_NAME)
	private StringInterval referrerFirstName;
	@FilterFieldProperty(fieldProperty = PersonFriendProperty.REFERRER_LAST_NAME)
	private StringInterval referrerLastName;
	@FilterFieldProperty(fieldProperty = PersonFriendProperty.START_DATE)
	private DateInterval startDateInterval;
	@FilterFieldProperty(fieldProperty = PersonFriendProperty.END_DATE)
	private DateInterval endDateInterval;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getFriendId() {
		return friendId;
	}

	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public StringInterval getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(StringInterval personFirstName) {
		this.personFirstName = personFirstName;
	}

	public StringInterval getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(StringInterval personLastName) {
		this.personLastName = personLastName;
	}

	public StringInterval getReferrerFirstName() {
		return referrerFirstName;
	}

	public void setReferrerFirstName(StringInterval referrerFirstName) {
		this.referrerFirstName = referrerFirstName;
	}

	public StringInterval getReferrerLastName() {
		return referrerLastName;
	}

	public void setReferrerLastName(StringInterval referrerLastName) {
		this.referrerLastName = referrerLastName;
	}

	public DateInterval getStartDateInterval() {
		return startDateInterval;
	}

	public void setStartDateInterval(DateInterval startDateInterval) {
		this.startDateInterval = startDateInterval;
	}

	public DateInterval getEndDateInterval() {
		return endDateInterval;
	}

	public void setEndDateInterval(DateInterval endDateInterval) {
		this.endDateInterval = endDateInterval;
	}

}