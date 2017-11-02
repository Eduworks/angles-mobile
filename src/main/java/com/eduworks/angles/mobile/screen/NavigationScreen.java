package com.eduworks.angles.mobile.screen;

import com.eduworks.ec.array.EcObject;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.Map;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.GlobalJQuery;


/**
 * Created by fray on 6/15/17.
 */
public class NavigationScreen extends AnglesScreen {
	Object navMap;
	public String htmlLocation;
	static int i = 0;

	//PRO TIP: Yes, this builds all the screens out to the tippy tip of the application.
	// If you have a loop, and it doesn't work, consider using Global.window.history.back() to back up to where you need to be.
	// Think of the back button!
	public NavigationScreen(String htmlLocation, Map<String,EcScreen> navMap){
		this.navMap = navMap;
		init(htmlLocation);
	}

	protected void init(String htmlLocation) {
		this.htmlLocation = htmlLocation;
		this.displayName = htmlLocation.replace("partial/screen/","").replace(".html","")+i++;
	}

	@Override
	public void display(final String containerId) {
		if (navMap != null) {
			Array<String> keys = EcObject.keys(navMap);
			final NavigationScreen me = this;
			for (int i = 0; i < keys.$length(); i++) {
				hookNavigation(containerId, me, keys.$get(i));
			}
		}
	}

	public void hookNavigation(final String containerId, final NavigationScreen me, final String key) {
		GlobalJQuery.$(key).click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				ScreenManager.changeScreen((EcScreen) JSObjectAdapter.$get(me.navMap, key), null,null,true);
				return false;
			}
		});
	}
	public Object getState(){return new Object();}

	@Override
	public String getHtmlLocation() {
		return htmlLocation;
	}
}
