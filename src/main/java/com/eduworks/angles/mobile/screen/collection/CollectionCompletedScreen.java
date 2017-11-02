package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.angles.schema.angles.VideoStory;
import org.cassproject.ebac.repository.EcRepository;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionCompletedScreen extends NavigationScreen {
	public CollectionCompletedScreen() {
		super("partial/screen/collection/completed.html", JSCollections.<String, EcScreen>$map());
	}

	VideoStory videoStory = null;

	@Override
	public void display(String containerId) {
		super.display(containerId);
		CollectionCompletedScreen me = this;
		EcRepository._save((VideoStory)getState(), new Callback1<String>() {
			@Override
			public void $invoke(String s) {
			}
		}, new Callback1<String>() {
			@Override
			public void $invoke(String s) {
				$(".lead").text(s);
			}
		});
		$("#home").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
			ScreenManager.loadHistoryScreen("welcome");
			return false;
			}
		});
	}

	public CollectionCompletedScreen setState(VideoStory s) {
		this.videoStory = s;
		return this;
	}

	@Override
	public Object getState() {
		return videoStory;
	}
}
