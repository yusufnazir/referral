package software.simple.solutions.referral.crm.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;

import software.simple.solutions.framework.core.ui.FrameworkUI;
import software.simple.solutions.referral.crm.constants.ReferralTheme;

@SpringUI(path = "/app/*")
@PushStateNavigation // proper url paths
@Push(transport = Transport.WEBSOCKET_XHR)
@PreserveOnRefresh
@SpringViewDisplay
@Theme(value = ReferralTheme.REFERRAL)
@Widgetset("com.simple.solutions.framework.widgetset.AppWidgetSet")
public class ReferralUI extends FrameworkUI implements ViewDisplay {

	private static final long serialVersionUID = 4740048384227503876L;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		super.init(vaadinRequest);
	}

	@WebServlet(value = "/app/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ReferralUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	public void showView(View view) {

	}

}
