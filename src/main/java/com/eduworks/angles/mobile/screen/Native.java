package com.eduworks.angles.mobile.screen;

import com.eduworks.ec.crypto.EcPk;
import org.angles.schema.angles.VideoStory;
import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.functions.Callback1;

/**
 * Created by fray on 6/26/17.
 */
@GlobalScope
@STJSBridge(sources = {"js/device.js"})
public class Native {
	public static void loadVideo(Callback1<Array<Object>> success, Callback1<String> failure) {

	}

	public static void captureVideo(Callback1<Array<Object>> success, Callback1<String> failure) {

	}

	public static void uploadFile(Object file, Callback1<VideoStory> success, Callback1<String> failure, Callback1<String> status)
	{

	}

	public static void uploadVideo(String uri, EcPk agent, Callback1<VideoStory> success, Callback1<String> failure, Callback1<String> status) {

	}
}
