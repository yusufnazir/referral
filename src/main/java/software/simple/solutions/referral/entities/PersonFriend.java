package software.simple.solutions.referral.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import software.simple.solutions.framework.core.annotations.FilterFieldProperties;
import software.simple.solutions.framework.core.annotations.FilterFieldProperty;
import software.simple.solutions.framework.core.entities.MappedSuperClass;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.properties.PersonProperty;
import software.simple.solutions.referral.constants.ReferralTables;
import software.simple.solutions.referral.properties.PersonFriendProperty;

@Audited
@AuditOverride(forClass = MappedSuperClass.class)
@Entity
@Table(name = ReferralTables.PERSON_FRIENDS_.NAME)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
public class PersonFriend extends MappedSuperClass {

	private static final long serialVersionUID = -1557214385825706822L;

	@Id
	@TableGenerator(name = "table", table = "sequences_", pkColumnName = "PK_NAME", valueColumnName = "PK_VALUE", initialValue = 1000000)
	@GeneratedValue(generator = "table", strategy = GenerationType.TABLE)
	@Column(name = ID_)
	private Long id;

	@ManyToOne
	@JoinColumn(name = ReferralTables.PERSON_FRIENDS_.COLUMNS.PERSON_ID_)
	@FilterFieldProperties(fieldProperties = {
			@FilterFieldProperty(fieldProperty = PersonFriendProperty.PERSON, referencedFieldProperty = PersonProperty.ID),
			@FilterFieldProperty(fieldProperty = PersonFriendProperty.PERSON_FIRST_NAME, referencedFieldProperty = PersonProperty.FIRST_NAME),
			@FilterFieldProperty(fieldProperty = PersonFriendProperty.PERSON_LAST_NAME, referencedFieldProperty = PersonProperty.LAST_NAME) })
	private Person person;

	@ManyToOne
	@JoinColumn(name = ReferralTables.PERSON_FRIENDS_.COLUMNS.FRIEND_ID_)
	@FilterFieldProperties(fieldProperties = {
			@FilterFieldProperty(fieldProperty = PersonFriendProperty.FRIEND, referencedFieldProperty = PersonProperty.ID),
			@FilterFieldProperty(fieldProperty = PersonFriendProperty.FRIEND_FIRST_NAME, referencedFieldProperty = PersonProperty.FIRST_NAME),
			@FilterFieldProperty(fieldProperty = PersonFriendProperty.FRIEND_LAST_NAME, referencedFieldProperty = PersonProperty.LAST_NAME) })
	private Person friend;

	@FilterFieldProperty(fieldProperty = PersonFriendProperty.START_DATE)
	@Column(name = ReferralTables.PERSON_FRIENDS_.COLUMNS.START_DATE_)
	private LocalDateTime startDate;

	@FilterFieldProperty(fieldProperty = PersonFriendProperty.END_DATE)
	@Column(name = ReferralTables.PERSON_FRIENDS_.COLUMNS.END_DATE_)
	private LocalDateTime endDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getFriend() {
		return friend;
	}

	public void setFriend(Person friend) {
		this.friend = friend;
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

	public Boolean getActive() {
		return endDate == null;
	}

	public void setActive(Boolean active) {
	}

}
