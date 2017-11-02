package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.array.EcArray;
import com.eduworks.ec.framework.browser.url.URLParams;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.angles.schema.angles.VideoStory;
import org.cass.competency.EcCompetency;
import org.schema.AlignmentObject;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionKeywordsScreen extends NavigationScreen {
	public CollectionKeywordsScreen() {
		super("partial/screen/collection/keywords.html", JSCollections.<String, EcScreen>$map());
	}

	VideoStory videoStory = null;

	public CollectionKeywordsScreen setState(VideoStory o) {
		videoStory = o;
		if (videoStory.keywords == null)
			videoStory.keywords = (String) (Object) new Array<String>();
		return this;
	}

	@Override
	public Boolean onClose() {
		return super.onClose();
	}

	@Override
	public void display(String containerId) {
		final CollectionKeywordsScreen me = this;
		String keywordsParams = URLParams.get("keywords");
		for (int i = 0; i < ((Array<String>) (Object) (videoStory.keywords)).$length(); i++)
			$("#keywords").append("<div class='keyword'></div>").children().last().text(((Array<String>) (Object) videoStory.keywords).$get(i)).click(new EventHandler() {
				@Override
				public boolean onEvent(Event ev, Element THIS) {
					EcArray.setRemove((Array<String>) (Object) me.videoStory.keywords, $(THIS).text());
					$(THIS).remove();
					return false;
				}
			});

		super.display(containerId);

		$("#addKeyword").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				String val = (String) $("#keyword").val();
				$("#keywords").append("<div class='keyword'></div>").children().last().text(val);
				((Array<String>) (Object) (me.videoStory.keywords)).push(val);
				$("#keyword").val("");
				return false;
			}
		});

		$("#next").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				ScreenManager.changeScreen(new CollectionAudienceScreen().setState((VideoStory) me.getState()), null, null, true);
				return false;
			}
		});

		if (videoStory.educationalAlignment != null) {
			Array<AlignmentObject> ea = new Array<AlignmentObject>();
			if (EcArray.isArray(videoStory.educationalAlignment) == false) {
				ea.push(videoStory.educationalAlignment);
				videoStory.educationalAlignment = (AlignmentObject) (Object) ea;
			}
			ea = (Array<AlignmentObject>) (Object) videoStory.educationalAlignment;
			for (int i = 0; i < ea.$length(); i++) {
				createCompetencyTag(ea, ea.$get(i));
			}
		}

		$("#addCompetency").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				String val = (String) $("#ac").val();
				EcCompetency competency = EcCompetency.getBlocking(val);
				$("#competencies")
						.append("<div class='keyword'></div>").children().last()
						.attr("id", val)
						.text(competency.name);

				Array<AlignmentObject> ea = new Array<AlignmentObject>();
				if (me.videoStory.educationalAlignment == null)
					me.videoStory.educationalAlignment = (AlignmentObject) (Object) ea;
				else if (EcArray.isArray(me.videoStory.educationalAlignment) == false) {
					ea.push(me.videoStory.educationalAlignment);
					me.videoStory.educationalAlignment = (AlignmentObject) (Object) ea;
				}
				ea = (Array<AlignmentObject>) (Object) me.videoStory.educationalAlignment;
				AlignmentObject ao = new AlignmentObject();
				ao.targetUrl = competency.shortId();
				ao.targetName = competency.name;
				ea.push(ao);
				$("#ac").val("");
				return false;
			}
		});

		bindAutocomplete();
	}

	private void createCompetencyTag(final Array<AlignmentObject> finalEa, final AlignmentObject ao) {
		$("#competencies").append("<div class='keyword'></div>").children().last()
				.text(EcCompetency.getBlocking(ao.targetUrl).name)
				.click(new EventHandler() {
					@Override
					public boolean onEvent(Event ev, Element THIS) {
						EcArray.setRemove(finalEa, ao);
						$(THIS).remove();
						return false;
					}
				});
	}

	public void bindAutocomplete() {
		//js stub
	}

	@Override
	public Object getState() {
		return videoStory;
	}
}
