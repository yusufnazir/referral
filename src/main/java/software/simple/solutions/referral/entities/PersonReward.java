package software.simple.solutions.referral.entities;

import java.math.BigDecimal;

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
@Table(name = ReferralTables.PERSON_REWARDS_.NAME)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
public class PersonReward extends MappedSuperClass {

	private static final long serialVersionUID = -1557214385825706822L;

	@Id
	@TableGenerator(name = "table", table = "sequences_", pkColumnName = "PK_NAME", valueColumnName = "PK_VALUE", initialValue = 1000000)
	@GeneratedValue(generator = "table", strategy = GenerationType.TABLE)
	@Column(name = ID_)
	private Long id;

	@ManyToOne
	@JoinColumn(name = ReferralTables.PERSON_REWARDS_.COLUMNS.PERSON_)
	private Person person;

	@Column(name = ReferralTables.PERSON_REWARDS_.COLUMNS.CUMULATIVE_REWARD_)
	private BigDecimal cumulativeReward;

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

	public BigDecimal getCumulativeReward() {
		return cumulativeReward;
	}

	public void setCumulativeReward(BigDecimal cumulativeReward) {
		this.cumulativeReward = cumulativeReward;
	}

	@Override
	public Boolean getActive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActive(Boolean active) {
		// TODO Auto-generated method stub

	}

}
