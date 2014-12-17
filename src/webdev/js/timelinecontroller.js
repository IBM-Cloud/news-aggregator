'use strict';

var blueMixTimeLine = angular.module('blueMixTimeLine', [ 'ngSanitize' ]);

blueMixTimeLine.controller('TimelineController', function($scope, $http) {
		$http.get('api/news').success(function(data) {
			
			if (data.curatedEntries) {
				for (var i = 0; i < data.curatedEntries.length; i++) {
					data.curatedEntries[i].newsEntryCurationDate = jQuery.timeago(data.curatedEntries[i].newsEntryCurationDate);
					if (!data.curatedEntries[i].authorTwitter) {
						data.curatedEntries[i].authorTwitter = data.curatedEntries[i].authorDisplayName;
					}
				}
			}			
			
			$scope.curatedEntries = data.curatedEntries;
			$scope.incomingEntries = data.incomingEntries;
			$scope.topStories = data.topStories;
		});
}).directive('bnavbar', function() {
    return {
        replace: true,
        restrict: 'E',  
        templateUrl: "directives/bluemixNavbar.html"
    };
}).directive('bluetweet', function() {
    return {
        replace: true,
        restrict: 'E',  
        templateUrl: "directives/bluemixTweets.html"
    };
}).directive('blueresources', function() {
    return {
        replace: true,
        restrict: 'E',  
        templateUrl: "directives/blueMixResources.html"
    };
}).directive('blueinfo', function() {
    return {
        replace: true,
        restrict: 'E',  
        templateUrl: "directives/blueMixInfo.html"
    };
}).directive('bluefooter', function() {
    return {
        replace: true,
        restrict: 'E',  
        templateUrl: "directives/blueMixFooter.html"
    };
}).directive('blueentry', function() {
    return {
        replace: true,
        scope : {'entry' : '='},
        restrict: 'E',  
        templateUrl: "directives/blueMixEntry.html"
    };
}).directive('bluetimeline', function() {
    return {
        replace: true,
        restrict: 'E',  
        templateUrl: "directives/blueTimeLine.html"
    };
});
