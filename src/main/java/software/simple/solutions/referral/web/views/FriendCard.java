package software.simple.solutions.referral.web.views;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import software.simple.solutions.framework.core.constants.FileReference;
import software.simple.solutions.framework.core.constants.ReferenceKey;
import software.simple.solutions.framework.core.entities.EntityFile;
import software.simple.solutions.framework.core.entities.Person;
import software.simple.solutions.framework.core.entities.PersonInformation;
import software.simple.solutions.framework.core.exceptions.FrameworkException;
import software.simple.solutions.framework.core.service.IFileService;
import software.simple.solutions.framework.core.util.ContextProvider;
import software.simple.solutions.referral.model.FriendModel;

public class FriendCard extends HorizontalLayout {

	private static final long serialVersionUID = 7057954939905170982L;

	private static final Logger logger = LogManager.getLogger(FriendCard.class);

	private Image image;
	private VerticalLayout verticalLayout;
	private Label nameFld;
	private Label emailFld;

	public FriendCard(FriendModel friendModel) {
		Person person = friendModel.getPerson();
		PersonInformation personInformation = friendModel.getPersonInformation();
		setSizeFull();
		addStyleName(ValoTheme.LAYOUT_CARD);
		image = new Image();
		image.setHeight("75px");
		image.setWidth("75px");

		try {
			IFileService fileService = ContextProvider.getBean(IFileService.class);
			EntityFile entityFile = fileService.findFileByEntityAndType(person.getId().toString(), ReferenceKey.PERSON,
					FileReference.USER_PROFILE_IMAGE);

			if (entityFile != null) {
				image.setSource(new StreamResource(new StreamSource() {

					private static final long serialVersionUID = -9150451917237177393L;

					@Override
					public InputStream getStream() {
						if (entityFile == null || entityFile.getFileObject() == null) {
							return null;
						}
						return new ByteArrayInputStream(entityFile.getFileObject());
					}
				}, entityFile.getName()));
			} else {
				image.setSource(new ThemeResource("img/header-logo.jpg"));
			}
		} catch (FrameworkException e) {
			logger.error(e.getMessage(), e);
		}

		image.addStyleName("appbar-profile-image");
		addComponent(image);

		verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(false);
		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1);
		setComponentAlignment(verticalLayout, Alignment.MIDDLE_LEFT);
		nameFld = new Label();
		nameFld.setValue(person.getFirstName() + " " + person.getLastName());
		verticalLayout.addComponent(nameFld);

		emailFld = new Label();
		emailFld.setValue(personInformation == null ? null : personInformation.getPrimaryEmail());
		verticalLayout.addComponent(emailFld);

	}
}
