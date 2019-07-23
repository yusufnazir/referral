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

import software.simple.solutions.framework.core.entities.MappedSuperClass;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.referral.constants.ReferralTables;

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
	private Person person;

	@ManyToOne
	@JoinColumn(name = ReferralTables.PERSON_FRIENDS_.COLUMNS.FRIEND_ID_)
	private Person friend;

	@Column(name = ReferralTables.PERSON_FRIENDS_.COLUMNS.START_DATE_)
	private LocalDateTime startDate;

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
