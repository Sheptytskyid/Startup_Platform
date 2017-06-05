var app = angular.module('greatStartApp')
    .controller('ProjectController', function ($rootScope, $scope, $uibModal, $routeParams, Project, Investment, $location) {
            var investedAmount = function () {
                var result = 0;
                for (var i = 0; i < $scope.projectInvestments.length; i++) {
                    if ($scope.projectInvestments[i].paid) {
                        result += $scope.projectInvestments[i].sum;
                    }
                }
                return result;
            };

            $scope.projectImage = '';
            $scope.projectCroppedImage = '';
            $scope.imageChanged = false;

            var handleProjectImageSelect = function (evt) {
                var file = evt.currentTarget.files[0];
                var reader = new FileReader();
                reader.onload = function (evt) {
                    $scope.$apply(function ($scope) {
                        $scope.projectImage = evt.target.result;
                        $scope.imageChanged = true;
                    });
                };

                reader.readAsDataURL(file);

            };

            angular.element(document.querySelector('#fileInput')).on('change', handleProjectImageSelect);

            $scope.update = function (project) {
                return $scope.project = Project.update({id: project.id}, project, function () {
                    $scope.project = Project.get({id: project.id})
                })
            };

            var fieldsAreValid = function () {
                $scope.validationMessage = '';
                if ($scope.project.desc.minInvestment > $scope.project.desc.cost) {
                    $scope.validationMessage = "Min. investment must be smaller or equal to the total cost";
                    return false;
                }
                if ($scope.project.desc.minInvestment != null && $scope.project.desc.cost % $scope.project.desc.minInvestment !== 0) {
                    $scope.validationMessage = "Cost must be exactly divisible by min. investment";
                    return false;
                }
                return true;
            };

            $scope.submit = function () {
                if ($scope.imageChanged) {
                    $scope.project.desc.image = $scope.projectCroppedImage.replace(/^data:image\/[a-z]+;base64,/, "");
                }
                if (fieldsAreValid()) {
                    if (!$scope.project.id) {
                        $scope.createProject($scope.project);
                    } else {
                        $scope.currentProjectId = $scope.project.id;
                        $scope.update($scope.project).$promise.then(function (success) {
                            $location.path('/project/' + $scope.currentProjectId);
                        }, function (error) {
                            $scope.error = error.status + " " + error.statusText;
                        });
                    }
                } else {
                    $scope.error = $scope.validationMessage;
                }
            };

            var allProjects = function () {
                return Project.query();
            };

            var myProjects = function () {
                return Project.my();
            };

            // load my projects
            if ($location.path().indexOf("/my") > -1 || $location.path().indexOf("/user/") > -1) {
                $scope.projects = myProjects();
                // load a single project
            } else if ($location.path().indexOf("/project/") > -1 && $routeParams.id) {
                Investment.project({id: $routeParams.id}, function (investments) {
                    $scope.projectInvestments = investments;
                    Project.get({id: $routeParams.id}, function (project) {
                        $scope.project = project;
                        $scope.investedAmount = investedAmount();
                        $scope.invProgressWithWidth = ivnProgressWithWidth($scope.investedAmount, project);
                    });
                });
                // load all projects
            } else {
                $scope.project = {};
                $scope.projects = allProjects();
            }


            var ivnProgressWithWidth = function (investedAmount, project) {
                return 'width: ' + investedAmount * 100 / project.desc.cost + '%';
            };


            $scope.closeApproveModal = function () {
                $scope.projectModal.dismiss();
            };

            $scope.openApproveModal = function () {
                $scope.projectModal = $uibModal.open({
                    templateUrl: 'views/investment/ApproveCreateInvestmentModal.html',
                    controller: 'first.modal.controller',
                    size: 'md',
                    backdrop: 'true',
                    resolve: {
                        project: $scope.project
                    }
                }).result.then(function (data) {
                  console.log("It works!");
                  console.log(data);
                  });
            }

            $scope.openCreateInvestmentModal = function () {
                $scope.closeApproveModal();
                $scope.investmentModal = $uibModal.open({
                    templateUrl: 'views/investment/add_investment.html',
                    controller: 'ProjectController',
                    size: 'sm',
                    backdrop: 'true',
                    resolve: {}
                }).result.then(function (investment) {
                  });
            };

            $scope.closeInvestmentModal = function () {
                $scope.investmentModal.close();
            };

            $scope.createProject = function () {
                Project.save($scope.project, function (success) {
                    $location.path('/projects');
                }, function (error) {
                    $scope.error = error.status + " " + error.statusText;
                });
            };

            $scope.deleteProject = function () {
                Project.delete({id: $scope.project.id}, $scope.project, function (success) {
                    $location.path('/projects');
                }, function (error) {
                    $scope.error = error.status + " " + error.statusText;
                });
            };


        }
    );

app.controller('first.modal.controller', function($scope, $uibModalInstance, $timeout, project) {
  console.log($scope);
  console.log($uibModalInstance);
  $scope.project = project;

  $timeout(function () {
    $uibModalInstance.close(project);
  });

})