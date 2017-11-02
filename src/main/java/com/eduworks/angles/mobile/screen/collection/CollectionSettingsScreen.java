package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import org.stjs.javascript.JSCollections;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionSettingsScreen extends NavigationScreen {
	public CollectionSettingsScreen() {
		super("partial/screen/collection/settings.html", JSCollections.<String, EcScreen>$map());
	}
}
