angular.module('queryorderdemo',[]).controller("QueryOrder", function ($scope,$http,$interval){
    $scope.reload = function () {
        $http.get('/v1/queryorder/').
            then(function (response) {
            	$scope.orderlist = response.data;
            });
    };
    $scope.reload();
    $interval($scope.reload, 10000);
});