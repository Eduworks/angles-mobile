package com.eduworks.angles.mobile.screen.presentation;

import com.eduworks.angles.AppController;
import com.eduworks.ec.array.EcObject;
import org.angles.schema.angles.VideoStory;
import org.cassproject.ebac.repository.EcRepository;
import org.cassproject.schema.cass.competency.Relation;
import org.cassproject.schema.general.EcRemoteLinkedData;
import org.schema.Question;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.JQueryCore;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 7/13/17.
 */
public class PresentationResultScreen extends PresentationWelcomeScreen {

	VideoStory vs = null;

	public PresentationResultScreen() {
		init("partial/screen/presentation/result.html");
	}

	public PresentationResultScreen setState(VideoStory o) {
		vs = o;
		return this;
	}

	@Override
	public Object getState() {
		Object o = new Object();
		JSObjectAdapter.$put(o,"id",vs.id);
		JSObjectAdapter.$put(o,"name",vs.name);
		return o;
	}

	boolean configured = false;

	@Override
	public void display(final String containerId) {
		if (EcObject.isObject(vs) && vs.type == null)
		{
			//Reload or Back logic
			String name = vs.name;
			VideoStory v = new VideoStory();
			v.copyFrom(EcRepository.getBlocking(vs.id));
			vs = v;
			vs.name = name;
		}
		super.display(containerId);
		final PresentationResultScreen me = this;
		if (!configured)
			autoConfigure($(containerId));
		configured = true;
		autoFill($(containerId), vs);
		String encodingFormat = vs.encodingFormat;

		if (JSObjectAdapter.$get(vs,"http://schema.eduworks.com/angles/0.1/storyCyberCategory") != null)
		{
			Array<String> categories = (Array<String>) JSObjectAdapter.$get(vs,"http://schema.eduworks.com/angles/0.1/storyCyberCategory");
			for (int i = 0;i < categories.$length();i++)
			{
				$("[storyCyberCategory='"+categories.$get(i)+"']").show();
			}
		}
		if (encodingFormat == null || encodingFormat == "") {
			encodingFormat = "video/mp4";
		}
		if (vs.keywords != null)
			$("#keywords").find("a").text(((Array<String>) (Object) vs.keywords).join(", "));

		AppController.serverController.getRepoInterface().search(
				new Relation().getSearchStringByType() + " AND (source:\"" + vs.shortId() + "\") AND \"http://schema.eduworks.com/angles/0.1/relations/raises\"",
				null, new Callback1<Array<EcRemoteLinkedData>>() {
					@Override
					public void $invoke(Array<EcRemoteLinkedData> relations) {
						me.autoRemove($(containerId), "question");
						for (int i = 0; i < relations.$length(); i++) {
							Relation r = new Relation();
							r.copyFrom(relations.$get(i));
							if (r.target.indexOf(new Question().type) != -1)
								EcRepository.get(r.target, new Callback1<EcRemoteLinkedData>() {
									@Override
									public void $invoke(EcRemoteLinkedData ecRemoteLinkedData) {
										final JQueryCore<?> question = me.autoAppend($(containerId), "question");
										final Question q = new Question();
										q.copyFrom(ecRemoteLinkedData);
										me.autoFill(question, q);
										AppController.serverController.getRepoInterface().search(
												new Relation().getSearchStringByType() + " AND (target:\"" + q.shortId() + "\") AND \"http://schema.eduworks.com/angles/0.1/relations/answers\"",
												null,
												new Callback1<Array<EcRemoteLinkedData>>() {
													@Override
													public void $invoke(Array<EcRemoteLinkedData> results) {
														if (results.$length() == 0) {
															question.hide();
															return;
														}
														question.show();
														question.css("color","#1779ba");
														question.click(new EventHandler() {
															@Override
															public boolean onEvent(Event ev, Element THIS) {
																AppController.serverController.getRepoInterface().search(
																		new Relation().getSearchStringByType() + " AND (target:\"" + q.shortId() + "\") AND \"http://schema.eduworks.com/angles/0.1/relations/answers\"",
																		null,
																		new Callback1<Array<EcRemoteLinkedData>>() {
																			@Override
																			public void $invoke(Array<EcRemoteLinkedData> results) {
																				if (results.$length() == 0) {
																					me.failure.$invoke("No stories available for this question.");
																					return;
																				}
																				final Relation r = new Relation();
																				r.copyFrom(results.$get(0));
																				EcRepository.get(r.source, new Callback1<EcRemoteLinkedData>() {
																							@Override
																							public void $invoke(EcRemoteLinkedData ecRemoteLinkedData) {
																								VideoStory vs = new VideoStory();
																								vs.copyFrom(ecRemoteLinkedData);
																								vs.name = q.text;
																								me.resultScreen(vs);
																							}
																						},
																						me.failure);
																			}
																		},
																		me.failure
																);
																return false;
															}
														});
													}
												},
												me.failure
										);
									}
								}, failure);
						}
					}
				}, me.failure);
		Object o = new Object();
		JSObjectAdapter.$put(o, "size", 5);
		AppController.serverController.getRepoInterface().searchWithParams(
				new VideoStory().getSearchStringByType() + " AND (" + vs.description + " " + vs.name + ")",
				o,
				null, new Callback1<Array<EcRemoteLinkedData>>() {
					@Override
					public void $invoke(Array<EcRemoteLinkedData> results) {
						for (int i = 0; i < results.$length(); i++) {
							VideoStory r = new VideoStory();
							r.copyFrom(results.$get(i));
							if (r.isId(me.vs.shortId())) continue;
							me.displayVideoStory(r);
						}

					}
				}, me.failure);
		$(containerId).find(".topics").on("click", "li", new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				AppController.serverController.getRepoInterface().search(
						new VideoStory().getSearchStringByType() + " AND (cyberCategory:\"" + $(ev.target).text() + "\")",
						null, new Callback1<Array<EcRemoteLinkedData>>() {
							@Override
							public void $invoke(Array<EcRemoteLinkedData> results) {
								Array<VideoStory> stories = new Array<VideoStory>();
								for (int i = 0; i < results.$length(); i++) {
									VideoStory r = new VideoStory();
									r.copyFrom(results.$get(i));
									if (r.isId(me.vs.shortId())) continue;
									stories.push(r);
								}
								me.resultsScreen(stories);
							}
						}, me.failure);
				return false;
			}
		});
	}
}
