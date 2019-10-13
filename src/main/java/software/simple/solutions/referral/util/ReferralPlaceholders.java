package software.simple.solutions.referral.util;

import software.simple.solutions.framework.core.util.Placeholders;
import software.simple.solutions.referral.config.MailTemplatePlaceholderGroupPersonFriend;
import software.simple.solutions.referral.entities.PersonFriend;

public class ReferralPlaceholders extends Placeholders {

	public static ReferralPlaceholders build() {
		return new ReferralPlaceholders();
	}

	public Placeholders addPersonFriend(PersonFriend personFriend) {
		add(MailTemplatePlaceholderGroupPersonFriend.PERSON_FRIEND_TOKEN, personFriend.getToken());
		add(MailTemplatePlaceholderGroupPersonFriend.PERSON_FRIEND_PERSON,
				personFriend.getPerson().getFirstName() + " " + personFriend.getPerson().getLastName());
		return this;
	}
}
