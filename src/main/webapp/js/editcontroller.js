'use strict';



function getURLParameter(name) {
	  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
	}

var blueMixTimeLine = angular.module('blueMixEdit', [ 'ngSanitize' ]);

blueMixTimeLine.controller('EditController', function($scope, $http) {
	
		$http.get('api/getentry?newsEntryId=' + getURLParameter('id') ).success(function(data) {
			
				
			
			$scope.authorDisplayName = data.authorDisplayName;
			$scope.authorPictureURL = data.authorPictureURL;
			$scope.authorTwitter = data.authorTwitter;
			$scope.newsEntryLink = data.newsEntryLink;
			$scope.newsEntryFirstSentences = data.newsEntryFirstSentences;
			$scope.newsEntryCurationDate = data.newsEntryCurationDate;
			$scope.newsEntryPublicationDate = data.newsEntryPublicationDate;
			$scope.newsEntryIsTopStory = data.newsEntryIsTopStory;
			$scope.newsEntryTopStoryPosition = data.newsEntryTopStoryPosition;
			$scope.newsEntryState = data.newsEntryState;
			$scope.newsEntryCurator = data.newsEntryCurator;
			$scope.newsEntryTitle = data.newsEntryTitle;
			$scope.newsEntryId = data.newsEntryId;
		});
});
