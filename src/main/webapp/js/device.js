/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
// example.js file
// Wait for device API libraries to load
//
function onLoad() {
    console.log("Document Loaded.");
    document.addEventListener("deviceready", onDeviceReady, false);
}

// device APIs are available
//
function onDeviceReady() {
    console.log("Device Ready.");
    document.addEventListener("pause", onPause, false);
    document.addEventListener("resume", onResume, false);
    document.addEventListener("menubutton", onMenuKeyDown, false);
    document.addEventListener("backbutton", onBack, false);
    // Add similar listeners for other events
}

function onBack(evt) {
    window.history.back();
}

function onPause(evt) {
    // Handle the pause event
}

function onResume(evt) {
    // Handle the resume event
}

function onMenuKeyDown(evt) {
    // Handle the menubutton event
}

ScreenManager.loadHistoryCallback = function(screen,historyObject){
	if (screen.setState != null){
		screen.setState(historyObject);
	}
}


function loadVideo(captureSuccess,captureError)
{
    navigator.camera.getPicture(
        function(success){
        	if (success.startsWith("/")) //We got a path.
        		window.resolveLocalFileSystemURL(
				  "file:" + success,
				  captureSuccess
		  		);
		  	else //We got a base64 encoded string/file.
		  		captureSuccess(success);
		},
        captureError,
        {
            mediaType:Camera.MediaType.VIDEO,
            sourceType:Camera.PictureSourceType.SAVEDPHOTOALBUM, destinationType:Camera.DestinationType.FILE_URI
        }
    );
}

function captureVideo(captureSuccess,captureError)
{
    navigator.device.capture.captureVideo(
        captureSuccess,
        captureError,
        {
            limit:1,
            quality:0
        }
    );
}

function uploadFile(file, success, failure, status) {
	status("Uploading...");
	var fd = new FormData();
	fd.append("file",file);
	EcRemote.postExpectingObject(
		AppController.serverController.getRepoInterface().selectedServer,
		"metadata/VideoStory",
		fd,
		success,
		failure
	)
}

function uploadVideo(uri, agent, success, failure, status) {
    var upload = function (fileEntry) {
        fileEntry.file(function (file) {
//        	uploadFile(
//        		file,
//        		success,
//				failure,
//				status
//        	);
//            var reader = new FileReader();
//            reader.onloadend = function (event) {
//                uploadFile(
//                    uri,
//                    event.target.result.split(",")[1],
//                    event.target.result.split(";")[0].split(":")[1],
//                    agent,
//                    success,
//                    failure,
//                    status
//                );
//            };
//            reader.readAsDataURL(file);
            var reader = new FileReader();
            reader.onloadend = function (event) {
                uploadFile(
                    new Blob([this.result],{type:"video/mp4"}),
                    success,
                    failure,
                    status
                );
            };
            reader.readAsArrayBuffer(file);
        });
    }
    if (!uri.startsWith("file:"))
        uri = "file:" + uri;
    if (uri.endsWith(".mp4")) {
        status("Compressing video.");
        VideoEditor.transcodeVideo(
            function (result) {
                window.resolveLocalFileSystemURL(
                    "file:" + result,
                    upload
                );
            },
            failure, {
                fileUri: uri,
                outputFileName: uri.substring(uri.lastIndexOf("/") + 1).replace(".mp4", "") + "_h264",
                outputFileType: VideoEditorOptions.OutputFileType.MPEG4,
                optimizeForNetworkUse: VideoEditorOptions.OptimizeForNetworkUse.YES,
                maintainAspectRatio: true,
                videoBitrate: 1 * 1024 * 1024, // 1 megabit
                audioChannels: 2,
                audioSampleRate: 44100,
                width: 720,
                height: 720,
                audioBitrate: 128 * 1024, // 128 kilobits
                progress: function (info) {
                    status("Compressing video: " + Math.round(info * 100) + "% complete.");
                }
            }
        );
    } else {
        window.resolveLocalFileSystemURL(
            uri, upload
        );
    }
}


/* VIDEO CONTROLS */

// toggle play/pause
$('body').on('click', '.video-controls', function () {
    var video = $(this).siblings('video').get(0);
    if (video.paused) {
        video.play();
        $(this).addClass('playing');
    } else {
        video.pause();
    }
});

