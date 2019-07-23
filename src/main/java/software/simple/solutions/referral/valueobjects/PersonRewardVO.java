package software.simple.solutions.referral.valueobjects;

import java.math.BigDecimal;

import software.simple.solutions.framework.core.valueobjects.SuperVO;

public class PersonRewardVO extends SuperVO {

	private static final long serialVersionUID = -2008276944089219114L;

	private Long id;
	private Long personId;
	private BigDecimal cumulativeReward;

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

	public BigDecimal getCumulativeReward() {
		return cumulativeReward;
	}

	public void setCumulativeReward(BigDecimal cumulativeReward) {
		this.cumulativeReward = cumulativeReward;
	}

}
