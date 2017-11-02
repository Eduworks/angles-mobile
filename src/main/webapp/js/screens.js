/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
var screens = [];
var currentScreen = null;

/* ---- COLLECTION TOOL SCREENS ---- */

$(document).ready(function () {
    showscreen('welcome');
});

function showPage(newPage) {

}

function showscreen(newScreen) {
    if (($('#' + newScreen).length) > 0) {
        console.log("moving from " + currentScreen + " to " + newScreen);

        $.
        if(currentScreen != null)
        screens.push(currentScreen)
        currentScreen = newScreen;
    } else {
        alert('The ' + newScreen + ' screen is not yet implemented.');
    }
}

function onBack(evt) {
    if (window.history.length > 0) {
        if (screens.length == 0)
            window.history.back();
        else
            showscreen(screens.pop());
        evt.preventDefault();
    }
}
