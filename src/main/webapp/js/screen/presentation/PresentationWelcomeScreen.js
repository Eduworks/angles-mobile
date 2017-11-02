/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
PresentationWelcomeScreen = (function(PresentationWelcomeScreen){
	PresentationWelcomeScreen.prototype.resultScreen = function(story){
		var screen = new PresentationResultScreen();
		screen.setState(story);
		ScreenManager.changeScreen(screen, null, screen.getState(), true);
	}
	PresentationWelcomeScreen.prototype.resultsScreen = function(stories){
		var screen = new PresentationResultsScreen();
		screen.setState(stories);
		ScreenManager.changeScreen(screen, null, screen.getState(), true);
	}
	PresentationWelcomeScreen.prototype.questionsScreen = function(questions){
		var screen = new PresentationQuestionsScreen();
		screen.setState(questions);
		ScreenManager.changeScreen(screen, null, screen.getState(), true);
	}
	return PresentationWelcomeScreen;
})(PresentationWelcomeScreen);