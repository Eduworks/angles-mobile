package com.eduworks.angles.mobile.screen.collection;

import com.eduworks.angles.AppController;
import com.eduworks.angles.mobile.screen.Native;
import com.eduworks.angles.mobile.screen.NavigationScreen;
import com.eduworks.ec.blob.BlobHelper;
import com.eduworks.ec.framework.view.EcScreen;
import com.eduworks.ec.framework.view.manager.ScreenManager;
import org.angles.schema.angles.VideoStory;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.functions.Callback1;

import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * Created by fray on 6/16/17.
 */
public class CollectionUploadingScreen extends NavigationScreen {
	private VideoStory videoStory;

	public CollectionUploadingScreen() {
		super("partial/screen/collection/uploading.html", JSCollections.<String, EcScreen>$map());
	}

	public CollectionUploadingScreen setState(VideoStory s) {
		this.videoStory = s;
		return this;
	}

	@Override
	public void display(String containerId) {
		super.display(containerId);
		final CollectionUploadingScreen me = this;
		if (videoStory.contentUrl != null)
			Native.uploadVideo(videoStory.contentUrl, AppController.identityController.selectedIdentity.ppk.toPk(), new Callback1<VideoStory>() {
				@Override
				public void $invoke(VideoStory vs) {
					me.videoStory.copyFrom(vs);
					ScreenManager.changeScreen(new CollectionKeywordsScreen().setState((VideoStory) me.getState()), null, null, false);
				}
			}, new Callback1<String>() {
			@Override
			public void $invoke(String s) {
				$(".lead").text(s);
			}
		}, new Callback1<String>() {
			@Override
			public void $invoke(String s) {
				$(".lead").text(s);
			}
		});
		else{
			Object f = BlobHelper.base64ToBlob((String)JSObjectAdapter.$get(me.videoStory,"content"),"video/mp4");
			JSObjectAdapter.$put(me.videoStory,"content",null);
			Native.uploadFile(f, new Callback1<VideoStory>() {
				@Override
				public void $invoke(VideoStory vs) {
					me.videoStory.copyFrom(vs);
					ScreenManager.changeScreen(new CollectionKeywordsScreen().setState((VideoStory) me.getState()), null, null, false);
				}
			}, new Callback1<String>() {
				@Override
				public void $invoke(String s) {
					$(".lead").text(s);
				}
			}, new Callback1<String>() {
				@Override
				public void $invoke(String s) {
					$(".lead").text(s);
				}
			});
		}
	}
	@Override
	public Object getState() {
		return videoStory;
	}
}
