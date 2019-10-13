package software.simple.solutions.referral.config;

import java.util.ArrayList;
import java.util.List;

import software.simple.solutions.framework.core.annotations.MailPlaceholders;
import software.simple.solutions.framework.core.config.MailTemplateGroup;
import software.simple.solutions.framework.core.config.mail.MailTemplatePlaceholder;
import software.simple.solutions.framework.core.constants.MailTemplatePlaceholderItem;
import software.simple.solutions.referral.properties.PersonFriendProperty;

@MailPlaceholders
public class MailTemplatePlaceholderGroupPersonFriend implements MailTemplatePlaceholder {
	public static final String MAIL_TEMPLATE_GROUP_PERSON_FRIEND = "mail.template.group.person.friend";

	public static final String PERSON_FRIEND_TOKEN = MailTemplateGroup.PREFIX + PersonFriendProperty.TOKEN;
	public static final String PERSON_FRIEND_PERSON = MailTemplateGroup.PREFIX + PersonFriendProperty.PERSON;

	@Override
	public List<MailTemplatePlaceholderItem> getItems() {
		List<MailTemplatePlaceholderItem> items = new ArrayList<MailTemplatePlaceholderItem>();
		items.add(new MailTemplatePlaceholderItem(MAIL_TEMPLATE_GROUP_PERSON_FRIEND, PERSON_FRIEND_TOKEN));
		items.add(new MailTemplatePlaceholderItem(MAIL_TEMPLATE_GROUP_PERSON_FRIEND, PERSON_FRIEND_PERSON));
		return items;
	}
}
