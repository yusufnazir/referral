package software.simple.solutions.referral.valueobjects;

import software.simple.solutions.framework.core.annotations.FilterFieldProperty;
import software.simple.solutions.framework.core.pojo.StringInterval;
import software.simple.solutions.framework.core.valueobjects.SuperVO;
import software.simple.solutions.referral.properties.ActivityTypeProperty;

public class ActivityTypeVO extends SuperVO {

	private static final long serialVersionUID = -2008276944089219114L;

	@FilterFieldProperty(fieldProperty = ActivityTypeProperty.ID)
	private Long id;
	private Boolean active;
	private String code;
	private String name;
	private String description;

	@FilterFieldProperty(fieldProperty = ActivityTypeProperty.CODE)
	private StringInterval codeInterval;
	@FilterFieldProperty(fieldProperty = ActivityTypeProperty.NAME)
	private StringInterval nameInterval;
	@FilterFieldProperty(fieldProperty = ActivityTypeProperty.DESCRIPTION)
	private StringInterval descriptionInterval;

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

	public StringInterval getNameInterval() {
		return nameInterval;
	}

	public void setNameInterval(StringInterval nameInterval) {
		this.nameInterval = nameInterval;
	}

	public StringInterval getDescriptionInterval() {
		return descriptionInterval;
	}

	public void setDescriptionInterval(StringInterval descriptionInterval) {
		this.descriptionInterval = descriptionInterval;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public StringInterval getCodeInterval() {
		return codeInterval;
	}

	public void setCodeInterval(StringInterval codeInterval) {
		this.codeInterval = codeInterval;
	}

}
