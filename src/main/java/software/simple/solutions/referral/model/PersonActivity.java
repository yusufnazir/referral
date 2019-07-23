package software.simple.solutions.referral.model;

import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.referral.entities.ActivityType;

public class PersonActivity {

	private Person person;
	private ActivityType activityType;
	private String date;
	private String amount;
	private String cumulativeAmount;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCumulativeAmount() {
		return cumulativeAmount;
	}

	public void setCumulativeAmount(String cumulativeAmount) {
		this.cumulativeAmount = cumulativeAmount;
	}

}
