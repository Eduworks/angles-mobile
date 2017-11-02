package com.eduworks.angles.mobile.screen.presentation;

import com.eduworks.angles.AppController;
import com.eduworks.angles.mobile.screen.NavigationScreen;
import org.angles.schema.angles.VideoStory;
import org.cassproject.ebac.repository.EcRepository;
import org.cassproject.schema.cass.competency.Relation;
import org.cassproject.schema.general.EcRemoteLinkedData;
import org.schema.Question;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import org.stjs.javascript.jquery.JQueryCore;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

public class PresentationWelcomeScreen extends NavigationScreen {

	public PresentationWelcomeScreen() {
		super("partial/screen/presentation/welcome.html", null);
	}

	@Override
	public void display(String containerId) {
		final PresentationWelcomeScreen me = this;

		failure =
				new Callback1<String>() {
					@Override
					public void $invoke(String s) {
						Global.alert(s);
					}
				};
		super.display(containerId);

		$("#search").click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				String val = (String) $("#question").val();
				if (val == null || val == "")
					val = "*";

				AppController.serverController.getRepoInterface().search(
						new Question().getSearchStringByType() + " AND (" + EcRepository.escapeSearch(val) + ")",
						null,
						new Callback1<Array<EcRemoteLinkedData>>() {
							@Override
							public void $invoke(Array<EcRemoteLinkedData> results) {
								Array<Question> questions = new Array<Question>();
								for (int i = 0; i < results.$length(); i++) {
									Question q = new Question();
									q.copyFrom(results.$get(i));
									me.showFirstRelatedStory(q);
									return;
								}
							}
						},
						me.failure
				);
				return false;
			}
		});
		$(".topics").children().click(new EventHandler() {
			@Override
			public boolean onEvent(Event event, Element THIS) {

				AppController.serverController.getRepoInterface().search(
						new Question().getSearchStringByType() + " AND (" + EcRepository.escapeSearch(THIS.getAttribute("storyCyberCategory")) + ")",
						null,
						new Callback1<Array<EcRemoteLinkedData>>() {
							@Override
							public void $invoke(Array<EcRemoteLinkedData> results) {
								Array<Question> questions = new Array<Question>();
								for (int i = 0; i < results.$length(); i++) {
									Question q = new Question();
									q.copyFrom(results.$get(i));
									questions.$set(i,q);
								}
								me.questionsScreen(questions);
							}
						},
						me.failure
				);
				return false;
			}
		});
	}

	protected JQueryCore<?> displayVideoStory(final VideoStory vs) {
		final PresentationWelcomeScreen me = this;
		String encodingFormat = vs.encodingFormat;
		if (encodingFormat == null || encodingFormat == "") {
			encodingFormat = "video/mp4";
		}
		final JQueryCore<?> result = me.autoAppend($("#results"), "result");
		if (vs.thumbnailUrl == null) {
			vs.thumbnailUrl = "https://angles.eduworks.org/api/thumbnail" + vs.embedUrl.substring(vs.embedUrl.lastIndexOf("/"));
			if (vs.thumbnailUrl.indexOf(",") != -1)
				vs.thumbnailUrl = vs.thumbnailUrl.substring(0, vs.thumbnailUrl.indexOf(","));
			vs.thumbnailUrl = vs.thumbnailUrl.replace("#", "?");
		}
		autoFill(result, vs);
		result.click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {

				me.resultScreen(vs);
				return false;
			}
		});
		AppController.serverController.getRepoInterface().search(
				new Relation().getSearchStringByType() + " AND (source:\"" + vs.shortId() + "\")",
				new Callback1<EcRemoteLinkedData>() {
					@Override
					public void $invoke(EcRemoteLinkedData ecRemoteLinkedData) {
						final Relation r = new Relation();
						r.copyFrom(ecRemoteLinkedData);
						EcRepository.get(r.target, new Callback1<EcRemoteLinkedData>() {
									@Override
									public void $invoke(EcRemoteLinkedData ecRemoteLinkedData) {
										Question q = new Question();
										q.copyFrom(ecRemoteLinkedData);
										JQueryCore<?> qui = me.autoAppend(result, "question");
										qui.text(q.text);
									}
								},
								new Callback1<String>() {
									@Override
									public void $invoke(String s) {
										Global.alert(s);
									}
								});
					}
				},
				new Callback1<Array<EcRemoteLinkedData>>() {
					@Override
					public void $invoke(Array<EcRemoteLinkedData> results) {

					}
				},
				new Callback1<String>() {
					@Override
					public void $invoke(String s) {
						Global.alert(s);
					}
				}
		);
		return result;
	}

	protected JQueryCore<?> displayQuestion(final Question question) {
		final PresentationWelcomeScreen me = this;
		final JQueryCore<?> result = me.autoAppend($("#results"), "result");
		autoFill(result, question);
		result.click(new EventHandler() {
			@Override
			public boolean onEvent(Event ev, Element THIS) {
				me.showFirstRelatedStory(question);
				return false;
			}
		});
		return result;
	}

	private void showFirstRelatedStory(final Question question) {
		final PresentationWelcomeScreen me = this;
		AppController.serverController.getRepoInterface().search(
                new Relation().getSearchStringByType() + " AND (target:\"" + question.shortId() + "\")",
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
                                        vs.name = question.text;
                                        me.resultScreen(vs);
                                    }
                                },
                                me.failure);
                    }
                },
                me.failure
        );
	}

	protected void questionsScreen(Array<Question> questions) {
		//js stub to avoid circular dependencies.
	}

	protected void resultsScreen(Array<VideoStory> stories) {
		//js stub to avoid circular dependencies.
	}

	protected void resultScreen(VideoStory story) {
		//js stub to avoid circular dependencies.
	}
}
