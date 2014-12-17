/*
 *
 *  Curation application submission controller, based on material design 
 * 
 */
angular.module( 'CurationApp', [ 'ngMaterial' ] )
            .controller('CurationController', function($scope, $http) {
    $scope.article = {
		"authorDisplayName" : "",
		"title" : "",
		"link" : "",
		"firstSentences" : "",
		"tweet" : "false"
	};
	
	$scope.author = {	
		"displayName" : "",
		"twitter" : "",
		"pictureUrl" : ""
	};
	
	$scope.result = "Content here";
	
	$scope.submitArticle = function($event) {
		$http.post('../api/addentry', $scope.article)
			.success(function(data, status, headers, config) {
				$scope.result = data.returnCode;
				$scope.article = {};
		})
			.error(function(data, status, headers, config) {
				$scope.result = data.returnCode;
		});
  };
	
	$scope.submitAuthor = function($event) {
    $http.post('../api/addperson', $scope.author)
			.success(function(data, status, headers, config) {
				$scope.result = data.returnCode;
				$scope.author = {};
		})
			.error(function(data, status, headers, config) {
				$scope.result = data.returnCode;
		});
  };
	
});
