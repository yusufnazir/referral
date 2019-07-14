package software.simple.solutions.referral.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import software.simple.solutions.framework.core.entities.MappedSuperClass;
import software.simple.solutions.referral.constants.ReferralTables;

@Audited
@AuditOverride(forClass = MappedSuperClass.class)
@Entity
@Table(name = ReferralTables.ACTIVITY_TYPES_.NAME)
@DynamicUpdate(value = true)
@SelectBeforeUpdate(value = true)
public class ActivityType extends MappedSuperClass {

	private static final long serialVersionUID = -467898040854735933L;

	@Id
	@TableGenerator(name = "table", table = "sequences_", pkColumnName = "PK_NAME", valueColumnName = "PK_VALUE", initialValue = 1000000)
	@GeneratedValue(generator = "table", strategy = GenerationType.TABLE)
	@Column(name = ID_)
	private Long id;

	@Column(name = ReferralTables.ACTIVITY_TYPES_.COLUMNS.ACTIVE_)
	private Boolean active;

	@Column(name = ReferralTables.ACTIVITY_TYPES_.COLUMNS.CODE_)
	private String code;

	@Column(name = ReferralTables.ACTIVITY_TYPES_.COLUMNS.NAME_)
	private String name;

	@Column(name = ReferralTables.ACTIVITY_TYPES_.COLUMNS.DESCRIPTION_)
	private String description;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
