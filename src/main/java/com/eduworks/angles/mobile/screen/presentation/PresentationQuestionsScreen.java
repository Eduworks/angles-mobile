package com.eduworks.angles.mobile.screen.presentation;

import com.eduworks.ec.array.EcArray;
import com.eduworks.ec.array.EcObject;
import org.cassproject.ebac.repository.EcRepository;
import org.schema.Question;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.jquery.JQueryCore;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

public class PresentationQuestionsScreen extends PresentationWelcomeScreen {

	static {

	}

	Array<Question> vses = null;

	public PresentationQuestionsScreen() {

		init("partial/screen/presentation/questions.html");
	}

	public PresentationQuestionsScreen setState(Array<Question> o) {
		vses = o;
		return this;
	}

	@Override
	public Object getState() {
		Object o = new Object();
		Array<String> ids = new Array<String>();
		for (int i = 0;i < vses.$length();i++)
			ids.push(vses.$get(i).id);
		JSObjectAdapter.$put(o,"ids",ids);
		return o;
	}

	boolean configured = false;
	@Override
	public void display(String containerId) {
		if (!EcArray.isArray(vses) && EcObject.isObject(vses))
			vses = (Array<Question>)JSObjectAdapter.$get(vses,"ids");
		super.display(containerId);
		if (!configured)
		autoConfigure($(containerId));
		configured = true;
		$("#results").html("");
		final PresentationQuestionsScreen me = this;
		for (int i = 0; i < vses.$length(); i++) {
			if (!EcObject.isObject(vses.$get(i)))
				vses.$set(i, (Question)EcRepository.getBlocking((String)(Object)vses.$get(i)));
			displayResult(i);
		}
	}

	private void displayResult(int i) {
		final Question vs = vses.$get(i);
		final JQueryCore<?> result = displayQuestion(vs);
	}

}
