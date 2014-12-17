'use strict';

var simpleApp = angular.module('simpleApp', [ 'ngSanitize' ]);

simpleApp.controller('AggregatorCtrlHelper', function($scope, $http) {
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
});