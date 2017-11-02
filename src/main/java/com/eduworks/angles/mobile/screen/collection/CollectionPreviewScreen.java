package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.AppController;
import com.eduworks.angles.mobile.screen.Native;
import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.angles.schema.angles.VideoStory;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionPreviewScreen extends NavigationScreen {
	public CollectionPreviewScreen() {
		super("partial/screen/collection/preview.html", JSCollections.<String, EcScreen>$map(
				)
		);
	}

	@Override
	public void display(String containerId) {
		super.display(containerId);
		$("#record").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				Native.captureVideo(new Callback1<Array<Object>>() {
					@Override
					public void $invoke(Array<Object> result) {
						VideoStory o = new VideoStory();
						o.generateId(AppController.serverController.getRepoInterface().selectedServer);
						o.addOwner(AppController.identityController.selectedIdentity.ppk.toPk());
						o.contentUrl = (String)JSObjectAdapter.$get(result.$get(0),"fullPath");
						ScreenManager.changeScreen(new CollectionReviewScreen().setState(o), null, null,true);
					}
				}, new Callback1<String>() {
					@Override
					public void $invoke(String s) {
						Global.alert((String)s);
					}
				});
				return false;
			}
		});
	}
}
