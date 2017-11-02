package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import org.stjs.javascript.JSCollections;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionHelpScreen extends NavigationScreen {
	public CollectionHelpScreen() {
		super("partial/screen/collection/help.html", JSCollections.<String, EcScreen>$map());
	}
}
