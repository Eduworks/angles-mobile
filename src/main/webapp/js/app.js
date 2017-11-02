/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
$(document).foundation();



var myQuestion = '';
var myTopic = '';

$('.topics a').on('click', function () {
    myTopic = $(this).text();
    $('#topicList').fadeOut('fast', function () {
        $('#questionText').text(myTopic + " stories");
        $('#topicResults').fadeIn();
        $('html,body').scrollTop(0);
    });
});

$('.questions a').on('click', function () {
    var question = $(this).text();
    searchFor(question);
});

// Capture ENTER keydown
$('#welcomeQuestion1').on("keypress", function (e) {
    if (e.keyCode == 13) {
        showResults(1);
        return false; // prevent the button click from happening
    }
});

function searchFor(topic) {
    $('#welcomeQuestion2').val(topic);
    showResults(2);
    $('#welcomeQuestion2').val('');
}

function showResults(inputNum) {
    myQuestion = $('#welcomeQuestion' + inputNum).val();
    var splitString = myQuestion.split(' ').join('+');
    var size = videoSize();
    console.log(size);
    $('#video').attr('src', 'http://placehold.it/' + size + '?text=Story+topic:+' + splitString);
    $('#welcome').fadeOut('fast', function () {
        //        $('#results').hide();
        $('#results').show();
        showVideo();
        $('html,body').scrollTop(0);
    });
}

var landscape = false;

function videoSize() {
    landscape = !landscape;
    if (landscape) {
        $('#video').removeClass('portrait');
        return "600x400";
    } else {
        $('#video').addClass('portrait');
        return "400x600";
    }
}

$('#topicResults .videoThumb').on('click', function () {
    searchFor(myTopic);
    //    var myQuestion = myTopic;
    //    var splitString = myQuestion.split(' ').join('+');
    //    $('#video').attr('src', 'http://placehold.it/600x400?text=Story+topic:+' + splitString);
    //    showVideo();
});

$('.questions .videoThumb').on('click', function () {
    var myQuestion = $(this).siblings('a').text();
    var splitString = myQuestion.split(' ').join('+');
    $('#video').attr('src', 'http://placehold.it/600x400?text=Story+topic:+' + splitString);
    showVideo();
});

function showVideo() {
    //    $('#videoResult, #rateVideo').fadeIn();
    $('#rateVideo .button').removeClass('warning secondary success');
    $('#rateVideo #thanks').hide();
    $('html,body').scrollTop(0);
    //    $('#videoResult').fadeOut(function () {
    //    });
    $('#videoResult, .rateVideo').fadeIn();



    //resizeVideo();

    //searchFor('Follow up with one of these stories');

    // Simulate a scroll down after video is done
    // removed per request
    //    setTimeout(function () {
    //        $('body,html').animate({
    //            scrollTop: window.innerHeight / 1.5
    //        }, 500);
    //    }, 3000);
}

function hideVideo() {
    $('#videoResult, #rateVideo').hide();
    $('#rateVideo #thanks').hide();
    $('#rateVideo .button').removeClass('warning secondary success');
    resizeVideo();
}

// Rate video (toggle buttons)
$('#rateVideo .yes.button').on('click', function () {
    $(this).addClass('success').removeClass('secondary');
    $('#rateVideo .no.button').addClass('secondary').removeClass('warning');
    $('#rateVideo #thanks').fadeIn();
});
$('#rateVideo .no.button').on('click', function () {
    $(this).addClass('warning').removeClass('secondary');
    $('#rateVideo .yes.button').addClass('secondary').removeClass('success');
    $('#rateVideo #thanks').fadeIn();
});

// Scale video to fit window.

$(window).resize(function () {
    resizeVideo();
});

$(window).load(function () {
    resizeVideo();
});

function resizeVideo() {
    if ($('#videoResult').is(":visible")) {
        //$('#searchResults').css('margin-top', window.innerHeight);
        //$('#results').css('padding-top', '0');
        //$('#video').css('height', '100%');
    } else {
        //$('#searchResults').css('margin-top', '');
        //$('#results').css('padding-top', '');
    }
}

function addKeyword() {
    var keyword = $('input#keyword').val();
    if (keyword == '') {
        return;
    } else {
        var newKeyword = $('<div class="key" style="display:none;">' + keyword + '</div>');
        $('div#keywords').append(newKeyword);
        newKeyword.fadeIn();
        $('input#keyword').val('');
    }
}

$('#keywords').on('click', '.key', function () {
    $(this).fadeOut(function () {
        $(this).remove();
    });
});

$('#specalty').change(function () {
    $("#role").attr("disabled", false);
});
