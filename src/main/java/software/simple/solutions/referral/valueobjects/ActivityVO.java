package software.simple.solutions.referral.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import software.simple.solutions.framework.core.annotations.FilterFieldProperty;
import software.simple.solutions.framework.core.pojo.DateInterval;
import software.simple.solutions.framework.core.pojo.DecimalInterval;
import software.simple.solutions.framework.core.pojo.StringInterval;
import software.simple.solutions.framework.core.valueobjects.SuperVO;
import software.simple.solutions.referral.properties.ActivityProperty;
import software.simple.solutions.referral.properties.ActivityTypeProperty;

public class ActivityVO extends SuperVO {

	private static final long serialVersionUID = -2008276944089219114L;

	@FilterFieldProperty(fieldProperty = ActivityTypeProperty.ID)
	private Long id;
	private Boolean active;
	private Long personId;
	private Long referrerId;
	private LocalDateTime dateOfActivity;
	private BigDecimal activityRewardAmount;
	private BigDecimal activityAmount;
	private BigDecimal cumulativeRewardAmount;
	private Boolean useReward = false;
	private BigDecimal usedReward;

	@FilterFieldProperty(fieldProperty = ActivityProperty.FRIEND_FIRST_NAME)
	private StringInterval personFirstName;
	@FilterFieldProperty(fieldProperty = ActivityProperty.FRIEND_LAST_NAME)
	private StringInterval personLastName;
	@FilterFieldProperty(fieldProperty = ActivityProperty.REFERRER_FIRST_NAME)
	private StringInterval referrerFirstName;
	@FilterFieldProperty(fieldProperty = ActivityProperty.REFERRER_LAST_NAME)
	private StringInterval referrerLastName;
	@FilterFieldProperty(fieldProperty = ActivityProperty.DATE_OF_ACTIVITY)
	private DateInterval dateOfActivityInterval;
	@FilterFieldProperty(fieldProperty = ActivityProperty.ACTIVITY_TYPE_ID)
	private Long activityTypeId;
	@FilterFieldProperty(fieldProperty = ActivityProperty.ACTIVITY_AMOUNT)
	private DecimalInterval activityAmountInterval;

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

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getReferrerId() {
		return referrerId;
	}

	public void setReferrerId(Long referrerId) {
		this.referrerId = referrerId;
	}

	public LocalDateTime getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(LocalDateTime dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}

	public BigDecimal getActivityRewardAmount() {
		return activityRewardAmount;
	}

	public void setActivityRewardAmount(BigDecimal activityRewardAmount) {
		this.activityRewardAmount = activityRewardAmount;
	}

	public BigDecimal getActivityAmount() {
		return activityAmount;
	}

	public void setActivityAmount(BigDecimal activityAmount) {
		this.activityAmount = activityAmount;
	}

	public BigDecimal getCumulativeRewardAmount() {
		return cumulativeRewardAmount;
	}

	public void setCumulativeRewardAmount(BigDecimal cumulativeRewardAmount) {
		this.cumulativeRewardAmount = cumulativeRewardAmount;
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

	public DateInterval getDateOfActivityInterval() {
		return dateOfActivityInterval;
	}

	public void setDateOfActivityInterval(DateInterval dateOfActivityInterval) {
		this.dateOfActivityInterval = dateOfActivityInterval;
	}

	public Long getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Long activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public DecimalInterval getActivityAmountInterval() {
		return activityAmountInterval;
	}

	public void setActivityAmountInterval(DecimalInterval activityAmountInterval) {
		this.activityAmountInterval = activityAmountInterval;
	}

	public Boolean getUseReward() {
		return useReward;
	}

	public void setUseReward(Boolean useReward) {
		this.useReward = useReward;
	}

	public BigDecimal getUsedReward() {
		return usedReward;
	}

	public void setUsedReward(BigDecimal usedReward) {
		this.usedReward = usedReward;
	}

}
