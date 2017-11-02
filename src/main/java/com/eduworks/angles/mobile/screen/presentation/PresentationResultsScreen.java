package com.eduworks.angles.mobile.screen.presentation;

import org.angles.schema.angles.VideoStory;
import org.stjs.javascript.Array;
import org.stjs.javascript.jquery.JQueryCore;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

public class PresentationResultsScreen extends PresentationWelcomeScreen {

	static {

	}

	Array<VideoStory> vses = null;

	public PresentationResultsScreen() {

		init("partial/screen/presentation/results.html");
	}

	public PresentationResultsScreen setState(Array<VideoStory> o) {
		vses = o;
		return this;
	}

	@Override
	public void display(String containerId) {
		super.display(containerId);
		autoConfigure($(containerId));
		$("#results").html("");
		final PresentationResultsScreen me = this;
		for (int i = 0; i < vses.$length(); i++)
			displayResult(i);
	}

	private void displayResult(int i) {
		final VideoStory vs = vses.$get(i);
		if (vs.embedUrl == null)
			return;
		final JQueryCore<?> result = displayVideoStory(vs);
	}

}
