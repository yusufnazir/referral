package software.simple.solutions.referral.model;

import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.entities.PersonInformation;

public class FriendModel {

	private Person person;
	private PersonInformation personInformation;

	public FriendModel(Person person, PersonInformation personInformation) {
		this.person = person;
		this.personInformation = personInformation;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PersonInformation getPersonInformation() {
		return personInformation;
	}

	public void setPersonInformation(PersonInformation personInformation) {
		this.personInformation = personInformation;
	}

}
