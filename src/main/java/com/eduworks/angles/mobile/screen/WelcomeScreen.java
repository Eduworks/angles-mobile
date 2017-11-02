package com.eduworks.angles.mobile.screen;

import com.eduworks.angles.mobile.screen.collection.CollectionWelcomeScreen;
import com.eduworks.angles.mobile.screen.presentation.PresentationWelcomeScreen;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;

import static org.stjs.javascript.jquery.GlobalJQuery.$;


/**
 * Created by fray on 6/15/17.
 */
public class WelcomeScreen extends NavigationScreen {

	public WelcomeScreen() {
		super("partial/screen/welcome.html", JSCollections.<String, EcScreen>$map(
				"#collection", new CollectionWelcomeScreen()
				)
		);
	}

	@Override
	public void display(final String containerId) {
		super.display(containerId);

		$("#presentation").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				ScreenManager.changeScreen(
						new PresentationWelcomeScreen(),
						null,
						null,
						true
				);
				return false;
			}
		});
	}

}
