package software.simple.solutions.referral.web.views;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FriendCard extends HorizontalLayout {

	private static final long serialVersionUID = 7057954939905170982L;
	private Image image;
	private VerticalLayout verticalLayout;
	private Label nameFld;
	private Label emailFld;

	public FriendCard() {
		setSizeFull();
		addStyleName(ValoTheme.LAYOUT_CARD);
		image = new Image();
		image.setHeight("75px");
		image.setWidth("75px");
		image.setSource(new ThemeResource("img/header-logo.jpg"));
		image.addStyleName("appbar-profile-image");
		addComponent(image);

		verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(false);
		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1);
		setComponentAlignment(verticalLayout, Alignment.MIDDLE_LEFT);
		nameFld = new Label();
		nameFld.setValue("Name");
		verticalLayout.addComponent(nameFld);

		emailFld = new Label();
		emailFld.setValue("Email");
		verticalLayout.addComponent(emailFld);

	}
}
