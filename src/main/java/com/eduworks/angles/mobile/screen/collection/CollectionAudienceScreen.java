package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.angles.schema.angles.VideoStory;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionAudienceScreen extends NavigationScreen {
	private VideoStory videoStory;

	public CollectionAudienceScreen() {
		super("partial/screen/collection/audience.html", JSCollections.<String, EcScreen>$map());
	}

	public CollectionAudienceScreen setState(VideoStory s) {
		this.videoStory = s;
		return this;
	}

	@Override
	public void display(String containerId) {
		super.display(containerId);
		$("#name").val(videoStory.name);
		$("#description").val(videoStory.description);
		final CollectionAudienceScreen me = this;
		$("#next").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				me.videoStory.name = (String) $("#name").val();
				me.videoStory.description = (String) $("#description").val();
				$("select").each(new Callback2<Integer, Element>() {
					@Override
					public void $invoke(Integer integer, Element element) {
						if ($(element).find("option:selected").attr("value") != null) {
							JSObjectAdapter.$put(
									me.videoStory,
									(String) $(element).attr("field"),
									$(element).find("option:selected").attr("value")
							);
						}
					}
				});
				CollectionCompletedScreen s = new CollectionCompletedScreen().setState((VideoStory) me.getState());
				ScreenManager.changeScreen(s, null, null, true);
				return false;
			}
		});
	}

	@Override
	public Object getState() {
		return videoStory;
	}
}
