package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.angles.schema.angles.VideoStory;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionReviewScreen extends NavigationScreen {
	private VideoStory videoObject;

	public CollectionReviewScreen() {
		super("partial/screen/collection/review.html", JSCollections.<String, EcScreen>$map());
	}

	public CollectionReviewScreen setState(VideoStory videoObject)
	{
		this.videoObject = videoObject;
		return this;
	}

	@Override
	public void display(String containerId) {
		$(containerId).find("video").html("Video not supported.").append("<source/>").children().last()
				.attr("src",((VideoStory)getState()).contentUrl)
				.attr("type","video/mp4");
		final CollectionReviewScreen me = this;
		$("#keywords").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				ScreenManager.changeScreen(new CollectionUploadingScreen().setState((VideoStory) me.getState()),null,null,true);
				return false;
			}
		});
	}

	@Override
	public Object getState() {
		return videoObject;
	}
}
