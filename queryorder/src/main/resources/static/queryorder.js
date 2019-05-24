angular.module('queryorderdemo', [])
.controller('QueryOrder', function($scope, $http) {
    $http.get('http://localhost:8085/v1/queryorder/').
        then(function(response) {
            $scope.orderlist = response.data;
            
        });
});