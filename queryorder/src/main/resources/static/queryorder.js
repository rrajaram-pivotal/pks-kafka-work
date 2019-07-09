/*angular.module('queryorderdemo', [])
.controller('QueryOrder', function($scope, $interval,$http) {
	
	$interval(function ()
			alert("In");
			$http.get('http://localhost:8085/v1/queryorder/').
			then(function(response) {
				$scope.orderlist = response.data;
				
			}), 5000);
});*/

angular.module('queryorderdemo',[]).controller("QueryOrder", function ($scope,$http,$interval){
    $scope.reload = function () {
        $http.get('http://queryorder:8085/v1/queryorder/').
            then(function (response) {
            	$scope.orderlist = response.data;
            });
    };
    $scope.reload();
    $interval($scope.reload, 10000);
});