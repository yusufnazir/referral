package software.simple.solutions.referral.entities;

import java.math.BigDecimal;
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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import software.simple.solutions.framework.core.entities.MappedSuperClass;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.referral.constants.ReferralTables;

@Audited
@AuditOverride(forClass = MappedSuperClass.class)
@Entity
@Table(name = ReferralTables.ACTIVITIES_.NAME)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
public class Activity extends MappedSuperClass {

	@Id
	@TableGenerator(name = "table", table = "sequences_", pkColumnName = "PK_NAME", valueColumnName = "PK_VALUE", initialValue = 1000000)
	@GeneratedValue(generator = "table", strategy = GenerationType.TABLE)
	@Column(name = ID_)
	private Long id;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVE_)
	private Boolean active;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.PERSON_ID_)
	private Person person;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.DATE_OF_ACTIVITY_)
	private LocalDateTime dateOfActivity;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVITY_TYPE_ID_)
	private ActivityType activityType;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.REFERRER_PERSON_ID_)
	private Person referrerPersonId;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVITY_AMOUNT_)
	private BigDecimal activityAmount;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.REFERRER_ACTIVITY_REWARD_)
	private BigDecimal referrerActivityReward;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.REFERRER_REWARD_AMOUNT_)
	private BigDecimal referrerRewardAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public LocalDateTime getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(LocalDateTime dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public Person getReferrerPersonId() {
		return referrerPersonId;
	}

	public void setReferrerPersonId(Person referrerPersonId) {
		this.referrerPersonId = referrerPersonId;
	}

	public BigDecimal getActivityAmount() {
		return activityAmount;
	}

	public void setActivityAmount(BigDecimal activityAmount) {
		this.activityAmount = activityAmount;
	}

	public BigDecimal getReferrerActivityReward() {
		return referrerActivityReward;
	}

	public void setReferrerActivityReward(BigDecimal referrerActivityReward) {
		this.referrerActivityReward = referrerActivityReward;
	}

	public BigDecimal getReferrerRewardAmount() {
		return referrerRewardAmount;
	}

	public void setReferrerRewardAmount(BigDecimal referrerRewardAmount) {
		this.referrerRewardAmount = referrerRewardAmount;
	}

}
