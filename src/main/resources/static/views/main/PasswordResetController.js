var PasswordResetController = angular.module('greatStartApp').controller('PasswordResetController',
    function ($rootScope, $scope, passwordResetService) {

        $scope.close = function () {
            $scope.forgotPassModal.dismiss();
        };

        $scope.submitEmail = function () {
            $scope.submitted = true;
            $scope.message = null;
            $scope.error = null;
            var data = passwordResetService.query({email:$scope.email}, function(){
                $scope.message = data[0];
            }, function(error) {
                $scope.submitted = false;
                $scope.error = error.data.message;
            });
        };

        $scope.submitPassword = function() {
            $scope.submitted = true;
            $scope.message = null;
            $scope.error = null;
            if(validatePassword()) {
            passwordResetService.save($scope.password, function (){
                $scope.message = "Password successfully changed!";
            }, function(){
                $scope.submitted = false;
                $scope.error = "Error changing password";
            });}
        };

        var validatePassword = function() {
            if($scope.password !== $scope.confirmPassword) {
                $scope.error = "Passwords do not match";
                $scope.submitted = false;
                return false;
            } else if($scope.password === null || $scope.password === undefined || $scope.password.length < 5) {
                $scope.error = "Password is too short";
                $scope.submitted = false;
                return false;
            } else if($scope.password.length >= 15) {
                $scope.error = "Password is too long";
                $scope.submitted = false;
                return false;
            } else {
                return true;
            }
        }
    });
