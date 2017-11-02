package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import org.stjs.javascript.JSCollections;

/**
 * Created by fray on 6/15/17.
 */
public class CollectionWelcomeScreen extends NavigationScreen {


	public CollectionWelcomeScreen() {
		super("partial/screen/collection/welcome.html",
				JSCollections.<String, EcScreen>$map(
						"#begin", new CollectionHomeScreen()
				)
		);
	}

	@Override
	public void display(final String containerId) {
		super.display(containerId);
	}
}
