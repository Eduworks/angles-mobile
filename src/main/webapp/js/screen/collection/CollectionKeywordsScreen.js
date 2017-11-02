/*
 Copyright 2015-2016 Eduworks Corporation and other contributing parties.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
CollectionKeywordsScreen = (function(CollectionKeywordsScreen){
	CollectionKeywordsScreen.prototype.bindAutocomplete = function(){
		$("#ac").autocomplete({
			source: function(request, response) {
				$.ajax({
					url: "https://dev.cassproject.org/api/data?q=@type:Competency AND name:"+$("#ac").val()+"*",
					success: function(data) {
						response($.map(data, function(item) {
							return {
								label: item.name,
								value: item["@id"]
							}
						}))
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert(errorThrown);
					}
				});
			},
			minLength: 1
		});
	}
	return CollectionKeywordsScreen;
})(CollectionKeywordsScreen);