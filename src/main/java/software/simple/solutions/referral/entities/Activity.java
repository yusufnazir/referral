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

import software.simple.solutions.framework.core.annotations.FilterFieldProperties;
import software.simple.solutions.framework.core.annotations.FilterFieldProperty;
import software.simple.solutions.framework.core.entities.MappedSuperClass;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.properties.PersonProperty;
import software.simple.solutions.referral.constants.ReferralTables;
import software.simple.solutions.referral.properties.ActivityProperty;
import software.simple.solutions.referral.properties.ActivityTypeProperty;

@Audited
@AuditOverride(forClass = MappedSuperClass.class)
@Entity
@Table(name = ReferralTables.ACTIVITIES_.NAME)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
public class Activity extends MappedSuperClass {

	private static final long serialVersionUID = 100873796019490516L;

	@Id
	@TableGenerator(name = "table", table = "sequences_", pkColumnName = "PK_NAME", valueColumnName = "PK_VALUE", initialValue = 1000000)
	@GeneratedValue(generator = "table", strategy = GenerationType.TABLE)
	@Column(name = ID_)
	@FilterFieldProperty(fieldProperty = ActivityProperty.ID)
	private Long id;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVE_)
	private Boolean active;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.PERSON_ID_)
	@FilterFieldProperties(fieldProperties = {
			@FilterFieldProperty(fieldProperty = ActivityProperty.FRIEND_FIRST_NAME, referencedFieldProperty = PersonProperty.FIRST_NAME),
			@FilterFieldProperty(fieldProperty = ActivityProperty.FRIEND_LAST_NAME, referencedFieldProperty = PersonProperty.LAST_NAME) })
	private Person person;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.DATE_OF_ACTIVITY_)
	@FilterFieldProperty(fieldProperty = ActivityProperty.DATE_OF_ACTIVITY)
	private LocalDateTime dateOfActivity;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVITY_TYPE_ID_)
	@FilterFieldProperty(fieldProperty = ActivityTypeProperty.ID)
	private ActivityType activityType;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.REFERRER_PERSON_ID_)
	@FilterFieldProperties(fieldProperties = {
			@FilterFieldProperty(fieldProperty = ActivityProperty.REFERRER_FIRST_NAME, referencedFieldProperty = PersonProperty.FIRST_NAME),
			@FilterFieldProperty(fieldProperty = ActivityProperty.REFERRER_LAST_NAME, referencedFieldProperty = PersonProperty.LAST_NAME) })
	private Person referrerPerson;

	@FilterFieldProperty(fieldProperty = ActivityProperty.ACTIVITY_AMOUNT)
	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVITY_AMOUNT_)
	private BigDecimal activityAmount;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.ACTIVITY_REWARD_AMOUNT_)
	private BigDecimal activityRewardAmount;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.CUMULATIVE_REWARD_AMOUNT_)
	private BigDecimal cumulativeRewardAmount;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.USE_REWARD_)
	private Boolean useReward;

	@Column(name = ReferralTables.ACTIVITIES_.COLUMNS.USED_REWARD_AMOUNT_)
	private BigDecimal usedRewardAmount;

	@ManyToOne
	@JoinColumn(name = ReferralTables.ACTIVITIES_.COLUMNS.MAIN_ACTIVITY_ID_)
	private Activity mainActivity;

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

	public Person getReferrerPerson() {
		return referrerPerson;
	}

	public void setReferrerPerson(Person referrerPerson) {
		this.referrerPerson = referrerPerson;
	}

	public BigDecimal getActivityAmount() {
		return activityAmount;
	}

	public void setActivityAmount(BigDecimal activityAmount) {
		this.activityAmount = activityAmount;
	}

	public BigDecimal getActivityRewardAmount() {
		return activityRewardAmount;
	}

	public void setActivityRewardAmount(BigDecimal activityRewardAmount) {
		this.activityRewardAmount = activityRewardAmount;
	}

	public BigDecimal getCumulativeRewardAmount() {
		return cumulativeRewardAmount;
	}

	public void setCumulativeRewardAmount(BigDecimal cumulativeRewardAmount) {
		this.cumulativeRewardAmount = cumulativeRewardAmount;
	}

	public Boolean getUseReward() {
		return useReward;
	}

	public void setUseReward(Boolean useReward) {
		this.useReward = useReward;
	}

	public BigDecimal getUsedRewardAmount() {
		return usedRewardAmount;
	}

	public void setUsedRewardAmount(BigDecimal usedRewardAmount) {
		this.usedRewardAmount = usedRewardAmount;
	}

	public Activity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(Activity mainActivity) {
		this.mainActivity = mainActivity;
	}

}
