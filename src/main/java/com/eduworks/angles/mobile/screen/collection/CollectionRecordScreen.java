package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.GlobalJQuery;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionRecordScreen extends NavigationScreen {
	public CollectionRecordScreen() {
		super("partial/screen/collection/record.html", JSCollections.<String, EcScreen>$map());
	}

	@Override
	public void display(String containerId) {
		super.display(containerId);
		final CollectionRecordScreen me = this;
		GlobalJQuery.$("#review").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				ScreenManager.replaceScreen(new CollectionReviewScreen(), null, me.getResults());
				return true;
			}
		});
	}

	public Object getResults() {
		Object results = new Object();
		return results;
	}
}
