var formApp = angular.module('formApp', []);
function formController($scope, $http, $window) {
    $scope.formData = {};
    $scope.processForm = function() {
        $http({
            method : 'POST',
            url : 'rest/user/add',
            data : $.param({'name' : $scope.formData.name, 'password' : $scope.formData.password}),
            headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
        }).success(function(){
            $window.location.href = 'success.html';
        }).error(function(){
            $scope.duplicateUserException = true;
        });

    };
}